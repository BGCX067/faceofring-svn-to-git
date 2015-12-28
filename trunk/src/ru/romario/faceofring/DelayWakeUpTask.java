package ru.romario.faceofring;


public class DelayWakeUpTask implements Runnable {

	private PhoneEventsHandler _faceOnOfSensorListener;


	protected DelayWakeUpTask(PhoneEventsHandler faceOnOfSensorListener) {

		_faceOnOfSensorListener = faceOnOfSensorListener;
	}


	public void run() {

		_faceOnOfSensorListener.onFinishDelayAfterFaceDownTask();
	}
}
