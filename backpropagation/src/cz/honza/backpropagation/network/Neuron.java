package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.List;

public class Neuron implements Serializable {

	private static final long serialVersionUID = 1734654068548880380L;
	// weights[0] is -treshold
	public double[] weights;
	public double[] weightsDerivation;
	public double potential;
	public double output;
	public double derivation;
	public double moment;
	
	
	public Neuron(List<Double> weights)
	{
		int inputCount = weights.size();
		moment = 0;
		this.weights = new double[inputCount];
		weightsDerivation = new double[inputCount];
		
		for (int i = 0; i < inputCount; i++)
		{
			this.weights[i] = weights.get(i);
			weightsDerivation[i] = 0;
		}
	}
	
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
		restart();
	}
	
	public void restart()
	{
		int inputCount = weights.length;
		final double mul = 1.0d / inputCount;
		weights[0] = Math.random() * 2 - 1;
		weightsDerivation[0] = 0;
		for (int i = 1; i < inputCount; i++)
		{
			weights[i] = (Math.random() * 2 - 1) * mul;
			weightsDerivation[i] = 0;
		}
	}
	
	public String toString()
	{
		StringBuffer sb = new StringBuffer();
		sb.append('[');
		for (int i = 0; i < weights.length; i++)
		{				
			sb.append(weights[i]);
			if (i < weights.length - 1)
				sb.append(", ");
		}
		sb.append(']');
		return sb.toString();
	}
	
	public void saveXml(String tabs, Writer writer) throws IOException
	{
		writer.write(tabs);
		writer.write(Xml.TAG_START);
		writer.write(Xml.NEURON);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < weights.length; i++)
		{
			writer.write(tabs + Xml.TAB);
			writer.write(Xml.TAG_START);
			writer.write(Xml.WEIGHT);
			writer.write(Xml.TAG_END);
			writer.write(String.valueOf(weights[i]));
			writer.write(Xml.TAG_TERMINATE_START);
			writer.write(Xml.WEIGHT);
			writer.write(Xml.TAG_END);
			writer.write(Xml.NEW_LINE);
		}
		
		writer.write(tabs);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.NEURON);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
}
