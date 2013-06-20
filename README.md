android-AnalogClockView
=======================

hour, minute, second hand available. can choose timezone.


How to use
resource
1. copy "res/values/attr.xml" to your project.
2. copy "res/drawable-mdpi/clock_*" to your project.

xml
<com.example.analogclockexample.AnalogClockView
        android:id="@+id/clock"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:layout_centerInParent="true"
        clock:dial="@drawable/clock_dial_simple"
        />

attributes
  dial        : frame of clock
  hand_hour   : hour hand
  hand_minute : minute hand
  hand_second : second hand

in activity
public class AnalogClockAct extends Activity {
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
}
