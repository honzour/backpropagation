package cz.honza.backpropagation.network.visualisation;

import android.os.Bundle;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class VisualisationActivity extends NetworkActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.network_visualisation);
	}
}
