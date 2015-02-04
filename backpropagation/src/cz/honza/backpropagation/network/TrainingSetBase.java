package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import cz.honza.backpropagation.R;

public class TrainingSetBase implements TrainingSet {
	private static final long serialVersionUID = 3556087741395041118L;
	public double[][] mInputs;
	public double[][] mOutputs;
	
	public TrainingSetBase(double[][] inputs, double[][] outputs)
	{
		mInputs = inputs;
		mOutputs = outputs;
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
	
	public TrainingSetBase(List<List<List<Double>>> trainingData)
	{
		final List<List<Double>> inputs = trainingData.get(0); 
		final List<List<Double>> outputs = trainingData.get(1);
		
		this.mInputs = list2array(inputs);
		this.mOutputs = list2array(outputs);
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
	
	protected void saveInputXml(Writer writer, int i) throws IOException
	{
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.INPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int j = 0; j < mInputs[i].length; j++)
		{
			saveNumberXml(writer, mInputs[i][j], 4);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.INPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	protected void saveOutputXml(Writer writer, int i) throws IOException
	{
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_START);
		writer.write(Xml.OUTPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
		
		for (int j = 0; j < mOutputs[i].length; j++)
		{
			saveNumberXml(writer, mOutputs[i][j], 4);
		}
		
		writer.write(Xml.TAB); writer.write(Xml.TAB); writer.write(Xml.TAB);
		writer.write(Xml.TAG_TERMINATE_START);
		writer.write(Xml.OUTPUT);
		writer.write(Xml.TAG_END);
		writer.write(Xml.NEW_LINE);
	}
	
	public void saveXml(Writer writer) throws IOException
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
		
		for (int i = 0; i < mInputs.length; i++)
		{
			saveInputXml(writer, i);
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
		
		for (int i = 0; i < mInputs.length; i++)
		{
			saveOutputXml(writer, i);
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
	
	public void saveCsv(Writer writer) throws IOException
	{
		for (int i = 0; i < mInputs.length; i++)
		{
			for (int j = 0; j < mInputs[i].length; j++)
			{
				writer.write(String.valueOf(mInputs[i][j]));
				writer.write(Csv.COMMA);
			}
			writer.write(Csv.COMMA);
			for (int j = 0; j < mOutputs[i].length; j++)
			{
				writer.write(String.valueOf(mOutputs[i][j]));
				if (j < mOutputs[i].length - 1)
					writer.write(Csv.COMMA);
			}
				
			writer.write(Csv.NEW_LINE);
		}
	}

	@Override
	public int length() {
		return mInputs.length;
	}

	@Override
	public double getInput(int inputIndex, int numberIndex) {
		return mInputs[inputIndex][numberIndex];
	}

	@Override
	public double getOutput(int outputIndex, int numberIndex) {
		return mOutputs[outputIndex][numberIndex];
	}

	@Override
	public int getInputDimension()
	{
		return mInputs[0].length;
	}
	
	@Override
	public int getOutputDimension()
	{
		return mOutputs[0].length;
	}
	
	@Override
	public boolean check(ParserResultHandler handler)
	{
		if (mInputs == null || mInputs == null)
		{
			handler.onError(R.string.null_elements);
			return false;
		}
		if (mInputs.length != mOutputs.length)
		{
			handler.onError(R.string.input_output_count);
			return false;
		}
		
		if (mInputs.length == 0)
		{
			handler.onError(R.string.empty_training_set);
			return false;
		}
		
		for (int i = 0; i < mInputs.length; i++)
		{
			if (mInputs[i].length != getInputDimension())
			{
				handler.onError(R.string.input_example_dimension);
				return false;
			}
		}
		
		for (int i = 0; i < mOutputs.length; i++)
		{
			if (mOutputs[i].length != getOutputDimension())
			{
				handler.onError(R.string.output_example_dimension);
				return false;
			}
		}
		return true;
	}
}
