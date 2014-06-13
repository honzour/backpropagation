package cz.honza.backpropagation.main;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.learning.LearningActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.visualisation.VisualisationActivity;
import cz.honza.backpropagation.result.ResultActivity;
import cz.honza.backpropagation.util.NetworkActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

public class MainActivity extends NetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setStartActivity(R.id.main_learning, LearningActivity.class);
		setStartActivity(R.id.main_network_visualisation, VisualisationActivity.class);
		setStartActivity(R.id.main_result_visualisation, ResultActivity.class);
		Spinner problem = (Spinner) findViewById(R.id.main_problem);
		problem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int item, long arg3) {
				
				// Identity
				int[] id_anatomy = {1, 1};
				double[][] id_inputs = {{0}, {1}};
				double[][] id_outputs = {{0}, {1}};
				
				// NOT
				int[] not_anatomy = {1, 1};
				double[][] not_inputs = {{0}, {1}};
				double[][] not_outputs = {{1}, {0}};
								
				// AND
				int[] and_anatomy = {2, 1};
				double[][] and_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
				double[][] and_outputs = {{0}, {0}, {0}, {1}};
								
				// OR
				int[] or_anatomy = {2, 1};
				double[][] or_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
				double[][] or_outputs = {{0}, {1}, {1}, {1}};
				
				
				// XOR
				int[] xor_anatomy = {2, 2, 1};
				double[][] xor_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
				double[][] xor_outputs = {{0}, {1}, {1}, {0}};
				
				
				switch (item)
				{
				case 0:
					NetworkApplication.sNetwork = new Network(id_anatomy, id_inputs, id_outputs);
					break;
				case 1:
					NetworkApplication.sNetwork = new Network(not_anatomy, not_inputs, not_outputs);
					break;
				case 2:
					NetworkApplication.sNetwork = new Network(and_anatomy, and_inputs, and_outputs);
					break;
				case 3:
					NetworkApplication.sNetwork = new Network(or_anatomy, or_inputs, or_outputs);
					break;
				case 4:
					NetworkApplication.sNetwork = new Network(xor_anatomy, xor_inputs, xor_outputs);
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
	
	}
}
