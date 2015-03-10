package cz.honza.backpropagation.network.trainingset;

import cz.honza.backpropagation.network.parser.Csv;

public class TrainingLineSingleTimeline implements TrainingLine {

	private static final long serialVersionUID = 6233134457374648904L;
	
	public double mValue;

	public TrainingLineSingleTimeline(double value)
	{
		mValue = value;
	}
	
	@Override
	public String toString()
	{
		return String.valueOf(mValue);
	}
	
	@Override
	public String getEditorType()
	{
		return Csv.TIMELINE;
	}

}
