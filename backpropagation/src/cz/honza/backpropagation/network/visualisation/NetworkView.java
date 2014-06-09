package cz.honza.backpropagation.network.visualisation;

import cz.honza.backpropagation.learning.LearningActivity;
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

	@Override
	protected void onDraw(Canvas canvas) {
		final int width = getWidth();
		final int height = getHeight();
		
		final Network n = LearningActivity.sNetwork;
		
		if (n == null)
			return;
		
		int layers = n.layers.length;
		
		Paint paint = new Paint();
		
		for (int i = 0; i < layers; i++)
		{
			int h = (height * (i + 1)) / (layers + 1);
			canvas.drawLine(0, h, width, h, paint);
			int neurons = n.layers[i].neurons.length;
			for (int j = 0; j < neurons; j++)
			{
				int w = (width * (j + 1)) / (neurons + 1);
				canvas.drawCircle(w, h, 10, paint);
			}
		}
	}
 	
 	
}
