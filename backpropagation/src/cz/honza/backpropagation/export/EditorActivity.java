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
	
	protected LinearLayout mData;
	protected List<Integer> mLayers;
	LayoutInflater mInflater;
	
	protected void addCaption(int res)
	{
		View captionLayout = mInflater.inflate(R.layout.editor_item_text, mData, false);
		TextView caption = (TextView)captionLayout.findViewById(R.id.editor_item_text_text);
		caption.setText(res);
		mData.addView(caption);
	}
	
	protected void refresh()
	{
		mData.removeAllViews();
		
		mInflater = LayoutInflater.from(this);
		
		addCaption(R.string.input_dimension);
				
		for (int i = 0; i < mLayers.size(); i++)
		{
			if (i == mLayers.size() - 1)
				addCaption(R.string.output_layer);
			else
				if (i == 1)
					addCaption(R.string.hidden_layers);
			final View item = mInflater.inflate(R.layout.editor_item, mData, false);
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
					refresh();
				}
			});
			
			
			minus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mLayers.set(finali, val - 1);
					refresh();
				}
			});
			
			
			delete.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mLayers.remove(finali);
					refresh();
				}
			});
			
			mData.addView(item);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editor);
		setCancelButton(R.id.editor_cancel);
		mData = (LinearLayout)findViewById(R.id.editor_data);
		mLayers = (List<Integer>)getLastNonConfigurationInstance();
		if (mLayers == null)
		{
			mLayers = new ArrayList<Integer>();
			mLayers.add(1);
			mLayers.add(1);
		}
		refresh();
		findViewById(R.id.editor_add).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mLayers.add(1);
				refresh();
			}
		});
	}
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		return mLayers;
	}
}
