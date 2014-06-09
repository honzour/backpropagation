package cz.honza.backpropagation.util;

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
}
