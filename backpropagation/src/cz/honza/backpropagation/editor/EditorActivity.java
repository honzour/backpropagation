package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.export.ImportActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.TrainingSet;

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
			Network network = (Network)getIntent().getSerializableExtra(ImportActivity.INTENT_EXTRA_NETWORK);
			if (network != null)
			{
				mLayers = new ArrayList<Integer>(network.layers.length);
				if (network.layers.length > 0 && network.layers[0].neurons.length > 0)
				{
					// input dimension
					mLayers.add(network.layers[0].neurons[0].weights.length - 1);
				}
				for (int i = 0; i < network.layers.length; i++)
				{
					mLayers.add(network.layers[i].neurons.length);
				}
				
				final int trainingSize = network.trainingSet.inputs.length;
				final int inputSize = network.trainingSet.inputs[0].length;
				final int outputSize = network.trainingSet.outputs[0].length;
				mTraining = new ArrayList<ArrayList<ArrayList<Double>>>(trainingSize);
				for (int i = 0; i < trainingSize; i++)
				{
					final ArrayList<ArrayList<Double>> item = new ArrayList<ArrayList<Double>>(2);
					final ArrayList<Double> input = new ArrayList<Double>(inputSize);
					final ArrayList<Double> output = new ArrayList<Double>(outputSize);
					for (int j = 0; j < inputSize; j++)
					{
						input.add(network.trainingSet.inputs[i][j]);
					}
					for (int j = 0; j < outputSize; j++)
					{
						output.add(network.trainingSet.outputs[i][j]);
					}
					item.add(input);
					item.add(output);
					mTraining.add(item);
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
		
		final int trainingSize = mTraining.size();
		
		final double[][] inputs = new double[trainingSize][];
		final double[][] outputs = new double[trainingSize][];
		
		if (trainingSize > 0)
		{
			final int inputDim = mTraining.get(0).get(0).size();
			final int outputDim = mTraining.get(0).get(1).size();
			for (int i = 0; i < trainingSize; i++)
			{
				final ArrayList<ArrayList<Double>> item = mTraining.get(i);
				inputs[i] = new double[inputDim];
				outputs[i] = new double[outputDim];
				for (int j = 0; j < inputDim; j++)
				{
					inputs[i][j] = item.get(0).get(j);
				}
				for (int j = 0; j < outputDim; j++)
				{
					outputs[i][j] = item.get(1).get(j);
				}
			}
		}
		
		TrainingSet training = new TrainingSet(inputs, outputs);
		
		
		NetworkApplication.sNetwork = new Network(layers, training);
		setResult(RESULT_OK);
		finish();
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
	
	@Override
	public String getHelpLink()
	{
		return "editor.php";
	}
}
