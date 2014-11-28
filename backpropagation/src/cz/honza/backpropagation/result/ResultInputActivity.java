package cz.honza.backpropagation.result;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class ResultInputActivity extends NetworkActivity {
	
	public static final String INPUT = "INPUT";
	
	protected EditText[] mInputs;
	protected TextView mOutputView;
	
	protected double[] mInput;
	protected double[] mOutput;
	
	protected void refresh()
	{
		for (int i = 0; i < mInputs.length; i++)
		{
			try
			{
				mInput[i] = Double.valueOf(mInputs[i].getText().toString());
			}
			catch (Exception e)
			{
				mOutputView.setText(R.string.input_error);
				return;
			}
		}
		NetworkApplication.sNetwork.calculate(mInput, mOutput, true);
		final StringBuffer sb = new StringBuffer("[");
		for (int i = 0; i < mOutput.length; i++)
		{
			sb.append(mOutput[i]);
			if (i < mOutput.length - 1)
				sb.append(", ");
		}
		sb.append("]");
		mOutputView.setText(sb.toString());
	}
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_input);
		mOutputView = (TextView) findViewById(R.id.result_input_result);
		final LinearLayout ll = (LinearLayout)findViewById(R.id.result_input_layout);
		final int inputsLength = NetworkApplication.sNetwork.getInputDimension();
		mInputs = new EditText[inputsLength];
		
		for (int i = 0; i < inputsLength; i++)
		{
			mInputs[i] = new EditText(this);
			mInputs[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
		}
		
		if (savedInstanceState != null)
			mInput = savedInstanceState.getDoubleArray(INPUT);
		if (mInput == null)
		{
			mInput = new double[inputsLength];
		}
		else
		{
			for (int i = 0; i < mInput.length; i++)
			{
				mInputs[i].setText(String.valueOf(mInput[i]));
			}
		}
		mOutput = new double[NetworkApplication.sNetwork.getOutputDimension()];
		for (int i = 0; i < inputsLength; i++)
		{
			mInputs[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					refresh();
					return false;
				}
			});
			
			mInputs[i].addTextChangedListener(new TextWatcher() {
				
				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
				}
				
				@Override
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
				}
				
				@Override
				public void afterTextChanged(Editable s) {
					refresh();
				}
			});
			
			ll.addView(mInputs[i]);
		}
		refresh();
	}



	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putDoubleArray(INPUT, mInput);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public String getHelpLink()
	{
		return "resinput.php";
	}
}
