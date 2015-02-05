package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Writer;

import cz.honza.backpropagation.network.ParserResultHandler;

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
		// TODO Auto-generated method stub
	}

	@Override
	public void saveCsv(Writer writer) throws IOException {
		// TODO Auto-generated method stub
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
		return mTimeline[mInputDimension + 1 + numberIndex];
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
		// TODO
		return false;
	}
}
