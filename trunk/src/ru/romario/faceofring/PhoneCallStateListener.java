package ru.romario.faceofring;


import ru.romario.android.prefs.PreferencesBox;
import ru.romario.faceofring.util.Log;
import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;


public class PhoneCallStateListener extends PhoneStateListener {

	private PhoneEventsHandler _phoneEventsHandler;
	private UpDownSensorListener _upDownSensorListener;

	private ServiceStateOnOffSound _serviceStateOnOffSound;


	protected PhoneCallStateListener(Context context, PhoneEventsHandler phoneEventsHandler,
			UpDownSensorListener upDownSensorListener, PreferencesBox preferencesBox) {

		_phoneEventsHandler = phoneEventsHandler;
		_upDownSensorListener = upDownSensorListener;

		_serviceStateOnOffSound = new ServiceStateOnOffSound(context, preferencesBox);
	}


	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		if (Log.DEBUG)
			Log.d("isRinging = " + (state == TelephonyManager.CALL_STATE_RINGING));

		if (state == TelephonyManager.CALL_STATE_RINGING) {

			// final boolean emergencyCall = isEmergencyCall(incomingNumber);

			// reset average calculation in cause of possible phone sleeping
			// before the call

			_upDownSensorListener.dropStatistics();
			_phoneEventsHandler.onRinging(true);

		} else {

			_phoneEventsHandler.onRinging(false);
		}

	}


	private int lastServiceState = ServiceState.STATE_IN_SERVICE;


	@Override
	public void onServiceStateChanged(ServiceState serviceState) {

		onServiceStateChangedInternal(serviceState);
	}


	private synchronized void onServiceStateChangedInternal(ServiceState serviceState) {

		if (Log.DEBUG)
			Log.d("serviceState = " + serviceState.toString());

		int currentState = serviceState.getState();

		if (lastServiceState == ServiceState.STATE_IN_SERVICE && currentState != ServiceState.STATE_IN_SERVICE) {
			_serviceStateOnOffSound.playOff();
		} else {
			if (lastServiceState != ServiceState.STATE_IN_SERVICE && currentState == ServiceState.STATE_IN_SERVICE) {
				_serviceStateOnOffSound.playOn();
			}
		}

		lastServiceState = currentState;

	}

	
	protected synchronized void onPreferencesChanged() {
		_serviceStateOnOffSound.initSounds();
	}
}
