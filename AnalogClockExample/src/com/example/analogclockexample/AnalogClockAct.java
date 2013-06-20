package com.example.analogclockexample;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class AnalogClockAct extends Activity {
	private AnalogClockView mClockView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_analog_clock);

		mClockView = (AnalogClockView)findViewById(R.id.clock);
		findViewById(R.id.toggle).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mClockView.toggle();
			}
		});
	}

	@Override
	public void onResume(){
		super.onResume();
		mClockView.start();
	}
	@Override
	public void onPause(){
		super.onPause();
		mClockView.stop();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.analog_clock, menu);
		return true;
	}
}