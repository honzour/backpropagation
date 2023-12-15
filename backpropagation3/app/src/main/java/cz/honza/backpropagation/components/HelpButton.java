package cz.honza.backpropagation.components;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

public class HelpButton extends Button {

	protected String mUrl;
	
	protected void init(AttributeSet attrs)
	{
		if (getText().length() == 0)
			setText(R.string.help);
		
		if (attrs != null)
		{
			TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.help);
				 
				final int count = a.getIndexCount();
				for (int i = 0; i < count; ++i)
				{
				    int attr = a.getIndex(i);
				    if (attr == R.styleable.help_url) {
						mUrl = a.getString(attr);
					}
					if (attr == R.styleable.help_link) {
				            mUrl = NetworkApplication.HELP_URL_ROOT + a.getString(attr);
				    }
				}
				a.recycle();
		}
		
		setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (mUrl == null)
					return;
				Uri uri = Uri.parse(mUrl);
                getContext().startActivity(new Intent(Intent.ACTION_VIEW, uri));
			}
		});
	}
	
	public HelpButton(Context context) {
		
		super(context);
		init(null);
	}
	
	public HelpButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs);
	}
	
	public HelpButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(attrs);
	}
}
