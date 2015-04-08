package cz.honza.backpropagation.result;

import android.graphics.Bitmap;

public class DrawResultThreadSequence extends DrawResultThread {
	

	
	public DrawResultThreadSequence(Runnable onEnd)
	{
		super(onEnd);
	}
	
	@Override
 	public void fillBitmap(Bitmap bmp)
 	{
 	}
	
}
