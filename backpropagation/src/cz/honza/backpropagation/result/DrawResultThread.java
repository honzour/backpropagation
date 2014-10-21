package cz.honza.backpropagation.result;

import cz.honza.backpropagation.NetworkApplication;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.view.View;

public class DrawResultThread extends Thread {
	
	private Bitmap mBmp;
	private View mView;
	private Handler mHandler;
	private volatile boolean mStop = false;
	private Runnable mOnEnd;
	
	public DrawResultThread(Runnable onEnd)
	{
		mOnEnd = onEnd;
	}
	
	public void canStop()
	{
		mStop = true;
	}
	
	public void start(Bitmap bmp, View v, double minX, double minY, double maxX, double maxY)
	{
		// TODO
		mBmp = bmp;
		mView = v;
		mHandler = new Handler();
		start();
	}
	
 	public void fillBitmap(Bitmap bmp)
 	{
 		double[] input = {0, 0};
		double[] output = {0, 0.5};
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				if (mStop)
					return;
				input[0] = -0.5 + 2 * i / (double) width;
				input[1] = 1.5 - 2 * j / (double) height;
				NetworkApplication.sNetwork.calculate(input, output);
				bmp.setPixel(i, j, Color.argb(255, 128, (int)(255 * output[0] + 0.5), (int)(255 * output[1] + 0.5)));
			}
		}
 	}
	
	@Override
	public void run()
	{
		fillBitmap(mBmp);
		mHandler.post(new Runnable() {
			@Override
			public void run() {
				mView.invalidate();
				((ResultView)mView).bitmapValid();
				if (mOnEnd != null)
					mOnEnd.run();
			}
		});
		
	}
}
