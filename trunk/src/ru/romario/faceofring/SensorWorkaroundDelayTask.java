package ru.romario.faceofring;


public class SensorWorkaroundDelayTask implements Runnable {

	private PhoneEventsHandler _faceOnOfSensorListener;

	protected SensorWorkaroundDelayTask(PhoneEventsHandler faceOnOfSensorListener) {

		this._faceOnOfSensorListener = faceOnOfSensorListener;
	}


	public void run() {

		_faceOnOfSensorListener.onSensorWorkaroundDelayPast();
	}
}
