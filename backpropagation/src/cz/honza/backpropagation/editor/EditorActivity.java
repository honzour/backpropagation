package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.export.NewTaskActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.trainingset.TrainingSet;

public class EditorActivity extends NetworkActivity {
	
	
	public static class SavedState
	{
		public TrainingSet mTraining;
		public ArrayList<Integer> mLayers;
		
		public SavedState(TrainingSet training,
				ArrayList<Integer> layers) {
			mTraining = training;
			mLayers = layers;
		}
	}
	
	public static final int REQUEST_CODE_ANATOMY = 1;
	public static final int REQUEST_CODE_TRAINING = 2;
	public static final String INTENT_EXTRA_ANATOMY = "INTENT_EXTRA_ANATOMY";
	public static final String INTENT_EXTRA_TRAINING = "INTENT_EXTRA_TRAINING";

	protected TrainingSet mTraining;
	protected ArrayList<Integer> mLayers;

	protected void addTraining()
	{
		mTraining.add();
	}

	protected static void addVector(TrainingSet set, int line, boolean input, StringBuffer sb)
	{
		sb.append('(');
		for (int i = 0; i < (input ? set.getInputDimension() : set.getOutputDimension()); i++)
		{
			if (i > 0)
			{
				sb.append(", ");
			}
			sb.append(String.valueOf(input ? set.getInput(line, i) : set.getOutput(line, i)));
		}
		sb.append(')');
	}
	
	public static void addElement(TrainingSet set, int line, StringBuffer sb)
	{
		addVector(set, line, true, sb);
		sb.append("->");
		addVector(set, line, false, sb);
	}
	
	protected void refreshTraining()
	{
		final TextView training = (TextView)findViewById(R.id.edit_training_set);
		final StringBuffer sb = new StringBuffer();
		final int limit = 8;
		for (int i = 0; i < mTraining.length(); i++)
		{
			if (i >= limit)
			{
				sb.append("\n...");
				break;
			}
			if (i > 0)
				sb.append('\n');
			addElement(mTraining, i, sb);			
		}
		training.setText(sb.toString());
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setResult(RESULT_CANCELED);
		setContentView(R.layout.editor);

		SavedState saved = (SavedState)getLastNonConfigurationInstance();
		if (saved != null)
		{
			mLayers = saved.mLayers;
			mTraining = saved.mTraining;
		}
		else
		{
			Network network = (Network)getIntent().getSerializableExtra(NewTaskActivity.INTENT_EXTRA_NETWORK);
			if (network != null)
			{
				mLayers = new ArrayList<Integer>(network.mLayers.length);
				if (network.mLayers.length > 0 && network.mLayers[0].neurons.length > 0)
				{
					// input dimension
					mLayers.add(network.mLayers[0].neurons[0].weights.length - 1);
				}
				for (int i = 0; i < network.mLayers.length; i++)
				{
					mLayers.add(network.mLayers[i].neurons.length);
				}
				
				try
				{
					mTraining = (TrainingSet)network.mTrainingSet.clone();
				}
				catch (CloneNotSupportedException e)
				{
					// must not happen
					throw new RuntimeException(e);
				}
			}
		}
		
		// init anatomy
		
		if (mLayers == null)
		{
			mLayers = new ArrayList<Integer>();
			mLayers.add(1);
			mLayers.add(1);
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
				intent.putExtra(INTENT_EXTRA_ANATOMY, mLayers);
				intent.putExtra(INTENT_EXTRA_TRAINING, mTraining);
				startActivityForResult(intent, REQUEST_CODE_TRAINING);
			}
		});
		
		setCancelButton(R.id.editor_cancel);
		findViewById(R.id.editor_ok).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				saveAndExit();
			}
		});

	}
	
	@Override
	public boolean onKeyDown (int keyCode, KeyEvent event)
	{
		if (keyCode == KeyEvent.KEYCODE_BACK)
		{
			saveAndExit();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void saveAndExit()
	{
		final int[] layers = new int[mLayers.size()];
		for (int i = 0; i < layers.length; i++)
			layers[i] = mLayers.get(i);
		
		NetworkApplication.sNetwork = new Network(layers, mTraining);
		setResult(RESULT_OK);
		finish();
	}
	
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return new SavedState(mTraining, mLayers);
	}
	/*
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
	}*/
	
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
			
			mTraining.setInputDimension(inputDimension);
			mTraining.setOutputDimension(outputDimension);
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
				mTraining = (TrainingSet)data.getSerializableExtra(INTENT_EXTRA_TRAINING);
				refreshTraining();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public String getHelpLink()
	{
		return "editor.php";
	}
}
