package cz.honza.backpropagation.editor;

import java.io.Serializable;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.trainingset.TrainingSet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrainingAdapter extends ArrayAdapter<Object> {

	LayoutInflater mInflater;
	
	public TrainingAdapter(TrainingSetActivity context, int resource,
			TrainingSet set) {
		super(context, resource, set.getLines());
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView (final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.training_list_item, null);
		}
		Object element = getItem(position);
		StringBuffer sb = new StringBuffer();
		sb.append(element.toString());
		
		TextView number = (TextView)convertView.findViewById(R.id.training_list_item_number);
		TextView text = (TextView)convertView.findViewById(R.id.training_list_item_text);
		
		text.setText(sb.toString());
		number.setText(String.valueOf(position) + '.');
		
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getContext(), TrainingSetDetailActivity.class);
				i.putExtra(TrainingSetDetailActivity.INTENT_EXTRA_NUMBER, position);
				i.putExtra(TrainingSetDetailActivity.INTENT_EXTRA_DATA, (Serializable)getItem(position));
				((Activity)getContext()).startActivityForResult(i, position);
			}
		});
		
		return convertView;
	}

}
