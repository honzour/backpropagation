package cz.honza.backpropagation.result;

import cz.honza.backpropagation.NetworkApplication;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class DrawResultThread1D extends DrawResultThread {
	

	
	public DrawResultThread1D(Runnable onEnd)
	{
		super(onEnd);
	}
	
	@Override
 	public void fillBitmap(Bitmap bmp)
 	{
		int iDim = NetworkApplication.sNetwork.getInputDimension();
 		int oDim = NetworkApplication.sNetwork.getOutputDimension();
 		
 		double[] input = new double[iDim];
		double[] output = new double[oDim];
		double[] oldOutput = new double[oDim];
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		Canvas c = new Canvas(bmp);
		c.drawRGB(255, 255, 255);
		Paint p = new Paint();
		//p.s
		
		for (int i = 0; i < width; i++)
		{
			
			if (mStop)
				return;
				
			input[0] = mMinX + i * (mMaxX - mMinX) / (double) width;
		
			NetworkApplication.sNetwork.calculate(input, output, false);
			if (i > 0)
				c.drawLine(i - 1, (float)(height * oldOutput[0]), i, (float)(height * output[0]), p);
			System.arraycopy(output, 0, oldOutput, 0, output.length);
		
		}
 		
 	}
	
}
