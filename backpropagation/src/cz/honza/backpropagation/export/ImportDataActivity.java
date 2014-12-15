package cz.honza.backpropagation.export;

import java.io.FileInputStream;
import java.io.InputStream;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.Parser;
import cz.honza.backpropagation.network.ParserResultHandler;

public class ImportDataActivity extends NetworkActivity {
	
	public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";
	
	private View mFileButton;
	View mWebButton;
	private EditText mFileName;
	private EditText mUrl;

	private FromWebThread mThread = null;


	protected void download(String url)
	{
		boolean xml = url.endsWith(".xml");
		
		savePref(xml ? NetworkApplication.PREFS_DEFAULT_IMPORT_XML_URL : NetworkApplication.PREFS_DEFAULT_IMPORT_CSV_URL, url);
		if (url.length() == 0)
		{
			Toast.makeText(this, R.string.enter_url_first, Toast.LENGTH_LONG).show();
			return;
		}
		mThread = new FromWebThread(this, url, xml ? ExportActivity.EXTRA_FORMAT_XML : ExportActivity.EXTRA_FORMAT_CSV);
		mWebButton.setEnabled(false);
		mThread.start();
		
	}
	
	protected void init()
	{
		mFileButton = findViewById(R.id.import_load);
		mFileName = (EditText)findViewById(R.id.import_load_text);
		final String defaultHint = ExportActivity.getDefaultFileName(ExportActivity.EXTRA_FORMAT_XML);
		
		mFileName.setHint(defaultHint);
		final String defaultFile = loadPref(NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE, defaultHint);
		mFileName.setText(defaultFile);
		
		mWebButton = findViewById(R.id.import_www);
		
		mThread = (FromWebThread) getLastNonConfigurationInstance();
		if (mThread != null)
		{
			mThread.setContext(this);
			mWebButton.setEnabled(false);
		}
		
		mUrl = (EditText)findViewById(R.id.import_www_text);
		mUrl.setText(loadPref(NetworkApplication.PREFS_DEFAULT_IMPORT_XML_URL, ""));
		
		mWebButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String url = mUrl.getText().toString();
				download(url);
			}
		});
		
		mFileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String filename = mFileName.getText().toString();
				savePref(NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE, filename);
				
				if (filename.length() == 0)
				{
					Toast.makeText(ImportDataActivity.this, R.string.enter_filename_first, Toast.LENGTH_LONG).show();
					return;
				}
				try
				{
					final InputStream inputStream = new FileInputStream(filename);
					Parser.parseXml(inputStream, new ParserResultHandler() {
						
						@Override
						public void onFinished(Network network) {
							NetworkApplication.sNetwork = network;
							finish();
						}
						
						@Override
						public void onError(String error) {
							Toast.makeText(ImportDataActivity.this, error, Toast.LENGTH_LONG).show();							
						}
					});
					inputStream.close();
				}
				catch (Throwable e)
				{
					Toast.makeText(ImportDataActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_data);
		
		init();
		
		String url = getIntent().getStringExtra(INTENT_EXTRA_URL);
		if (url != null)
		{
			download(url);
		}
	}

}
