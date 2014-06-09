package cz.honza.backpropagation.learning;

import cz.honza.backpropagation.network.Network;

public class LearningThread extends Thread {

	private volatile boolean mEnding = false;
	private Network mNetwork;
	
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
					LearningActivity.sInstance.update();
					return;
				}
			}
			mNetwork.trainingStep();
			if (mNetwork.getItration() % 4096 == 0)
				LearningActivity.sInstance.update();
		}
	}
}
