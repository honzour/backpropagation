package cz.honza.backpropagation.result;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.Network;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ResultView extends View {
	
	private Bitmap mBmp = null;
	private boolean mDrawBitmap = true;
	Paint mPaint = null;
	DrawResultThread mThread;

	public ResultView(Context context) {
		super(context);
	}

 	public ResultView(Context context, AttributeSet attrs) {
 		super(context, attrs);
 	}
 	
 	public ResultView(Context context, AttributeSet attrs, int defStyleAttr) {
 		super(context, attrs, defStyleAttr);
 	}

	@Override
	protected void onDraw(Canvas canvas) {
		
		final Network n = NetworkApplication.sNetwork;
		
		if (n == null)
			return;
		
		final double delta = 0.01;
		double minX = - delta / 2;
		double minY = minX;
		double maxX = delta / 2;
		double maxY = maxX;
		
		double[][] inputs = n.trainingSet.inputs; 
		if (n.trainingSet.inputs.length > 0)
		{
			if (inputs[0].length > 0)
				minX = maxX = inputs[0][0];
			if (inputs[0].length > 1)
				minY = maxY = inputs[0][1];
			
			for (int i = 1; i < inputs.length; i++)
			{
				if (inputs[i].length > 0)
				{
					if (minX > inputs[i][0])
						minX = inputs[i][0];
					if (maxX < inputs[i][0])
						maxX = inputs[i][0];
				}
				if (inputs[i].length > 1)
				{
					if (minY > inputs[i][1])
						minY = inputs[i][1];
					if (maxY < inputs[i][1])
						maxY = inputs[i][1];
				}
			}
		}
		
		
		
		final int width = getWidth();
		final int height = getHeight();
		
		final int radius = (width + height) >> 6;
		if (mPaint == null)
			mPaint = new Paint();
		mPaint.setTextSize(radius);
		
		if (mBmp == null || mBmp.getWidth() != width || mBmp.getHeight() != height)
		{
			mBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mDrawBitmap = false;
			
			mThread.start(mBmp, this);
		}
		else
		{
			if (mDrawBitmap)
				canvas.drawBitmap(mBmp, 0, 0, mPaint);
		}
		float x0 = width / 4;
		float y0 = height * 3 / 4;
		float x1 = width * 3 / 4;
		float y1 = height / 4;
		
		canvas.drawLine(0, y0, width, y0, mPaint);
		canvas.drawLine(x0, 0, x0, height, mPaint);
		
		canvas.drawLine(x1, y0, x1, y0 - 10, mPaint);
		canvas.drawLine(x0, y1, x0 + 10, y1, mPaint);
		
		for (int i = 0; i < n.trainingSet.inputs.length; i++)
		{
			double ix = 0;
			double iy = 0;
			
			if (n.trainingSet.inputs[i].length > 0)
			{
				ix = n.trainingSet.inputs[i][0];
				if (n.trainingSet.inputs[i].length > 1)
				{
					iy = n.trainingSet.inputs[i][1];
				}
			}
			
			double x = width * (ix + 0.5) / 2;
			double y = height - 1 -  height * (iy + 0.5) / 2;
			if (Math.abs(n.trainingSet.outputs[i][0]) < 0.5)
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			else
				mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)x, (float)y, radius, mPaint);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawText("[" + ix + ", " + iy + "]", (float)x + radius, (float)y - radius, mPaint);
		}
		
	}
	
	public void bitmapValid()
	{
		mDrawBitmap = true;
	}
 	
	public void setThread(DrawResultThread thread)
	{
		mThread = thread;
	}
 	
}
