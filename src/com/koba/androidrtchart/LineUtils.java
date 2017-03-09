package com.koba.androidrtchart;

import android.graphics.PointF;
import android.util.Log;

public class LineUtils {

	public static float calcPointY(int height, int maxValue, int minValue,
			int value) {
		if(height < 20){
			Log.e("", "");
		}
		int margin = value - minValue;
		return height - (margin * height / (maxValue - minValue)) - 20;
	}

	public static float calcTMAngle(int maxValue, int minValue, int angle) {
		float ans = 0;
		try {

			int count = maxValue - minValue;
			float one = 180 / count;
			int w = angle - minValue;
			ans = w * one;
			if (ans < 0) {
				ans = 0;
			}

		} catch (ArithmeticException e) {
			e.printStackTrace();
		}
		return ans;
	}

	public static PointF calcTargetPoint(float angle, float radius, PointF origin) {
		if(angle == 90){
			return new PointF(origin.x, radius);
		}
		
		boolean bool = false;
		if(angle > 90){
			angle = 180 - angle;
			bool = true;
		}
		double angleRadian = Math.toRadians(angle);
		double y = Math.sin(angleRadian);
		double radiy = y * radius;
		double x = Math.sqrt(radius * radius  - radiy *radiy);
		
		PointF point = new PointF();
		if (!bool)
			x = -x;
		return new PointF((float)x, (float) (radiy));
	}
	public static PointF calcTargetMsgPoint(float angle, float radius, PointF origin){
		PointF point = calcTargetPoint(angle, radius, origin);
		float a = point.x;
		float b = point.y;
		
		if(angle > 90)
		point.x = a + 10;
		else
			point.x = a-60;
		point.y = b / a * point.x;
		
		return point;
		
		
	}
}
