package cz.honza.backpropagation.export;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class EditorActivity extends NetworkActivity {
	
	protected LinearLayout mLayersLayout;
	protected List<Integer> mLayers;
	protected LinearLayout mTrainingLayout;
	protected List<List<List<Double>>> mTraining;
	LayoutInflater mInflater;
	
	protected void addLayersCaption(int res)
	{
		View captionLayout = mInflater.inflate(R.layout.editor_item_text, mLayersLayout, false);
		TextView caption = (TextView)captionLayout.findViewById(R.id.editor_item_text_text);
		caption.setText(res);
		mLayersLayout.addView(caption);
	}

	
	protected void refreshLayers()
	{
		mLayersLayout.removeAllViews();
		
		addLayersCaption(R.string.input_dimension);
				
		for (int i = 0; i < mLayers.size(); i++)
		{
			if (i == mLayers.size() - 1)
				addLayersCaption(R.string.output_layer);
			else
				if (i == 1)
					addLayersCaption(R.string.hidden_layers);
			final View item = mInflater.inflate(R.layout.editor_layer_item, mLayersLayout, false);
			final TextView tv = (TextView)item.findViewById(R.id.editor_item_text);
			final int val = mLayers.get(i);
			tv.setText(String.valueOf(val));
			final int finali = i;
			
			final View plus = item.findViewById(R.id.editor_item_plus);
			final View minus = item.findViewById(R.id.editor_item_minus);
			final View delete = item.findViewById(R.id.editor_item_delete);
			
			if (val < 2)
				minus.setEnabled(false);
			if (mLayers.size() < 3)
				delete.setEnabled(false);
			
			plus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mLayers.set(finali, val + 1);
					refreshLayers();
				}
			});
			
			
			minus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mLayers.set(finali, val - 1);
					refreshLayers();
				}
			});
			
			
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mLayers.remove(finali);
					refreshLayers();
				}
			});
			
			mLayersLayout.addView(item);
		}
	}
	
	
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
			
			mTrainingLayout.addView(item);
		}
	}
	
	protected void addTraining()
	{
		List<List<Double>> item = new ArrayList<List<Double>>();
		List<Double> inputItem = new ArrayList<Double>();
		List<Double> outputItem = new ArrayList<Double>();
		for (int i = 0; i < mLayers.get(0); i++)
		{
			inputItem.add(0.0);
		}
		for (int i = 0; i < mLayers.get(mLayers.size() - 1); i++)
		{
			outputItem.add(0.0);
		}
		item.add(inputItem);
		item.add(outputItem);
		mTraining.add(item);
		refreshTraining();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mInflater = LayoutInflater.from(this);
		setContentView(R.layout.editor);
		setCancelButton(R.id.editor_cancel);
		mLayersLayout = (LinearLayout)findViewById(R.id.editor_data);
		mTrainingLayout = (LinearLayout)findViewById(R.id.editor_training);
		List<Object> l = (List<Object>)getLastNonConfigurationInstance();
		if (l != null)
		{
			mLayers = (List<Integer>)l.get(0);
			mTraining = (List<List<List<Double>>>)l.get(1);
		}
		if (mLayers == null)
		{
			mLayers = new ArrayList<Integer>();
			mLayers.add(1);
			mLayers.add(1);
		}
		
		if (mTraining == null)
		{
			mTraining = new ArrayList<List<List<Double>>>();
			addTraining();
		}
		refreshLayers();
		refreshTraining();
		findViewById(R.id.editor_add_layer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLayers.add(1);
				refreshLayers();
			}
		});
		
		findViewById(R.id.editor_add_training).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				addTraining();
				refreshLayers();
			}
		});

	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		final List<Object> l = new ArrayList<Object>();
		l.add(mLayers);
		l.add(mTraining);
		return l;
	}
}
