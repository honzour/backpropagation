package cz.honza.backpropagation.export;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import cz.honza.backpropagation.NetworkApplication;
import cz.honza.backpropagation.R;
import cz.honza.backpropagation.components.NetworkActivity;
import cz.honza.backpropagation.editor.EditorActivity;
import cz.honza.backpropagation.network.Network;
import cz.honza.backpropagation.network.TrainingSet;
import cz.honza.backpropagation.network.TrainingSetBase;

public class NewTaskActivity extends NetworkActivity {
	
	private View mExampleButton;
	private View mEditorButton;
	
	private Spinner mExample;
	private FromWebThread mThread = null;
	private static final int REQUEST_CODE_EDITOR = 0;
	
	public static final String INTENT_EXTRA_NETWORK = "INTENT_EXTRA_NETWORK";

	protected void loadExample()
	{
		final int item = mExample.getSelectedItemPosition(); 
		
		// Identity
		int[] id_anatomy = {1, 1};
		double[][] id_inputs = {{0}, {1}};
		double[][] id_outputs = {{0}, {1}};
		
		// NOT
		int[] not_anatomy = {1, 1};
		double[][] not_inputs = {{0}, {1}};
		double[][] not_outputs = {{1}, {0}};
						
		// AND
		int[] and_anatomy = {2, 1};
		double[][] and_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] and_outputs = {{0}, {0}, {0}, {1}};
						
		// OR
		int[] or_anatomy = {2, 1};
		double[][] or_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] or_outputs = {{0}, {1}, {1}, {1}};
		
		// XOR
		int[] xor_anatomy = {2, 2, 1};
		double[][] xor_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] xor_outputs = {{0}, {1}, {1}, {0}};
		
		// Exception
		int[] ex_anatomy = {2, 4, 1};
		double[][] ex_inputs =  {{0, 0}, {0, 1}, {1, 0}, {1, 1}, {0.5, 0}, {0.5, 1}, {0, 0.5}, {1, 0.5},
								  {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5}, {0.5, 0.5} };
		double[][] ex_outputs = {{0},    {0},    {0},    {0},    {0},      {0},      {0},      {0},
								  {1}, {1}, {1}, {1}, {1}, {1}, {1}, {1} };
		
		// Chess board
		int[] chess_anatomy = {2, 4, 2, 1};
		double[][] chess_inputs = 
			{{0, 0}, {0, 0.4}, {0.4, 0}, {0.4, 0.4},
			 {0.6, 0.6}, {0.6, 1}, {1, 0.6}, {1, 1},
			 {0.6, 0}, {1, 0}, {1, 0.4}, {0.6, 0.4},
			 {0, 0.6}, {0.4, 1}, {0.4, 0.6}, {0, 1}
			};

		double[][] chess_outputs =
			{{0}, {0}, {0}, {0},
			 {0}, {0}, {0}, {0},
			 {1}, {1}, {1}, {1},
			 {1}, {1}, {1}, {1}						
			};
		
		// 3D
		int[] multioutput_anatomy = {2, 3};
		double[][] multioutput_inputs = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
		double[][] multioutput_outputs = {{0, 0, 0}, {0, 1, 0}, {1, 0, 0}, {1, 1, 1}};
		
		
		switch (item)
		{
		case 0:
			NetworkApplication.sNetwork = new Network(id_anatomy, new TrainingSetBase(id_inputs, id_outputs));
			break;
		case 1:
			NetworkApplication.sNetwork = new Network(not_anatomy, new TrainingSetBase(not_inputs, not_outputs));
			break;
		case 2:
			NetworkApplication.sNetwork = new Network(and_anatomy, new TrainingSetBase(and_inputs, and_outputs));
			break;
		case 3:
			NetworkApplication.sNetwork = new Network(or_anatomy, new TrainingSetBase(or_inputs, or_outputs));
			break;
		case 4:
			NetworkApplication.sNetwork = new Network(xor_anatomy, new TrainingSetBase(xor_inputs, xor_outputs));
			break;
		case 5:
			NetworkApplication.sNetwork = new Network(ex_anatomy, new TrainingSetBase(ex_inputs, ex_outputs));
			break;
		case 6:
			NetworkApplication.sNetwork = new Network(chess_anatomy, new TrainingSetBase(chess_inputs, chess_outputs));
			break;
		case 7:
			NetworkApplication.sNetwork = new Network(multioutput_anatomy, new TrainingSetBase(multioutput_inputs, multioutput_outputs));
			break;

		}
		finish();
	}
	
	protected void initExamples()
	{
		mExample = (Spinner) findViewById(R.id.new_task_spinner_example);
		mExampleButton = findViewById(R.id.new_task_button_examle);
		mExampleButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loadExample();
			}
		});
	}
	
	
	protected void initEditor()
	{
		mEditorButton = findViewById(R.id.new_task_run_editor);
		mEditorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(NewTaskActivity.this, EditorActivity.class);
				i.putExtra(INTENT_EXTRA_NETWORK, NetworkApplication.sNetwork);
				startActivityForResult(i, REQUEST_CODE_EDITOR);
			}
		});
	}

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.new_task);
		
		initExamples();
		initEditor();
		
	}
	
	@Override
	public Object onRetainNonConfigurationInstance () {
		return mThread;
	}

	@Override
	protected void onDestroy() {
		if (mThread != null)
		{
			mThread.setContext(null);
		}
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_EDITOR && resultCode == RESULT_OK)
		{
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public String getHelpLink()
	{
		return "newtask.php";
	}
	
}
