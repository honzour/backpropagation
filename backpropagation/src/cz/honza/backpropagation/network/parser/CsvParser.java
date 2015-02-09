package cz.honza.backpropagation.network.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.trainingset.TrainingSet;
import cz.honza.backpropagation.network.trainingset.TrainingSetBase;

public class CsvParser {
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
}
