package com.koba.androidrtchart;

import android.graphics.Path;
import android.graphics.Region;


public class LineDot {
	private int value = 0;
	private float pointX = 0;
	private float pointY = 0;
	private float radius = 0;
	private Region region;
	private Path path;

	public LineDot(int value, float radius) {
		setValue(value);
		this.radius = radius;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public void setPointX(float pointX) {
		this.pointX = pointX;
	}

	public void setPointY(float pointY) {
		this.pointY = pointY;
	}

	public void setPoint(float pointX, float pointY) {
		setPointX(pointX);
		setPointY(pointY);
	}
	public void setRegion(Region region){
		this.region = region;
	}
	public void setPath(Path path){
		this.path = path;
	}

	public int getValue() {
		return value;
	}

	public float getPointX() {
		return pointX;
	}

	public float getPointY() {
		return pointY;
	}

	public float getRadius() {
		return radius;
	}
	public Region getRegion(){
		return region;
	}
	public Path getPath(){
		return path;
	}
}
