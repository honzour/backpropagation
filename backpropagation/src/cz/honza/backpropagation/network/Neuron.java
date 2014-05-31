package cz.honza.backpropagation.network;

public class Neuron {
	// weights[0] is -treshold
	public double[] weights;
	public double[] weightsDerivation;
	public double potential;
	public double output;
	public double derivation;
	public double moment;
	
	/**
	 * 
	 * @param inputCount number of inputs not counting treshold
	 */
	public Neuron(int inputCount)
	{
		inputCount++;
		moment = 0;
		weights = new double[inputCount];
		weightsDerivation = new double[inputCount];
		final double mul = 1.0d / inputCount;
		for (int i = 0; i < inputCount; i++)
		{
			weights[i] = Math.random() * mul;
			weightsDerivation[i] = 0;
		}
	}
}
