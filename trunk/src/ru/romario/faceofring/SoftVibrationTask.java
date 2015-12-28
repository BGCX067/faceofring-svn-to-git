package ru.romario.faceofring;




public class SoftVibrationTask implements Runnable {

	private PhoneEventsHandler _faceOnOfSensorListener;

	protected SoftVibrationTask(PhoneEventsHandler faceOnOfSensorListener) {

		_faceOnOfSensorListener = faceOnOfSensorListener;
	}


	public void run() {

		_faceOnOfSensorListener.onSoftVibration();
	}
}
