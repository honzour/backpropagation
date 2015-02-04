package cz.honza.backpropagation.network;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;

public interface TrainingSet extends Serializable {
	void saveXml(Writer writer) throws IOException;
	void saveCsv(Writer writer) throws IOException;
	int length();
	double getInput(int inputIndex, int numberIndex);
	double getOutput(int outputIndex, int numberIndex);
	
	int getInputDimension();
	int getOutputDimension();
}
