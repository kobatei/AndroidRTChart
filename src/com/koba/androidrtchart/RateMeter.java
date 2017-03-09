package com.koba.androidrtchart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class RateMeter extends View {

	private static final String METER_COLOR_1 = "#66cdaa";
	private static final String METER_COLOR_2 = "#ffdb38";
	private static final String METER_COLOR_3 = "#ff5e3b";
	private static final String METER_COLOR_VAL = "#ff444444";

	private RateMeterHandler handler;
	private float graphHeight;
	private float graphWidth;
	private Paint paintMeter1;
	private Paint paintMeter2;
	private Paint paintMeter3;
	private Paint paintMeterDevine1;
	private Paint paintMeterDevine2;
	private Paint paintMeterDevine3;
	private Paint paintMeterVal;
	private Paint paintMeterMsg;
	private PointF origin;

	private int meterMaxValue = 210;
	private int meterMinValue = 30;
	private int meterTargetValue = 180;
	private int meterCurrentValue = 130;
	private float padding = 120f;

	public RateMeter(Context context) {
		super(context);
		init();
	}

	public RateMeter(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public RateMeter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public void meterTargetSetting(int targetVal) {
		meterTargetValue = (targetVal < 180) ? 180 : targetVal;
		meterMinValue = (targetVal - 150 < 0) ? 0 : targetVal - 150;
		meterMaxValue = targetVal + 30;

		if (meterCurrentValue < meterMinValue)
			meterCurrentValue = meterMinValue;
		if (meterCurrentValue > meterMaxValue)
			meterCurrentValue = meterMaxValue;

		notifyDataSetChenged();
	}

	private void init() {
		handler = new RateMeterHandler(this);

		paintPaletteSetting();
	}

	private void paintPaletteSetting() {
		paintMeter1 = new Paint();
		paintMeter2 = new Paint();
		paintMeter3 = new Paint();
		paintMeterDevine1 = new Paint();
		paintMeterDevine2 = new Paint();
		paintMeterDevine3 = new Paint();
		paintMeterVal = new Paint();
		paintMeterMsg = new Paint();

		paintMeter1.setAntiAlias(true);
		paintMeter2.setAntiAlias(true);
		paintMeter3.setAntiAlias(true);
		paintMeterDevine1.setAntiAlias(true);
		paintMeterDevine2.setAntiAlias(true);
		paintMeterDevine3.setAntiAlias(true);
		paintMeterVal.setAntiAlias(true);
		paintMeterMsg.setAntiAlias(true);

		paintMeter1.setStyle(Paint.Style.STROKE);
		paintMeter2.setStyle(Paint.Style.STROKE);
		paintMeter3.setStyle(Paint.Style.STROKE);
		paintMeterDevine1.setStyle(Paint.Style.STROKE);
		paintMeterDevine2.setStyle(Paint.Style.STROKE);
		paintMeterDevine3.setStyle(Paint.Style.STROKE);
		paintMeterVal.setStyle(Paint.Style.FILL);

		paintMeter1.setColor(Color.parseColor(METER_COLOR_1));
		paintMeter2.setColor(Color.parseColor(METER_COLOR_2));
		paintMeter3.setColor(Color.parseColor(METER_COLOR_3));
		paintMeterDevine1.setColor(Color.parseColor(METER_COLOR_1));
		paintMeterDevine2.setColor(Color.parseColor(METER_COLOR_2));
		paintMeterDevine3.setColor(Color.parseColor(METER_COLOR_3));
		paintMeterVal.setColor(Color.parseColor(METER_COLOR_VAL));

		paintMeterMsg.setTextSize(40F);

	}

	protected void setCurrentValue(int value) {
		this.meterCurrentValue = value;
	}
	public int getCurrentValue(){
		return meterCurrentValue;
	}
	public void setMeterValAnimate(int angleVal) {

		if (angleVal < meterMinValue)
			angleVal = meterMinValue;
		else if (angleVal > meterMaxValue)
			angleVal = meterMaxValue;

		handler.execMessage(angleVal);

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
		// グラフのサイズを指定
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		
		float w, h;
		
		w = this.getWidth();
		h = this.getHeight();
		if( h != w ){
			if(h < w){
				w = h;
				getLayoutParams().width = (int)h;
				requestLayout();
			}
			else if(h > w){
				h = w;
				getLayoutParams().height = (int)w;
				requestLayout();
			}
		}
		graphWidth = w - padding;
		graphHeight = h - padding;

		float width = graphWidth / 14;
		paintMeter1.setStrokeWidth(width);
		paintMeter2.setStrokeWidth(width);
		paintMeter3.setStrokeWidth(width);
		paintMeterDevine1.setStrokeWidth(width * 1.5f);
		paintMeterDevine2.setStrokeWidth(width * 1.5f);
		paintMeterDevine3.setStrokeWidth(width * 1.5f);

		origin = new PointF(w/2, h/2);
	}

	protected void drawRateMeter(Canvas canvas) {
		RectF rectF = new RectF(0 + padding, 0 + padding, graphWidth,
				graphHeight);
		canvas.drawArc(rectF, 180, 90, false, paintMeter1);
		canvas.drawArc(rectF, 269, 1, false, paintMeterDevine1);
		canvas.drawArc(rectF, 272, 57, false, paintMeter2);
		canvas.drawArc(rectF, 329, 1, false, paintMeterDevine2);
		canvas.drawArc(rectF, 332, 28, false, paintMeter3);
		canvas.drawArc(rectF, 359, 1, false, paintMeterDevine3);

	}

	protected void drawRateMeterMsg(Canvas canvas) {
		paintMeterMsg.setColor(Color.parseColor(METER_COLOR_1));
		canvas.drawText(String.valueOf(meterMinValue), 10, (graphHeight + padding) / 2, paintMeterMsg);
		paintMeterMsg.setColor(Color.parseColor(METER_COLOR_2));
		canvas.drawText(String.valueOf(meterTargetValue), graphWidth, graphHeight/4+padding/3, paintMeterMsg);
		paintMeterMsg.setColor(Color.parseColor(METER_COLOR_3));
		canvas.drawText(String.valueOf(meterMaxValue), graphWidth+(padding/2)-10, (graphHeight + padding) / 2, paintMeterMsg);
	}

	protected void drawRateMeterVal(Canvas canvas) {
		RectF rectF = new RectF(0 + padding, 0 + padding, graphWidth,
				graphHeight);
		canvas.drawArc(rectF, meterCurrentValue - meterMinValue + 179,
				3, true, paintMeterVal);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawRateMeter(canvas);
		drawRateMeterVal(canvas);
		drawRateMeterMsg(canvas);
	}

	public void notifyDataSetChenged() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		});

	}
}
