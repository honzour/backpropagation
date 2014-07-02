package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

public class Network {
	
	public Layer[] layers;
	public double[][] inputs;
	public double[][] outputs;
	private long mIteration;
	public double alpha = 1;

	static double sigma(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	public Network(int[] layersDimensions, double[][] inputs, double[][] outputs) {
		layers = new Layer[layersDimensions.length - 1];
		for (int i = 0; i < layersDimensions.length - 1; i++) {
			layers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i]);
		}
		initTraining(inputs, outputs);
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

	public void calculate(double[] input, double output[]) {
		int i, j, k;

		for (i = 0; i < layers.length; i++) {
			Layer l = layers[i];
			for (j = 0; j < l.neurons.length; j++) {
				Neuron n = l.neurons[j];
				double potential = 0;
				for (k = 1; k < n.weights.length; k++) {
					potential += (i == 0 ? input[k - 1]
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
	}

	private void initTraining(double[][] inputs, double[][] outputs) {
		this.inputs = inputs;
		this.outputs = outputs;
		mIteration = 0;
	}

	public double getError()
	{
		double sumError = 0;
		double[] output = new double[layers[layers.length - 1].neurons.length];
		for (int i = 0; i < inputs.length; i++) {
			calculate(inputs[i], output);
			for (int j = 0; j < output.length; j++) {
				final double diff = output[j] - outputs[i][j];
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
		double[] output = new double[layers[layers.length - 1].neurons.length];
		for (i = 0; i < inputs.length; i++) {
			calculate(inputs[i], output);
			for (j = 0; j < output.length; j++) {
				final double diff = output[j] - outputs[i][j];
				sumError += diff * diff;
			}
			// from the last to the first layer 
			for (j = layers.length - 1; j >= 0; j--) { // backpropagation - go back
				// for each neuron in the layer
				for (k = 0; k < layers[j].neurons.length; k++) {
					Neuron n = layers[j].neurons[k];
					if (j == layers.length - 1) {
						// in the last layer calculate difference from the expected result
						n.derivation = n.output - outputs[i][k];
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
								* ((l == 0) ? 1 : (j == 0 ? inputs[i][l - 1] : layers[j - 1].neurons[l - 1].output));
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
	
	public void save(Writer writer) throws IOException
	{
		writer.write(Xml.HEADER);
		writer.write('\n');
		writer.write(Xml.TAG_START);
		writer.write(Xml.NETWORK);
		writer.write(Xml.TAG_END);
		
		for (int i = 0; i < layers.length; i++)
			layers[i].save(writer);
		
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.NETWORK);
		writer.write(Xml.TAG_END);
	}
	
	public void save(String filename) throws IOException
	{
		PrintWriter writer = new PrintWriter(filename, "UTF-8");
		save(writer);
		writer.close();
	}
}
