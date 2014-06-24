package cz.honza.backpropagation.learning;

import java.util.Calendar;

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
	
		long lastUpdate = 0;
		long now;
		
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
			now = Calendar.getInstance().getTimeInMillis();
			if (now > lastUpdate + 500)
			{
				LearningActivity.sInstance.update();
				lastUpdate = now;
			}
		}
	}
}
