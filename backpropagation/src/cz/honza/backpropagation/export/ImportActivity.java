package cz.honza.backpropagation.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.TrainingSet;
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
	
	protected List<Double> parseNumbers(Node node)
	{
		final List<Double> result = new ArrayList<Double>();
		final NodeList numbers = node.getChildNodes();
		final int numbersCount = numbers.getLength();
		for (int j = 0; j < numbersCount; j++)
		{
			final Node number = numbers.item(j);
			if (!number.getNodeName().equals(Xml.NUMBER))
				continue;
			Double value;
			try {
				Node data = number.getChildNodes().item(0);
				value = Double.valueOf(((Text)data).getData());
			} catch (Exception e)
			{
				value = null;
			}
			
			result.add(value);
		}
		return result;
	}
	
	protected List<List<List<Double>>> parseTraining(Node network) {
		final Node trainingNode = getFirstChildWithName(network, Xml.TRAINING, true);
		if (trainingNode == null)
		{
			return null;
		}
		
		final List<List<List<Double>>> result = new ArrayList<List<List<Double>>>();
		final List<List<Double>> inputList = new ArrayList<List<Double>>();
		final List<List<Double>> outputList = new ArrayList<List<Double>>();
		result.add(inputList);
		result.add(outputList);
		
		final Node inputsNode = getFirstChildWithName(trainingNode, Xml.INPUTS, true);
		if (inputsNode != null)
		{
			final NodeList inputs = inputsNode.getChildNodes();
			final int inputsCount = inputs.getLength();
			for (int i = 0; i < inputsCount; i++)
			{
				final Node inputNode = inputs.item(i);
				if (!inputNode.getNodeName().equals(Xml.INPUT))
					continue;
				inputList.add(parseNumbers(inputNode));
			}
		}
		
		final Node outputsNode = getFirstChildWithName(trainingNode, Xml.OUTPUTS, true);
		if (outputsNode != null)
		{
			final NodeList outputs = outputsNode.getChildNodes();
			final int outputsCount = outputs.getLength();
			for (int i = 0; i < outputsCount; i++)
			{
				final Node outputNode = outputs.item(i);
				if (!outputNode.getNodeName().equals(Xml.OUTPUT))
					continue;
				outputList.add(parseNumbers(outputNode));
			}
		}
		
		return result;
	}
	
	protected List<List<List<Double>>> parseLayers(Node network) {
		final Node layersNode = getFirstChildWithName(network, Xml.LAYERS, true);

		if (layersNode == null)
		{
			return null;
		}
		
		NodeList layers = layersNode.getChildNodes();

		final List<List<List<Double>>> layersData = new ArrayList<List<List<Double>>>();
		
		final int layersCount = layers.getLength();
		
		for (int i = 0; i < layersCount; i++)
		{
			final Node layerNode = layers.item(i);
			String name = layerNode.getNodeName();
			if (name == null || !name.equals(Xml.LAYER))
				continue;
			
			final List<List<Double>> layerData = new ArrayList<List<Double>>();
			
			final NodeList neurons = layerNode.getChildNodes();
			final int neuronsCount = neurons.getLength();
			for (int j = 0; j < neuronsCount; j++)
			{
				final Node neuronNode = neurons.item(j);
				name = neuronNode.getNodeName();
				if (name == null || !name.equals(Xml.NEURON))
					continue;
				
				final List<Double> neuronData = new ArrayList<Double>();
				
				final NodeList weights = neuronNode.getChildNodes();
				final int weightsCount = weights.getLength();
				
				for (int k = 0; k < weightsCount; k++)
				{
					final Node weightNode = weights.item(k);
					name = weightNode.getNodeName();
					if (name == null || !name.equals(Xml.WEIGHT))
						continue;
				
					
					Double val = null;
					
					try {
						Node data = weightNode.getChildNodes().item(0);
						val = Double.valueOf(((Text)data).getData());
					} catch (Exception e)
					{
					}
					
					neuronData.add(val);
				}
				
				layerData.add(neuronData);							
			}
			
			layersData.add(layerData);
		}
		return layersData;
	}
	
	
	protected void parseFile()
	{
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
			
			final List<List<List<Double>>> layers = parseLayers(network);
			if (layers == null)
				return;
			
			final List<List<List<Double>>> training = parseTraining(network);
			if (training == null)
				return;
			
			Network networkTmp = new Network(layers, training);
			// TODO check
			NetworkApplication.sNetwork = networkTmp;
		}
		catch (Throwable e)
		{
			Toast.makeText(ImportActivity.this, e.toString(), Toast.LENGTH_LONG).show();
		}
		finish();

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.import_xml);

		Spinner problem = (Spinner) findViewById(R.id.import_new_task);
		problem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int item, long arg3) {
				
				if (item == 0)
					return;
				
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
				
				
				switch (item)
				{
				case 1:
					NetworkApplication.sNetwork = new Network(id_anatomy, new TrainingSet(id_inputs, id_outputs));
					break;
				case 2:
					NetworkApplication.sNetwork = new Network(not_anatomy, new TrainingSet(not_inputs, not_outputs));
					break;
				case 3:
					NetworkApplication.sNetwork = new Network(and_anatomy, new TrainingSet(and_inputs, and_outputs));
					break;
				case 4:
					NetworkApplication.sNetwork = new Network(or_anatomy, new TrainingSet(or_inputs, or_outputs));
					break;
				case 5:
					NetworkApplication.sNetwork = new Network(xor_anatomy, new TrainingSet(xor_inputs, xor_outputs));
					break;
				case 6:
					NetworkApplication.sNetwork = new Network(ex_anatomy, new TrainingSet(ex_inputs, ex_outputs));
					break;
				case 7:
					NetworkApplication.sNetwork = new Network(chess_anatomy, new TrainingSet(chess_inputs, chess_outputs));
					break;

				}
				finish();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});

		
		
		mFileButton = findViewById(R.id.import_load);
		mFileName = (EditText)findViewById(R.id.import_load_text);
		final String defaultHint = ExportActivity.getDefaultXmlName();
		mFileName.setText(defaultHint);
		mFileName.setHint(defaultHint);
		
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
				parseFile();
			}
		});
	}
}
