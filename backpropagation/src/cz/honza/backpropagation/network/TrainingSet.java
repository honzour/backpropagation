package cz.honza.backpropagation.network;

import java.util.List;

public class TrainingSet {
	public double[][] inputs;
	public double[][] outputs;
	
	public TrainingSet(double[][] inputs, double[][] outputs)
	{
		this.inputs = inputs;
		this.outputs = outputs;
	}
	
	protected double[][] list2array(List<List<Double>> list)
	{
		final int size = list.size();
		final double[][] array = new double[size][];
		for (int i = 0; i < size; i++)
		{
			final List<Double> item = list.get(i);
			final int itemSize = item.size();
			array[i] = new double[itemSize];
			for (int j = 0; j < itemSize; j++)
			{
				array[i][j] = item.get(j).doubleValue();
			}
		}
		return array;
	}
	
	public TrainingSet(List<List<List<Double>>> trainingData)
	{
		final List<List<Double>> inputs = trainingData.get(0); 
		final List<List<Double>> outputs = trainingData.get(1);
		
		this.inputs = list2array(inputs);
		this.outputs = list2array(outputs);
	}
}
