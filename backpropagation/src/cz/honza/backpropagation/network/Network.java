package cz.honza.backpropagation.network;

public class Network {
	private Layer[] layers;
	private double sumError;
	
	static double sigma(double x)
	{
		  return 1.0 / (1.0 + Math.exp(-x));
	}
	
	Network(int[] layersDimensions)
	{
		layers = new Layer[layersDimensions.length - 1];
		for (int i = 0; i < layersDimensions.length - 1; i++)
		{
			layers[i] = new Layer(layersDimensions[i + 1], layersDimensions[i]);
		}
	}
	
	void calculate(double[] input, double output[])
	{
		
	}
	
	void initTraining(double[][] inputs, double[][] outputs)
	{
		sumError = -1;
	}
	
	double trainingStep()
	{
		return 0;
	}
}
