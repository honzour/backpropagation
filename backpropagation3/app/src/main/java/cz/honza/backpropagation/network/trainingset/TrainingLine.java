package cz.honza.backpropagation.network.trainingset;

import java.io.Serializable;

public interface TrainingLine extends Serializable {
	public String toString();
	public String getEditorType();
}
