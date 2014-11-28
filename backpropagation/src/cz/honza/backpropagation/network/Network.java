package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import cz.honza.backpropagation.R;



public class Network implements Serializable {
	
	private static final long serialVersionUID = -6200117127679042346L;
	
	public double[][] inputScale;
	public double[][] outputScale;
	
	public Layer[] layers;
	public double alpha = 1;
	
	public TrainingSet trainingSet;
	
	private long mIteration;

	static double sigma(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}
	
	public int getInputDimension()
	{
		if (layers == null || layers.length == 0 || layers[0] == null || layers[0].neurons == null || layers[0].neurons[0] == null || layers[0].neurons[0].weights == null)
			return 0;
		return layers[0].neurons[0].weights.length - 1;
	}
	
	public int getOutputDimension()
	{
		if (layers == null || layers.length == 0 || layers[layers.length - 1] == null || layers[layers.length - 1].neurons == null)
			return 0;
		return layers[layers.length - 1].neurons.length;
	}
	
	/**
	 * Is everything ok?
	 * @param handler runs onError if not ok
	 * @return
	 */
	public boolean check(ParserResultHandler handler)
	{
		if (layers == null || trainingSet == null || trainingSet.inputs == null || trainingSet.inputs == null)
		{
			handler.onError(R.string.null_elements);
			return false;
		}
		if (layers.length < 1)
		{
			handler.onError(R.string.layers_missing);
			return false;
		}
		for (int i = 0; i < layers.length; i++)
		{
			if (layers[i].neurons.length < 1)
			{
				handler.onError(R.string.neurons_missing);
				return false;
			}
		}
		
		for (int i = 1; i < layers.length; i++)
		{
			if ((layers[i].neurons[0].weights.length - 1) != layers[i - 1].neurons.length)
			{
				handler.onError(R.string.weights_count);
				return false;
			}
		}
		
		for (int i = 0; i < layers.length; i++)
		{
			for (int j = 1; j < layers[i].neurons.length; j++)
			{
				if (layers[i].neurons[0].weights.length != layers[i].neurons[j].weights.length)
				{
					handler.onError(R.string.weights_count);
					return false;
				}
			}
		}
		
		if (trainingSet.inputs.length != trainingSet.outputs.length)
		{
			handler.onError(R.string.input_output_count);
			return false;
		}
		
		if (trainingSet.inputs.length == 0)
		{
			handler.onError(R.string.empty_training_set);
			return false;
		}
		
		for (int i = 0; i < trainingSet.inputs.length; i++)
		{
			if (trainingSet.inputs[i].length != getInputDimension())
			{
				handler.onError(R.string.input_example_dimension);
				return false;
			}
		}
		
		for (int i = 0; i < trainingSet.outputs.length; i++)
		{
			if (trainingSet.outputs[i].length != getOutputDimension())
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
		layers = new Layer[layersData.size()];
		for (int i = 0; i < layers.length; i++)
		{
			layers[i] = new Layer(layersData.get(i));
		}
		trainingSet = new TrainingSet(trainingData);
		initTraining(trainingSet);
	}
	
	public Network(int[] layersDimensions, TrainingSet training) {
		layers = new Layer[layersDimensions.length - 1];
		for (int i = 0; i < layersDimensions.length - 1; i++) {
			layers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i]);
		}
		initTraining(training);
	}
	
	public void restart()
	{
		for (int i = 0; i < layers.length; i++)
			for (int j = 0; j < layers[i].neurons.length; j++)
				layers[i].neurons[j].restart();
	}
	
	public void restartNeuron()
	{
		int layer = ((int) (Math.random() * layers.length)) % layers.length;
		int neuron = ((int) (Math.random() * layers[layer].neurons.length)) % layers[layer].neurons.length;
		

		layers[layer].neurons[neuron].restart();
	}

	/**
	 * Input and output are like in the training set, all scaling is done here
	 * @param input before scaling. Array data will not be changed in this method.
	 * @param output after scaling
	 */
	public void calculate(double[] input, double output[], boolean scaleOutput) {
		int i, j, k;

		for (i = 0; i < layers.length; i++) {
			Layer l = layers[i];
			for (j = 0; j < l.neurons.length; j++) {
				Neuron n = l.neurons[j];
				double potential = 0;
				for (k = 1; k < n.weights.length; k++) {
					potential += (i == 0 ? (inputScale == null ? input[k - 1] : input[k - 1] * inputScale[k - 1][0] + inputScale[k - 1][1])
							: layers[i - 1].neurons[k - 1].output) * n.weights[k];
				}
				potential += n.weights[0];
				n.potential = potential;
				n.output = sigma(potential);
			}
		}
		Layer last = layers[layers.length - 1];
		for (i = 0; i < last.neurons.length; i++) {
			output[i] = last.neurons[i].output;
		}
		if (outputScale != null && scaleOutput)
		{
			for (i = 0; i < outputScale.length; i++)
			{
				output[i] = output[i] * outputScale[i][0] + outputScale[i][1]; 
			}
		}
	}

	private void initTraining(TrainingSet training) {
		trainingSet = training;
		mIteration = 0;
		
		inputScale = new double[getInputDimension()][];
		outputScale = new double[getOutputDimension()][];
		
		// default values
		for (int i = 0; i < inputScale.length; i++)
		{
			inputScale[i] = new double[2];
			inputScale[i][0] = 1;
			inputScale[i][1] = 0;
		}

		// default values
		for (int i = 0; i < outputScale.length; i++)
		{
			outputScale[i] = new double[2];
			outputScale[i][0] = 1;
			outputScale[i][1] = 0;
		}
		
		if (training == null || training.inputs.length == 0 || training.inputs[0].length == 0 || training.outputs[0].length == 0)
			return;

		// for each input dimension, calculate scale from (x1, x2) to <-1, 1>
		for (int i = 0; i < inputScale.length; i++)
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
				inputScale[i][0] = 1;
				inputScale[i][1] = -max;
			}
			else
			{
				inputScale[i][0] = 2.0 / diff;
				inputScale[i][1] = -(max + min) / diff;
			}
		}
		
		// for each output dimension, calculate scale from (0, 1) to (y1, y2)
		for (int i = 0; i < outputScale.length; i++)
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
					outputScale[i][0] = 1;
					outputScale[i][1] = 0;
		}
				else
				{
					outputScale[i][0] = 1;
					outputScale[i][1] = min;
				}
			}
			else
			{
				outputScale[i][0] = diff;
				outputScale[i][1] = min;
			}
		}

	}

	public double getError()
	{
		double sumError = 0;
		double[] output = new double[getOutputDimension()];
		for (int i = 0; i < trainingSet.inputs.length; i++) {
			calculate(trainingSet.inputs[i], output, true);
			for (int j = 0; j < output.length; j++) {
				final double diff = output[j] - trainingSet.outputs[i][j];
				sumError += diff * diff;
			}
		}
		return sumError * 0.5;
	}
	
	public double trainingStep() {
		int i, j, k, l;

		double sumError = 0;
		for (i = 0; i < layers.length; i++) {
			for (j = 0; j < layers[i].neurons.length; j++) {
				Neuron n = layers[i].neurons[j];
				for (k = 0; k < n.weights.length; k++) {
					n.weightsDerivation[k] = 0;
				}
			}
		}
		double[] output = new double[getOutputDimension()];
		for (i = 0; i < trainingSet.inputs.length; i++) {
			calculate(trainingSet.inputs[i], output, true);
			for (j = 0; j < output.length; j++) {
				final double diff = output[j] - trainingSet.outputs[i][j];
				sumError += diff * diff;
			}
			// from the last to the first layer 
			for (j = layers.length - 1; j >= 0; j--) { // backpropagation - go back
				// for each neuron in the layer
				for (k = 0; k < layers[j].neurons.length; k++) {
					Neuron n = layers[j].neurons[k];
					if (j == layers.length - 1) {
						// in the last layer calculate difference from the expected result
						n.derivation = n.output - (trainingSet.outputs[i][k] - outputScale[k][1]) / outputScale[k][0];
					} else {
						// in the hidden layer calculate the derivation by this form
						layers[j].neurons[k].derivation = 0;
						// for each neuron in the next layer
						for (l = 0; l < layers[j + 1].neurons.length; l++) {
							Neuron n2 = layers[j + 1].neurons[l];
							n.derivation += n2.derivation * n2.output
									* (1 - n2.output) * n2.weights[k + 1]; 
						}
					}
					for (l = 0; l < n.weights.length; l++) {
						n.weightsDerivation[l] += n.derivation
								* n.output * (1 - n.output)
								* ((l == 0) ? 1 : (j == 0 ? (trainingSet.inputs[i][l - 1] * inputScale[l - 1][0] + inputScale[l - 1][1]) : layers[j - 1].neurons[l - 1].output));
					}
				}
			}
		}
		
		synchronized (this) {
			for (i = 0; i < layers.length; i++) {
				for (j = 0; j < layers[i].neurons.length; j++) {
					for (k = 0; k < layers[i].neurons[j].weights.length; k++) {
						layers[i].neurons[j].weights[k] -= alpha * layers[i].neurons[j].weightsDerivation[k];
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
		
		for (int j = 0; j < trainingSet.inputs[i].length; j++)
		{
			saveNumber(writer, trainingSet.inputs[i][j], 4);
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
		
		for (int j = 0; j < trainingSet.outputs[i].length; j++)
		{
			saveNumber(writer, trainingSet.outputs[i][j], 4);
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
		
		for (int i = 0; i < trainingSet.inputs.length; i++)
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
		
		for (int i = 0; i < trainingSet.inputs.length; i++)
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
		
		for (int i = 0; i < layers.length; i++)
			layers[i].save(Xml.TAB + Xml.TAB, writer);
		
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
