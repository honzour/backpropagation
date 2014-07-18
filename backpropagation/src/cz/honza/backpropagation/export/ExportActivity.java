package cz.honza.backpropagation.export;

import java.io.IOException;
import java.io.StringWriter;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class ExportActivity extends NetworkActivity {
	
	private View mSaveButton;
	private View mMailButton;

	private String getXml() throws IOException
	{
		final StringWriter writer = new StringWriter();
		NetworkApplication.sNetwork.save(writer);
		return writer.getBuffer().toString();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.export);
		
		TextView xml = (TextView) findViewById(R.id.export_text);
		try
		{
			xml.setText(getXml());
		}
		catch (IOException e)
		{
			// should not happen
		}
		
		mSaveButton = findViewById(R.id.export_save);
		mMailButton = findViewById(R.id.export_mail);
		
		mSaveButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		mMailButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});

	}
}
