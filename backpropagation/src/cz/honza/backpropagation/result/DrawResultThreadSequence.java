package cz.honza.backpropagation.result;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.trainingset.TrainingSetSingleSequence;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawResultThreadSequence extends DrawResultThread {
	

	
	public DrawResultThreadSequence(Runnable onEnd)
	{
		super(onEnd);
	}
	
	@Override
 	public void fillBitmap(Bitmap bmp)
 	{
		final TrainingSetSingleSequence ts = (TrainingSetSingleSequence)NetworkApplication.sNetwork.mTrainingSet; 
		final int length = ts.mTimeline.length * 2;
		double[] data = new double[length];
		double[] input = new double[NetworkApplication.sNetwork.getInputDimension()];
		double[] output = new double[NetworkApplication.sNetwork.getOutputDimension()];
		
		System.arraycopy(ts.mTimeline, 0, data, 0, ts.length());
		for (int i = ts.mTimeline.length; i < length; i++)
		{
			System.arraycopy(data, i - input.length, input, 0, input.length);
			NetworkApplication.sNetwork.calculate(input, output, true);
			data[i] = output[0];
		}
		
		Canvas c = new Canvas(bmp);
		c.drawRGB(255, 255, 255);
		Paint p = new Paint();
		c.drawLine(0, 0, c.getWidth(), c.getHeight(), p);
 	}
}
