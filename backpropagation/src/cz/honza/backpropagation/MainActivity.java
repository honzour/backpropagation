package cz.honza.backpropagation;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class MainActivity extends Activity {
	
	public static MainActivity sInstance;
	private static LearningThread sThread;
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
	
	public void update(long iteration, double error)
	{
		if (isFinishing() || !mIsCreated)
			return;
		mIteration = iteration;
		mError = error;
		mHandler.post(mIterationRunnable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sInstance = this;
		setContentView(R.layout.main);
		mHandler = new Handler();
		mIteratonView = (TextView)findViewById(R.id.main_iteration);
		mErrorView = (TextView)findViewById(R.id.main_error);
		mStart = (Button)findViewById(R.id.main_start_stop);
		mStart.setText(sThread == null ? R.string.start : R.string.stop);
		mStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*
				// XOR
				int[] anatomy = {2, 2, 2, 1};
				double[][] inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
				double[][] outputs = {{0}, {1}, {1}, {0}};
				*/
				
				// single neuron, x > 0.5
				int[] anatomy = {1, 1};
				double[][] inputs = {{0}, {1}};
				double[][] outputs = {{0}, {1}};
				
				if (sThread == null)
				{
					sThread = new LearningThread();
					sThread.start(anatomy, inputs, outputs);
					mStart.setText(R.string.stop);
				}
				else
				{
					sThread.end();
					sThread = null;
					mStart.setText(R.string.start);
				}
			}
		});
		mIsCreated = true;
	}
}
