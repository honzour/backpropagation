package cz.honza.backpropagation.util;

import cz.honza.backpropagation.NetworkApplication;
import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class NetworkActivity extends Activity {
	protected void setStartActivity(int resource, final Class<?> clazz)
	{
		final View v = findViewById(resource);
		if (v != null)
		{
			v.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					startActivity(new Intent(NetworkActivity.this, clazz));
				}
			});
		}
	}
	
	protected void savePref(String key, String value)
	{
		NetworkApplication.sInstance.savePref(key, value);
	}
	
	
	protected String loadPref(String key, String defaultVaule)
	{
		return NetworkApplication.sInstance.loadPref(key, defaultVaule);
	}
	protected void setCancelButton(int resource)
	{
		View v = findViewById(resource);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
}
