package cz.honza.backpropagation.editor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class EditorActivity extends NetworkActivity {
	
	public static final int ANATOMY_REQUEST_CODE = 1;
	public static final int TRAINING_REQUEST_CODE = 2;
	public static final String INTENT_EXTRA_ANATOMY = "INTENT_EXTRA_ANATOMY";
	public static final String INTENT_EXTRA_TRAINING = "INTENT_EXTRA_TRAINING";

	protected ArrayList<ArrayList<ArrayList<Double>>> mTraining;
	protected ArrayList<Integer> mLayers;
	
	protected void addTraining()
	{
		// TODO split with TrainingSetActivity implementation
		ArrayList<ArrayList<Double>> item = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> inputItem = new ArrayList<Double>();
		ArrayList<Double> outputItem = new ArrayList<Double>();
		
		item.add(inputItem);
		item.add(outputItem);
		mTraining.add(item);
	}
	
	protected void refreshTraining()
	{
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.editor);

		// init anatomy
		mLayers = (ArrayList<Integer>)getLastNonConfigurationInstance();
		if (mLayers == null)
		{
			mLayers = new ArrayList<Integer>();
			mLayers.add(1);
			mLayers.add(1);
		}
		refreshAnatomy();

		// inint training
		if (mTraining == null)
		{
			mTraining = new ArrayList<ArrayList<ArrayList<Double>>>();
			addTraining();
		}
		refreshTraining();
		
		findViewById(R.id.edit_anatomy).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditorActivity.this, AnatomyActivity.class);
				intent.putExtra(INTENT_EXTRA_ANATOMY, mLayers);
				startActivityForResult(intent, ANATOMY_REQUEST_CODE);
			}
		});
	
		
		findViewById(R.id.edit_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditorActivity.this, TrainingSetActivity.class);
				intent.putExtra(INTENT_EXTRA_TRAINING, mTraining);
				startActivityForResult(intent, TRAINING_REQUEST_CODE);
			}
		});
		
		setCancelButton(R.id.editor_cancel);

	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return mLayers;
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
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK)
		{
			if (requestCode == ANATOMY_REQUEST_CODE)
			{
				mLayers = (ArrayList<Integer>)data.getSerializableExtra(INTENT_EXTRA_ANATOMY);
				refreshAnatomy();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	
}
