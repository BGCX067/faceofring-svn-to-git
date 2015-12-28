package ru.romario.android.prefs;


import ru.romario.faceofring.util.Log;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class PreferencesBoxItem {

	private static final int BOOL = 0;
	private static final int FLOAT = 1;
	private static final int INT = 2;
	private static final int LONG = 3;
	private static final int STR = 4;

	
	private int _type;

	private String _key;
	private Object _defaultValue;
	private Object _value;


	public PreferencesBoxItem(String key, Object defaultValue) {

		_key = key;
		_defaultValue = defaultValue;
	}


	public PreferencesBoxItem(String key, boolean defaultValue) {

		this(key, (Boolean) defaultValue);

		_type = BOOL;
	}


	public PreferencesBoxItem(String key, float defaultValue) {

		this(key, (Float) defaultValue);

		_type = FLOAT;
	}


	public PreferencesBoxItem(String key, int defaultValue) {

		this(key, (Integer) defaultValue);

		_type = INT;
	}


	public PreferencesBoxItem(String key, long defaultValue) {

		this(key, (Long) defaultValue);

		_type = LONG;
	}


	public PreferencesBoxItem(String key, String defaultValue) {

		this(key, (Object) defaultValue);

		_type = STR;
	}


	protected String getKey() {

		return _key;
	}


	protected void cleanValue() {

		_value = null;
	}


	protected Object getValue(SharedPreferences sharedPreferences) {

		if (_value != null) {

			if (Log.DEBUG)
				Log.d(_key + " = " + _value);

			return _value;
		}

		switch (_type) {
			case (BOOL): {
				_value = sharedPreferences.getBoolean(_key, (Boolean) _defaultValue);
				break;
			}
			case (FLOAT): {
				_value = sharedPreferences.getFloat(_key, (Float) _defaultValue);
				break;
			}
			case (INT): {
				_value = sharedPreferences.getInt(_key, (Integer) _defaultValue);
				break;
			}
			case (LONG): {
				_value = sharedPreferences.getLong(_key, (Long) _defaultValue);
				break;
			}
			case (STR): {
				_value = sharedPreferences.getString(_key, (String) _defaultValue);
				break;
			}
		}

		if (Log.DEBUG)
			Log.d(_key + " = " + _value);

		return _value;

	}


	protected void setValue(SharedPreferences sharedPreferences, Object value) {

		_value = value;

		Editor editor = sharedPreferences.edit();

		switch (_type) {
			case (BOOL): {
				editor.putBoolean(_key, (Boolean) _value);
				break;
			}
			case (FLOAT): {
				editor.putFloat(_key, (Float) _value);
				break;
			}
			case (INT): {
				editor.putInt(_key, (Integer) _value);
				break;
			}
			case (LONG): {
				editor.putLong(_key, (Long) _value);
				break;
			}
			case (STR): {
				editor.putString(_key, (String) _value);
				break;
			}
		}

		editor.commit();

		if (Log.DEBUG)
			Log.d(_key + " = " + _value);

	}

}
