package ru.romario.faceofring;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;


public class ServiceManager {

	private static final Intent INTEND_FOREGROUND_SERVICE = new Intent(ForegroundService.class.getName());

	private boolean _isServiceRunning;
	
	protected synchronized boolean isServiceRunning(Context context) {

		final ServiceConnection serviceConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName componentname, IBinder ibinder) {

				_isServiceRunning = true;
			}


			public void onServiceDisconnected(ComponentName componentname) {

			}

		};

		context.bindService(new Intent(INTEND_FOREGROUND_SERVICE), serviceConnection, 0);
		context.unbindService(serviceConnection);

		return _isServiceRunning;

	}
}
