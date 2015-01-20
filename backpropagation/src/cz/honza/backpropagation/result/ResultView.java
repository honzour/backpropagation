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
		
		double[][] inputs = n.mTrainingSet.mInputs; 
		if (n.mTrainingSet.mInputs.length > 0)
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
				if (w[2] != 0)
				{
					double ymi = ((-w[0] - w[1] * (n.mInputScale[0][0] * minX + n.mInputScale[0][1])) / w[2] - n.mInputScale[1][1])  / n.mInputScale[1][0];
					double yma = ((-w[0] - w[1] * (n.mInputScale[0][0] * maxX + n.mInputScale[0][1])) / w[2] - n.mInputScale[1][1])  / n.mInputScale[1][0];

					float ymis = (float)(height - (ymi - minY) / (maxY - minY) * height);
					float ymas = (float)(height - (yma - minY) / (maxY - minY) * height);
					
					mPaint.setColor(0xFFFFFFFF);
					canvas.drawLine(0, ymis, width, ymas, mPaint);
				}
			}
		}
		
		mPaint.setColor(0xFF000000);
		canvas.drawLine(0, y0, width, y0, mPaint);
		canvas.drawLine(x0, 0, x0, height, mPaint);
		
		for (int i = 0; i < n.mTrainingSet.mInputs.length; i++)
		{
			double ix = 0;
			double iy = 0;
			
			if (n.mTrainingSet.mInputs[i].length > 0)
			{
				ix = n.mTrainingSet.mInputs[i][0];
				if (n.mTrainingSet.mInputs[i].length > 1)
				{
					iy = n.mTrainingSet.mInputs[i][1];
				}
			}
			
			double x = width * (ix - minX) / (maxX - minX);
			double y = height - 1 -  height * (iy - minY) / (maxY - minY);
			if (Math.abs(n.mTrainingSet.mOutputs[i][0]) < 0.5)
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			else
				mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)x, (float)y, radius, mPaint);
			
			if (n.mTrainingSet.mInputs.length <= mMaxTexts)
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
		
		
		final double[][] inputs = n.mTrainingSet.mInputs;
		final double[][] outputs = n.mTrainingSet.mOutputs;
		
		if (inputs.length > 0)
		{
			if (inputs[0].length > 0)
				minX = maxX = inputs[0][0];
			if (outputs[0].length > 0)
				minY = maxY = outputs[0][0];
			
			for (int i = 1; i < inputs.length; i++)
			{
				if (inputs[i].length > 0)
				{
					if (minX > inputs[i][0])
						minX = inputs[i][0];
					if (maxX < inputs[i][0])
						maxX = inputs[i][0];
				}
				if (outputs[i].length > 0)
				{
					if (minY > outputs[i][0])
						minY = outputs[i][0];
					if (maxY < outputs[i][0])
						maxY = outputs[i][0];
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
		
		for (int i = 0; i < n.mTrainingSet.mInputs.length; i++)
		{
			double ix = 0;
			double iy = 0;
			
			ix = inputs[i][0];
			iy = outputs[i][0];
			
			double x = width * (ix - minX) / (maxX - minX);
			double y = height - 1 -  height * (iy - minY) / (maxY - minY);
			if (Math.abs(n.mTrainingSet.mOutputs[i][0]) < 0.5)
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			else
				mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)x, (float)y, radius, mPaint);
			
			if (n.mTrainingSet.mInputs.length <= mMaxTexts)
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
		if (mMaxFullSize < n.mTrainingSet.mInputs.length)
		{
			radius = (int)(
					radius / Math.sqrt(((n.mTrainingSet.mInputs.length) / (double) mMaxFullSize)) 
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
