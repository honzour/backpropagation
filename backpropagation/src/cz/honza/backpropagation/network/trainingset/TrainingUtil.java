package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Writer;

import cz.honza.backpropagation.network.parser.Csv;
import cz.honza.backpropagation.network.parser.Xml;

public class TrainingUtil {
	
	public static void saveCsvSimple(TrainingSet set, Writer writer) throws IOException
	{
		writer.write(Csv.SIMPLE);
		writer.write(Csv.NEW_LINE);
		for (int i = 0; i < set.length(); i++)
		{
			for (int j = 0; j < set.getInputDimension(); j++)
			{
				writer.write(String.valueOf(set.getInput(i, j)));
				writer.write(Csv.COMMA);
			}
			writer.write(Csv.COMMA);
			for (int j = 0; j < set.getOutputDimension(); j++)
			{
				writer.write(String.valueOf(set.getOutput(i, j)));
				if (j < set.getOutputDimension() - 1)
					writer.write(Csv.COMMA);
			}
				
			writer.write(Csv.NEW_LINE);
		}
	}

	
	protected static void saveNumberXml(Writer writer, double number, int tabs) throws IOException
	{
		for (int i = 0; i < tabs; i++)
		{
			writer.write(Xml.TAB);
		}
		writer.write(Xml.TAG_START);
		writer.write(Xml.NUMBER);
		writer.write(Xml.TAG_END);
		
		writer.write(String.valueOf(number));
		
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.NUMBER);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	protected static void saveInputXml(TrainingSet set, Writer writer, int i) throws IOException
	{
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.INPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int j = 0; j < set.getInputDimension(); j++)
		{
			saveNumberXml(writer, set.getInput(i, j), 4);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.INPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	protected static void saveOutputXml(TrainingSet set, Writer writer, int i) throws IOException
	{
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int j = 0; j < set.getOutputDimension(); j++)
		{
			saveNumberXml(writer, set.getOutput(i, j), 4);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	public static void saveXml(TrainingSet set, Writer writer) throws IOException
	{
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.TRAINING);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.INPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < set.length(); i++)
		{
			saveInputXml(set, writer, i);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.INPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int i = 0; i < set.length(); i++)
		{
			saveOutputXml(set, writer, i);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUTS);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.TRAINING);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}

}
