package cz.honza.backpropagation.network;

public class TrainingSet {
	public double[][] inputs;
	public double[][] outputs;
	
	public TrainingSet(double[][] inputs, double[][] outputs)
	{
		this.inputs = inputs;
		this.outputs = outputs;
	}
}
