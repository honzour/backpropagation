package cz.honza.backpropagation.network;

public class Network {
	private Layer[] layers;
	private double sumError;
	private double[][] inputs;
	private double[][] outputs;

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
		    double[] output;
		    for (i = 0; i < inputs.length; i++) {
		      vypocet(t[i][0], vystup);
		      for (j = 0; j < vystup.size(); j++) {
		        sumChyba += ((vystup[j] - t[i][1][j]) * (vystup[j] - t[i][1][j]));
		      }
		      for (j = size() - 1; j >= 0; j--) { // backpropagation
		        for (k = 0; k < (*this)[j].size(); k++) {
		          if (j == size() - 1) {
		            (*this)[j][k].derivace = (*this)[j][k].vystup - t[i][1][k];
		          } else {
		            (*this)[j][k].derivace = 0;
		            for (l = 0; l < (*this)[j + 1].size(); l++) {
		              (*this)[j][k].derivace += 
		               (*this)[j + 1][l].derivace *
		               (*this)[j + 1][l].vystup *
		               (1 - (*this)[j + 1][l].vystup) *
		               (*this)[j + 1][l][k];
		            }
		          }
		          for (l = 0; l < (*this)[j][k].size(); l++) {
		            (*this)[j][k].derivaceVah[l] +=
		              (*this)[j][k].derivace *
		              (*this)[j][k].vystup *
		              (1 - (*this)[j][k].vystup) *
		              ((l == (*this)[j][k].size() - 1) ? -1 : 
		                (j == 0 ? t[i][0][l] : (*this)[j - 1][l].vystup));
		          }
		        }
		      }
		    }
		    for (i = 0; i < size(); i++) {
		      for (j = 0; j < (*this)[i].size(); j++) {
		        for (k = 0; k < (*this)[i][j].size(); k++) {
		          (*this)[i][j].moment = 
		            0.01 * (*this)[i][j].derivaceVah[k] + 0.0 * (*this)[i][j].moment;
		          (*this)[i][j][k] -= (*this)[i][j].moment;
		        }
		      }
		    }
		    sumError /= 2;
		  return sumError;
	}
}
