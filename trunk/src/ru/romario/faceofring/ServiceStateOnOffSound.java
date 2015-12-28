package ru.romario.faceofring;


import java.util.HashMap;

import ru.romario.android.prefs.PreferencesBox;
import ru.romario.faceofring.util.Log;

import android.content.Context;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.SoundPool;
import android.net.Uri;


public class ServiceStateOnOffSound {

	private Context _context;

	private SoundPool _soundPool;
	private HashMap<Integer, Integer> _soundPoolMap = new HashMap<Integer, Integer>();

	private Ringtone _ringtonOff;
	private Ringtone _ringtonOn;

	private PreferencesBox _prefs;


	protected ServiceStateOnOffSound(Context context, PreferencesBox preferenceBox) {

		_context = context;
		_prefs = preferenceBox;

		initSounds();

	}


	protected void initSounds() {

		if (Log.DEBUG) {
			Log.d();
		}

		_ringtonOff = null;
		_ringtonOn = null;

		SoundPool localSoundPool = new SoundPool(4 + 20, AudioManager.STREAM_MUSIC, 100);
		HashMap<Integer, Integer> localSoundPoolMap = new HashMap<Integer, Integer>();

		int streamId = -1;

		if ((Boolean) _prefs.getValue(Prefs.OUTOF_IS_IN)) {

			if (Log.DEBUG) {
				Log.d("init out of service sound");
			}

			if ((Boolean) _prefs.getValue(Prefs.OUTOF_IS_IN_IS_CUSTOM_SOUND)) {

				if (Log.DEBUG) {
					Log.d("init out of service sound - custom sound");
				}

				String ringtoneOnPrefs = (String) _prefs.getValue(Prefs.OUTOF_IS_IN_CUSTOM_SOUND);
				_ringtonOn = RingtoneManager.getRingtone(_context, Uri.parse(ringtoneOnPrefs));

			} else {

				streamId = localSoundPool.load(_context, R.raw.sound_connect, 1);
				localSoundPoolMap.put(R.raw.sound_connect, streamId);
			}
		}

		if ((Boolean) _prefs.getValue(Prefs.OUTOF_IS_OUT)) {

			if (Log.DEBUG) {
				Log.d("init in service sound");
			}

			if ((Boolean) _prefs.getValue(Prefs.OUTOF_IS_OUT_IS_CUSTOM_SOUND)) {

				if (Log.DEBUG) {
					Log.d("init in service sound - custom sound");
				}

				String ringtoneOffPrefs = (String) _prefs.getValue(Prefs.OUTOF_IS_OUT_CUSTOM_SOUND);
				_ringtonOff = RingtoneManager.getRingtone(_context, Uri.parse(ringtoneOffPrefs));

			} else {

				streamId = localSoundPool.load(_context, R.raw.sound_disconnect, 1);
				localSoundPoolMap.put(R.raw.sound_disconnect, streamId);
			}
		}

		setSoundPool(localSoundPool, localSoundPoolMap);
	}


	private synchronized void setSoundPool(SoundPool localSoundPool, HashMap<Integer, Integer> localSoundPoolMap) {

		_soundPool = localSoundPool;
		_soundPoolMap = localSoundPoolMap;
	}


	protected void playOn() {

		if (_ringtonOn != null) {
			_ringtonOn.play();
			return;
		}

		play(R.raw.sound_connect);
	}


	protected void playOff() {

		if (_ringtonOff != null) {
			_ringtonOff.play();
			return;
		}

		play(R.raw.sound_disconnect);
	}


	private void play(int soundId) {

		AudioManager mgr = (AudioManager) _context.getSystemService(Context.AUDIO_SERVICE);
		int streamVolume = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);

		play(soundId, streamVolume);
	}


	private synchronized void play(int soundId, int streamVolume) {

		Integer pooledId = _soundPoolMap.get(soundId);
		if (pooledId == null) {
			return;
		}

		_soundPool.play(pooledId, streamVolume, streamVolume, 1, 0, 1f);
	}
}
