package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.parser.ParserResultHandler;

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

	@Override
	public void saveXml(Writer writer) throws IOException {
		TrainingUtil.saveXmlSimple(this, writer);
	}

	@Override
	public void saveCsv(Writer writer) throws IOException {
		TrainingUtil.saveCsvSimple(this, writer);
	}

	@Override
	public void add() {
		double[][] inputs = new double[mInputs.length + 1][];
		double[][] outputs = new double[mOutputs.length + 1][];;
		
		System.arraycopy(mInputs, 0, inputs, 0, mInputs.length);
		System.arraycopy(mOutputs, 0, outputs, 0, mOutputs.length);
		
		inputs[mInputs.length] = new double[mInputs[0].length];
		outputs[mOutputs.length] = new double[mOutputs[0].length];
		
		mInputs = inputs;
		mOutputs = outputs;
	}

	@Override
	public void setInputDimension(int dim) {
		if (dim == getInputDimension()) return;
		for (int i = 0; i < mInputs.length; i++)
		{
			final double[] input = new double[dim];
			System.arraycopy(mInputs[i], 0, input, 0, input.length < mInputs[i].length ? input.length : mInputs[i].length);
			mInputs[i] = input;
		}
	}

	@Override
	public void setOutputDimension(int dim) {
		if (dim == getOutputDimension()) return;
		for (int i = 0; i < mOutputs.length; i++)
		{
			final double[] output = new double[dim];
			System.arraycopy(mOutputs[i], 0, output, 0, output.length < mOutputs[i].length ? output.length : mOutputs[i].length);
			mOutputs[i] = output;
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException
	{
		double inputs[][] = new double[mInputs.length][];
		double outputs[][] = new double[mOutputs.length][];
		
		for (int i = 0; i < inputs.length; i++)
			inputs[i] = mInputs[i];
		
		for (int i = 0; i < outputs.length; i++)
			outputs[i] = mOutputs[i];
		
		return new TrainingSetBase(inputs, outputs);
	}

	@Override
	public void remove(int index) {
		double inputs[][] = new double[mInputs.length - 1][];
		double outputs[][] = new double[mOutputs.length - 1][];
		
		System.arraycopy(mInputs, 0, inputs, 0, index);
		System.arraycopy(mInputs, index + 1, inputs, index, inputs.length - index);
		
		System.arraycopy(mOutputs, 0, outputs, 0, index);
		System.arraycopy(mOutputs, index + 1, outputs, index, outputs.length - index);
		
		mInputs = inputs;
		mOutputs = outputs;
	}

	@Override
	public void set(int index, TrainingLine element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<TrainingLine> getLines() {
		ArrayList<TrainingLine> list = new ArrayList<TrainingLine>();
		for (int i = 0; i < mInputs.length; i++)
		{
			final TrainingLineBase item = new TrainingLineBase();
			final ArrayList<Double> inputItem = new ArrayList<Double>();
			final ArrayList<Double> outputItem = new ArrayList<Double>();
			
			for (int j = 0; j < mInputs[i].length; j++)
				inputItem.add(mInputs[i][j]);
			
			for (int j = 0; j < mOutputs[i].length; j++)
				outputItem.add(mOutputs[i][j]);
			
			item.add(inputItem);
			item.add(outputItem);
			list.add(item);
		}
		return list;
	}
}
