package ru.romario.faceofring;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import ru.romario.android.prefs.PreferencesBox;
import ru.romario.faceofring.util.Log;


public class ForegroundService extends Service {

	private PhoneEventsHandler _phoneEventsHandler;

	private PhoneCallStateListener _phoneCallStateListener;
	private UpDownSensorListener _upDownSensorListener;
	private ScreenOnOffBroadcastReceiver _screenOnOffBroadcastReceiver;

	private PreferencesBox _preferencesBox;

	@Override
	public void onCreate() {

		super.onCreate();

		if (Log.DEBUG)
			Log.d("");

		super.setForeground(true);

		Context context = this.getApplicationContext();

		_preferencesBox = Prefs.createPreferencesBox(context);
		_phoneEventsHandler = new PhoneEventsHandler(context, _preferencesBox);

		_upDownSensorListener = new UpDownSensorListener(_phoneEventsHandler);
		_phoneCallStateListener = new PhoneCallStateListener(context, _phoneEventsHandler, _upDownSensorListener, _preferencesBox);

		_screenOnOffBroadcastReceiver = new ScreenOnOffBroadcastReceiver(_upDownSensorListener, _phoneEventsHandler);


		_preferencesBox.registerListener(new OnSharedPreferenceChangeListener() {

			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedpreferences, String s) {

				_phoneEventsHandler.onPreferencesChanged();
				_phoneCallStateListener.onPreferencesChanged();
				
			}});
		
		
		if (Log.DEBUG)
			Log.d("-");
	}


	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);

		if (Log.DEBUG)
			Log.d("");

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(_phoneCallStateListener, PhoneStateListener.LISTEN_CALL_STATE
				| PhoneStateListener.LISTEN_SERVICE_STATE);

		_screenOnOffBroadcastReceiver.onStart(this.getApplicationContext());

		if (Log.DEBUG)
			Log.d("-");
	}


	@Override
	public void onDestroy() {

		super.onDestroy();

		if (Log.DEBUG)
			Log.d("");

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(_phoneCallStateListener, PhoneStateListener.LISTEN_NONE);

		_phoneEventsHandler.onDestroy();

		_screenOnOffBroadcastReceiver.onStop(this.getApplicationContext());

		if (Log.DEBUG)
			Log.d("-");
	}


	@Override
	protected void finalize() throws Throwable {

		if (Log.DEBUG)
			Log.d("");

		super.finalize();


		_phoneEventsHandler.onDestroy();

	}


	@Override
	public IBinder onBind(Intent intent) {

		return preferencesListener;
	}


	private final IPreferencesListener.Stub preferencesListener = new IPreferencesListener.Stub() {

		@Override
		public void onPreferencesChanged(String key) throws RemoteException {

			if (Log.DEBUG)
				Log.d("");
			
			_preferencesBox.firePreferencesChange(key);
			
		}

	};

}
