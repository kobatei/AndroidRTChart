package com.koba.androidrtchart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Point;
import android.graphics.Region;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class LineGraph extends View {
	private int targetColor = Color.parseColor("#FF6347");
	private int averageColor = Color.parseColor("#90333399");
	private int measureColor = Color.parseColor("#dd339933");
	private int selectedColor = Color.parseColor("#F5FFFA");
	private int gridColor = Color.parseColor("#F5FFFA");
	private float dotRadius = 24.0F;
	private float gridStroke = 0.5F;
	private float lineStroke = 12.0F;
	private int graphWidth;
	private int graphHeight;
	private int graphMaxValue = 250;
	private int graphMinValue = 50;
	private Handler handler;
	private int dataLimit = 20;
	private int targetValue;
	private int averageValue = -1;
	private int selectedIndex = -1;
	private LineDotTouchListener listener;
	private boolean isReCalcPoint = true;
	private List<LineDot> lineDots = Collections
			.synchronizedList(new ArrayList<LineDot>(dataLimit));

	public LineGraph(Context context) {
		super(context);
		handler = new Handler();
	}

	// Call
	public LineGraph(Context context, AttributeSet attrs) {
		super(context, attrs);
		handler = new Handler();

	}

	public LineGraph(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		handler = new Handler();

	}

	public void setLineDotTouchListener(LineDotTouchListener listener){
		this.listener = listener;
	}
	public void notifyDataSetChenged() {
		handler.post(new Runnable() {
			@Override
			public void run() {
				invalidate();
			}
		});
	}

	public void notifyDatePointCaluculate() {
		for (LineDot dot : lineDots) {
			dot.setPointY(LineUtils.calcPointY(graphHeight, graphMaxValue,
					graphMinValue, dot.getValue()));
		}
		notifyDataSetChenged();
	}

	public void setLineDotLmit(int limit) {
		dataLimit = limit;
		lineDots = Collections.synchronizedList(new ArrayList<LineDot>(
				dataLimit));
	}

	public void setPointReCalculate(boolean flag) {
		isReCalcPoint = flag;
	}

	public int getLineDotLimit() {
		return dataLimit;
	}

	public void addLineDot(int value) {
		if (lineDots.size() >= dataLimit) {
			lineDots.remove(0);
		}

		LineDot dot = new LineDot(value, dotRadius);
		dot.setPointY(LineUtils.calcPointY(graphHeight, graphMaxValue,
				graphMinValue, value));
		lineDots.add(dot);

	}

	public void setTargetValue(int value) {
		this.targetValue = value;
		notifyDataSetChenged();
	}
	public void setAverageValue(int averageValue){
		this.averageValue = averageValue;
		notifyDataSetChenged();
	}
	public void calucurateAverageValue(){
		averageValue = calcMesureAvg();
		notifyDataSetChenged();
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
	}

	@Override
	protected void onMeasure(int width, int height) {
		super.onMeasure(width, height);
		// グラフのサイズを指定
		graphWidth = MeasureSpec.getSize(width);
		graphHeight = MeasureSpec.getSize(height);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
		graphWidth = this.getWidth();
		graphHeight = this.getHeight();

		if (isReCalcPoint) {
			notifyDatePointCaluculate();
			isReCalcPoint = false;
		}

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(lineStroke);

//		 drowGrid(canvas, paint);
		drowTargetLine(canvas, paint);
		drowAverageLine(canvas, paint);
		drowLineGraph(canvas, paint);
		
		if(selectedIndex != -1){
			drowDotSelected(canvas, paint, lineDots.get(selectedIndex));
			selectedIndex = -1;
		}
	}

	private void drowTargetLine(Canvas canvas, Paint paint) {
		float y = LineUtils.calcPointY(graphHeight, graphMaxValue,
				graphMinValue, targetValue);
		paint.setColor(targetColor);
		canvas.drawLine(0, y, graphWidth, y, paint);
		paint.setTextSize(50F);
		canvas.drawText(String.valueOf(targetValue), graphWidth - 80, y - 20,
				paint);
	}

	private void drowAverageLine(Canvas canvas, Paint paint) {
		if (averageValue < 0)
			return;
		
		float y = LineUtils.calcPointY(graphHeight, graphMaxValue,
				graphMinValue, averageValue);
		paint.setColor(averageColor);
		paint.setTextSize(50F);
		canvas.drawText(String.valueOf(averageValue), 0, y - 20,
				paint);
		
		Paint paint2 = new Paint();
		paint2.setColor(averageColor);
		paint2.setPathEffect( new DashPathEffect(new float[]{ 2.0f, 2.0f }, 0.0F));
		paint2.setStyle(Paint.Style.STROKE); // スタイルは線(Stroke)を指定する
	    paint2.setStrokeWidth(5); // 線の太さ
		canvas.drawLine(0, y, graphWidth, y, paint2);
		
	}

	private synchronized void drowLineGraph(Canvas canvas, Paint paint) {
		if (lineDots.size() < 1) {
			return;
		}
		synchronized (lineDots) {
			float span = (float) graphWidth / (dataLimit + 2);

			LineDot currentDot;
			LineDot beforeDot;

			beforeDot = lineDots.get(0);
			beforeDot.setPointX(span);
			drowDot(canvas, paint, beforeDot);

			for (int i = 1; i < lineDots.size(); i++) {
				currentDot = lineDots.get(i);
				currentDot.setPointX(span * (i + 1));
				drowDot(canvas, paint, currentDot);
				drowDotToDotLine(canvas, paint, beforeDot, currentDot);
				beforeDot = currentDot;
			}
		}
	}

	private void drowDot(Canvas canvas, Paint paint, LineDot dot) {
		paint.setColor(measureColor);
		canvas.drawCircle(dot.getPointX(), dot.getPointY(), dotRadius, paint);
		Path path = new Path();
		path.addCircle(dot.getPointX(), dot.getPointY(), dotRadius,
				Direction.CW);
		dot.setPath(path);
		dot.setRegion(new Region((int) (dot.getPointX() - dotRadius),
				(int) (dot.getPointY() - dotRadius),
				(int) (dot.getPointX() + dotRadius),
				(int) (dot.getPointY() + dotRadius)));

	}
	private void drowDotSelected(Canvas canvas, Paint paint, LineDot dot) {
		paint.setColor(selectedColor);
		canvas.drawCircle(dot.getPointX(), dot.getPointY(), dotRadius/2, paint);
	}
	

	private void drowDotToDotLine(Canvas canvas, Paint paint,
			LineDot beforeDot, LineDot currentDot) {
		paint.setColor(measureColor);
		canvas.drawLine(beforeDot.getPointX(), beforeDot.getPointY(),
				currentDot.getPointX(), currentDot.getPointY(), paint);
	}

	private void drowGrid(Canvas canvas, Paint paint) {
		paint.setColor(gridColor);
		paint.setStrokeWidth(gridStroke);
		for (int y = 0; y < dataLimit; y++) {
			// canvas.drawLine(0, y+1*dataLimit, graphWidth, y, paint);
		}
		for (int x = 0; x < graphWidth % dataLimit; x++) {
			canvas.drawLine(x, 0, x, graphHeight, paint);
		}
	}

	private synchronized int calcMesureAvg() {
		if (lineDots.size() < 1) {
			return 0;
		}
		int w = 0;
		for (LineDot dot : lineDots) {
			w += dot.getValue();
		}
		return w / lineDots.size();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		Point point = new Point();
		point.x = (int) event.getX();
		point.y = (int) event.getY();

		int count = 0;

		Region r = new Region();
		for (LineDot dot : lineDots) {
			if (dot.getPath() != null && dot.getRegion() != null) {
				r.setPath(dot.getPath(), dot.getRegion());
				if (r.contains(point.x, point.y)
						&& event.getAction() == MotionEvent.ACTION_DOWN) {
					selectedIndex = count;
					listener.onTouch(count, dot);
				} 
//					else if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (r.contains(point.x, point.y) && listener != null
//							&& selectedIndex != -1) {
//						listener.onTouch(count, dot);
//					}
//					selectedIndex = -1;
//				}
			}
			count++;
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			notifyDataSetChenged();
		}

		return true;
	}

	// @Override
	// public boolean onTouchEvent(MotionEvent event) {
	//
	// if (event.getAction() != MotionEvent.ACTION_DOWN) {
	// return false;
	// }
	//
	// Point point = new Point();
	// point.x = (int) event.getX();
	// point.y = (int) event.getY();
	//
	// for (LineDot dot : lineDots) {
	// if (dot.getPointX() - dot.getRadius() > point.x
	// && dot.getPointX() + dot.getRadius() > point.x) {
	// if (dot.getPointY() - dot.getRadius() > point.y
	// && dot.getPointY() + dot.getRadius() > point.y) {
	// Log.i(VIEW_LOG_TAG, "touch1111");
	//
	// }
	// }
	// }
	// Log.i(VIEW_LOG_TAG, "touch");
	//
	// // Log.i(VIEW_LOG_TAG, "touch");
	// //
	// // Region r = new Region();
	// // for (Line line : lines) {
	// // pointCount = 0;
	// // for (LinePoint p : line.getPoints()) {
	// //
	// // if (p.getPath() != null && p.getRegion() != null) {
	// // r.setPath(p.getPath(), p.getRegion());
	// // if (r.contains(point.x, point.y)
	// // && event.getAction() == MotionEvent.ACTION_DOWN) {
	// // indexSelected = count;
	// // } else if (event.getAction() == MotionEvent.ACTION_UP) {
	// // if (r.contains(point.x, point.y) && listener != null) {
	// // listener.onClick(lineCount, pointCount);
	// // }
	// // indexSelected = -1;
	// // }
	// // }
	// //
	// // pointCount++;
	// // count++;
	// // }
	// // lineCount++;
	// //
	// // }
	// //
	// // if (event.getAction() == MotionEvent.ACTION_DOWN
	// // || event.getAction() == MotionEvent.ACTION_UP) {
	// // shouldUpdate = true;
	// // postInvalidate();
	// // }
	//
	// return true;
	// }
}
