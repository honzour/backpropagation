package cz.honza.backpropagation;

import cz.honza.backpropagation.network.Network;

public class LearningThread extends Thread {

	private volatile boolean mEnding = false;
	private volatile long mIteration = 0;
	
	public synchronized void end()
	{
		mEnding = true;
	}
	

	public void start(Network network, double[][] inputs, double outputs[][])
	{
		
	}
	
	@Override
	public void run() {
		
		while (true)
		{
			
			synchronized(this)
			{
				if (mEnding) 
				{
					MainActivity.sInstance.update(-1);
					return;
				}
			}
			mIteration++;
			MainActivity.sInstance.update(mIteration);
		}
	}
}
