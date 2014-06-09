package cz.honza.backpropagation.main;

import cz.honza.backpropagation.R;
import cz.honza.backpropagation.learning.LearningActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		findViewById(R.id.main_learning).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						startActivity(new Intent(MainActivity.this, LearningActivity.class));
					}
				}
			);
	}
	

}
