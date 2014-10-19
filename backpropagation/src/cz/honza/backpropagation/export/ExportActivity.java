package cz.honza.backpropagation.export;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class ExportActivity extends NetworkActivity {
	
	private View mSaveButton;
	private View mMailButton;
	private EditText mFileName;

	private String getXml() throws IOException
	{
		final StringWriter writer = new StringWriter();
		NetworkApplication.sNetwork.save(writer);
		return writer.getBuffer().toString();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.export);
		
		TextView xml = (TextView) findViewById(R.id.export_text);
		try
		{
			xml.setText(getXml());
		}
		catch (IOException e)
		{
			// should not happen
		}
		
		mSaveButton = findViewById(R.id.export_save);
		mMailButton = findViewById(R.id.export_mail);
		
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(0);				
			}
		});
		
		mMailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String xml = "";
				try
				{
					xml = getXml();
				}
				catch (IOException e)
				{
					// should not happen
				}
				
				
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
				intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(R.string.neural_network_xml));
				intent.putExtra(Intent.EXTRA_TEXT, xml);
				Intent chooser = Intent.createChooser(intent, getResources().getText(R.string.send_mail));
				
				if (chooser != null)
					startActivity(chooser);
				
			}
		});

	}
	
	public static String getDefaultXmlName()
	{
		final File dir = Environment.getExternalStorageDirectory();
		String name = NetworkApplication.sInstance.getResources().getText(R.string.network_xml).toString();
		if (dir != null)
		{
			name = dir.getAbsolutePath() + "/" + name;
		}
		return name;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		mFileName = new EditText(this);
		final String hint = getDefaultXmlName();
		mFileName.setHint(hint);
		mFileName.setText(loadPref(NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE, hint));
		
		return new AlertDialog.Builder(this)
	    .setTitle(R.string.save)
	    .setMessage(R.string.save_to_public_memory)
	    .setView(mFileName)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	String filename = mFileName.getText().toString();
	        	savePref(NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE, filename);
	        	try
	        	{
	        		NetworkApplication.sNetwork.save(filename);
	        		Toast.makeText(ExportActivity.this, R.string.file_saved, Toast.LENGTH_LONG).show();
	        	}
	        	catch (IOException e)
	        	{
	        		Toast.makeText(ExportActivity.this, getResources().getText(R.string.cannot_export) + " " + filename,
	        				Toast.LENGTH_LONG).show();
	        	}
	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

	        }
	     })
	    .create();
	}
	
	
}
