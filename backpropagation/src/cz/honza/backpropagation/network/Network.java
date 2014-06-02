package cz.honza.backpropagation.network;

public class Network {
	private Layer[] layers;
	private double sumError;

	static double sigma(double x) {
		return 1.0 / (1.0 + Math.exp(-x));
	}

	Network(int[] layersDimensions) {
		layers = new Layer[layersDimensions.length - 1];
		for (int i = 0; i < layersDimensions.length - 1; i++) {
			layers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i]);
		}
	}

	void calculate(double[] input, double output[]) {
		int i, j, k;

		for (i = 0; i < layers.length; i++) {
			Layer l = layers[i];
			for (j = 0; j < l.neurons.length; j++) {
				Neuron n = l.neurons[j];
				double potential = 0;
				for (k = 1; k < n.weights.length; k++) {
					potential += (i == 0 ? input[k] : layers[i - 1].neurons[k].output) * n.weights[k];
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

	void initTraining(double[][] inputs, double[][] outputs) {
		sumError = -1;
	}

	double trainingStep() {
		return 0;
	}
}
