package ru.romario.faceofring;


import ru.romario.faceofring.util.Log;
import android.content.Context;
import android.media.AudioManager;
import android.os.Vibrator;


public class NotificationController {

	private AudioManager _audioManager;
	private Vibrator _vibrator;

	private int _defaultVibrateSettings = -1;

	private boolean _vibrateIsMuted = false;
	private boolean _ringIsMuted = false;
	private boolean _otherSoundsIsMuted = false;

	private int _muteCounter = 0;


	protected NotificationController(Context context) {

		_audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		_vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}


	protected void setRingTo(boolean on) {

		if (Log.DEBUG)
			Log.d("on = " + on);

		if (on && _ringIsMuted) {
			_ringIsMuted = false;

			_audioManager.setStreamMute(AudioManager.STREAM_RING, false);
			_muteCounter--;
		}

		if (!on && !_ringIsMuted) {
			_ringIsMuted = true;

			_audioManager.setStreamMute(AudioManager.STREAM_RING, true);
			_muteCounter++;
		}

	}
	
	
	protected void setAllOtherSoundsTo(boolean on) {

		if (Log.DEBUG)
			Log.d("on = " + on);

		if (on && _otherSoundsIsMuted) {
			_otherSoundsIsMuted = false;

			_audioManager.setStreamMute(AudioManager.STREAM_ALARM, false);
			_audioManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
			_audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
			
		}

		if (!on && !_otherSoundsIsMuted) {
			_otherSoundsIsMuted = true;

			_audioManager.setStreamMute(AudioManager.STREAM_ALARM, true);
			_audioManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
			_audioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
			
		}

	}


	protected void setVibrateTo(boolean on) {

		if (Log.DEBUG)
			Log.d("on = " + on);

		if (on && _vibrateIsMuted) {

			_vibrateIsMuted = false;

			if (_defaultVibrateSettings == AudioManager.VIBRATE_SETTING_OFF) {
				return;
			}

			_audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, _defaultVibrateSettings);
			_audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, _defaultVibrateSettings);

		}

		if (!on && !_vibrateIsMuted) {

			_vibrateIsMuted = true;

			_defaultVibrateSettings = _audioManager.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER);
			if (_defaultVibrateSettings == AudioManager.VIBRATE_SETTING_OFF) {
				return;
			}

			_vibrator.cancel();
			_audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER, AudioManager.VIBRATE_SETTING_OFF);
			_audioManager.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION, AudioManager.VIBRATE_SETTING_OFF);
			_vibrator.cancel();
		}
	}


	protected void vibrate(long mls) {

		_vibrator.vibrate(mls);
	}

}
