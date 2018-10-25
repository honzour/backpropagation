package cz.honza.backpropagation.components;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;;

public class ErrorTextView extends TextView {

	public ErrorTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	public ErrorTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public ErrorTextView(Context context) {
		super(context);
	}
	
	public void setNumber(double number)
	{
		double redLog = 1;
		double greenLog = -2;
		setText(String.valueOf(number));
		if (number > 0)
			number = Math.log10(number);
		if (number < greenLog)
			number = greenLog;
		if (number > redLog)
			number = redLog;
		
		int red = (int) (255 * ((number - greenLog) / (redLog - greenLog)));
		if (red < 0)
			red = 0;
		if (red > 255)
			red = 255;
		int green = 255 - red;
		setTextColor((255 << 24) | ((red) << 16) | ((green / 2) << 8));
	}

}
