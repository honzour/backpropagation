package cz.honza.backpropagation.editor;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class TrainingSetDetailActivity extends NetworkActivity {
	
	public static final String INTENT_EXTRA_NUMBER = "INTENT_EXTRA_NUMBER";
	public static final String INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA";
	
	
	protected ArrayList<ArrayList<Double>> mData; 
	
	
	protected void fillData()
	{
		final View delete = findViewById(R.id.editor_training_item_delete);
				
		delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mData = null;
				finish();
			}
		});
		
		final LinearLayout inputLayout = (LinearLayout)findViewById(R.id.editor_training_item_input);
		final LinearLayout outputLayout = (LinearLayout)findViewById(R.id.editor_training_item_output);
		
		final List<Double> elementInput = mData.get(0);
		final List<Double> elementOutput = mData.get(1);
		
		for (int j = 0; j < elementInput.size(); j++)
		{
			final EditText e = new EditText(this);
			e.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
			e.setText(String.valueOf(elementInput.get(j)));
			inputLayout.addView(e);
		}
		for (int j = 0; j < elementOutput.size(); j++)
		{
			final EditText e = new EditText(this);
			e.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
			e.setText(String.valueOf(elementOutput.get(j)));
			outputLayout.addView(e);
		}
	}
	
	protected void save()
	{
		final LinearLayout inputLayout = (LinearLayout)findViewById(R.id.editor_training_item_input);
		final LinearLayout outputLayout = (LinearLayout)findViewById(R.id.editor_training_item_output);
		final int inputCount = inputLayout.getChildCount();
		for (int j = 0; j < inputCount; j++)
		{
			final TextView input = (TextView)inputLayout.getChildAt(j);
			double d = 0;
			try
			{
				d = Double.valueOf(input.getText().toString());
			}
			catch (Exception e) {}
			mData.get(0).set(j, d);
		}
		final int outputCount = outputLayout.getChildCount();
		for (int j = 0; j < outputCount; j++)
		{
			final TextView input = (TextView)outputLayout.getChildAt(j);
			double d = 0;
			try
			{
				d = Double.valueOf(input.getText().toString());
			}
			catch (Exception e) {}
			mData.get(1).set(j, d);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_item);
		mData = (ArrayList<ArrayList<Double>>)getIntent().getSerializableExtra(INTENT_EXTRA_DATA);
		fillData();
	}
	
	@Override
	public void finish() {
		if (mData != null)
			save();
		Intent resultIntent = new Intent();
		resultIntent.putExtra(EditorActivity.INTENT_EXTRA_TRAINING, mData);
		setResult(RESULT_OK, resultIntent);
		super.finish();
	}
}
