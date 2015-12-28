package ru.romario.android.prefs;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import ru.romario.android.prefs.PreferencesBoxItem;
import ru.romario.faceofring.util.Log;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class PreferencesBox {

	private SharedPreferences _sharedPreferences;
	private Context _context;

	private Set<SharedPreferences.OnSharedPreferenceChangeListener> listeners = new HashSet<SharedPreferences.OnSharedPreferenceChangeListener>();

	private Map<String, PreferencesBoxItem> items = new HashMap<String, PreferencesBoxItem>();


	public PreferencesBox(Context context) {

		_sharedPreferences = PreferencesBox.getSharedPreferences(context);
		_context = context.getApplicationContext();

	}


	public void putToMap(PreferencesBoxItem item) {

		items.put(item.getKey(), item);
	}


	public Object getValue(String key) {

		PreferencesBoxItem item = items.get(key);

		if (item == null) {

			String msg = "Property with key \"" + key + "\" not registered";

			RuntimeException ex = new NullPointerException(msg);
			Log.e(msg, ex);
			throw ex;
		}

		return item.getValue(_sharedPreferences);
	}


	public void setValue(String key, Object value) {

		PreferencesBoxItem item = items.get(key);

		if (item == null) {

			String msg = "Property with key \"" + key + "\" not registered";

			RuntimeException ex = new NullPointerException(msg);
			Log.e(msg, ex);
			throw ex;
		}

		item.setValue(_sharedPreferences, value);
	}


	public static SharedPreferences getSharedPreferences(Context context) {

		return PreferenceManager.getDefaultSharedPreferences(context);
	}


	/**
	 * Emulate event notification. This is a workaround of a bug where service
	 * can not receive notification about preferences change.
	 * 
	 * @param key
	 */
	public synchronized void firePreferencesChange(String key) {

		cleanProperties();

		if (listeners.size() == 0) {
			return;
		}

		for (SharedPreferences.OnSharedPreferenceChangeListener listener : listeners) {
			listener.onSharedPreferenceChanged(_sharedPreferences, key);
		}
	}


	/**
	 * Register listener
	 * 
	 * @param listener
	 */
	public synchronized void registerListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {

		listeners.add(listener);
	}


	protected void cleanProperties() {

		if (Log.DEBUG)
			Log.d("");

		_sharedPreferences = PreferencesBox.getSharedPreferences(_context);

		for (PreferencesBoxItem item : items.values()) {
			item.cleanValue();
		}

	}


	public static void dropAllPreferences(Context context) {

		if (Log.DEBUG) {
			Log.d();
		}

		SharedPreferences prefs = getSharedPreferences(context);

		Map<String, ?> allPrefs = prefs.getAll();

		Editor editor = prefs.edit();

		for (String key : allPrefs.keySet()) {

			if (Log.DEBUG) {
				Log.d("Remove preference named \"" + key + "\"");
			}
			
			editor.remove(key);
		}
		
		editor.commit();
	}

}
