package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

public class Layer {
	public Neuron[] neurons;
	
	public Layer(List<List<Double>> layersData)
	{
		// TODO
	}
	
	public Layer(int count, int previousCount)
	{
		neurons = new Neuron[count];
		for (int i = 0; i < count; i++)
			neurons[i] = new Neuron(previousCount);
	}
	
	public void save(String tabs, Writer writer) throws IOException
	{
		writer.write(tabs);
		writer.write(Xml.TAG_START);
		writer.write(Xml.LAYER);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < neurons.length; i++)
			neurons[i].save(tabs + Xml.TAB, writer);
		
		writer.write(tabs);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.LAYER);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
}
