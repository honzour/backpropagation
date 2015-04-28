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
		
		System.arraycopy(ts.mTimeline, 0, data, 0, ts.mTimeline.length);
		for (int i = ts.mTimeline.length; i < length; i++)
		{
			System.arraycopy(data, i - input.length, input, 0, input.length);
			NetworkApplication.sNetwork.calculate(input, output, true);
			data[i] = output[0];
		}
		
		double max = data[0];
		double min = data[0];
		
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] < min)
				min = data[i];
			if (data[i] > max)
				max = data[i];
		}
		
		Canvas c = new Canvas(bmp);
		c.drawRGB(255, 255, 255);
		Paint p = new Paint();
		
		final float xCoef = (c.getWidth() - 1) / (float)data.length;
		final float yCoef = (c.getHeight() - 1) / (float)(max - min) * 2f / 3f;
		final float yAdd = c.getHeight() * 5 / 6f;
		
		for (int i = 0; i < data.length - 1;  i++)
		{
			if (i >= data.length / 2 - 1)
				p.setARGB(255, 255, 0, 0);
			c.drawLine(i * xCoef, 
					- (float) (data[i] - min) * yCoef + yAdd,
					(i + 1) * xCoef,
					- (float) (data[i + 1] - min) * yCoef + yAdd,
					p);
		}
 	}
}
