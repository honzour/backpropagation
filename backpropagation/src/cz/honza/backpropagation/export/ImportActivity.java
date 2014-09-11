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
	
	protected Node getFirstChildWithName(Node root, String name, boolean toastError)
	{
		NodeList nodes = root.getChildNodes();
		final int length = nodes.getLength();
		for (int i = 0; i < length; i++)
		{
			 final Node n = nodes.item(i);
			 final String s = n.getNodeName();
			 if (s != null && s.equals(name))
			 {
				 return n;
			 }
		}
		if (toastError)
		{
			Toast.makeText(ImportActivity.this, "No " + name + " tag", Toast.LENGTH_LONG).show();
		}
		return null;
	}
	
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

					final Node network = getFirstChildWithName(doc, Xml.NETWORK, true);
					if (network == null)
					{
						return;
					}
					final Node layers = getFirstChildWithName(network, Xml.LAYERS, true);

					if (layers == null)
					{
						return;
					}
				}
				catch (Throwable e)
				{
					Toast.makeText(ImportActivity.this, e.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}
}
