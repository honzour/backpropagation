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
 			return (getWidth() * (neuron + 1)) / (n.layers[0].neurons[0].weights.length);
 		return (getWidth() * (neuron + 1)) / (n.layers[layer].neurons.length + 1);
 	}
 	
 	protected int getY(Network n, int layer)
 	{
 		return (getHeight() * (layer + 1)) / (n.layers.length + 1);
 	}

	@Override
	protected void onDraw(Canvas canvas) {
		final int width = getWidth();
		final int height = getHeight();
		
		final Network n = NetworkApplication.sNetwork;
		
		if (n == null)
			return;
		
		int layers = n.layers.length;
		
		Paint paint = new Paint();
		
		final int maxLayer = n.layers.length - 1;
		// output
		for (int j = 0; j < n.layers[maxLayer].neurons.length; j++)
		{
			canvas.drawLine(getX(n, maxLayer, j), height, getX(n, maxLayer, j), getY(n, maxLayer), paint);
		}
		
		// input
		for (int j = 0; j < n.layers[0].neurons.length; j++)
		{
			for (int l = 0; l < n.layers[0].neurons[0].weights.length - 1; l++)
			{
				canvas.drawLine(getX(n, 0, j), getY(n, 0), getX(n, -1, l), 0, paint);
			}
		}
		
		// synapses
		for (int i = 1; i < n.layers.length; i++)
		{
			for (int j = 0; j < n.layers[i].neurons.length; j++)
			{
				for (int l = 0; l < n.layers[i - 1].neurons.length; l++)
				{
					canvas.drawLine(getX(n, i, j), getY(n, i), getX(n, i - 1, l), getY(n,i - 1), paint);
				}
			}	
		}
		
		for (int i = 0; i < layers; i++)
		{
			int neurons = n.layers[i].neurons.length;
			for (int j = 0; j < neurons; j++)
			{
				canvas.drawCircle(getX(n, i, j), getY(n, i), (width + height) / 64, paint);
			}
		}
	}
 	
 	
}
