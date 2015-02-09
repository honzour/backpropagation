package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.parser.Csv;
import cz.honza.backpropagation.network.parser.ParserResultHandler;
import cz.honza.backpropagation.network.parser.Xml;
import cz.honza.backpropagation.network.trainingset.TrainingSet;
import cz.honza.backpropagation.network.trainingset.TrainingSetBase;



public class Network implements Serializable {
	
	private static final long serialVersionUID = -6200117127679042346L;
	
	public double[][] mInputScale;
	protected double[][] mOutputScale;
	
	protected double[] mOutput;
	
	public Layer[] mLayers;
	public double mAlpha = 1;
	public boolean mAutoAlpha = true;
	public static final double MAX_ALPHA = 100000000;
	public static final double MIN_ALPHA = 0.0000001;
	
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
		if (mLayers == null || mTrainingSet == null)
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
			
		return mTrainingSet.check(handler);
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
		mTrainingSet = new TrainingSetBase(trainingData);
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
		mAlpha = 1;
		for (int i = 0; i < mLayers.length; i++)
			for (int j = 0; j < mLayers[i].neurons.length; j++)
				mLayers[i].neurons[j].restart();
	}
	
	public void restartNeuron()
	{
		mAlpha = 1;
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
		
		if (training == null || training.length() == 0 || training.getInputDimension() == 0 ||
				training.getInputDimension() == 0)
			return;

		// for each input dimension, calculate scale from (x1, x2) to <-1, 1>
		for (int i = 0; i < mInputScale.length; i++)
		{
			double min = training.getInput(0, i);
			double max = min;
			
			for (int j = 1; j < training.length(); j++)
			{
				final double val = training.getInput(j, i); 
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
			double min = training.getOutput(0, i);
			double max = min;
			
			
			for (int j = 1; j < training.length(); j++)
			{
				final double val = training.getOutput(j, i); 
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

	private double[] mInputCache = null;
	
	public double getError()
	{
		double sumError = 0;
		
		if (mInputCache == null || mInputCache.length != mTrainingSet.getInputDimension())
		{
			mInputCache = new double[mTrainingSet.getInputDimension()];
		}
		
		for (int i = 0; i < mTrainingSet.length(); i++) {
			for (int j = 0; j < mTrainingSet.getInputDimension(); j++)
			{
				mInputCache[j] = mTrainingSet.getInput(i, j);
			}
			calculate(mInputCache, mOutput, true);
			for (int j = 0; j < mOutput.length; j++) {
				final double diff = mOutput[j] - mTrainingSet.getOutput(i, j);
				sumError += diff * diff;
			}
		}
		return sumError * 0.5;
	}
	
	public void trainingStep() {
		int i, j, k, l;
		
		double errorBefore = 0;
		double errorAfter = 0;
		
		if (mAutoAlpha)
		{
			errorBefore = getError();
		}

		for (i = 0; i < mLayers.length; i++) {
			for (j = 0; j < mLayers[i].neurons.length; j++) {
				Neuron n = mLayers[i].neurons[j];
				for (k = 0; k < n.weights.length; k++) {
					n.weightsDerivation[k] = 0;
				}
			}
		}
		
		if (mInputCache == null || mInputCache.length != mTrainingSet.getInputDimension())
		{
			mInputCache = new double[mTrainingSet.getInputDimension()];
		}

		for (i = 0; i < mTrainingSet.length(); i++) {
			for (j = 0; j < mTrainingSet.getInputDimension(); j++)
			{
				mInputCache[j] = mTrainingSet.getInput(i, j);
			}
			calculate(mInputCache, mOutput, true);
			// from the last to the first layer 
			for (j = mLayers.length - 1; j >= 0; j--) { // backpropagation - go back
				// for each neuron in the layer
				for (k = 0; k < mLayers[j].neurons.length; k++) {
					Neuron n = mLayers[j].neurons[k];
					if (j == mLayers.length - 1) {
						// in the last layer calculate difference from the expected result
						n.derivation = n.output - (mTrainingSet.getOutput(i, k) - mOutputScale[k][1]) / mOutputScale[k][0];
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
								* ((l == 0) ? 1 : (j == 0 ? (mTrainingSet.getInput(i, l - 1) * mInputScale[l - 1][0] + mInputScale[l - 1][1]) : mLayers[j - 1].neurons[l - 1].output));
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
		mIteration++;
		
		if (mAutoAlpha)
		{
			errorAfter = getError();
			if (errorAfter > errorBefore)
			{
				// cancel training step
				synchronized (this) {
					for (i = 0; i < mLayers.length; i++) {
						for (j = 0; j < mLayers[i].neurons.length; j++) {
							for (k = 0; k < mLayers[i].neurons[j].weights.length; k++) {
								mLayers[i].neurons[j].weights[k] += mAlpha * mLayers[i].neurons[j].weightsDerivation[k];
							}
						}
					}
				}
				
				mAlpha *= 0.5;
				if (mAlpha == 0)
					mAlpha = MIN_ALPHA;
			}
			else
			{
				mAlpha *= 1.01;
				if (mAlpha > MAX_ALPHA)
					mAlpha = MAX_ALPHA;
			}
		}
	}
	
	public long getItration()
	{
		return mIteration;
	}
	
	protected void saveLayersXml(Writer writer) throws IOException
	{
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.LAYERS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < mLayers.length; i++)
			mLayers[i].saveXml(Xml.TAB + Xml.TAB, writer);
		
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.LAYERS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	
	public void saveXml(Writer writer) throws IOException
	{
		writer.write(Xml.HEADER);
		writer.write(Xml.NEW_LINE);
		writer.write(Xml.TAG_START);
		writer.write(Xml.NETWORK);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		saveLayersXml(writer);
		mTrainingSet.saveXml(writer);
		
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.NETWORK);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	public void saveXml(String filename) throws IOException
	{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		saveXml(writer);
		writer.close();
	}
	
	protected void saveLayersCsv(Writer writer) throws IOException
	{
		
		writer.append(String.valueOf(getInputDimension()));
		writer.append(Csv.COMMA);
		for (int i = 0; i < mLayers.length; i++)
		{
			writer.append(String.valueOf(mLayers[i].neurons.length));
			if (i < mLayers.length - 1)
				writer.append(Csv.COMMA);
		}
		
		writer.write(Csv.NEW_LINE);
	}
	
	public void saveCsv(Writer writer) throws IOException
	{
		writer.append(";Format\n");
		writer.append("CSV1\n");
		writer.append(";Anatomy\n");
		saveLayersCsv(writer);
		writer.append(";Training set\n");
		mTrainingSet.saveCsv(writer);
	}
	
	public void saveCsv(String filename) throws IOException
	{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		saveCsv(writer);
		writer.close();
	}
}
