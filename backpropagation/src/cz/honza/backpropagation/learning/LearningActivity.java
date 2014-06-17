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
	
	protected void startThread()
	{
		if (NetworkApplication.sThread == null)
		{
			NetworkApplication.sThread = new LearningThread();
			NetworkApplication.sThread.start(NetworkApplication.sNetwork);
		}
		mStart.setText(R.string.stop);
	}
	
	protected void stopThread()
	{
		if (NetworkApplication.sThread != null)
		{
			NetworkApplication.sThread.end();
			NetworkApplication.sThread = null;
		}
		mStart.setText(R.string.start);
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
		mStart.setText(NetworkApplication.sThread == null ? R.string.start : R.string.stop);
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
