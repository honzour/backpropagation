package cz.honza.backpropagation.network.visualisation;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.Network;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class NetworkView extends View {

	protected Paint mPaint;
	protected int mMaxNeuronsInLayer = 8;
	protected int mMaxDrawText = 3;
	
	protected void init()
	{
		mPaint = new Paint();
	}
	
	public NetworkView(Context context) {
		super(context);
		init();
	}

 	public NetworkView(Context context, AttributeSet attrs) {
 		super(context, attrs);
 		init();
 	}
 	
 	public NetworkView(Context context, AttributeSet attrs, int defStyleAttr) {
 		super(context, attrs, defStyleAttr);
 		init();
 	}
 	
 	protected int getX(Network n, int layer, int neuron)
 	{
 		int count;
 			
 		if (layer < 0)
 		{
 			count = n.mLayers[0].neurons[0].weights.length - 1;
 		}
 		else
 		{
 			count = n.mLayers[layer].neurons.length;
 		}
 		if (count > mMaxNeuronsInLayer)
 			count = mMaxNeuronsInLayer + 1;
 		return (getWidth() * (neuron + 1)) / (count + 1);
 	}
 	
 	protected int getY(Network n, int layer)
 	{
 		return (getHeight() * (layer + 1)) / (n.mLayers.length + 1);
 	}
 	
 	protected static String weight2String(Network n, int layer, int neuron, int weight)
 	{
 		String r = String.valueOf(n.mLayers[layer].neurons[neuron].weights[weight]);
 		if (r.length() > 5)
 			r = r.substring(0, 5);
 		return r;
 	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int width = getWidth();
		final int height = getHeight();
		final int radius = (width + height) >> 6;
		
		final Network n = NetworkApplication.sNetwork;
		
		if (n == null)
			return;
		
		int layers = n.mLayers.length;
		
		
		mPaint.setTextSize(radius);
		
		if (n.getInputDimension() > mMaxNeuronsInLayer)
			canvas.drawText("...", getX(n, 0, mMaxNeuronsInLayer), 0.7f * getY(n, -1) + 0.3f * getY(n, 0), mPaint);
		
		final int maxLayer = n.mLayers.length - 1;
		// output
		int limit = n.mLayers[maxLayer].neurons.length;
		if (limit > mMaxNeuronsInLayer)
			limit = mMaxNeuronsInLayer;
		
		for (int j = 0; j < limit; j++)
		{
			canvas.drawLine(getX(n, maxLayer, j), height, getX(n, maxLayer, j), getY(n, maxLayer), mPaint);
		}
		
		// input
		
		limit = n.mLayers[0].neurons.length;
		if (limit > mMaxNeuronsInLayer)
			limit = mMaxNeuronsInLayer;
		
		for (int j = 0; j < limit; j++)
		{
			final int x1 = getX(n, 0, j);
			final int y1 = getY(n, 0);
			final int y2 = 0;
			
			int limit2 = n.getInputDimension();
			if (limit2 > mMaxNeuronsInLayer)
				limit2 = mMaxNeuronsInLayer;
			for (int l = 0; l < limit2; l++)
			{
				final int x2 = getX(n, -1, l);
				canvas.drawLine(x1, y1, x2, y2, mPaint);
				if (limit <= mMaxDrawText && limit2 <= mMaxDrawText)
					canvas.drawText(weight2String(n, 0, j, l + 1), x1 + (x2 - x1) / 3 , y1 + (y2 - y1) / 3, mPaint);
			}
			final int tresholdX = x1 + getWidth() / (2 * (n.mLayers[0].neurons.length + 1));
			
			
			if (limit <= mMaxDrawText)
			{
				canvas.drawLine(x1, y1, tresholdX, y1, mPaint);
				canvas.drawText(weight2String(n, 0, j, 0), tresholdX, y1, mPaint);
			}
		}
		
		// synapses
		for (int i = 1; i < n.mLayers.length; i++)
		{
			limit = n.mLayers[i].neurons.length;
			if (limit > mMaxNeuronsInLayer)
				limit = mMaxNeuronsInLayer;
			for (int j = 0; j < limit; j++)
			{
				final int x1 = getX(n, i, j);
				final int y1 = getY(n, i);
				final int y2 = getY(n, i - 1);
				
				int limit2 = n.mLayers[i - 1].neurons.length;
				if (limit2 > mMaxNeuronsInLayer)
					limit2 = mMaxNeuronsInLayer;
				for (int l = 0; l < limit2; l++)
				{
					final int x2 = getX(n, i - 1, l);
					canvas.drawLine(x1, y1, x2, y2, mPaint);
					if (limit <= mMaxDrawText && limit2 <= mMaxDrawText)
						canvas.drawText(weight2String(n, i, j, l + 1), x1 + (x2 - x1) / 3 , y1 + (y2 - y1) / 3, mPaint);
				}
				final int tresholdX = x1 + getWidth() / (2 * (n.mLayers[i].neurons.length + 1));
				
				canvas.drawLine(x1, y1, tresholdX, y1, mPaint);
				if (limit <= mMaxDrawText)
					canvas.drawText(weight2String(n, i, j, 0), tresholdX, y1, mPaint);
			}	
		}
		
		for (int i = 0; i < layers; i++)
		{
			int neurons = n.mLayers[i].neurons.length;
			if (neurons > mMaxNeuronsInLayer)
				neurons = mMaxNeuronsInLayer;
			int h = getY(n, i);
			for (int j = 0; j < neurons; j++)
			{
				canvas.drawCircle(getX(n, i, j), h, radius, mPaint);
			}
			if (neurons < n.mLayers[i].neurons.length)
			{
				canvas.drawText("...", getX(n, i, neurons), h, mPaint);
			}
		}
	}
}
