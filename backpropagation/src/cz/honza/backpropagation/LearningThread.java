package cz.honza.backpropagation;

import cz.honza.backpropagation.network.Network;

public class LearningThread extends Thread {

	private volatile boolean mEnding = false;
	private volatile long mIteration = 0;
	private Network mNetwork;
	private double[][] mTrainingInputs;
	private double mTrainingOutputs[][];
	private double mError;
	
	public synchronized void end()
	{
		mEnding = true;
	}
	

	public void start(int[] networkAnatomy, double[][] trainingInputs, double trainingOutputs[][])
	{
		mNetwork = new Network(networkAnatomy);
		mTrainingInputs = trainingInputs;
		mTrainingOutputs = trainingOutputs;
		start();
	}
	
	@Override
	public void run() {
	
		mNetwork.initTraining(mTrainingInputs, mTrainingOutputs);
		while (true)
		{
			mError = mNetwork.trainingStep();
			synchronized(this)
			{
				if (mEnding) 
				{
					MainActivity.sInstance.update(-1, -1);
					return;
				}
			}
			mIteration++;
			MainActivity.sInstance.update(mIteration, mError);
		}
	}
}
