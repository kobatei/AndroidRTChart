package com.koba.androidrtchart;


public class ColorBarItem {

	private long barValue;
	private String comment;
	private String colorCode;
	public ColorBarItem(long value, String comment, String colorCode) {
		setBarValue(value);
		setComment(comment);
		setColorCode(colorCode);
	}
	public long getBarValue() {
		return barValue;
	}
	public void setBarValue(long barValue) {
		this.barValue = barValue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public String getColorCode() {
		return colorCode;
	}
	public void setColorCode(String colorCode) {
		this.colorCode = colorCode;
	}

}
