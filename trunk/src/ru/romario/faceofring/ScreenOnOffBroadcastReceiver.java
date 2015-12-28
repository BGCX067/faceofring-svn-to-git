package ru.romario.faceofring;


import ru.romario.faceofring.util.Log;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.SensorManager;


public class ScreenOnOffBroadcastReceiver extends BroadcastReceiver {

	private UpDownSensorListener _upDownSensorListener;
	private PhoneEventsHandler _phoneEventsHandler;


	protected ScreenOnOffBroadcastReceiver(UpDownSensorListener upDownSensorListener, PhoneEventsHandler phoneEventsHandler) {

		_upDownSensorListener = upDownSensorListener;
		_phoneEventsHandler = phoneEventsHandler;

	}


	public void onReceive(Context context, Intent intent) {

		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			onScreenOff(context);
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			onScreenOn(context);
		}

	}


	protected void onStart(Context context) {

		onScreenOn(context);

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);

		context.registerReceiver(this, filter);
	}


	protected void onStop(Context context) {

		onScreenOff(context);

		context.unregisterReceiver(this);
	}


	private void onScreenOn(Context context) {

		if (Log.DEBUG)
			Log.d("");

		_upDownSensorListener.onScreenOn();
		_phoneEventsHandler.onScreenOn();
		
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.registerListener(_upDownSensorListener, SensorManager.SENSOR_ORIENTATION,
				SensorManager.SENSOR_DELAY_NORMAL);
		
		

	}


	private void onScreenOff(Context context) {

		if (Log.DEBUG)
			Log.d("");
		
		SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		sensorManager.unregisterListener(_upDownSensorListener);
		
		_phoneEventsHandler.onScreenOff();
		_upDownSensorListener.onScreenOff();

	}
}
