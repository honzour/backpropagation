package cz.honza.backpropagation.network.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.trainingset.TrainingSet;
import cz.honza.backpropagation.network.trainingset.TrainingSetBase;
import cz.honza.backpropagation.network.trainingset.TrainingSetSingleTimeline;

public class XmlParser {
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
			
			int inputDim;
			int outputDim;
			
			try
			{
				inputDim = layers.get(0).get(0).size();
				outputDim = layers.get(layers.size() - 1).size();
			}
			catch (IndexOutOfBoundsException e)
			{
				handler.onError(R.string.cannot_parse_layers);
				return;
			}
			
			TrainingSet training = parseTraining(network, inputDim, outputDim, handler);
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
	
	protected static TrainingSet parseSimpleTrainingSet(Node trainingNode, ParserResultHandler handler)
	{
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
		
		return new TrainingSetBase(result); 
	}
	
	protected static TrainingSet parseTimelineTrainingSet(Node trainingNode, int inputDimension, int outputDimension, ParserResultHandler handler)
	{
		final Node lineNode = getFirstChildWithName(trainingNode, Xml.LINE, handler);
		if (lineNode == null)
			return null;
		List<Double> numbers = parseNumbers(lineNode, handler);
		if (numbers == null)
			return null;
		double[] timeline = new double[numbers.size()];
		int i = 0;
		for (Double d : numbers)
		{
			timeline[i++] = d;
		}
		return new TrainingSetSingleTimeline(inputDimension, outputDimension, timeline);
	}

	protected static TrainingSet parseTraining(Node network, int inputDimension, int outputDimension, ParserResultHandler handler) {
		final Node trainingNode = getFirstChildWithName(network, Xml.TRAINING, handler);
		if (trainingNode == null)
		{
			return null;
		}
		
		int type = Csv.SIMPLE_CODE;
		
		final NamedNodeMap attrs = trainingNode.getAttributes();
		if (attrs != null)
		{
			Node node = attrs.getNamedItem(Xml.TYPE);
			if (node != null)
			{
				String typeString = node.getNodeValue();
				if (typeString != null)
				{
					if (typeString.equals(Csv.TIMELINE))
						type = Csv.TIMELINE_CODE;
				}
			}
		}
		
		if (type == Csv.SIMPLE_CODE)
			return parseSimpleTrainingSet(trainingNode, handler);
		if (type == Csv.TIMELINE_CODE)
			return parseTimelineTrainingSet(trainingNode, inputDimension, outputDimension, handler);
		handler.onError(R.string.unknown_training_set_type);
		return null;
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
