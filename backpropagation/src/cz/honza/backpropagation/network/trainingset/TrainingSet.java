package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

import cz.honza.backpropagation.network.ParserResultHandler;

public interface TrainingSet extends Serializable {
	void saveXml(Writer writer) throws IOException;
	void saveCsv(Writer writer) throws IOException;
	int length();
	double getInput(int inputIndex, int numberIndex);
	double getOutput(int outputIndex, int numberIndex);
	
	int getInputDimension();
	int getOutputDimension();
	boolean check(ParserResultHandler handler);
}
