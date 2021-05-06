package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

import cz.honza.backpropagation.network.parser.Xml;

public class Layer implements Serializable {
	private static final long serialVersionUID = -4328364654490455402L;
	public Neuron[] neurons;
	
	public Layer(List<List<Double>> layersData)
	{
		final int count = layersData.size();
		
		neurons = new Neuron[count];
		for (int i = 0; i < count; i++)
			neurons[i] = new Neuron(layersData.get(i));
	}
	
	public Layer(int count, int previousCount)
	{
		neurons = new Neuron[count];
		for (int i = 0; i < count; i++)
			neurons[i] = new Neuron(previousCount);
	}
	
	public void saveXml(String tabs, Writer writer) throws IOException
	{
		writer.write(tabs);
		writer.write(Xml.TAG_START);
		writer.write(Xml.LAYER);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < neurons.length; i++)
			neurons[i].saveXml(tabs + Xml.TAB, writer);
		
		writer.write(tabs);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.LAYER);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}

	public void saveJava(Writer writer) throws IOException
	{

		writer.write("\t\t// the next layer\n");
		writer.write("\t\tnext = new double[" + neurons.length + "];\n");
		for (int i = 0; i < neurons.length; i++) {
			neurons[i].saveJava(i, writer);
		}

	}

}
