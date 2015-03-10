package cz.honza.backpropagation.editor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.network.trainingset.TrainingLine;
import cz.honza.backpropagation.network.trainingset.TrainingSet;

public class TrainingSetActivity extends NetworkActivity {
	
	protected ListView mList;
	protected TrainingSet mTraining;
	protected TrainingAdapter mAdapter;
	protected LayoutInflater mInflater;
	
	protected void addTraining()
	{
		mTraining.add();
		ArrayList<TrainingLine> lines = mTraining.getLines();
		mAdapter.add(lines.get(lines.size() - 1));
	}
	
	protected void refreshTraining()
	{
		mAdapter = new TrainingAdapter(this, R.layout.training_list_item, mTraining);
		mList.setAdapter(mAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training);
		mTraining = (TrainingSet)getLastNonConfigurationInstance();
		if (mTraining == null)
		{
			mTraining = (TrainingSet)getIntent().getSerializableExtra(EditorActivity.INTENT_EXTRA_TRAINING);
		}
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
		final TrainingLine element = (TrainingLine)data.getSerializableExtra(EditorActivity.INTENT_EXTRA_TRAINING);
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
