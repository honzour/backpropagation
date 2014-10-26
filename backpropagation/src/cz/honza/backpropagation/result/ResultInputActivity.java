package cz.honza.backpropagation.result;

import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;

public class ResultInputActivity extends NetworkActivity {
	
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
		NetworkApplication.sNetwork.calculate(mInput, mOutput);
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
		final int inputsLength = NetworkApplication.sNetwork.layers[0].neurons[0].weights.length - 1;
		mInputs = new EditText[inputsLength];
		mInput = new double[inputsLength];
		mOutput = new double[NetworkApplication.sNetwork.layers[NetworkApplication.sNetwork.layers.length - 1].neurons.length];
		for (int i = 0; i < inputsLength; i++)
		{
			mInputs[i] = new EditText(this);
			mInputs[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
			mInputs[i].setOnEditorActionListener(new TextView.OnEditorActionListener() {
				
				@Override
				public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
					refresh();
					return false;
				}
			});
			ll.addView(mInputs[i]);
		}
		refresh();
	}
}
