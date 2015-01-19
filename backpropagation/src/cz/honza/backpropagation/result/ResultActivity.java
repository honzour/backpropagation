package cz.honza.backpropagation.result;

import android.os.Bundle;
import android.view.View;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;

public class ResultActivity extends NetworkActivity {
	
	ResultView mResultView;
	DrawResultThread mThread;
	View mProgresBar;
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result_visualisation);
		mProgresBar = findViewById(R.id.result_progress);
		
		mResultView = (ResultView)findViewById(R.id.result_result);
		
		final Runnable r = new Runnable() {
			@Override
			public void run() {
				mProgresBar.setVisibility(View.GONE);
			}
		};
		
		
		if (NetworkApplication.sNetwork.getInputDimension() > 1)
			mThread = new DrawResultThread2D(r);
		else
			mThread = new DrawResultThread1D(r);
			
		mResultView.setThread(mThread);
	}
	
	
	@Override
	protected void onDestroy()
	{
		mThread.canStop();
		super.onDestroy();
	}
	
	@Override
	public String getHelpLink()
	{
		return "resvis.php";
	}
}
