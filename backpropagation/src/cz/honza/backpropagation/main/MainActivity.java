package cz.honza.backpropagation.main;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.learning.LearningActivity;
import cz.honza.backpropagation.util.NetworkActivity;
import android.os.Bundle;

public class MainActivity extends NetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setStartActivity(R.id.main_learning, LearningActivity.class);
	}
	

}
