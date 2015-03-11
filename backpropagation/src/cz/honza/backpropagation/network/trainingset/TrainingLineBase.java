package cz.honza.backpropagation.network.trainingset;

import java.util.ArrayList;

import cz.honza.backpropagation.network.parser.Csv;

public class TrainingLineBase implements TrainingLine {

	private static final long serialVersionUID = -2777317734401540881L;
	
	public ArrayList<ArrayList<Double>> mData;
	
	public TrainingLineBase()
	{
		mData = new ArrayList<ArrayList<Double>>();
	}
	
	public String toString()
	{
		return mData.toString();
	}

	@Override
	public String getEditorType() {
		return Csv.SIMPLE;
	}

}
