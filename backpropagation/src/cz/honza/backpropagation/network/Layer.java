package cz.honza.backpropagation.network;

public class Layer {
	public Neuron[] neurons;
	public Layer(int count, int previousCount)
	{
		neurons = new Neuron[count];
		for (int i = 0; i < count; i++)
			neurons[i] = new Neuron(previousCount);
	}
}
