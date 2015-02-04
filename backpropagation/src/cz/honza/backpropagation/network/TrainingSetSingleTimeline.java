package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Writer;

public class TrainingSetSingleTimeline implements TrainingSet {

	private static final long serialVersionUID = -2798729495999954517L;

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getInput(int inputIndex, int numberIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getOutput(int outputIndex, int numberIndex) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getInputDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getOutputDimension() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean check(ParserResultHandler handler) {
		// TODO Auto-generated method stub
		return false;
	}
}
