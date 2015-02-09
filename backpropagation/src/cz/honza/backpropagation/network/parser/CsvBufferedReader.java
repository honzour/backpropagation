package cz.honza.backpropagation.network.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class CsvBufferedReader extends BufferedReader
{
	protected int mLine;
	
	public CsvBufferedReader(Reader in) {
		super(in);
		mLine = 0;
	}
	
	public String readLine() throws IOException
	{
		String line;
		
		while (true)
		{
			line = super.readLine();
			mLine++;
			if (line == null)
				return null;
			if (line.length() > 0 && line.charAt(0) == ';')
				continue;
			return line;
		}
	}
	
	public int getLine()
	{
		return mLine;
	}
	
	

}
