How to use
==========
##resource<br/>
1. copy "res/values/attr.xml" to your project.<br/>
2. copy "res/drawable-mdpi/clock_*" to your project.<br/>

##*xml*
<pre><code>
&lt;com.example.analogclockexample.AnalogClockView
        android:id="@+id/clock"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        clock:dial="@drawable/clock_dial_simple"
/&gt;</code></pre>
####*attributes*<br/>
*	dial        : frame of clock<br/>
*	hand_hour   : hour hand<br/>
*	hand_minute : minute hand<br/>
*	hand_second : second hand<br/>

##in activity
<pre><code>public class AnalogClockAct extends Activity {
  private AnalogClockView mClockView;
  @Override
	protected void onCreate(Bundle savedInstanceState) {
    ...
  	mClockView = (AnalogClockView)findViewById(R.id.clock);
    ...
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
}</code></pre>
