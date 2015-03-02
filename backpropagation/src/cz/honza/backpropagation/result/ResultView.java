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
	
	private int mMaxTexts = 8;
	private int mMaxFullSize = 8;
	
	private Bitmap mBmp = null;
	private boolean mDrawBitmap = true;
	private Paint mPaint = null;
	private DrawResultThread mThread;

	public ResultView(Context context) {
		super(context);
	}

 	public ResultView(Context context, AttributeSet attrs) {
 		super(context, attrs);
 	}
 	
 	public ResultView(Context context, AttributeSet attrs, int defStyleAttr) {
 		super(context, attrs, defStyleAttr);
 	}

 	protected void onDraw2d(Canvas canvas, Network n, int width, int height, int radius) {
 		final double delta = 0.01;
		
		// will be real x of left of the screen
		double minX = - delta / 2;
		// will be real y of bottom of the screen
		double minY = minX;
		// will be real x of right of the screen
		double maxX = delta / 2;
		// will be real y of top of the screen
		double maxY = maxX;
		
		//double[][] inputs = n.mTrainingSet.mInputs; 
		if (n.mTrainingSet.length() > 0)
		{
			if (n.mTrainingSet.getInputDimension() > 0)
				minX = maxX = n.mTrainingSet.getInput(0, 0);
			if (n.mTrainingSet.getInputDimension() > 1)
				minY = maxY = n.mTrainingSet.getInput(0, 1);
			
			for (int i = 1; i < n.mTrainingSet.length(); i++)
			{
				if (n.mTrainingSet.getInputDimension() > 0)
				{
					if (minX > n.mTrainingSet.getInput(i, 0))
						minX = n.mTrainingSet.getInput(i, 0);
					if (maxX < n.mTrainingSet.getInput(i, 0))
						maxX = n.mTrainingSet.getInput(i, 0);
				}
				if (n.mTrainingSet.getInputDimension() > 1)
				{
					if (minY > n.mTrainingSet.getInput(i, 1))
						minY = n.mTrainingSet.getInput(i, 1);
					if (maxY < n.mTrainingSet.getInput(i, 1))
						maxY = n.mTrainingSet.getInput(i, 1);
				}
			}
		}
		
		if (minX == maxX)
		{
			minX -= delta / 2;
			maxX += delta / 2;
		}
		
		if (minY == maxY)
		{
			minY -= delta / 2;
			maxY += delta / 2;
		}
		
		final double deltaX2 = (maxX - minX) / 2;
		minX -= deltaX2;
		maxX += deltaX2;
		
		final double deltaY2 = (maxY - minY) / 2;
		minY -= deltaY2;
		maxY += deltaY2;
		
		if (mBmp == null || mBmp.getWidth() != width || mBmp.getHeight() != height)
		{
			mBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mDrawBitmap = false;
			
			mThread.start(mBmp, this, minX, minY, maxX, maxY);
		}
		else
		{
			if (mDrawBitmap)
				canvas.drawBitmap(mBmp, 0, 0, mPaint);
		}
		
		double ax = (maxX - minX) / width;
		double ay = (maxY - minY) / height;
		
		// screen x of real 0
		float x0 = (float)(width - maxX / ax);
		// screen y of real 0
		float y0 = (float)(0 + maxY / ay);
		
		if (true)
		{
			for (int i = 0; i < n.mLayers[0].neurons.length; i++)
			{
				final double[] w = n.mLayers[0].neurons[i].weights;
				if (w[1] != 0 || w[2] != 0) // avoid division by zero
				{
					mPaint.setColor(0xFFFFFFFF);
					if (Math.abs(w[2]) > Math.abs(w[1])) // divide by greater number
					{
						double ymi = ((-w[0] - w[1] * (n.mInputScale[0][0] * minX + n.mInputScale[0][1])) / w[2] - n.mInputScale[1][1])  / n.mInputScale[1][0];
						double yma = ((-w[0] - w[1] * (n.mInputScale[0][0] * maxX + n.mInputScale[0][1])) / w[2] - n.mInputScale[1][1])  / n.mInputScale[1][0];

						float ymis = (float)(height - (ymi - minY) / (maxY - minY) * height);
						float ymas = (float)(height - (yma - minY) / (maxY - minY) * height);
						
						canvas.drawLine(0, ymis, width, ymas, mPaint);
					}
					else
					{
						double xmi = ((-w[0] - w[2] * (n.mInputScale[1][0] * minY + n.mInputScale[1][1])) / w[1] - n.mInputScale[0][1])  / n.mInputScale[0][0];
						double xma = ((-w[0] - w[2] * (n.mInputScale[1][0] * maxY + n.mInputScale[1][1])) / w[1] - n.mInputScale[0][1])  / n.mInputScale[0][0];

						float xmis = (float)((xmi - minX) / (maxX - minX) * width);
						float xmas = (float)((xma - minX) / (maxX - minX) * width);
						
						canvas.drawLine(xmis, height, xmas, 0, mPaint);
					}
				}
			}
		}
		
		mPaint.setColor(0xFF000000);
		canvas.drawLine(0, y0, width, y0, mPaint);
		canvas.drawLine(x0, 0, x0, height, mPaint);
		
		for (int i = 0; i < n.mTrainingSet.length(); i++)
		{
			double ix = 0;
			double iy = 0;
			
			if (n.mTrainingSet.getInputDimension() > 0)
			{
				ix = n.mTrainingSet.getInput(i, 0);
				if (n.mTrainingSet.getInputDimension() > 1)
				{
					iy = n.mTrainingSet.getInput(i, 1);
				}
			}
			
			double x = width * (ix - minX) / (maxX - minX);
			double y = height - 1 -  height * (iy - minY) / (maxY - minY);
			if (Math.abs(n.mTrainingSet.getOutput(i, 0)) < 0.5)
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			else
				mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)x, (float)y, radius, mPaint);
			
			if (n.mTrainingSet.length() <= mMaxTexts)
			{
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
				canvas.drawText("[" + ix + ", " + iy + "]", (float)x + radius, (float)y - radius, mPaint);
			}
		}
 	}
 	
 	protected void onDraw1d(Canvas canvas, Network n, int width, int height, int radius) {
 		final double delta = 0.01;
		
		// will be real x of left of the screen
		double minX = - delta / 2;
		// will be real y of bottom of the screen
		double minY = minX;
		// will be real x of right of the screen
		double maxX = delta / 2;
		// will be real y of top of the screen
		double maxY = maxX;
		
		if (n.mTrainingSet.length() > 0)
		{
			if (n.mTrainingSet.getInputDimension() > 0)
				minX = maxX = n.mTrainingSet.getInput(0, 0);
			if (n.mTrainingSet.getOutputDimension() > 0)
				minY = maxY = n.mTrainingSet.getOutput(0, 0);
			
			for (int i = 1; i < n.mTrainingSet.length(); i++)
			{
				if (n.mTrainingSet.getInputDimension() > 0)
				{
					if (minX > n.mTrainingSet.getInput(i, 0))
						minX = n.mTrainingSet.getInput(i, 0);
					if (maxX < n.mTrainingSet.getInput(i, 0))
						maxX = n.mTrainingSet.getInput(i, 0);
				}
				if (n.mTrainingSet.getOutputDimension() > 0)
				{
					if (minY > n.mTrainingSet.getOutput(i, 0))
						minY = n.mTrainingSet.getOutput(i, 0);
					if (maxY < n.mTrainingSet.getOutput(i, 0))
						maxY = n.mTrainingSet.getOutput(i, 0);
				}
			}
		}
		
		if (minX == maxX)
		{
			minX -= delta / 2;
			maxX += delta / 2;
		}
		
		if (minY == maxY)
		{
			minY -= delta / 2;
			maxY += delta / 2;
		}
		
		final double deltaX2 = (maxX - minX) / 2;
		minX -= deltaX2;
		maxX += deltaX2;
		
		final double deltaY2 = (maxY - minY) / 2;
		minY -= deltaY2;
		maxY += deltaY2;

		
		if (mBmp == null || mBmp.getWidth() != width || mBmp.getHeight() != height)
		{
			mBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mDrawBitmap = false;
			
			mThread.start(mBmp, this, minX, minY, maxX, maxY);
		}
		else
		{
			if (mDrawBitmap)
				canvas.drawBitmap(mBmp, 0, 0, mPaint);
		}
		
		for (int i = 0; i < n.mTrainingSet.length(); i++)
		{
			double ix = 0;
			double iy = 0;
			
			ix = n.mTrainingSet.getInput(i, 0);
			iy = n.mTrainingSet.getOutput(i, 0);
			
			double x = width * (ix - minX) / (maxX - minX);
			double y = height - 1 -  height * (iy - minY) / (maxY - minY);
			if (Math.abs(n.mTrainingSet.getOutput(i, 0)) < 0.5)
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			else
				mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)x, (float)y, radius, mPaint);
			
			if (n.mTrainingSet.length() <= mMaxTexts)
			{
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
				canvas.drawText("[" + ix + ", " + iy + "]", (float)x + radius, (float)y - radius, mPaint);
			}
		}
 	}

 	
	@Override
	protected void onDraw(Canvas canvas) {
		
		final Network n = NetworkApplication.sNetwork;
		
		if (n == null)
			return;
		
		final int width = getWidth();
		final int height = getHeight();
		
		int radius = (width + height) >> 6;
		if (mMaxFullSize < n.mTrainingSet.length())
		{
			radius = (int)(
					radius / Math.sqrt(((n.mTrainingSet.length()) / (double) mMaxFullSize)) 
					+ 0.5);
		}
		if (mPaint == null)
			mPaint = new Paint();
		mPaint.setTextSize(radius);

		
		if (n.getInputDimension() > 1)
			onDraw2d(canvas, n, width, height, radius);
		else
			onDraw1d(canvas, n, width, height, radius);
		
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
