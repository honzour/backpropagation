package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Writer;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.parser.Csv;
import cz.honza.backpropagation.network.parser.ParserResultHandler;

public class TrainingSetSingleTimeline implements TrainingSet {

	private static final long serialVersionUID = -2798729495999954517L;
	
	protected int mInputDimension;
	protected int mOutputDimension;
	protected double[] mTimeline;
	
	public TrainingSetSingleTimeline(int inputDimension, int outputDimension, double[] timeline)
	{
		mInputDimension = inputDimension;
		mOutputDimension = outputDimension;
		mTimeline = timeline;
	}

	@Override
	public void saveXml(Writer writer) throws IOException {
		TrainingUtil.saveXmlSimple(this, writer);
	}

	@Override
	public void saveCsv(Writer writer) throws IOException {
		writer.write(Csv.TIMELINE);
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
		return mTimeline.length - mInputDimension - mOutputDimension + 1;
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
}
