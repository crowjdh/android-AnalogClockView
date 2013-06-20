package com.example.analogclockexample;

import java.util.Calendar;
import java.util.TimeZone;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

public class AnalogClockView extends RelativeLayout{
	private Context mContext;

	/* views */
	private ImageView mHourHand;
	private ImageView mMinuteHand;
	private ImageView mSecondHand;

	/* state */
	private boolean isRunning = false;
	private boolean isFirstTick = true;
	
	/* angle */
	private int mHourAngle = INVALID_ANGLE;
	private int mMinuteAngle = INVALID_ANGLE;
	private int mSecondAngle = INVALID_ANGLE;
	
	/* resources */
	private int mDialBackgroundResource = R.drawable.clock_dial_typical;
	private int mHourBackgroundResource = R.drawable.clock_hand_hour;
	private int mMinuteBackgroundResource = R.drawable.clock_hand_minute;
	private int mSecondBackgroundResource = R.drawable.clock_hand_second;
	
	private static final int INVALID_ANGLE = -1;
	private static final int DEGREE_MINUTE = 6;
	private static final int MINUTE_TO_HOUR_DEGREE = 12;
	private static final int HOUR_TO_HOUR_DEGREE = 30;
	private static final String TAG = "clock";
	private static final String TIMEZONE_ID = "America/Los_Angeles";

	public AnalogClockView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}
	public AnalogClockView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setIcons(context, attrs);
		init(context);
	}
	public AnalogClockView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		setIcons(context, attrs);
		init(context);
	}
	private void setIcons(Context context, AttributeSet attrs){
		TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AnalogClockView);

		mDialBackgroundResource = array.getResourceId(R.styleable.AnalogClockView_dial, R.drawable.clock_dial_typical);
		mHourBackgroundResource = array.getResourceId(R.styleable.AnalogClockView_hand_hour, R.drawable.clock_hand_hour);
		mMinuteBackgroundResource = array.getResourceId(R.styleable.AnalogClockView_hand_minute, R.drawable.clock_hand_minute);
		mSecondBackgroundResource = array.getResourceId(R.styleable.AnalogClockView_hand_second, R.drawable.clock_hand_second);
		array.recycle();
	}

	private void init(Context con){
		this.mContext = con;

		RelativeLayout.LayoutParams lp;

		mHourHand = new ImageView(mContext);
		setHourResource(mHourBackgroundResource);
		mHourHand.setScaleType(ScaleType.CENTER_INSIDE);
		lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(CENTER_IN_PARENT);
		this.addView(mHourHand, lp);

		mMinuteHand = new ImageView(mContext);
		setMinuteResource(mMinuteBackgroundResource);
		mMinuteHand.setScaleType(ScaleType.CENTER_INSIDE);
		lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(CENTER_IN_PARENT);
		this.addView(mMinuteHand, lp);

		mSecondHand = new ImageView(mContext);
		setSecondResource(mSecondBackgroundResource);
		mSecondHand.setScaleType(ScaleType.CENTER_INSIDE);
		lp = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(CENTER_IN_PARENT);
		this.addView(mSecondHand, lp);

		setBackgroundResource(mDialBackgroundResource);
	}

	public void toggle(){
		if (isRunning)
			stop();
		else
			start();
	}
	public void start(){
		if (isRunning)
			return;

		isRunning = true;
		isFirstTick = true;

//		Calendar tempCal = Calendar.getInstance();
		int diffInMilli = (int)System.currentTimeMillis()%1000;
		mHandler.sendEmptyMessageDelayed(0, 1000 - diffInMilli);
	}
	public void stop(){
		if (!isRunning)
			return;
		isRunning = false;
	}
	private void tick(){
		if (!isRunning)
			return;
		TimeZone tz = TimeZone.getTimeZone(TIMEZONE_ID);

		Calendar tempCal = Calendar.getInstance();
		tempCal.setTimeZone(tz);
		
		int hour = tempCal.get(Calendar.HOUR);
		int min = tempCal.get(Calendar.MINUTE);
		int sec = tempCal.get(Calendar.SECOND);
		Log.i(TAG, "hour : " + hour + ", minute : " + min + ", sec : " + sec);

		int newHourAngle = hour * HOUR_TO_HOUR_DEGREE + (min/MINUTE_TO_HOUR_DEGREE)*DEGREE_MINUTE;
		int newMinuteAngle = min * DEGREE_MINUTE;
		int newSecondAngle = sec * DEGREE_MINUTE;

		if (isFirstTick){
			if (mHourAngle != INVALID_ANGLE && mMinuteAngle != INVALID_ANGLE && mSecondAngle != INVALID_ANGLE){
				rotate(mHourHand, mHourAngle, newHourAngle);
				rotate(mMinuteHand, mMinuteAngle, newMinuteAngle);
				rotate(mSecondHand, mSecondAngle, newSecondAngle);
			}
			else{
				rotate(mHourHand, newHourAngle, newHourAngle);
				rotate(mMinuteHand, newMinuteAngle, newMinuteAngle);
				rotate(mSecondHand, newSecondAngle, newSecondAngle);
			}
			isFirstTick = false;
		}
		else{
			if (min == 0 && sec == 0)
				rotate(mHourHand, newHourAngle - DEGREE_MINUTE, newHourAngle);
			if (sec == 0)
				rotate(mMinuteHand, newMinuteAngle - DEGREE_MINUTE, newMinuteAngle);

			rotate(mSecondHand, newSecondAngle - DEGREE_MINUTE, newSecondAngle);
		}
		mHourAngle = newHourAngle;
		mMinuteAngle = newMinuteAngle;
		mSecondAngle = newSecondAngle;
		Log.i(TAG, "hourAngle : " + mHourAngle + ", minuteAngle : " + mMinuteAngle + ", secAngle : " + mSecondAngle);
	}

//	private void _rotate(ImageView view, Point p, int angle){
//		int xd = (getWidth() - p.x)/2;
//		int yd = (getHeight() - p.y)/2;
//		//		int xd = getWidth()/2 - p.x/5;//(getWidth() - p.x)/2;
//		//		int yd = getHeight()/2 - p.y/5;//(getHeight() - p.y)/2;
//		//		int xd = (getWidth() - p.x)/2 + p.x/5;
//		//		int yd = (getHeight() - p.y)/2 + p.y/5;
//
//		Matrix matrix=new Matrix();
//		matrix.reset();
//		matrix.postRotate((float) angle, p.x/2, p.y/2);
//		matrix.postTranslate(xd, yd);
//		//		matrix.postRotate((float) angle, p.x/5, p.y/5);
//		view.setImageMatrix(matrix);
//		view.invalidate();
//	}
	private void rotate(ImageView view, int fromAngle, int toAngle){
		Animation anim;
		anim = new RotateAnimation(fromAngle, toAngle,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		int gap = Math.abs(toAngle - fromAngle);
		if (gap != DEGREE_MINUTE){
			anim.setDuration(600);
			anim.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.accelerate_interpolator));
		}
		else{
			anim.setDuration(150);
			anim.setInterpolator(AnimationUtils.loadInterpolator(mContext,
					android.R.anim.overshoot_interpolator));
		}
		anim.setFillAfter(true);
		view.startAnimation(anim);
	}

	public void setDialResource(int id){
		this.mDialBackgroundResource = id;
		setBackgroundResource(mDialBackgroundResource);//
	}
	public void setHourResource(int id){
		this.mHourBackgroundResource = id;
		mHourHand.setImageResource(id);
	}
	public void setMinuteResource(int id){
		this.mMinuteBackgroundResource = id;
		mMinuteHand.setImageResource(id);
	}
	public void setSecondResource(int id){
		this.mSecondBackgroundResource = id;
		mSecondHand.setImageResource(id);
	}

	private Handler mHandler = new Handler(){
		public void handleMessage( Message msg ){
			if (isRunning){
//				long startMilli = System.currentTimeMillis();
				tick();
				long endMilli = System.currentTimeMillis();
				long delay = endMilli%1000;//(endMilli - startMilli)%1000; 
				
				mHandler.sendEmptyMessageDelayed(0, 1000 - delay);
			}
		}
	};
}
