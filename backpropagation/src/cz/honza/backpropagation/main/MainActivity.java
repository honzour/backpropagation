package cz.honza.backpropagation.main;

import java.io.IOException;
import java.io.StringWriter;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.export.ExportActivity;
import cz.honza.backpropagation.export.ImportDataActivity;
import cz.honza.backpropagation.export.NewTaskActivity;
import cz.honza.backpropagation.learning.LearningActivity;
import cz.honza.backpropagation.network.visualisation.VisualisationActivity;
import cz.honza.backpropagation.result.ResultActivity;
import cz.honza.backpropagation.result.ResultInputActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;

public class MainActivity extends NetworkActivity {
	
	protected Spinner mExportFormat;
	protected Spinner mImportFormat; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setStartActivity(R.id.main_new_task, NewTaskActivity.class);
		setStartActivity(R.id.main_learning, LearningActivity.class);
		setStartActivity(R.id.main_network_visualisation, VisualisationActivity.class);
		setStartActivity(R.id.main_result_visualisation, ResultActivity.class);
		setStartActivity(R.id.main_result_input, ResultInputActivity.class);
		
		View v = findViewById(R.id.main_export);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent i = new Intent(MainActivity.this, ExportActivity.class);
				i.putExtra(ExportActivity.EXTRA_FORMAT, mExportFormat.getSelectedItemPosition());
				startActivity(i);
			}
		});
		
		mExportFormat = (Spinner) findViewById(R.id.main_export_format);
		
		v = findViewById(R.id.main_import);
		v.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final Intent i = new Intent(MainActivity.this, ImportDataActivity.class);
				i.putExtra(ExportActivity.EXTRA_FORMAT, mImportFormat.getSelectedItemPosition());
				startActivity(i);
			}
		});
		
		mImportFormat = (Spinner) findViewById(R.id.main_import_format);
		
		Intent startIntent = getIntent();
		Uri uri = startIntent.getData();
		if (uri != null)
		{
			Uri.Builder builder = uri.buildUpon();
			builder.scheme("http");
			uri = builder.build();
			
			Intent i = new Intent(this, ImportDataActivity.class);
			i.putExtra(ImportDataActivity.INTENT_EXTRA_URL, uri.toString());
			startActivity(i);
		}

	}
	
	protected void refresh()
	{
		boolean enabled = (NetworkApplication.sNetwork != null);
		findViewById(R.id.main_network_visualisation).setEnabled(enabled);
		findViewById(R.id.main_result_visualisation).setEnabled(enabled);
		findViewById(R.id.main_result_input).setEnabled(enabled);
		findViewById(R.id.main_learning).setEnabled(enabled);
		findViewById(R.id.main_export).setEnabled(enabled);
		mExportFormat.setEnabled(enabled);
	}

	@Override
	protected void onResume() {
		super.onResume();
		refresh();
	}

	@Override
	protected void onDestroy() {
		if (NetworkApplication.sNetwork != null)
		{
			StringWriter writer = new StringWriter();
			try {
				NetworkApplication.sNetwork.saveXml(writer);
				savePref(NetworkApplication.PREFS_STORED_NET, writer.toString());
			} catch (IOException e)
			{
				// ignore
			}
		}
		super.onDestroy();
	}
	
	@Override
	public String getHelpLink()
	{
		return "main.php";
	}
	
}
