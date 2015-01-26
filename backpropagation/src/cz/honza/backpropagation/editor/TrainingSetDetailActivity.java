package cz.honza.backpropagation.editor;

import android.os.Bundle;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class TrainingSetDetailActivity extends NetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.training_item);
	}

}
