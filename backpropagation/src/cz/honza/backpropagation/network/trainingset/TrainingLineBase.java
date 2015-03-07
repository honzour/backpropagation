package cz.honza.backpropagation.network.trainingset;

import java.util.ArrayList;

import cz.honza.backpropagation.network.parser.Csv;

public class TrainingLineBase extends ArrayList<ArrayList<Double>> implements TrainingLine {

	private static final long serialVersionUID = -2777317734400540881L;

	@Override
	public String getEditorType() {
		return Csv.SIMPLE;
	}

}
