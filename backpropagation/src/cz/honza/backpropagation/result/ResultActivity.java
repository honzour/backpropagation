package cz.honza.backpropagation.result;

import android.os.Bundle;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class ResultActivity extends NetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_visualisation);
	}
}