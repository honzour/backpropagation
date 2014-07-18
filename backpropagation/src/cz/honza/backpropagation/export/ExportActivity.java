package cz.honza.backpropagation.export;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
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
				// TODO Auto-generated method stub
				
			}
		});

	}

	@Override
	protected Dialog onCreateDialog(int id) {
		mFileName = new EditText(this);
		final File dir = Environment.getExternalStorageDirectory();
		String name = "network.xml";
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

	        }
	     })
	    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 

	        }
	     })
	    .setIcon(android.R.drawable.ic_dialog_alert).create();
	}
	
	
}
