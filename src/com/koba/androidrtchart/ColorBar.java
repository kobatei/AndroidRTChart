package com.koba.androidrtchart;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

public class ColorBar extends View {
	private float barWidth;
	private float barHeight;
	private float barWidthBack;
	private float barHeightBack;
	private Handler handler;
	private Paint paint;
	private List<ColorBarItem> items;
	private int roundColor;
	private static final String roundColorCode = "#50708090";
	private static final float padding = 3f;

	public ColorBar(Context context) {
		super(context);
		init();
	}

	public ColorBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public ColorBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		handler = new Handler();
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStyle(Paint.Style.FILL);
		items = new ArrayList<ColorBarItem>();
		roundColor = Color.parseColor(roundColorCode);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
		// グラフのサイズを指定
		// barWidth = MeasureSpec.getSize(width);
		// barHeight = MeasureSpec.getSize(height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		barHeightBack = this.getHeight();
		barWidthBack = this.getWidth();
		barWidth = this.getWidth() - (padding * 2);
		barHeight = this.getHeight() - (padding * 2);

		// if (isReCalcPoint) {
		// notifyDatePointCaluculate();
		// isReCalcPoint = false;
		// }

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		RectF rectF;
		float unit = barWidth / ((float) calcBarItemValue());
		float x = 0.0f;

		paint.setColor(roundColor);
		rectF = new RectF(0, 0, barWidthBack, barHeightBack);
		canvas.drawRoundRect(rectF, 10f, 10f, paint);

		for (ColorBarItem item : items) {
			paint.setColor(Color.parseColor(item.getColorCode()));
			float before = ((float) item.getBarValue()) * unit;
			rectF = new RectF(x + padding, padding, before + x + padding,
					barHeight + padding);
			canvas.drawRect(rectF, paint);
			x += before;
		}

	}

	private long calcBarItemValue() {
		long w = 0;
		synchronized (items) {
			for (ColorBarItem item : items) {
				w += item.getBarValue();
			}
		}
		return w;
	}

	public void addBarItem(ColorBarItem item) {
		items.add(item);
		notifyDataSetChenged();
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
