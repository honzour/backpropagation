package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class EditorActivity extends NetworkActivity {
	
	
	public static class SavedState
	{
		public ArrayList<ArrayList<ArrayList<Double>>> mTraining;
		public ArrayList<Integer> mLayers;
		
		public SavedState(ArrayList<ArrayList<ArrayList<Double>>> training,
				ArrayList<Integer> layers) {
			mTraining = training;
			mLayers = layers;
		}
	}
	
	public static final int REQUEST_CODE_ANATOMY = 1;
	public static final int REQUEST_CODE_TRAINING = 2;
	public static final String INTENT_EXTRA_ANATOMY = "INTENT_EXTRA_ANATOMY";
	public static final String INTENT_EXTRA_TRAINING = "INTENT_EXTRA_TRAINING";

	protected ArrayList<ArrayList<ArrayList<Double>>> mTraining;
	protected ArrayList<Integer> mLayers;
	
	protected void addTraining()
	{
		// TODO split with TrainingSetActivity implementation
		final ArrayList<ArrayList<Double>> item = new ArrayList<ArrayList<Double>>();
		final ArrayList<Double> inputItem = new ArrayList<Double>();
		final ArrayList<Double> outputItem = new ArrayList<Double>();
		
		item.add(inputItem);
		item.add(outputItem);
		mTraining.add(item);
	}
	
	protected void addVector(List<Double> list, StringBuffer sb)
	{
		sb.append('(');
		for (int i = 0; i < list.size(); i++)
		{
			if (i > 0)
			{
				sb.append(", ");
			}
			sb.append(String.valueOf(list.get(i)));
		}
		sb.append(')');
	}
	
	protected void refreshTraining()
	{
		final TextView training = (TextView)findViewById(R.id.edit_training_set);
		final StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mTraining.size(); i++)
		{
			if (i > 0)
				sb.append('\n');
			final ArrayList<ArrayList<Double>> item = mTraining.get(i);
			final ArrayList<Double> inputItem = item.get(0);
			final ArrayList<Double> outputItem = item.get(1);
			addVector(inputItem, sb);
			sb.append("->");
			addVector(outputItem, sb);
			
		}
		training.setText(sb.toString());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.editor);

		SavedState saved = (SavedState)getLastNonConfigurationInstance();
		if (saved != null)
		{
			mLayers = saved.mLayers;
			mTraining = saved.mTraining;
		}
		
		// init anatomy
		
		if (mLayers == null)
		{
			mLayers = new ArrayList<Integer>();
			mLayers.add(1);
			mLayers.add(1);
		}
		// inint training
		if (mTraining == null)
		{
			mTraining = new ArrayList<ArrayList<ArrayList<Double>>>();
			addTraining();
		}
		refreshAnatomy();
		refreshTraining();
		
		findViewById(R.id.edit_anatomy).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditorActivity.this, AnatomyActivity.class);
				intent.putExtra(INTENT_EXTRA_ANATOMY, mLayers);
				startActivityForResult(intent, REQUEST_CODE_ANATOMY);
			}
		});
	
		
		findViewById(R.id.edit_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditorActivity.this, TrainingSetActivity.class);
				intent.putExtra(INTENT_EXTRA_TRAINING, mTraining);
				startActivityForResult(intent, REQUEST_CODE_TRAINING);
			}
		});
		
		setCancelButton(R.id.editor_cancel);

	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return new SavedState(mTraining, mLayers);
	}
	
	protected void setToSize(List<Double> list, int requestedSize)
	{
		while (list.size() < requestedSize)
		{
			list.add(0d);
		}
		while (list.size() > requestedSize)
		{
			list.remove(list.size() - 1);
		}
	}
	
	protected void refreshAnatomy()
	{
		TextView anatomy = (TextView) findViewById(R.id.editor_anatomy);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < mLayers.size(); i++)
		{
			sb.append(String.valueOf(mLayers.get(i)));
			if (i < mLayers.size() - 1)
				sb.append('-');
		}
		anatomy.setText(sb.toString());
		if (mTraining != null)
		{
			final int inputDimension = mLayers.get(0);
			final int outputDimension = mLayers.get(mLayers.size() - 1);
			
			for (int i = 0; i < mTraining.size(); i++)
			{
				final ArrayList<ArrayList<Double>> item = mTraining.get(i);
				setToSize(item.get(0), inputDimension);
				setToSize(item.get(1), outputDimension);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
		{
			if (requestCode == REQUEST_CODE_ANATOMY)
			{
				mLayers = (ArrayList<Integer>)data.getSerializableExtra(INTENT_EXTRA_ANATOMY);
				refreshAnatomy();
				refreshTraining();
			}
			if (requestCode == REQUEST_CODE_TRAINING)
			{
				mTraining = (ArrayList<ArrayList<ArrayList<Double>>>)data.getSerializableExtra(INTENT_EXTRA_TRAINING);
				refreshTraining();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
