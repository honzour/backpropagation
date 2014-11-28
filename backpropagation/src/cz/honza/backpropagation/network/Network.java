package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import cz.honza.backpropagation.R;



public class Network implements Serializable {
	
	private static final long serialVersionUID = -6200117127679042346L;
	
	protected double[][] mInputScale;
	protected double[][] mOutputScale;
	
	protected double[] mOutput;
	
	public Layer[] mLayers;
	public double mAlpha = 1;
	
	public TrainingSet mTrainingSet;
	
	private long mIteration;

	static double sigma(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}
	
	public int getInputDimension()
	{
		if (mLayers == null || mLayers.length == 0 || mLayers[0] == null || mLayers[0].neurons == null || mLayers[0].neurons[0] == null || mLayers[0].neurons[0].weights == null)
			return 0;
		return mLayers[0].neurons[0].weights.length - 1;
	}
	
	public int getOutputDimension()
	{
		if (mLayers == null || mLayers.length == 0 || mLayers[mLayers.length - 1] == null || mLayers[mLayers.length - 1].neurons == null)
			return 0;
		return mLayers[mLayers.length - 1].neurons.length;
	}
	
	/**
	 * Is everything ok?
	 * @param handler runs onError if not ok
	 * @return
	 */
	public boolean check(ParserResultHandler handler)
	{
		if (mLayers == null || mTrainingSet == null || mTrainingSet.inputs == null || mTrainingSet.inputs == null)
		{
			handler.onError(R.string.null_elements);
			return false;
		}
		if (mLayers.length < 1)
		{
			handler.onError(R.string.layers_missing);
			return false;
		}
		for (int i = 0; i < mLayers.length; i++)
		{
			if (mLayers[i].neurons.length < 1)
			{
				handler.onError(R.string.neurons_missing);
				return false;
			}
		}
		
		for (int i = 1; i < mLayers.length; i++)
		{
			if ((mLayers[i].neurons[0].weights.length - 1) != mLayers[i - 1].neurons.length)
			{
				handler.onError(R.string.weights_count);
				return false;
			}
		}
		
		for (int i = 0; i < mLayers.length; i++)
		{
			for (int j = 1; j < mLayers[i].neurons.length; j++)
			{
				if (mLayers[i].neurons[0].weights.length != mLayers[i].neurons[j].weights.length)
				{
					handler.onError(R.string.weights_count);
					return false;
				}
			}
		}
		
		if (mTrainingSet.inputs.length != mTrainingSet.outputs.length)
		{
			handler.onError(R.string.input_output_count);
			return false;
		}
		
		if (mTrainingSet.inputs.length == 0)
		{
			handler.onError(R.string.empty_training_set);
			return false;
		}
		
		for (int i = 0; i < mTrainingSet.inputs.length; i++)
		{
			if (mTrainingSet.inputs[i].length != getInputDimension())
			{
				handler.onError(R.string.input_example_dimension);
				return false;
			}
		}
		
		for (int i = 0; i < mTrainingSet.outputs.length; i++)
		{
			if (mTrainingSet.outputs[i].length != getOutputDimension())
			{
				handler.onError(R.string.output_example_dimension);
				return false;
			}
		}
			
		return true;
	}

	/**
	 * Parsing constructor
	 * @param layersData
	 */
	public Network(List<List<List<Double>>> layersData, List<List<List<Double>>> trainingData) {
		mLayers = new Layer[layersData.size()];
		for (int i = 0; i < mLayers.length; i++)
		{
			mLayers[i] = new Layer(layersData.get(i));
		}
		mTrainingSet = new TrainingSet(trainingData);
		initTraining(mTrainingSet);
	}
	
	public Network(int[] layersDimensions, TrainingSet training) {
		mLayers = new Layer[layersDimensions.length - 1];
		for (int i = 0; i < layersDimensions.length - 1; i++) {
			mLayers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i]);
		}
		initTraining(training);
	}
	
	public void restart()
	{
		for (int i = 0; i < mLayers.length; i++)
			for (int j = 0; j < mLayers[i].neurons.length; j++)
				mLayers[i].neurons[j].restart();
	}
	
	public void restartNeuron()
	{
		int layer = ((int) (Math.random() * mLayers.length)) % mLayers.length;
		int neuron = ((int) (Math.random() * mLayers[layer].neurons.length)) % mLayers[layer].neurons.length;
		

		mLayers[layer].neurons[neuron].restart();
	}

	/**
	 * Input and output are like in the training set, all scaling is done here
	 * @param input before scaling. Array data will not be changed in this method.
	 * @param output after scaling
	 */
	public void calculate(double[] input, double output[], boolean scaleOutput) {
		int i, j, k;

		for (i = 0; i < mLayers.length; i++) {
			Layer l = mLayers[i];
			for (j = 0; j < l.neurons.length; j++) {
				Neuron n = l.neurons[j];
				double potential = 0;
				for (k = 1; k < n.weights.length; k++) {
					potential += (i == 0 ? (mInputScale == null ? input[k - 1] : input[k - 1] * mInputScale[k - 1][0] + mInputScale[k - 1][1])
							: mLayers[i - 1].neurons[k - 1].output) * n.weights[k];
				}
				potential += n.weights[0];
				n.potential = potential;
				n.output = sigma(potential);
			}
		}
		Layer last = mLayers[mLayers.length - 1];
		for (i = 0; i < last.neurons.length; i++) {
			output[i] = last.neurons[i].output;
		}
		if (mOutputScale != null && scaleOutput)
		{
			for (i = 0; i < mOutputScale.length; i++)
			{
				output[i] = output[i] * mOutputScale[i][0] + mOutputScale[i][1]; 
			}
		}
	}

	private void initTraining(TrainingSet training) {
		mTrainingSet = training;
		mIteration = 0;
		
		mOutput = new double[getOutputDimension()];
		mInputScale = new double[getInputDimension()][];
		mOutputScale = new double[getOutputDimension()][];
		
		// default values
		for (int i = 0; i < mInputScale.length; i++)
		{
			mInputScale[i] = new double[2];
			mInputScale[i][0] = 1;
			mInputScale[i][1] = 0;
		}

		// default values
		for (int i = 0; i < mOutputScale.length; i++)
		{
			mOutputScale[i] = new double[2];
			mOutputScale[i][0] = 1;
			mOutputScale[i][1] = 0;
		}
		
		if (training == null || training.inputs.length == 0 || training.inputs[0].length == 0 || training.outputs[0].length == 0)
			return;

		// for each input dimension, calculate scale from (x1, x2) to <-1, 1>
		for (int i = 0; i < mInputScale.length; i++)
		{
			double min = training.inputs[0][i];
			double max = min;
			
			for (int j = 1; j < training.inputs.length; j++)
			{
				final double val = training.inputs[j][i]; 
				if (val < min)
					min = val;
				if (val > max)
					max = val;
			}
			//final double avg = sum / training.inputs.length;
			final double diff = max - min;
			
			if (diff == 0d)
			{
				mInputScale[i][0] = 1;
				mInputScale[i][1] = -max;
			}
			else
			{
				mInputScale[i][0] = 2.0 / diff;
				mInputScale[i][1] = -(max + min) / diff;
			}
		}
		
		// for each output dimension, calculate scale from (0, 1) to (y1, y2)
		for (int i = 0; i < mOutputScale.length; i++)
		{
			double min = training.outputs[0][i];
			double max = min;
			
			
			for (int j = 1; j < training.outputs.length; j++)
			{
				final double val = training.outputs[j][i]; 
				if (val < min)
					min = val;
				if (val > max)
					max = val;
			}
			final double diff = max - min;
			
			if (diff == 0d)
			{
				if (min <= 1 && min >= 0)
				{
					mOutputScale[i][0] = 1;
					mOutputScale[i][1] = 0;
		}
				else
				{
					mOutputScale[i][0] = 1;
					mOutputScale[i][1] = min;
				}
			}
			else
			{
				mOutputScale[i][0] = diff;
				mOutputScale[i][1] = min;
			}
		}

	}

	public double getError()
	{
		double sumError = 0;
		
		for (int i = 0; i < mTrainingSet.inputs.length; i++) {
			calculate(mTrainingSet.inputs[i], mOutput, true);
			for (int j = 0; j < mOutput.length; j++) {
				final double diff = mOutput[j] - mTrainingSet.outputs[i][j];
				sumError += diff * diff;
			}
		}
		return sumError * 0.5;
	}
	
	public double trainingStep() {
		int i, j, k, l;

		double sumError = 0;
		for (i = 0; i < mLayers.length; i++) {
			for (j = 0; j < mLayers[i].neurons.length; j++) {
				Neuron n = mLayers[i].neurons[j];
				for (k = 0; k < n.weights.length; k++) {
					n.weightsDerivation[k] = 0;
				}
			}
		}

		for (i = 0; i < mTrainingSet.inputs.length; i++) {
			calculate(mTrainingSet.inputs[i], mOutput, true);
			for (j = 0; j < mOutput.length; j++) {
				final double diff = mOutput[j] - mTrainingSet.outputs[i][j];
				sumError += diff * diff;
			}
			// from the last to the first layer 
			for (j = mLayers.length - 1; j >= 0; j--) { // backpropagation - go back
				// for each neuron in the layer
				for (k = 0; k < mLayers[j].neurons.length; k++) {
					Neuron n = mLayers[j].neurons[k];
					if (j == mLayers.length - 1) {
						// in the last layer calculate difference from the expected result
						n.derivation = n.output - (mTrainingSet.outputs[i][k] - mOutputScale[k][1]) / mOutputScale[k][0];
					} else {
						// in the hidden layer calculate the derivation by this form
						mLayers[j].neurons[k].derivation = 0;
						// for each neuron in the next layer
						for (l = 0; l < mLayers[j + 1].neurons.length; l++) {
							Neuron n2 = mLayers[j + 1].neurons[l];
							n.derivation += n2.derivation * n2.output
									* (1 - n2.output) * n2.weights[k + 1]; 
						}
					}
					for (l = 0; l < n.weights.length; l++) {
						n.weightsDerivation[l] += n.derivation
								* n.output * (1 - n.output)
								* ((l == 0) ? 1 : (j == 0 ? (mTrainingSet.inputs[i][l - 1] * mInputScale[l - 1][0] + mInputScale[l - 1][1]) : mLayers[j - 1].neurons[l - 1].output));
					}
				}
			}
		}
		
		synchronized (this) {
			for (i = 0; i < mLayers.length; i++) {
				for (j = 0; j < mLayers[i].neurons.length; j++) {
					for (k = 0; k < mLayers[i].neurons[j].weights.length; k++) {
						mLayers[i].neurons[j].weights[k] -= mAlpha * mLayers[i].neurons[j].weightsDerivation[k];
					}
				}
			}
		}
		sumError *= 0.5;
		mIteration++;
		return sumError;
	}
	
	public long getItration()
	{
		return mIteration;
	}
	
	protected void saveNumber(Writer writer, double number, int tabs) throws IOException
	{
		for (int i = 0; i < tabs; i++)
		{
			writer.write(Xml.TAB);
		}
		writer.write(Xml.TAG_START);
		writer.write(Xml.NUMBER);
		writer.write(Xml.TAG_END);
		
		writer.write(String.valueOf(number));
		
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.NUMBER);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
	}
	
	protected void saveInput(Writer writer, int i) throws IOException
	{
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.INPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int j = 0; j < mTrainingSet.inputs[i].length; j++)
		{
			saveNumber(writer, mTrainingSet.inputs[i][j], 4);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.INPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	protected void saveOutput(Writer writer, int i) throws IOException
	{
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int j = 0; j < mTrainingSet.outputs[i].length; j++)
		{
			saveNumber(writer, mTrainingSet.outputs[i][j], 4);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	protected void saveTraining(Writer writer) throws IOException
	{
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.TRAINING);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.INPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < mTrainingSet.inputs.length; i++)
		{
			saveInput(writer, i);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.INPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < mTrainingSet.inputs.length; i++)
		{
			saveOutput(writer, i);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.TRAINING);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	protected void saveLayers(Writer writer) throws IOException
	{
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.LAYERS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < mLayers.length; i++)
			mLayers[i].save(Xml.TAB + Xml.TAB, writer);
		
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.LAYERS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	
	public void save(Writer writer) throws IOException
	{
		writer.write(Xml.HEADER);
		writer.write(Xml.NEW_LINE);
		writer.write(Xml.TAG_START);
		writer.write(Xml.NETWORK);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		saveLayers(writer);
		saveTraining(writer);
		
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.NETWORK);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	public void save(String filename) throws IOException
	{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		save(writer);
		writer.close();
	}
}
