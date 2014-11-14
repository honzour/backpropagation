package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class TrainingSetActivity extends NetworkActivity {
	
	protected LinearLayout mTrainingLayout;
	protected ArrayList<ArrayList<ArrayList<Double>>> mTraining;
	protected ArrayList<Integer> mLayers;
	
	LayoutInflater mInflater;
	
	protected void addTraining()
	{
		save();
		final ArrayList<ArrayList<Double>> item = new ArrayList<ArrayList<Double>>();
		final ArrayList<Double> inputItem = new ArrayList<Double>();
		final ArrayList<Double> outputItem = new ArrayList<Double>();
		
		final int inputDim = mLayers.get(0);
		final int outputDim = mLayers.get(mLayers.size() - 1);
		
		for (int i = 0; i < inputDim; i++)
			inputItem.add(0d);
		for (int i = 0; i < outputDim; i++)
			outputItem.add(0d);
		
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
			
			final ArrayList<ArrayList<Double>> element = mTraining.get(i);
			final List<Double> elementInput = element.get(0);
			final List<Double> elementOutput = element.get(1);
			
			for (int j = 0; j < elementInput.size(); j++)
			{
				final EditText e = new EditText(this);
				e.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
				e.setText(String.valueOf(elementInput.get(j)));
				inputLayout.addView(e);
			}
			for (int j = 0; j < elementOutput.size(); j++)
			{
				final CheckBox cb = new CheckBox(this);
				cb.setChecked(elementOutput.get(j) >= 0.5);
				outputLayout.addView(cb);
			}
			
			mTrainingLayout.addView(item);
		}
	}
	
	protected void save()
	{
		final int count = mTrainingLayout.getChildCount();
		for (int i = 0; i < count; i++)
		{
			final View item = mTrainingLayout.getChildAt(i);
			final LinearLayout inputLayout = (LinearLayout)item.findViewById(R.id.editor_training_item_input);
			final LinearLayout outputLayout = (LinearLayout)item.findViewById(R.id.editor_training_item_output);
			final int inputCount = inputLayout.getChildCount();
			for (int j = 0; j < inputCount; j++)
			{
				final TextView input = (TextView)inputLayout.getChildAt(j);
				double d = 0;
				try
				{
					d = Double.valueOf(input.getText().toString());
				}
				catch (Exception e) {}
				mTraining.get(i).get(0).set(j, d);
			}
			final int outputCount = outputLayout.getChildCount();
			for (int j = 0; j < outputCount; j++)
			{
				final CheckBox output = (CheckBox)outputLayout.getChildAt(j);
				final double d = output.isChecked() ? 1d : 0d;
				mTraining.get(i).get(1).set(j, d);
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training);
		mTraining = (ArrayList<ArrayList<ArrayList<Double>>>)getLastNonConfigurationInstance();
		if (mTraining == null)
		{
			mTraining = (ArrayList<ArrayList<ArrayList<Double>>>)getIntent().getSerializableExtra(EditorActivity.INTENT_EXTRA_TRAINING);
		}
		mLayers = (ArrayList<Integer>)getIntent().getExtras().getSerializable(EditorActivity.INTENT_EXTRA_ANATOMY);
		mInflater = LayoutInflater.from(this);
		mTrainingLayout = (LinearLayout)findViewById(R.id.training_training);
		findViewById(R.id.training_add_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addTraining();
			}
		});
		refreshTraining();
	}
	
	@Override
	public void finish() {
		save();
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EditorActivity.INTENT_EXTRA_TRAINING, mTraining);
		setResult(RESULT_OK, resultIntent);
		super.finish();
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		save();
		return mTraining;
	}
	
	@Override
	public String getHelpLink()
	{
		return "trainingset.php";
	}
}
