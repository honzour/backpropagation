package cz.honza.backpropagation.result;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.Network;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class ResultView extends View {
	
	private Bitmap mBmp = null;
	private int mWidth;
	private int mHeight;
	Paint mPaint = null;

	public ResultView(Context context) {
		super(context);
	}

 	public ResultView(Context context, AttributeSet attrs) {
 		super(context, attrs);
 	}
 	
 	public ResultView(Context context, AttributeSet attrs, int defStyleAttr) {
 		super(context, attrs, defStyleAttr);
 	}
 	
 	public static void fillBitmap(Bitmap bmp)
 	{
 		double[] input = {0, 0};
		double[] output = {0, 0.5};
		int width = bmp.getWidth();
		int height = bmp.getHeight();
		
		for (int i = 0; i < width; i++)
		{
			for (int j = 0; j < height; j++)
			{
				input[0] = -0.5 + 2 * i / (double) width;
				input[1] = 1.5 - 2 * j / (double) height;
				NetworkApplication.sNetwork.calculate(input, output);
				bmp.setPixel(i, j, Color.argb(255, 128, (int)(255 * output[0] + 0.5), (int)(255 * output[1] + 0.5)));
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
		
		final int radius = (width + height) >> 6;
		if (mPaint == null)
			mPaint = new Paint();
		mPaint.setTextSize(radius);
		
		if (mBmp == null || mWidth != width || mHeight != height)
		{
			mBmp = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			mWidth = width;
			mHeight = height;
		}
		
		if (mBmp != null)
			fillBitmap(mBmp);
		
		canvas.drawBitmap(mBmp, 0, 0, mPaint);
		float x0 = width / 4;
		float y0 = height * 3 / 4;
		float x1 = width * 3 / 4;
		float y1 = height / 4;
		
		canvas.drawLine(0, y0, width, y0, mPaint);
		canvas.drawLine(x0, 0, x0, height, mPaint);
		
		canvas.drawLine(x1, y0, x1, y0 - 10, mPaint);
		canvas.drawLine(x0, y1, x0 + 10, y1, mPaint);
		
		for (int i = 0; i < n.inputs.length; i++)
		{
			double ix = 0;
			double iy = 0;
			
			if (n.inputs[i].length > 0)
			{
				ix = n.inputs[i][0];
				if (n.inputs[i].length > 1)
				{
					iy = n.inputs[i][1];
				}
			}
			
			double x = width * (ix + 0.5) / 2;
			double y = height - 1 -  height * (iy + 0.5) / 2;
			if (Math.abs(n.outputs[i][0]) < 0.5)
				mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			else
				mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle((float)x, (float)y, radius, mPaint);
			mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
			canvas.drawText("[" + ix + ", " + iy + "]", (float)x + radius, (float)y - radius, mPaint);
		}
		
	}
 	
 	
}
