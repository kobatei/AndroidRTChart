package com.koba.androidrtchart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class ChangeLogger extends View {
	private static final String COLOR_1 = "#66cdaa";
	private static final String COLOR_2 = "#ffdb38";
	private static final String COLOR_3 = "#ff5e3b";
	private float viewWidth;
	private float viewHeight;
	private int logLimit = 30;
	private int maxValue = 210;
	private int minValue = 30;
	private int dengerValue = 180;
	private List<Integer> logs;
	private Handler handler;
	private Paint paintNormal;
	private Paint paintDenger;
	private Paint paintMax;
	private int colorNormal;
	private int colorDengerColor;
	private int colorMaxColor;
	private float verticalUnit;
	private float horizontalUnit;

	public ChangeLogger(Context context) {
		super(context);
		init();
	}

	public ChangeLogger(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ChangeLogger(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		handler = new Handler();
		logs = Collections.synchronizedList(new ArrayList<Integer>(logLimit));
		for (int i = 0; i < logLimit; i++) {
			logs.add(0);
		}
		paintPaletteSetting();
	}

	private void paintPaletteSetting() {
		paintNormal = new Paint();
		paintDenger = new Paint();
		paintMax = new Paint();

		colorNormal = Color.parseColor(COLOR_1);
		colorDengerColor = Color.parseColor(COLOR_2);
		colorMaxColor = Color.parseColor(COLOR_3);

		paintNormal.setColor(colorNormal);
		paintDenger.setColor(colorDengerColor);
		paintMax.setColor(colorMaxColor);

		paintNormal.setStyle(Paint.Style.FILL);
		paintDenger.setStyle(Paint.Style.FILL);
		paintMax.setStyle(Paint.Style.FILL);

		paintNormal.setAntiAlias(true);
		paintDenger.setAntiAlias(true);
		paintMax.setAntiAlias(true);

	}

	public void setLogLomit(int limit) {
		this.logLimit = limit;
		logs = Collections.synchronizedList(new ArrayList<Integer>(
				this.logLimit));
		for (int i = 0; i < logLimit; i++) {
			logs.add(0);
		}
	}

	public void addLogData(int value) {
		logs.remove(0);
		logs.add(value);
		notifyDataSetChenged();
	}

	public void loggerSetting(int dengerValue) {
		dengerValue = (dengerValue < 180) ? 180 : dengerValue;
		minValue = (dengerValue - 150 < 0) ? 0 : dengerValue - 150;
		maxValue = dengerValue + 30;

		notifyDataSetChenged();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);

		viewWidth = this.getWidth();
		viewHeight = this.getHeight();

		verticalUnit = viewHeight / (maxValue - minValue);
		horizontalUnit = viewWidth / (logLimit );
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Path path = new Path();
		path.moveTo(viewWidth, viewHeight);
		Path path1 = new Path();
		path1.moveTo(viewWidth, viewHeight);
		
		int i = logLimit-1;
		float x,y;
		for (Integer val : logs) {
			int w = (val <= minValue) ? minValue + 1 : val;
			x = i * horizontalUnit;
			y = viewHeight - verticalUnit * (w - minValue);
			path.lineTo(x, y);
			i--;
		}
		path1.lineTo(0, viewHeight);
		path1.lineTo(viewWidth, viewHeight);
		canvas.drawPath(path1, paintNormal);
		path.lineTo(0, viewHeight);
		path.lineTo(viewWidth, viewHeight);
		canvas.drawPath(path, paintNormal);
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
