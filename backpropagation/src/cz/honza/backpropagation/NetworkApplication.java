package cz.honza.backpropagation;

import cz.honza.backpropagation.learning.LearningThread;
import cz.honza.backpropagation.network.Network;
import android.app.Application;

public class NetworkApplication extends Application {

	public static LearningThread sThread;
	public volatile static Network sNetwork = null;

	
	@Override
	public void onCreate() {
		super.onCreate();
		if (NetworkApplication.sNetwork == null)
		{
			// XOR
			int[] anatomy = {2, 2, 2, 1};
			double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
			double[][] outputs = {{0}, {1}, {1}, {0}};
							
			/*
			// single neuron, x > 0.5
			int[] anatomy = {1, 1};
			double[][] inputs = {{0}, {1}};
			double[][] outputs = {{0}, {1}};
			*/

			NetworkApplication.sNetwork = new Network(anatomy, inputs, outputs);
		}
	}
}
