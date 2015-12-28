package ru.romario.faceofring;


import ru.romario.android.prefs.PreferencesBox;
import ru.romario.faceofring.util.Log;
import android.content.Context;
import android.os.Handler;


public class PhoneEventsHandler {

	private static final long DELAY_FOR_SCREEN_OFF_WORKAROUND = 5000L;

	private NotificationController _notificationController;
	private PreferencesBox _preferencesBox;

	private int _lastState = UpDownSensorListener.STATE_DEFAULT;

	private long _lastFaceDownTime = 0;

	private Handler _objHandler = new Handler();
	private DelayWakeUpTask _delayAfterFaceDownTask;
	private SoftVibrationTask _softVibrationTask;
	private SensorWorkaroundDelayTask _sensorWorkaroundDelayTask;

	private boolean _isRinging = false;
	private boolean _isScreenOn = true;
	private boolean _isShaked = false;

	private boolean _feedEvents = true;

	private boolean _settingsOnFaceUp_isMuteRingStream = false;
	private boolean _settingsOnFaceUp_isMuteOtherAudioStreams = false;
	private boolean _settingsOnFaceUp_isDisableVibration = true;
	private boolean _settingsOnFaceDown_isMuteRingStream = true;
	private boolean _settingsOnFaceDown_isMuteOtherAudioStreams = false;
	private boolean _settingsOnFaceDown_isDisableVibration = true;
	private boolean _settingsOnFaceDown_isWakeUpDelayEnabled = true;
	private long _settingsOnFaceDown_WakeUpDelay = 10000L;
	private boolean _settingsOnFaceDown_isSoftVibrationEnabled = true;
	private long _settingsOnFaceDown_SoftVibrationInterval = 10000L;


	public PhoneEventsHandler(Context context, PreferencesBox preferencesBox) {

		_preferencesBox = preferencesBox;
		_notificationController = new NotificationController(context);

		_delayAfterFaceDownTask = new DelayWakeUpTask(this);
		_softVibrationTask = new SoftVibrationTask(this);
		_sensorWorkaroundDelayTask = new SensorWorkaroundDelayTask(this);

		_preferencesBox = preferencesBox;

		loadPreferences();

	}


	protected synchronized void onFaceDownState(int accuracyState) {

		if (Log.DEBUG)
			Log.d("");

		_lastFaceDownTime = System.currentTimeMillis();

		if (_lastState == UpDownSensorListener.STATE_FACE_DOWN) {
			return;
		}

		if (_feedEvents) {
			onFaceDownInternal();
		}

		_isShaked = false;
		_lastState = UpDownSensorListener.STATE_FACE_DOWN;
	}


	private synchronized void onFaceDownInternal() {

		_notificationController.setRingTo(!_settingsOnFaceDown_isMuteRingStream);
		_notificationController.setAllOtherSoundsTo(!_settingsOnFaceDown_isMuteOtherAudioStreams);

		if (_isRinging) {
			_notificationController.setVibrateTo(!_settingsOnFaceDown_isDisableVibration);
		}
	}


	protected synchronized void onFaceUpState(int accuracyState) {

		if (Log.DEBUG)
			Log.d("");

		if (_lastState == UpDownSensorListener.STATE_FACE_UP) {
			return;
		}

		if (_feedEvents) {

			if (_lastState == UpDownSensorListener.STATE_FACE_DOWN && _settingsOnFaceDown_isWakeUpDelayEnabled
					&& _isRinging) {
				startDelayFeed();
			} else {
				onFaceUpInternal();
			}
		}

		_isShaked = false;
		_lastState = UpDownSensorListener.STATE_FACE_UP;
	}


	private synchronized void onFaceUpInternal() {

		_notificationController.setRingTo(!_settingsOnFaceUp_isMuteRingStream);
		_notificationController.setAllOtherSoundsTo(!_settingsOnFaceUp_isMuteOtherAudioStreams);

		if (_isRinging) {
			_notificationController.setVibrateTo(!_settingsOnFaceUp_isDisableVibration);
		}

	}


	protected synchronized void onDefaultState(int accuracyState) {

		if (Log.DEBUG)
			Log.d("");

		if (_lastState == UpDownSensorListener.STATE_DEFAULT) {
			return;
		}

		if (_feedEvents) {
			if (_lastState == UpDownSensorListener.STATE_FACE_DOWN && _settingsOnFaceDown_isWakeUpDelayEnabled
					&& _isRinging) {
				startDelayFeed();
			} else {
				onDefaultInternal();
			}
		}

		_isShaked = false;
		_lastState = UpDownSensorListener.STATE_DEFAULT;

	}


	private synchronized void onDefaultInternal() {

		_notificationController.setRingTo(true);
		_notificationController.setAllOtherSoundsTo(true);

		if (_isRinging) {
			_notificationController.setVibrateTo(true);
		}
	}


	protected void onUnknownState(int accuracyState) {

		if (Log.DEBUG)
			Log.d("");

		_isShaked = true;
	}


	protected synchronized void onDestroy() {

		try {
			_objHandler.removeCallbacks(_delayAfterFaceDownTask);
			_objHandler.removeCallbacks(_softVibrationTask);
		} catch (RuntimeException ex) {
			// Ignore
		}

		onDefaultInternal();
	}


	private synchronized void startDelayFeed() {

		if (Log.DEBUG)
			Log.d("");

		_feedEvents = false;
		_objHandler.postDelayed(_delayAfterFaceDownTask, _settingsOnFaceDown_WakeUpDelay);
	}


	protected synchronized void onFinishDelayAfterFaceDownTask() {

		if (Log.DEBUG)
			Log.d("start");

		long currTime = System.currentTimeMillis();

		// if phone should sleep a little more? (phone was on FaceDown state
		// after
		// delayWakeUpTask was started)

		long neededSleepTime = (_lastFaceDownTime + _settingsOnFaceDown_WakeUpDelay) - currTime;

		if (_lastState != UpDownSensorListener.STATE_FACE_DOWN && neededSleepTime > 0) {

			if (Log.DEBUG)
				Log.d("prePostDelay: " + neededSleepTime);

			_objHandler.postDelayed(_delayAfterFaceDownTask, neededSleepTime);

			if (Log.DEBUG)
				Log.d("afterPostDelay");

			return;
		}

		_feedEvents = true;
		repeatLastEventInternal();

		if (Log.DEBUG)
			Log.d("end. Starts feeding");

	}


	protected synchronized void onSoftVibration() {

		if (Log.DEBUG)
			Log.d("");

		if (_isRinging && _lastState == UpDownSensorListener.STATE_FACE_DOWN) {

			_notificationController.vibrate(100);
			_objHandler.postDelayed(_softVibrationTask, _settingsOnFaceDown_SoftVibrationInterval);
		}
	}


	protected void onRinging(boolean isRinging) {

		if (Log.DEBUG)
			Log.d("isRinging = " + isRinging);

		_isRinging = isRinging;

		if (isRinging) {
			// turn off all notifications for 1 sec

			if (!_isScreenOn && (_settingsOnFaceUp_isMuteRingStream || _settingsOnFaceDown_isMuteRingStream)) {
				_notificationController.setRingTo(false);
			}

			if (!_isScreenOn && (_settingsOnFaceUp_isDisableVibration || _settingsOnFaceDown_isDisableVibration)) {
				_notificationController.setVibrateTo(false);
			}

			_feedEvents = false;

			if (Log.DEBUG)
				Log.d("isScreenOn = " + _isScreenOn);

			if (_isScreenOn) {
				onSensorWorkaroundDelayPast();
			} else {
				_objHandler.postDelayed(_sensorWorkaroundDelayTask, DELAY_FOR_SCREEN_OFF_WORKAROUND);
			}

			if (Log.DEBUG)
				Log.d("time : " + System.currentTimeMillis());
			
		} else {
			_notificationController.setVibrateTo(true);
		}

		_isScreenOn = true;

	}


	protected void onSensorWorkaroundDelayPast() {

		if (Log.DEBUG)
			Log.d("time = " + System.currentTimeMillis());

		_feedEvents = true;
		repeatLastEventInternal();

		if (_isRinging) {

			if (_isShaked) {
				onDefaultState(UpDownSensorListener.ACCURACY_EXCELLENT);
			} else {

				if (_settingsOnFaceDown_isSoftVibrationEnabled && _lastState == UpDownSensorListener.STATE_FACE_DOWN) {

					if (Log.DEBUG)
						Log.d("start soft vobration");

					onSoftVibration();
				}
			}
		}

	}


	protected void onScreenOn() {

		if (Log.DEBUG)
			Log.d("");

		_isScreenOn = true;
	}


	protected void onScreenOff() {

		if (Log.DEBUG)
			Log.d("");

		_isScreenOn = false;
	}


	private synchronized void repeatLastEventInternal() {

		switch (_lastState) {
			case UpDownSensorListener.STATE_DEFAULT: {
				onDefaultInternal();
				return;
			}
			case UpDownSensorListener.STATE_FACE_DOWN: {
				onFaceDownInternal();
				return;
			}
			case UpDownSensorListener.STATE_FACE_UP: {
				onFaceUpInternal();
				return;
			}
		}
	}


	protected synchronized void onPreferencesChanged() {

		if (Log.DEBUG)
			Log.d("");

		loadPreferences();

		repeatLastEventInternal();
	}


	private synchronized void loadPreferences() {

		if (Log.DEBUG)
			Log.d("");

		_settingsOnFaceUp_isMuteRingStream = (Boolean) _preferencesBox.getValue(Prefs.ONFACEUP_IS_MUTE_RING_STREAM);
		_settingsOnFaceUp_isMuteOtherAudioStreams = (Boolean) _preferencesBox
				.getValue(Prefs.ONFACEUP_IS_MUTE_OTHER_AUDIO_STREAMS);
		_settingsOnFaceUp_isDisableVibration = (Boolean) _preferencesBox.getValue(Prefs.ONFACEUP_IS_DISABLE_VIBRATION);

		_settingsOnFaceDown_isMuteRingStream = (Boolean) _preferencesBox.getValue(Prefs.ONFACEDOWN_IS_MUTE_RING_STREAM);
		_settingsOnFaceDown_isMuteOtherAudioStreams = (Boolean) _preferencesBox
				.getValue(Prefs.ONFACEDOWN_IS_MUTE_OTHER_AUDIO_STREAMS);
		_settingsOnFaceDown_isDisableVibration = (Boolean) _preferencesBox
				.getValue(Prefs.ONFACEDOWN_IS_DISABLE_VIBRATION);
		_settingsOnFaceDown_isWakeUpDelayEnabled = (Boolean) _preferencesBox
				.getValue(Prefs.ONFACEDOWN_IS_WAKE_UP_DELAY_ENABLED);
		_settingsOnFaceDown_WakeUpDelay = 1000L * (Integer) _preferencesBox.getValue(Prefs.ONFACEDOWN_WAKE_UP_DELAY);
		_settingsOnFaceDown_isSoftVibrationEnabled = (Boolean) _preferencesBox
				.getValue(Prefs.ONFACEDOWN_IS_SOFT_VIBRATION_ENABLED);
		_settingsOnFaceDown_SoftVibrationInterval = 1000L * (Integer) _preferencesBox
				.getValue(Prefs.ONFACEDOWN_SOFT_VIBRATION_INTERVAL);

	}

}
