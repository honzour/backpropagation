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

public class ImportActivity extends NetworkActivity {
	
	private View mFileButton;
	private View mWebButton;
	private EditText mFileName;
	private EditText mUrl;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_xml);
		
	

		mFileButton = findViewById(R.id.import_load);
		mFileName = (EditText)findViewById(R.id.import_load_text);
		mWebButton = findViewById(R.id.import_www);
		mUrl = (EditText)findViewById(R.id.import_www_text);
/*		
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
		*/

	}
/*
	@Override
	protected Dialog onCreateDialog(int id) {
		mFileName = new EditText(this);
		final File dir = Environment.getExternalStorageDirectory();
		String name = getResources().getText(R.string.network_xml).toString();
		if (dir != null)
		{
			name = dir.getAbsolutePath() + "/" + name;
		}
		mFileName.setText(name);
		
		return new AlertDialog.Builder(this)
	    .setTitle(R.string.save)
	    .setMessage(R.string.save_to_public_memory)
	    .setView(mFileName)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	String filename = mFileName.getText().toString();
	        	try
	        	{
	        		NetworkApplication.sNetwork.save(filename);
	        		Toast.makeText(ImportActivity.this, R.string.file_saved, Toast.LENGTH_LONG).show();
	        	}
	        	catch (IOException e)
	        	{
	        		Toast.makeText(ImportActivity.this, getResources().getText(R.string.cannot_export) + " " + filename,
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
	
	*/
}
