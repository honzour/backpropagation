package cz.honza.backpropagation.util;

import cz.honza.backpropagation.NetworkApplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
		SharedPreferences prefs = getSharedPreferences(NetworkApplication.PREFS, Context.MODE_PRIVATE);
		SharedPreferences.Editor e = prefs.edit();
		e.putString(key, value);
		e.commit();
	}
	
	
	protected String loadPref(String key, String defaultVaule)
	{
		final SharedPreferences prefs = getSharedPreferences(NetworkApplication.PREFS, Context.MODE_PRIVATE);
		return prefs.getString(key, defaultVaule);
	}
}
