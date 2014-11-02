package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class EditorActivity extends NetworkActivity {
	
	public static final int ANATOMY_REQUEST_CODE = 1;
	public static final int TRAINING_REQUEST_CODE = 2;
	public static final String INTENT_EXTRA_ANATOMY = "INTENT_EXTRA_ANATOMY"; 

	protected LinearLayout mTrainingLayout;
	protected List<List<List<Double>>> mTraining;
	protected ArrayList<Integer> mLayers;
	
	LayoutInflater mInflater;
	
	
	protected void refreshTraining()
	{
		mTrainingLayout.removeAllViews();
				
		for (int i = 0; i < mTraining.size(); i++)
		{
			final View item = mInflater.inflate(R.layout.editor_training_item, mTrainingLayout, false);
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
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLayers = (ArrayList<Integer>)getLastNonConfigurationInstance();
		if (mLayers == null)
		{
			mLayers = new ArrayList<Integer>();
			mLayers.add(1);
			mLayers.add(1);
		}
		mInflater = LayoutInflater.from(this);
		setContentView(R.layout.editor);
		
		refreshAnatomy();
		
		setCancelButton(R.id.editor_cancel);
		
		mTrainingLayout = (LinearLayout)findViewById(R.id.editor_training);
		
		
		if (mTraining == null)
		{
			mTraining = new ArrayList<List<List<Double>>>();
			addTraining();
		}
		
		refreshTraining();
		
		
		findViewById(R.id.editor_add_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addTraining();
			}
		});
		
		findViewById(R.id.edit_anatomy).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(EditorActivity.this, AnatomyActivity.class);
				intent.putExtra(INTENT_EXTRA_ANATOMY, mLayers);
				startActivityForResult(intent, ANATOMY_REQUEST_CODE);
			}
		});

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
