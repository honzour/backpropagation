package cz.honza.backpropagation;

import cz.honza.backpropagation.network.Network;

public class LearningThread extends Thread {

	private volatile boolean mEnding = false;
	private volatile long mIteration = 0;
	private Network mNetwork;
	private double mError;
	
	public synchronized void end()
	{
		mEnding = true;
	}
	

	public void start(Network network)
	{
		mNetwork = network;
		start();
	}
	
	@Override
	public void run() {
	
		while (true)
		{
			synchronized(this)
			{
				if (mEnding) 
				{
					MainActivity.sInstance.update(mIteration, mError);
					return;
				}
			}
			mError = mNetwork.trainingStep();
			mIteration++;
			if (mIteration % 4096 == 0)
				MainActivity.sInstance.update(mIteration, mError);
		}
	}
}
