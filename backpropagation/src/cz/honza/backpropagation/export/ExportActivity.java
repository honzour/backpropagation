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
import cz.honza.backpropagation.components.NetworkActivity;

public class ExportActivity extends NetworkActivity {
	
	public static final int EXTRA_FORMAT_XML = 1;
	public static final int EXTRA_FORMAT_CSV = 0;
	public static final String EXTRA_FORMAT = "EXTRA_FORMAT";
	
	protected int mFormat;
	
	private View mSaveButton;
	private View mMailButton;
	private EditText mFileName;

	private String getXml() throws IOException
	{
		final StringWriter writer = new StringWriter();
		NetworkApplication.sNetwork.saveXml(writer);
		return writer.getBuffer().toString();
	}
	
	private String getCsv() throws IOException
	{
		final StringWriter writer = new StringWriter();
		NetworkApplication.sNetwork.saveCsv(writer);
		return writer.getBuffer().toString();
	}
	
	private String getValue() throws IOException
	{
		switch (mFormat)
		{
		case EXTRA_FORMAT_CSV:
			return getCsv();
		default:
			return getXml();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mFormat = getIntent().getIntExtra(EXTRA_FORMAT, EXTRA_FORMAT_CSV);
		switch (mFormat)
		{
		case EXTRA_FORMAT_CSV:
			setTitle(R.string.export_csv);
			break;
		case EXTRA_FORMAT_XML:
			setTitle(R.string.export_xml);
			break;
		}
		
		setContentView(R.layout.export);
		
		TextView value = (TextView) findViewById(R.id.export_text);
		try
		{
			value.setText(getValue());
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
				
				String value = "";
				try
				{
					value = getValue();
				}
				catch (IOException e)
				{
					// should not happen
				}
				
				
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("text/plain");
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {""});
				intent.putExtra(Intent.EXTRA_SUBJECT, getResources().getText(
						mFormat == EXTRA_FORMAT_XML ? R.string.neural_network_xml : R.string.neural_network_csv 
						));
				intent.putExtra(Intent.EXTRA_TEXT, value);
				Intent chooser = Intent.createChooser(intent, getResources().getText(R.string.send_mail));
				
				if (chooser != null)
					startActivity(chooser);
				
			}
		});

	}
	
	public static String getDefaultFileName(int format)
	{
		final File dir = Environment.getExternalStorageDirectory();
		String name = NetworkApplication.sInstance.getResources().getText(
				format == EXTRA_FORMAT_XML ? R.string.network_xml : R.string.network_csv
				).toString();
		if (dir != null)
		{
			name = dir.getAbsolutePath() + "/" + name;
		}
		return name;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		mFileName = new EditText(this);
		final String hint = getDefaultFileName(mFormat);
		mFileName.setHint(hint);
		mFileName.setText(loadPref(
				mFormat == EXTRA_FORMAT_XML ? NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE : NetworkApplication.PREFS_DEFAULT_EXPORT_CSV_FILE,
				hint));
		
		return new AlertDialog.Builder(this)
	    .setTitle(R.string.save)
	    .setMessage(R.string.save_to_public_memory)
	    .setView(mFileName)
	    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) {
	        	String filename = mFileName.getText().toString();
	        	savePref(mFormat == EXTRA_FORMAT_XML ? NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE : NetworkApplication.PREFS_DEFAULT_EXPORT_CSV_FILE, filename);
	        	try
	        	{
	        		switch (mFormat)
	        		{
	        		case EXTRA_FORMAT_CSV:
	        			NetworkApplication.sNetwork.saveCsv(filename);
	        			break;
	        		default:
	        			NetworkApplication.sNetwork.saveXml(filename);
	        			break;
	        		}
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
	
	@Override
	public String getHelpLink()
	{
		return mFormat == EXTRA_FORMAT_XML ? "exportxml.php" : "exportcsv.php";
	}
}
