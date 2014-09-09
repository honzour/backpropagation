package cz.honza.backpropagation.export;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.Xml;
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
		
		mFileName.setText(ExportActivity.getDefaultXmlName());
		
		mWebButton = findViewById(R.id.import_www);
		mUrl = (EditText)findViewById(R.id.import_www_text);
		
		mWebButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String url = mUrl.getText().toString();
				if (url.length() == 0)
					Toast.makeText(ImportActivity.this, R.string.enter_filename_first, Toast.LENGTH_LONG).show();
			}
		});
		
		mFileButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String filename = mFileName.getText().toString();
				
				if (filename.length() == 0)
					Toast.makeText(ImportActivity.this, R.string.enter_filename_first, Toast.LENGTH_LONG).show();
				
				try
				{
					File fXmlFile = new File(filename);
					DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
					DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
					Document doc = dBuilder.parse(fXmlFile);
					doc.getDocumentElement().normalize();
					NodeList nodes = doc.getChildNodes();
					final int length = nodes.getLength();
					Node network = null;
					for (int i = 0; i < length; i++)
					{
						 network = nodes.item(i);
						 break;
					}
					if (network == null)
					throw new RuntimeException("No " + Xml.NETWORK + " tag");
				}
				catch (Throwable e)
				{
					Toast.makeText(ImportActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
