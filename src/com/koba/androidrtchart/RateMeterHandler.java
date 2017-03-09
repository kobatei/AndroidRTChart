package com.koba.androidrtchart;

import android.os.Handler;
import android.os.Message;

public class RateMeterHandler extends Handler {
	private RateMeter meter;
	private long delayMills = 1;
	private int meterValue = 0;
	private int addAngleValue = 0;
	private boolean isUP = false;
	private boolean isRepeat = false;

	public RateMeterHandler(RateMeter meter) {
		this.meter = meter;
	}

	@Override
	public void handleMessage(Message msg) {
		if (isUP) {
			meter.setCurrentValue(meter.getCurrentValue() + 1);
			if (meterValue < meter.getCurrentValue()) {
				removeMessages(0);
				isRepeat = false;
			}
		} else {
			meter.setCurrentValue(meter.getCurrentValue() - 1);
			if (meterValue > meter.getCurrentValue()) {
				removeMessages(0);
				isRepeat = false;
			}
		}
		meter.invalidate();
		if (isRepeat) {
			sleep();
		}

	}

	public void execMessage(int value) {
		removeMessages(0);
		this.meterValue = value;
		if (meterValue > meter.getCurrentValue()) {
			isUP = true;
			addAngleValue = (meterValue - meter.getCurrentValue()) / 5;
		} else {
			isUP = false;
			addAngleValue = (meter.getCurrentValue() - meterValue) / 10;
		}
		isRepeat = true;

		sleep();
	}

	private void sleep() {
		sendMessageDelayed(obtainMessage(0), delayMills);
	}
}
