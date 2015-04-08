package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.parser.Csv;
import cz.honza.backpropagation.network.parser.ParserResultHandler;

public class TrainingSetSingleSequence implements TrainingSet {

	private static final long serialVersionUID = -2798729495999954517L;
	
	protected int mInputDimension;
	protected int mOutputDimension;
	public double[] mTimeline;
	
	public TrainingSetSingleSequence(int inputDimension, int outputDimension, double[] timeline)
	{
		mInputDimension = inputDimension;
		mOutputDimension = outputDimension;
		mTimeline = timeline;
	}
	
	public TrainingSetSingleSequence(int inputDimension, int outputDimension)
	{
		mInputDimension = inputDimension;
		mOutputDimension = outputDimension;
		mTimeline = new double[inputDimension + outputDimension];
	}

	@Override
	public void saveXml(Writer writer) throws IOException {
		TrainingUtil.saveXmlTimeline(this, writer);
	}

	@Override
	public void saveCsv(Writer writer) throws IOException {
		writer.write(Csv.SEQUENCE);
		writer.write(Csv.NEW_LINE);
		for (int i = 0; i < mTimeline.length; i++)
		{
			writer.write(String.valueOf(mTimeline[i]));
			if (i < mTimeline.length - 1)
				writer.write(Csv.COMMA);
		}
		writer.write(Csv.NEW_LINE);
	}

	@Override
	public int length() {
		int length = mTimeline.length - mInputDimension - mOutputDimension + 1;
		if (length <= 0)
			return 0;
		return length;
	}

	@Override
	public double getInput(int inputIndex, int numberIndex) {
		return mTimeline[inputIndex + numberIndex];
	}

	@Override
	public double getOutput(int outputIndex, int numberIndex) {
		return mTimeline[mInputDimension + numberIndex + outputIndex];
	}

	@Override
	public int getInputDimension() {
		return mInputDimension;
	}

	@Override
	public int getOutputDimension() {
		return mOutputDimension;
	}

	@Override
	public boolean check(ParserResultHandler handler) {
		if (mTimeline.length < mInputDimension + mOutputDimension)
		{
			handler.onError(R.string.sequence_to_short);
			return false;
		}
		return true;
	}
	
	@Override
	public void add() {
		double[] timeline = new double[mTimeline.length + 1];
		System.arraycopy(mTimeline, 0, timeline, 0, mTimeline.length);
		timeline[mTimeline.length] = 0;
		mTimeline = timeline;
	}

	@Override
	public void setInputDimension(int dim) {
		mInputDimension = dim;
	}

	@Override
	public void setOutputDimension(int dim) {
		mOutputDimension = dim;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		return new TrainingSetSingleSequence(mInputDimension, mOutputDimension, mTimeline.clone());
	}

	@Override
	public void remove(int index) {
		double timeline[] = new double[mTimeline.length - 1];
		
		System.arraycopy(mTimeline, 0, timeline, 0, index);
		System.arraycopy(mTimeline, index + 1, timeline, index, timeline.length - index);
		
		mTimeline = timeline;
	}

	@Override
	public void set(int index, TrainingLine element) {
		mTimeline[index] = ((TrainingLineSingleSequence) element).mValue;
	}

	@Override
	public ArrayList<TrainingLine> getLines() {
		ArrayList<TrainingLine> list = new ArrayList<TrainingLine>();
		for (int i = 0; i < mTimeline.length; i++)
			list.add(new TrainingLineSingleSequence(mTimeline[i]));
		return list;
	}
	
	@Override
	public String getType() {
		return Csv.SEQUENCE;
	}
}
