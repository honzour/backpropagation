package cz.honza.backpropagation.editor;

import java.util.ArrayList;

import cz.honza.backpropagation.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TrainingAdapter extends ArrayAdapter<ArrayList<ArrayList<Double>>> {

	LayoutInflater mInflater;
	
	public TrainingAdapter(Context context, int resource,
			ArrayList<ArrayList<ArrayList<Double>>> data) {
		super(context, resource, data);
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView (final int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.training_list_item, null);
		}
		ArrayList<ArrayList<Double>> element = getItem(position);
		StringBuffer sb = new StringBuffer();
		EditorActivity.addElement(element, sb);
		
		TextView number = (TextView)convertView.findViewById(R.id.training_list_item_number);
		TextView text = (TextView)convertView.findViewById(R.id.training_list_item_text);
		
		text.setText(sb.toString());
		number.setText(String.valueOf(position) + '.');
		
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(getContext(), "kliknul " + position, Toast.LENGTH_LONG).show();
				
			}
		});
		
		return convertView;
	}

}
