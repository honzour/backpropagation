package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class TrainingSetActivity extends NetworkActivity {
	
	protected LinearLayout mTrainingLayout;
	protected List<List<List<Double>>> mTraining;
	
	LayoutInflater mInflater;
	
	protected void addTraining()
	{
		List<List<Double>> item = new ArrayList<List<Double>>();
		List<Double> inputItem = new ArrayList<Double>();
		List<Double> outputItem = new ArrayList<Double>();
		
		item.add(inputItem);
		item.add(outputItem);
		mTraining.add(item);
		refreshTraining();
	}
	
	protected void refreshTraining()
	{
		mTrainingLayout.removeAllViews();
				
		for (int i = 0; i < mTraining.size(); i++)
		{
			final View item = mInflater.inflate(R.layout.training_item, mTrainingLayout, false);
			final View delete = item.findViewById(R.id.editor_training_item_delete);
			final int finali = i;
			
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mTraining.remove(finali);
					refreshTraining();
				}
			});
			
			final LinearLayout inputLayout = (LinearLayout)item.findViewById(R.id.editor_training_item_input);
			final LinearLayout outputLayout = (LinearLayout)item.findViewById(R.id.editor_training_item_output);
			
			final List<List<Double>> element = mTraining.get(i);
			final List<Double> elementInput = element.get(0);
			final List<Double> elementOutput = element.get(1);
			
			for (int j = 0; j < elementInput.size(); j++)
				inputLayout.addView(new EditText(this));
			for (int j = 0; j < elementOutput.size(); j++)
				outputLayout.addView(new CheckBox(this));
			
			mTrainingLayout.addView(item);
		}
	}
	
	

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training);
		mTraining = (List<List<List<Double>>>)getIntent().getSerializableExtra(EditorActivity.INTENT_EXTRA_TRAINING);
		mInflater = LayoutInflater.from(this);
		mTrainingLayout = (LinearLayout)findViewById(R.id.training_training);
		findViewById(R.id.training_add_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addTraining();
			}
		});
	}

}
