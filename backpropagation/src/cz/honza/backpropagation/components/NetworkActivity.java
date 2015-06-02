package cz.honza.backpropagation.components;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

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
		return "index.php";
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
			Uri uri = Uri.parse(NetworkApplication.HELP_URL_ROOT + getHelpLink());
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home)
		{
			Toast.makeText(this, "Neklikej", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Integer.valueOf(Build.VERSION.SDK).intValue() >= 11)
		{
			requestWindowFeature(8/*Window.FEATURE_ACTION_BAR */);
		}
		super.onCreate(savedInstanceState);
	}
	
	
	
}
