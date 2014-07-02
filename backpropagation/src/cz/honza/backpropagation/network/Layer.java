package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Writer;

public class Layer {
	public Neuron[] neurons;
	public Layer(int count, int previousCount)
	{
		neurons = new Neuron[count];
		for (int i = 0; i < count; i++)
			neurons[i] = new Neuron(previousCount);
	}
	
	public void save(Writer writer) throws IOException
	{
		writer.write(Xml.TAG_START);
		writer.write(Xml.LAYER);
		writer.write(Xml.TAG_END);
		
		for (int i = 0; i < neurons.length; i++)
			neurons[i].save(writer);
		
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.LAYER);
		writer.write(Xml.TAG_END);
	}
}
