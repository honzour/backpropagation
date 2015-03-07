package cz.honza.backpropagation.network.trainingset;

import java.io.IOException;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;

import cz.honza.backpropagation.network.parser.ParserResultHandler;

public interface TrainingSet extends Serializable {
	void saveXml(Writer writer) throws IOException;
	void saveCsv(Writer writer) throws IOException;
	int length();
	double getInput(int inputIndex, int numberIndex);
	double getOutput(int outputIndex, int numberIndex);
	
	int getInputDimension();
	int getOutputDimension();
	void setInputDimension(int dim);
	void setOutputDimension(int dim);
	boolean check(ParserResultHandler handler);
	void add();
	Object clone() throws CloneNotSupportedException;
	/**
	 * Removes the logical line. It is an example in the simple set or line element in single line training set.
	 * @param index element index.
	 */
	void remove(int index);
	void set(int index, TrainingLine element);
	ArrayList<TrainingLine> getLines();
}
