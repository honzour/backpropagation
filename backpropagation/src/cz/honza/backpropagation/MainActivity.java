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
	private Button mStart;
	private volatile long mIteration;
	private volatile boolean mIsCreated = false;
	
	private Runnable mIterationRunnable = new Runnable() {
		
		@Override
		public void run() {
			mIteratonView.setText(mIteration < 0 ? "" : String.valueOf(mIteration));
		}
	};
	
	public void update(long iteration, double error)
	{
		if (isFinishing() || !mIsCreated)
			return;
		mIteration = iteration;
		mHandler.post(mIterationRunnable);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sInstance = this;
		setContentView(R.layout.main);
		mHandler = new Handler();
		mIteratonView = (TextView)findViewById(R.id.main_iteration);
		mStart = (Button)findViewById(R.id.main_start_stop);
		mStart.setText(sThread == null ? R.string.start : R.string.stop);
		mStart.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (sThread == null)
				{
					sThread = new LearningThread();
					sThread.start();
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
