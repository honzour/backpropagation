package cz.honza.backpropagation.result;

import android.graphics.Bitmap;
import android.os.Handler;
import android.view.View;

public abstract class DrawResultThread extends Thread {
	
	protected Bitmap mBmp;
	protected View mView;
	protected Handler mHandler;
	protected volatile boolean mStop = false;
	protected Runnable mOnEnd;
	protected double mMinX;
	protected double mMinY;
	protected double mMaxX;
	protected double mMaxY;
	
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
		mMaxX = maxX;
		mMaxY = maxY;
		mMinX = minX;
		mMinY = minY;
		mBmp = bmp;
		mView = v;
		mHandler = new Handler();
		start();
	}
	
 	public abstract void fillBitmap(Bitmap bmp);
	
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
