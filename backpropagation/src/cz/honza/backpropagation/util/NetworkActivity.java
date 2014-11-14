package cz.honza.backpropagation.util;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	public String getHelpLink()
	{
		return "index.html";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.help, menu);
	    return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (item.getItemId() == R.id.menu_help)
		{
			Uri uri = Uri.parse("http://backpropagation.moxo.cz/" + getHelpLink());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	
	
}
