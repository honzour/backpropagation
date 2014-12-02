package cz.honza.backpropagation.learning;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class LearningActivity extends NetworkActivity {
	
	public static LearningActivity sInstance;
	
	private Handler mHandler;
	private TextView mIteratonView;
	private TextView mErrorView;
	private TextView mAlphaView;
	private Button mStart;
	private Button mRestartNetwork;
	private Button mRestartNeuron;
	private SeekBar mSeekBar;
	private CheckBox mAutoAlpha;
	private volatile long mIteration;
	private volatile double mError;
	private volatile boolean mIsCreated = false;
			
	private Runnable mIterationRunnable = new Runnable() {
		
		@Override
		public void run() {
			mIteratonView.setText(mIteration < 0 ? "" : String.valueOf(mIteration));
			mErrorView.setText(mIteration < 0 ? "" : String.valueOf(mError));
			mAlphaView.setText(String.valueOf(NetworkApplication.sNetwork.mAlpha));
		}
	};
	
	public void update()
	{
		if (isFinishing() || !mIsCreated)
			return;
		mIteration = NetworkApplication.sNetwork.getItration();
		mError = NetworkApplication.sNetwork.getError();
		mHandler.post(mIterationRunnable);
	}
	
	protected void setButtons(boolean waiting)
	{
		mStart.setText(waiting ? R.string.start : R.string.stop);
		mRestartNetwork.setEnabled(waiting);
		mRestartNeuron.setEnabled(waiting);
	}
	
	protected void startThread()
	{
		if (NetworkApplication.sThread == null)
		{
			NetworkApplication.sThread = new LearningThread();
			NetworkApplication.sThread.start(NetworkApplication.sNetwork);
		}
		setButtons(false);
	}
	
	protected void stopThread()
	{
		if (NetworkApplication.sThread != null)
		{
			NetworkApplication.sThread.end();
			NetworkApplication.sThread = null;
		}
		setButtons(true);
	}
	
	private void updateAlpha(int seekBarPosition)
	{
		double alpha = 0.001 * Math.pow(10, 5 * seekBarPosition / (double )mSeekBar.getMax());
		mAlphaView.setText(String.valueOf(alpha));
		synchronized(NetworkApplication.sNetwork)
		{
			NetworkApplication.sNetwork.mAlpha = alpha; 
		}
	}
	
	private void updateSeekBarPosition(double alpha)
	{
		final int max = mSeekBar.getMax();
		int pos = (int) (max * (3 + Math.log10(alpha)) / 5.0 + 0.5);
		if (pos < 0) pos = 0;
		if (pos > max) pos = max;
		mSeekBar.setProgress(pos);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sInstance = this;
		setContentView(R.layout.learning);
		mHandler = new Handler();
		mIteratonView = (TextView)findViewById(R.id.learning_iteration);
		mErrorView = (TextView)findViewById(R.id.learning_error);
		mAlphaView = (TextView)findViewById(R.id.learning_alpha);
		mStart = (Button)findViewById(R.id.learning_start_stop);
		mSeekBar = (SeekBar)findViewById(R.id.learning_alpha_seek);
		updateSeekBarPosition(NetworkApplication.sNetwork.mAlpha);
		mRestartNetwork = (Button)findViewById(R.id.main_restart_all);
		mRestartNeuron = (Button)findViewById(R.id.main_restart_neuron);
		mAutoAlpha = (CheckBox) findViewById(R.id.learning_auto_alpha);
		
		mAutoAlpha.setChecked(NetworkApplication.sNetwork.mAutoAlpha);
		
		mAutoAlpha.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				NetworkApplication.sNetwork.mAutoAlpha = isChecked; 
				mSeekBar.setEnabled(!isChecked);
			}
		});
		
		mSeekBar.setEnabled(!NetworkApplication.sNetwork.mAutoAlpha);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				if (fromUser)
				{
					updateAlpha(progress);
				}
			}
		});
		
		setButtons(NetworkApplication.sThread == null);
		mStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (NetworkApplication.sThread == null)
				{
					startThread();
				}
				else
				{
					stopThread();
				}
			}
		});
		
		mRestartNetwork.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetworkApplication.sNetwork.restart();
				update();
			}
		});
		
		mRestartNeuron.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetworkApplication.sNetwork.restartNeuron();
				update();
			}
		});
		
		mIsCreated = true;
		
		if (NetworkApplication.sThread == null)
			update();
	}
	
	protected void onPause ()
	{
		stopThread();
		super.onPause();
	}
	
	@Override
	public String getHelpLink()
	{
		return "learning.php";
	}
}
