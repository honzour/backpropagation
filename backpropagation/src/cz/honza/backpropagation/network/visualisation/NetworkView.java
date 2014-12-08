package cz.honza.backpropagation.network.visualisation;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.network.Network;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class NetworkView extends View {

	public NetworkView(Context context) {
		super(context);
	}

 	public NetworkView(Context context, AttributeSet attrs) {
 		super(context, attrs);
 	}
 	
 	public NetworkView(Context context, AttributeSet attrs, int defStyleAttr) {
 		super(context, attrs, defStyleAttr);
 	}
 	
 	protected int getX(Network n, int layer, int neuron)
 	{
 		if (layer < 0)
 			return (getWidth() * (neuron + 1)) / (n.mLayers[0].neurons[0].weights.length);
 		return (getWidth() * (neuron + 1)) / (n.mLayers[layer].neurons.length + 1);
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
		
		Paint paint = new Paint();
		paint.setTextSize(radius);
		
		final int maxLayer = n.mLayers.length - 1;
		// output
		for (int j = 0; j < n.mLayers[maxLayer].neurons.length; j++)
		{
			canvas.drawLine(getX(n, maxLayer, j), height, getX(n, maxLayer, j), getY(n, maxLayer), paint);
		}
		
		// input
		for (int j = 0; j < n.mLayers[0].neurons.length; j++)
		{
			final int x1 = getX(n, 0, j);
			final int y1 = getY(n, 0);
			final int y2 = 0;
			
			for (int l = 0; l < n.mLayers[0].neurons[0].weights.length - 1; l++)
			{
				final int x2 = getX(n, -1, l);
				canvas.drawLine(x1, y1, x2, y2, paint);
				canvas.drawText(weight2String(n, 0, j, l + 1), x1 + (x2 - x1) / 3 , y1 + (y2 - y1) / 3, paint);
			}
			final int tresholdX = x1 + getWidth() / (2 * (n.mLayers[0].neurons.length + 1));
			
			canvas.drawLine(x1, y1, tresholdX, y1, paint);
			canvas.drawText(weight2String(n, 0, j, 0), tresholdX, y1, paint);
		}
		
		// synapses
		for (int i = 1; i < n.mLayers.length; i++)
		{
			for (int j = 0; j < n.mLayers[i].neurons.length; j++)
			{
				final int x1 = getX(n, i, j);
				final int y1 = getY(n, i);
				final int y2 = getY(n, i - 1);
				for (int l = 0; l < n.mLayers[i - 1].neurons.length; l++)
				{
					final int x2 = getX(n, i - 1, l);
					canvas.drawLine(x1, y1, x2, y2, paint);
					canvas.drawText(weight2String(n, i, j, l + 1), x1 + (x2 - x1) / 3 , y1 + (y2 - y1) / 3, paint);
				}
				final int tresholdX = x1 + getWidth() / (2 * (n.mLayers[i].neurons.length + 1));
				
				canvas.drawLine(x1, y1, tresholdX, y1, paint);
				canvas.drawText(weight2String(n, i, j, 0), tresholdX, y1, paint);
			}	
		}
		
		for (int i = 0; i < layers; i++)
		{
			int neurons = n.mLayers[i].neurons.length;
			for (int j = 0; j < neurons; j++)
			{
				canvas.drawCircle(getX(n, i, j), getY(n, i), radius, paint);
			}
		}
	}
 	
 	
}
