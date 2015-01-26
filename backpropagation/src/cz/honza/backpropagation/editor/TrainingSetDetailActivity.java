package cz.honza.backpropagation.editor;

import android.os.Bundle;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class TrainingSetDetailActivity extends NetworkActivity {
	
	public static final String INTENT_EXTRA_NUMBER = "INTENT_EXTRA_NUMBER";
	public static final String INTENT_EXTRA_DATA = "INTENT_EXTRA_DATA";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_item);
	}

}
