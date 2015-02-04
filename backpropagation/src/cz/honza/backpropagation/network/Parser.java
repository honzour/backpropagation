package cz.honza.backpropagation.network;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;

public class Parser {
	
	protected static int[] line2ints(String line, int lineNumber, ParserResultHandler handler)
	{
		String[] textVals = line.split(",");
		int[] vals = new int[textVals.length];
		for (int i = 0; i < textVals.length; i++)
		{
			try
			{
				vals[i] = Integer.valueOf(textVals[i]);
				if (vals[i] <= 0)
				{
					handler.onError(R.string.negative_layer_size);	
				}
			}
			catch (NumberFormatException e)
			{
				String error = String.format(Locale.getDefault(), NetworkApplication.sInstance.getResources().getString(R.string.parser_error_convert), lineNumber, line, textVals[i]);
				handler.onError(error);
				return null;
			}
		}
		return vals;
	}
	
	protected static double[] line2doubles(String line, int ignoredIndex, int lineNumber, ParserResultHandler handler)
	{
		String[] textVals = line.split(",");
		double[] vals = new double[textVals.length];
		for (int i = 0; i < textVals.length; i++)
		{
			try
			{
				if (i != ignoredIndex)
					vals[i] = Double.valueOf(textVals[i]);
				else
					if (textVals[i].trim().length() > 0)
					{
						String error = String.format(Locale.getDefault(), NetworkApplication.sInstance.getResources().getString(R.string.parser_error_missing_space), lineNumber, line, textVals[i]);
						handler.onError(error);
						return null;
					}
			}
			catch (NumberFormatException e)
			{
				String error = String.format(Locale.getDefault(), NetworkApplication.sInstance.getResources().getString(R.string.parser_error_convert), lineNumber, line, textVals[i]);
				handler.onError(error);
				return null;
			}
		}
		return vals;
	}
	
	public static void parseCsv(InputStream is, ParserResultHandler handler)
	{
		try
		{
			CsvBufferedReader in = new CsvBufferedReader(new InputStreamReader(is));
			String line = in.readLine();
			if (line == null)
			{
				handler.onError(R.string.missing_format_description);
				return;
			}
			if (!line.equals("CSV1"))
			{
				handler.onError(R.string.unknown_format);
				return;
			}
			line = in.readLine();
			if (line == null)
			{
				handler.onError(R.string.missing_anatomy);
				return;
			}
			
			final int[] anatomy = line2ints(line, 0, handler);
			if (anatomy == null)
				return;
			if (anatomy.length < 2)
			{
				handler.onError(R.string.not_enough_layers);
				return;
			}
			
			List<double[]> training = new ArrayList<double[]>();
			
			final int inputDim = anatomy[0];
			final int outputDim = anatomy[anatomy.length - 1];
			
			while ((line = in.readLine()) != null)
			{
				final double[] elements = line2doubles(line, anatomy[0], in.getLine(), handler);
				if (elements == null)
					return;
				training.add(elements);
			}
			
			if (training.size() == 0)
			{
				handler.onError(R.string.empty_training_set);
				return;
			}
			
			// all parsed in int[] anatomy, List<double[]> training
			
			double inputs[][] = new double[training.size()][];
			double outputs[][] = new double[training.size()][];
			
			for (int i = 0; i < training.size(); i++)
			{
				inputs[i] = new double[inputDim];
				outputs[i] = new double[outputDim];
				double[] element = training.get(i);
				
				System.arraycopy(element, 0, inputs[i], 0, inputDim);
				System.arraycopy(element, inputDim + 1, outputs[i], 0, outputDim);
			}
			
			TrainingSet trainingSet = new TrainingSetBase(inputs, outputs);
			Network n = new Network(anatomy, trainingSet);
			if (n.check(handler))
			{
				handler.onFinished(n);
			}
			
		}
		catch (Exception e)
		{
			handler.onError(e.toString());
		}
		
	}
	
	
	public static void parseXml(InputStream is, ParserResultHandler handler)
	{
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		
			Document doc = dBuilder.parse(is);
			doc.getDocumentElement().normalize();

			final Node network = getFirstChildWithName(doc, Xml.NETWORK, handler);
			if (network == null)
			{
				return;
			}
			
			final List<List<List<Double>>> layers = parseLayers(network, handler);
			if (layers == null)
			return;
			
			final List<List<List<Double>>> training = parseTraining(network, handler);
			if (training == null)
				return;
			
			final Network networkTmp = new Network(layers, training);
			if (!networkTmp.check(handler))
				return;
			handler.onFinished(networkTmp);
		}
		catch (Exception e)
		{
			handler.onError(e.toString());
		}
	}
	
	protected static Node getFirstChildWithName(Node root, String name, ParserResultHandler handler)
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
		handler.onError(missingTag(Xml.LAYERS));
		return null;
	}

	protected static List<List<List<Double>>> parseTraining(Node network, ParserResultHandler handler) {
		final Node trainingNode = getFirstChildWithName(network, Xml.TRAINING, handler);
		if (trainingNode == null)
		{
			return null;
		}
		
		final List<List<List<Double>>> result = new ArrayList<List<List<Double>>>();
		final List<List<Double>> inputList = new ArrayList<List<Double>>();
		final List<List<Double>> outputList = new ArrayList<List<Double>>();
		result.add(inputList);
		result.add(outputList);
		
		final Node inputsNode = getFirstChildWithName(trainingNode, Xml.INPUTS, handler);
		if (inputsNode == null)
			return null;

		final NodeList inputs = inputsNode.getChildNodes();
		final int inputsCount = inputs.getLength();
		for (int i = 0; i < inputsCount; i++)
		{
			final Node inputNode = inputs.item(i);
			if (!inputNode.getNodeName().equals(Xml.INPUT))
				continue;
			List<Double> n = parseNumbers(inputNode, handler);
			if (n == null)
				return null;
			inputList.add(n);
		}

		
		final Node outputsNode = getFirstChildWithName(trainingNode, Xml.OUTPUTS, handler);
		if (outputsNode == null)
			return null;

		final NodeList outputs = outputsNode.getChildNodes();
		final int outputsCount = outputs.getLength();
		for (int i = 0; i < outputsCount; i++)
		{
			final Node outputNode = outputs.item(i);
			if (!outputNode.getNodeName().equals(Xml.OUTPUT))
				continue;
			List<Double> n = parseNumbers(outputNode, handler);
			if (n == null)
				return null;
			outputList.add(n);
		}
		
		return result;
	}
	
	protected static String missingTag(String tag)
	{
		return String.format(NetworkApplication.sInstance.getResources().getString(R.string.missing_tag), tag);
	}
	
	protected static List<List<List<Double>>> parseLayers(Node network, ParserResultHandler handler) {
		final Node layersNode = getFirstChildWithName(network, Xml.LAYERS, handler);

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
						handler.onError(e.toString());
						return null;
					}
					
					neuronData.add(val);
				}
				
				layerData.add(neuronData);							
			}
			
			layersData.add(layerData);
		}
		return layersData;
	}

	protected static List<Double> parseNumbers(Node node, ParserResultHandler handler)
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
				handler.onError(e.toString());
				return null;
			}
			
			result.add(value);
		}
		return result;
	}
}
