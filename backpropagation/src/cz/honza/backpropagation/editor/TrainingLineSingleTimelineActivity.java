package cz.honza.backpropagation.editor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.network.trainingset.TrainingLineSingleSequence;

public class TrainingLineSingleTimelineActivity extends NetworkActivity {
	
	public static final String INTENT_EXTRA_NUMBER = "INTENT_EXTRA_NUMBER";
	public static final String INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA";
	
	protected TrainingLineSingleSequence mData;
	protected TextView mValue;
	
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

		mValue.setText(String.valueOf(mData.mValue));
	}
	
	protected void save()
	{
		try
		{
			mData.mValue = Double.valueOf(mValue.getText().toString());
		}
		catch (NumberFormatException e)
		{
			mData.mValue = 0;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_item_single_timeline);
		mValue = (TextView) findViewById(R.id.item_value);
		mData = (TrainingLineSingleSequence)getLastNonConfigurationInstance();
		if (mData == null)
		{
			Object o = getIntent().getSerializableExtra(INTENT_EXTRA_DATA);
			mData = (TrainingLineSingleSequence)getIntent().getSerializableExtra(INTENT_EXTRA_DATA);
		}
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
	
	@Override
	public Object onRetainNonConfigurationInstance()
	{
		save();
		return mData;
	}
	
	@Override
	public String getHelpLink()
	{
		return "trainingsetdetail.php";
	}
}
