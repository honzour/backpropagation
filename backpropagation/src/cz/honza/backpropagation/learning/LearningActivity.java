package cz.honza.backpropagation.learning;

import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.util.NetworkActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LearningActivity extends NetworkActivity {
	
	public static LearningActivity sInstance;
	
	private Handler mHandler;
	private TextView mIteratonView;
	private TextView mErrorView;
	private Button mStart;
	private Button mRestartNetwork;
	private Button mRestartNeuron;
	private volatile long mIteration;
	private volatile double mError;
	private volatile boolean mIsCreated = false;
		
	private Runnable mIterationRunnable = new Runnable() {
		
		@Override
		public void run() {
			mIteratonView.setText(mIteration < 0 ? "" : String.valueOf(mIteration));
			mErrorView.setText(mIteration < 0 ? "" : String.valueOf(mError));
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sInstance = this;
		setContentView(R.layout.learning);
		mHandler = new Handler();
		mIteratonView = (TextView)findViewById(R.id.main_iteration);
		mErrorView = (TextView)findViewById(R.id.main_error);
		mStart = (Button)findViewById(R.id.main_start_stop);
		
		mRestartNetwork = (Button)findViewById(R.id.main_restart_all);
		mRestartNeuron = (Button)findViewById(R.id.main_restart_neuron);
		
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
}
