package ru.romario.faceofring;


import ru.romario.android.prefs.PreferencesBox;
import ru.romario.faceofring.util.Log;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.preference.PreferenceActivity;


public class AppPreferenceActivity extends PreferenceActivity {

	private IPreferencesListener _preferencesListener;


	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		bindToService();

		addPreferencesFromResource(R.xml.preferences);

		PreferencesBox.getSharedPreferences(this.getApplicationContext()).registerOnSharedPreferenceChangeListener(
				onSharedPreferenceChangeListener);

	}


	private SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {

		@Override
		public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

			if (Log.DEBUG)
				Log.d("key = " + key);
			
			if (key.equals("DROP_PREFS")) {
				PreferencesBox.dropAllPreferences(AppPreferenceActivity.this.getApplicationContext());
			}
					

			synchronized (AppPreferenceActivity.this) {

				if (_preferencesListener == null) {
					return;
				}

				try {
					AppPreferenceActivity.this._preferencesListener.onPreferencesChanged(key);
				} catch (RemoteException e) {

					if (Log.DEBUG)
						Log.e("", e);

				}
			}

		}
	};


	@Override
	protected void onDestroy() {

		super.onDestroy();

		if (Log.DEBUG)
			Log.d("");

		unbindFromService();
		PreferencesBox.getSharedPreferences(this.getApplicationContext()).unregisterOnSharedPreferenceChangeListener(
				onSharedPreferenceChangeListener);
	}


	@Override
	protected void onPause() {

		super.onPause();

		if (Log.DEBUG)
			Log.d("");

//		unbindFromService();
//		PreferencesBox.getSharedPreferences(this.getApplicationContext()).unregisterOnSharedPreferenceChangeListener(
//				onSharedPreferenceChangeListener);
	}


	@Override
	protected void onResume() {

		super.onResume();

		if (Log.DEBUG)
			Log.d("");

//		bindToService();
//		PreferencesBox.getSharedPreferences(this.getApplicationContext()).registerOnSharedPreferenceChangeListener(
//				onSharedPreferenceChangeListener);
	}


	private final ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName componentname, IBinder ibinder) {

			if (Log.DEBUG)
				Log.d("ibinder = " + ibinder);

			setPreferencesListener(IPreferencesListener.Stub.asInterface(ibinder));

		}


		public void onServiceDisconnected(ComponentName componentname) {

			setPreferencesListener(null);
		}

	};


	private void bindToService() {

		if (Log.DEBUG)
			Log.d("");

		bindService(new Intent(Main.INTEND_FOREGRAUND_SERVICE), serviceConnection, 0);

	}


	private void unbindFromService() {

		if (Log.DEBUG)
			Log.d("");

		try {
			unbindService(serviceConnection);
		} catch (RuntimeException ex) {
			// Ignore
		}
	}


	private synchronized void setPreferencesListener(IPreferencesListener preferencesListener) {

		this._preferencesListener = preferencesListener;
	}

}
