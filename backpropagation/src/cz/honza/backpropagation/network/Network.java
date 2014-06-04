package cz.honza.backpropagation.network;

public class Network {
	private Layer[] layers;
	private double sumError;
	private double[][] inputs;
	private double[][] outputs;

	static double sigma(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	public Network(int[] layersDimensions) {
		layers = new Layer[layersDimensions.length - 1];
		for (int i = 0; i < layersDimensions.length - 1; i++) {
			layers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i]);
		}
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

	public void initTraining(double[][] inputs, double[][] outputs) {
		sumError = -1;
		this.inputs = inputs;
		this.outputs = outputs;
	}

	public double trainingStep() {
		int i, j, k, l;

		sumError = 0;
		for (i = 0; i < layers.length; i++) {
			for (j = 0; j < layers[i].neurons.length; j++) {
				Neuron n = layers[i].neurons[j];
				for (k = 0; k < n.weights.length; k++) {
					n.weightsDerivation[k] = 0;
					n.moment = 0;
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
			for (j = layers.length - 1; j >= 0; j--) { // backpropagation
				for (k = 0; k < layers[j].neurons.length; k++) {
					Neuron n = layers[j].neurons[k];
					if (j == layers.length - 1) {
						n.derivation = n.output - outputs[i][k];
					} else {
						layers[j].neurons[k].derivation = 0;
						for (l = 0; l < layers[j + 1].neurons.length; l++) {
							Neuron n2 = layers[j + 1].neurons[l];
							n.derivation += n2.derivation * n2.output
									* (1 - n2.output) * n.weights[k];
						}
					}
					for (l = 0; l < n.weights.length; l++) {
						n.weightsDerivation[l] += n.derivation
								* n.output
								* (1 - n.output)
								* ((l == n.weights.length - 1) ? -1
										: (j == 0 ? inputs[i][l]
												: layers[j - 1].neurons[l].output));
					}
				}
			}
		}
		for (i = 0; i < layers.length; i++) {
			for (j = 0; j < layers[i].neurons.length; j++) {
				for (k = 0; k < layers[i].neurons[j].weights.length; k++) {
					layers[i].neurons[j].moment = 0.01
							* layers[i].neurons[j].weightsDerivation[k] + 0.0
							* layers[i].neurons[j].moment;
					layers[i].neurons[j].weights[k] -= layers[i].neurons[j].moment;
				}
			}
		}
		sumError *= 0.5;
		return sumError;
	}
}
