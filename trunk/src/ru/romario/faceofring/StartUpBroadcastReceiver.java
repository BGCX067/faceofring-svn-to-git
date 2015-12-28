package ru.romario.faceofring;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


// BOOT_COMPLETED_ACTION
public class StartUpBroadcastReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
			if ((Boolean) Prefs.createPreferencesBox(context).getValue(Prefs.IS_RUNNING)) {
				context.startService(Main.INTEND_FOREGRAUND_SERVICE);
			}
		}
		/**
		 * <intent-filter> <action
		 * android:name="android.intent.action.PACKAGE_INSTALL" /> <action
		 * android:name="android.intent.action.PACKAGE_ADDED" /> <action
		 * android:name="android.intent.action.PACKAGE_CHANGED" /> <data
		 * android:scheme="package" /> </intent-filter>
		 * 
		 */
		// if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
		// Log.d("Intent.ACTION_PACKAGE_ADDED " + intent.getData().toString());
		// Log.d("Intent.ACTION_PACKAGE_ADDED " +
		// intent.getDataString().toString());
		// }
		//		
		// if (Intent.ACTION_PACKAGE_CHANGED.equals(intent.getAction())) {
		// Log.d("Intent.ACTION_PACKAGE_CHANGED " +
		// intent.getData().toString());
		// Log.d("Intent.ACTION_PACKAGE_CHANGED " +
		// intent.getDataString().toString());
		// }
		//
		// if (Intent.ACTION_PACKAGE_INSTALL.equals(intent.getAction())) {
		// Log.d("Intent.ACTION_PACKAGE_INSTALL " +
		// intent.getData().toString());
		// Log.d("Intent.ACTION_PACKAGE_INSTALL " +
		// intent.getDataString().toString());
		// }
		//
		// if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
		// Log.d("Intent.ACTION_PACKAGE_REMOVED " +
		// intent.getData().toString());
		// Log.d("Intent.ACTION_PACKAGE_REMOVED " +
		// intent.getDataString().toString());
		// }
		//
		// if (Intent.ACTION_PACKAGE_RESTARTED.equals(intent.getAction())) {
		// Log.d("Intent.ACTION_PACKAGE_RESTARTED " +
		// intent.getData().toString());
		// Log.d("ACTION_PACKAGE_RESTARTED " +
		// intent.getDataString().toString());
		// }
	}

}
