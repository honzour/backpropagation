package cz.honza.backpropagation.result;

import cz.honza.backpropagation.NetworkApplication;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class DrawResultThread2D extends DrawResultThread {
	
	public DrawResultThread2D(Runnable onEnd)
	{
		super(onEnd);
	}

	@Override
 	public void fillBitmap(Bitmap bmp)
 	{
 		int iDim = NetworkApplication.sNetwork.getInputDimension();
 		int oDim = NetworkApplication.sNetwork.getOutputDimension();
 		
 		double[] input = {0, 0};
		double[] output = {0, 0.5, 0.5};
		
		if (iDim > 2)
			input = new double[iDim];
		
		if (oDim > 3)
			output = new double[oDim];
		
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (mStop)
					return;
				
				input[0] = mMinX + i * (mMaxX - mMinX) / (double) width;
				input[1] = mMinY + (height - j - 1) * (mMaxY - mMinY) / height;
				NetworkApplication.sNetwork.calculate(input, output, false);
				bmp.setPixel(i, j, Color.argb(255, (int)(255 * output[2] + 0.5), (int)(255 * output[0] + 0.5), (int)(255 * output[1] + 0.5)));
			}
		}
		Canvas c = new Canvas(bmp);
		c.drawRGB(255, 255, 255);
		Paint p = new Paint();
		c.drawLine(1, 1, c.getWidth() - 1, c.getHeight() - 1, p);
 	}
}
