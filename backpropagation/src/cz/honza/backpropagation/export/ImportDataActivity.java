package cz.honza.backpropagation.export;

import java.io.FileInputStream;
import java.io.InputStream;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.parser.Parser;
import cz.honza.backpropagation.network.parser.ParserResultHandler;

public class ImportDataActivity extends NetworkActivity {
	
	public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";
	
	protected View mFileButton;
	protected View mWebButton;
	protected TextView mFileText;
	protected TextView mWwwText;
	protected EditText mFileName;
	protected EditText mUrl;

	protected FromWebThread mThread = null;
	protected boolean mXml;
	protected int mFormat;


	protected void download(String url)
	{
		savePref(mXml ? NetworkApplication.PREFS_DEFAULT_IMPORT_XML_URL : NetworkApplication.PREFS_DEFAULT_IMPORT_CSV_URL, url);
		if (url.length() == 0)
		{
			Toast.makeText(this, R.string.enter_url_first, Toast.LENGTH_LONG).show();
			return;
		}
		mThread = new FromWebThread(this, url, mXml ? ExportActivity.EXTRA_FORMAT_XML : ExportActivity.EXTRA_FORMAT_CSV);
		mWebButton.setEnabled(false);
		mThread.start();
		
	}
	
	protected void initGui()
	{
		mFileText = (TextView)findViewById(R.id.import_load_data_text);
		mWwwText = (TextView)findViewById(R.id.import_load_www_text);
		findViewById(R.id.import_load_www_text);
		if (mXml)
		{
			setTitle(R.string.import_xml);
			mFileText.setText(R.string.load_xml_data);
			mWwwText.setText(R.string.load_xml_www);
		}
		else
		{
			setTitle(R.string.import_csv);
			mFileText.setText(R.string.load_csv_data);
			mWwwText.setText(R.string.load_csv_www);
		}
		mFileButton = findViewById(R.id.import_load);
		mFileName = (EditText)findViewById(R.id.import_load_text);
		final String defaultHint = ExportActivity.getDefaultFileName(mXml ? ExportActivity.EXTRA_FORMAT_XML : ExportActivity.EXTRA_FORMAT_CSV);
		
		mFileName.setHint(defaultHint);
		final String defaultFile = loadPref(mXml ? NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE : NetworkApplication.PREFS_DEFAULT_EXPORT_CSV_FILE, defaultHint);
		mFileName.setText(defaultFile);
		
		mWebButton = findViewById(R.id.import_www);
		
		mThread = (FromWebThread) getLastNonConfigurationInstance();
		if (mThread != null)
		{
			mThread.setContext(this);
			mWebButton.setEnabled(false);
		}
		
		mUrl = (EditText)findViewById(R.id.import_www_text);
		mUrl.setText(loadPref(mXml ? NetworkApplication.PREFS_DEFAULT_IMPORT_XML_URL : NetworkApplication.PREFS_DEFAULT_IMPORT_CSV_URL, ""));
		
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
				savePref(mXml ? NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE : NetworkApplication.PREFS_DEFAULT_EXPORT_CSV_FILE, filename);
				
				if (filename.length() == 0)
				{
					Toast.makeText(ImportDataActivity.this, R.string.enter_filename_first, Toast.LENGTH_LONG).show();
					return;
				}
				try
				{
					final InputStream inputStream = new FileInputStream(filename);
					final ParserResultHandler handler = new ParserResultHandler() {
						
						@Override
						public void onFinished(Network network) {
							NetworkApplication.sNetwork = network;
							finish();
						}
						
						@Override
						public void onError(String error) {
							Toast.makeText(ImportDataActivity.this, error, Toast.LENGTH_LONG).show();							
						}
					};
					if (mXml)
						Parser.parseXml(inputStream, handler);
					else
						Parser.parseCsv(inputStream, handler);
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
		
		mFormat = getIntent().getIntExtra(ExportActivity.EXTRA_FORMAT, -1);
		String url = getIntent().getStringExtra(INTENT_EXTRA_URL);
		
		mXml = false;
		if (mFormat == ExportActivity.EXTRA_FORMAT_XML)
			mXml = true;
		if (mFormat < 0)
			if (url != null)
			{
				mXml = url.endsWith(".xml");
			}
			
		initGui();
		
		if (url != null)
		{
			download(url);
		}
	}
	
	@Override
	public String getHelpLink()
	{
		return mFormat == ExportActivity.EXTRA_FORMAT_XML ? "importxml.php" : "importcsv.php";
	}

}
