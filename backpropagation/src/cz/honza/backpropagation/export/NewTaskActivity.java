package cz.honza.backpropagation.export;

import java.io.FileInputStream;
import java.io.InputStream;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.editor.EditorActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.Parser;
import cz.honza.backpropagation.network.ParserResultHandler;
import cz.honza.backpropagation.network.TrainingSet;

public class NewTaskActivity extends NetworkActivity {
	
	private View mFileXmlButton;
	View mWebXmlButton;
	private EditText mFileNameXml;
	private EditText mUrlXml;
	
	private View mFileCsvButton;
	View mWebCsvButton;
	private EditText mFileNameCsv;
	private EditText mUrlCsv;
	
	private View mExampleButton;
	private View mEditorButton;
	
	private Spinner mExample;
	private FromWebThread mThread = null;
	private static final int REQUEST_CODE_EDITOR = 0;
	
	public static final String INTENT_EXTRA_NETWORK = "INTENT_EXTRA_NETWORK";
	public static final String INTENT_EXTRA_URL = "INTENT_EXTRA_URL";

	protected void loadExample()
	{
		final int item = mExample.getSelectedItemPosition(); 
		
		// Identity
		int[] id_anatomy = {1, 1};
		double[][] id_inputs = {{0}, {1}};
		double[][] id_outputs = {{0}, {1}};
		
		// NOT
		int[] not_anatomy = {1, 1};
		double[][] not_inputs = {{0}, {1}};
		double[][] not_outputs = {{1}, {0}};
						
		// AND
		int[] and_anatomy = {2, 1};
		double[][] and_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] and_outputs = {{0}, {0}, {0}, {1}};
						
		// OR
		int[] or_anatomy = {2, 1};
		double[][] or_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] or_outputs = {{0}, {1}, {1}, {1}};
		
		// XOR
		int[] xor_anatomy = {2, 2, 1};
		double[][] xor_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] xor_outputs = {{0}, {1}, {1}, {0}};
		
		// Exception
		int[] ex_anatomy = {2, 4, 1};
		double[][] ex_inputs =  {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {0.5, 0}, {0.5, 1}, {0, 0.5}, {1, 0.5},
								  {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5} };
		double[][] ex_outputs = {{0},    {0},    {0},    {0},    {0},      {0},      {0},      {0},
								  {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1} };
		
		// Chess board
		int[] chess_anatomy = {2, 2, 2, 1};
		double[][] chess_inputs = 
			{{0, 0}, {0, 0.4}, {0.4, 0}, {0.4, 0.4},
			 {0.6, 0.6}, {0.6, 1}, {1, 0.6}, {1, 1},
			 {0.6, 0}, {1, 0}, {1, 0.4}, {0.6, 0.4},
			 {0, 0.6}, {0.4, 1}, {0.4, 0.6}, {0, 1}
			};

		double[][] chess_outputs =
			{{0}, {0}, {0}, {0},
			 {0}, {0}, {0}, {0},
			 {1}, {1}, {1}, {1},
			 {1}, {1}, {1}, {1}						
			};
		
		// 3D
		int[] multioutput_anatomy = {2, 3};
		double[][] multioutput_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] multioutput_outputs = {{0, 0, 0}, {0, 1, 0}, {1, 0, 0}, {1, 1, 1}};
		
		
		switch (item)
		{
		case 0:
			NetworkApplication.sNetwork = new Network(id_anatomy, new TrainingSet(id_inputs, id_outputs));
			break;
		case 1:
			NetworkApplication.sNetwork = new Network(not_anatomy, new TrainingSet(not_inputs, not_outputs));
			break;
		case 2:
			NetworkApplication.sNetwork = new Network(and_anatomy, new TrainingSet(and_inputs, and_outputs));
			break;
		case 3:
			NetworkApplication.sNetwork = new Network(or_anatomy, new TrainingSet(or_inputs, or_outputs));
			break;
		case 4:
			NetworkApplication.sNetwork = new Network(xor_anatomy, new TrainingSet(xor_inputs, xor_outputs));
			break;
		case 5:
			NetworkApplication.sNetwork = new Network(ex_anatomy, new TrainingSet(ex_inputs, ex_outputs));
			break;
		case 6:
			NetworkApplication.sNetwork = new Network(chess_anatomy, new TrainingSet(chess_inputs, chess_outputs));
			break;
		case 7:
			NetworkApplication.sNetwork = new Network(multioutput_anatomy, new TrainingSet(multioutput_inputs, multioutput_outputs));
			break;

		}
		finish();
	}
	
	protected void initExamples()
	{
		mExample = (Spinner) findViewById(R.id.import_new_task);
		mExampleButton = findViewById(R.id.import_examle);
		mExampleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadExample();
			}
		});
	}
	
	protected void downloadXml(String url)
	{
		savePref(NetworkApplication.PREFS_DEFAULT_IMPORT_XML_URL, url);
		if (url.length() == 0)
		{
			Toast.makeText(NewTaskActivity.this, R.string.enter_url_first, Toast.LENGTH_LONG).show();
			return;
		}
		mThread = new FromWebThread(NewTaskActivity.this, url, ExportActivity.EXTRA_FORMAT_XML);
		mWebXmlButton.setEnabled(false);
		mThread.start();
		
	}
	
	protected void initXml()
	{
		mFileXmlButton = findViewById(R.id.import_load_xml);
		mFileNameXml = (EditText)findViewById(R.id.import_load_text_xml);
		final String defaultHint = ExportActivity.getDefaultFileName(ExportActivity.EXTRA_FORMAT_XML);
		
		mFileNameXml.setHint(defaultHint);
		final String defaultFile = loadPref(NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE, defaultHint);
		mFileNameXml.setText(defaultFile);
		
		mWebXmlButton = findViewById(R.id.import_www_xml);
		
		mThread = (FromWebThread) getLastNonConfigurationInstance();
		if (mThread != null)
		{
			mThread.setContext(this);
			mWebXmlButton.setEnabled(false);
		}
		
		mUrlXml = (EditText)findViewById(R.id.import_www_text_xml);
		mUrlXml.setText(loadPref(NetworkApplication.PREFS_DEFAULT_IMPORT_XML_URL, ""));
		
		mWebXmlButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String url = mUrlXml.getText().toString();
				downloadXml(url);
			}
		});
		
		mFileXmlButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String filename = mFileNameXml.getText().toString();
				savePref(NetworkApplication.PREFS_DEFAULT_EXPORT_XML_FILE, filename);
				
				if (filename.length() == 0)
				{
					Toast.makeText(NewTaskActivity.this, R.string.enter_filename_first, Toast.LENGTH_LONG).show();
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
							Toast.makeText(NewTaskActivity.this, error, Toast.LENGTH_LONG).show();							
						}
					});
					inputStream.close();
				}
				catch (Throwable e)
				{
					Toast.makeText(NewTaskActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	protected void downloadCsv(String url)
	{
		savePref(NetworkApplication.PREFS_DEFAULT_IMPORT_CSV_URL, url);
		if (url.length() == 0)
		{
			Toast.makeText(NewTaskActivity.this, R.string.enter_url_first, Toast.LENGTH_LONG).show();
			return;
		}
		mThread = new FromWebThread(NewTaskActivity.this, url, ExportActivity.EXTRA_FORMAT_CSV);
		mWebCsvButton.setEnabled(false);
		mThread.start();
	}
	
	protected void initCsv()
	{
		mFileCsvButton = findViewById(R.id.import_load_csv);
		mFileNameCsv = (EditText)findViewById(R.id.import_load_text_csv);
		final String defaultHint = ExportActivity.getDefaultFileName(ExportActivity.EXTRA_FORMAT_CSV);
		
		mFileNameCsv.setHint(defaultHint);
		final String defaultFile = loadPref(NetworkApplication.PREFS_DEFAULT_EXPORT_CSV_FILE, defaultHint);
		mFileNameCsv.setText(defaultFile);
		
		mWebCsvButton = findViewById(R.id.import_www_csv);
		
		mThread = (FromWebThread) getLastNonConfigurationInstance();
		if (mThread != null)
		{
			mThread.setContext(this);
			mWebCsvButton.setEnabled(false);
		}
		
		mUrlCsv = (EditText)findViewById(R.id.import_www_text_csv);
		mUrlCsv.setText(loadPref(NetworkApplication.PREFS_DEFAULT_IMPORT_CSV_URL, ""));
		
		mWebCsvButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String url = mUrlCsv.getText().toString();
				downloadCsv(url);
			}
		});
		
		mFileCsvButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String filename = mFileNameCsv.getText().toString();
				savePref(NetworkApplication.PREFS_DEFAULT_EXPORT_CSV_FILE, filename);
				
				if (filename.length() == 0)
				{
					Toast.makeText(NewTaskActivity.this, R.string.enter_filename_first, Toast.LENGTH_LONG).show();
					return;
				}
				try
				{
					final InputStream inputStream = new FileInputStream(filename);
					Parser.parseCsv(inputStream, new ParserResultHandler() {
						
						@Override
						public void onFinished(Network network) {
							NetworkApplication.sNetwork = network;
							finish();
						}
						
						@Override
						public void onError(String error) {
							Toast.makeText(NewTaskActivity.this, error, Toast.LENGTH_LONG).show();							
						}
					});
					inputStream.close();
				}
				catch (Throwable e)
				{
					Toast.makeText(NewTaskActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	protected void initEditor()
	{
		mEditorButton = findViewById(R.id.import_run_editor);
		mEditorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(NewTaskActivity.this, EditorActivity.class);
				i.putExtra(INTENT_EXTRA_NETWORK, NetworkApplication.sNetwork);
				startActivityForResult(i, REQUEST_CODE_EDITOR);
			}
		});
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_task);
		
		initExamples();
		initXml();
		initCsv();
		initEditor();
		
		String url = getIntent().getStringExtra(INTENT_EXTRA_URL);
		if (url != null)
		{
			if (url.endsWith(".xml"))
				downloadXml(url);
			else
				downloadCsv(url);
		}
	}
	
	@Override
	public Object onRetainNonConfigurationInstance () {
		return mThread;
	}

	@Override
	protected void onDestroy() {
		if (mThread != null)
		{
			mThread.setContext(null);
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_EDITOR && resultCode == RESULT_OK)
		{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public String getHelpLink()
	{
		return "newtask.php";
	}
	
}
