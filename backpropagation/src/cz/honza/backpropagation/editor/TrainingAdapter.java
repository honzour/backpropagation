package cz.honza.backpropagation.editor;

import java.util.ArrayList;

import android.content.Context;
import android.widget.ArrayAdapter;

public class TrainingAdapter extends ArrayAdapter<ArrayList<ArrayList<Double>>> {

	public TrainingAdapter(Context context, int resource,
			ArrayList<ArrayList<ArrayList<Double>>> data) {
		super(context, resource, data);
	}

}
