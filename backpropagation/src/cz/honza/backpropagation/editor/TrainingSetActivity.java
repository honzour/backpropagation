package cz.honza.backpropagation.editor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class TrainingSetActivity extends NetworkActivity {
	
	protected ListView mList;
	protected ArrayList<ArrayList<ArrayList<Double>>> mTraining;
	protected ArrayList<Integer> mLayers;
	protected TrainingAdapter mAdapter;
	protected LayoutInflater mInflater;
	
	protected void addTraining()
	{
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
		mAdapter.add(item);
	}
	
	protected void refreshTraining()
	{
		mAdapter = new TrainingAdapter(this, R.layout.training_list_item, mTraining);
		mList.setAdapter(mAdapter);
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
		mList = (ListView)findViewById(R.id.training_list);
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
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EditorActivity.INTENT_EXTRA_TRAINING, mTraining);
		setResult(RESULT_OK, resultIntent);
		super.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		final ArrayList<ArrayList<Double>> element = (ArrayList<ArrayList<Double>>)data.getSerializableExtra(EditorActivity.INTENT_EXTRA_TRAINING);
		if (element == null)
		{
			mTraining.remove(requestCode);
			refreshTraining();
		}
		else
		{
			mTraining.set(requestCode, element);
			refreshTraining();
		}
	}

	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return mTraining;
	}
	
	@Override
	public String getHelpLink()
	{
		return "trainingset.php";
	}
}
