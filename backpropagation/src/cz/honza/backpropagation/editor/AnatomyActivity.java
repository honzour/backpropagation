package cz.honza.backpropagation.editor;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class AnatomyActivity extends NetworkActivity {

	protected LinearLayout mLayersLayout;
	protected ArrayList<Integer> mLayers;
	LayoutInflater mInflater;
	
	protected void addLayersCaption(int res)
	{
		View captionLayout = mInflater.inflate(R.layout.anatomy_item_text, mLayersLayout, false);
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
			final View item = mInflater.inflate(R.layout.anatomy_layer_item, mLayersLayout, false);
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

	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.anatomy);
		mInflater = LayoutInflater.from(this);
		mLayersLayout = (LinearLayout)findViewById(R.id.anatomy_data);
		
		mLayers = (ArrayList<Integer>)getLastNonConfigurationInstance();
		if (mLayers == null)
		{
			mLayers = (ArrayList<Integer>)getIntent().getExtras().getSerializable(EditorActivity.INTENT_EXTRA_ANATOMY);
		}
		refreshLayers();
		findViewById(R.id.anatomy_add_layer).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mLayers.add(1);
				refreshLayers();
			}
		});
	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return mLayers;
	}

	@Override
	public void finish() {
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EditorActivity.INTENT_EXTRA_ANATOMY, mLayers);
		setResult(RESULT_OK, resultIntent);
		super.finish();
	}
	
}
