package cz.honza.backpropagation.export;

import java.io.IOException;
import java.io.StringWriter;

import android.os.Bundle;
import android.widget.TextView;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class ExportActivity extends NetworkActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.export);
		
		TextView xml = (TextView) findViewById(R.id.export_text);
		StringWriter writer = new StringWriter();
		try
		{
			NetworkApplication.sNetwork.save(writer);
			xml.setText(writer.getBuffer().toString());
		}
		catch (IOException e)
		{
			// should not happen
		}
	}
}
