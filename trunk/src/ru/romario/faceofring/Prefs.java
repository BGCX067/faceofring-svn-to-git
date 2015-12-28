package ru.romario.faceofring;


import ru.romario.android.prefs.PreferencesBox;
import ru.romario.android.prefs.PreferencesBoxItem;
import android.content.Context;


public class Prefs {

	public static final String IS_RUNNING = "State";

	public static final String ONFACEUP_IS_MUTE_RING_STREAM = "ONFACEUP_IS_MUTE_RING_STREAM";
	public static final String ONFACEUP_IS_MUTE_OTHER_AUDIO_STREAMS = "ONFACEUP_IS_MUTE_OTHER_AUDIO_STREAMS";
	public static final String ONFACEUP_IS_DISABLE_VIBRATION = "ONFACEUP_IS_DISABLE_VIBRATION";

	public static final String ONFACEDOWN_IS_MUTE_RING_STREAM = "ONFACEDOWN_IS_MUTE_RING_STREAM";
	public static final String ONFACEDOWN_IS_MUTE_OTHER_AUDIO_STREAMS = "ONFACEDOWN_IS_MUTE_OTHER_AUDIO_STREAMS";
	public static final String ONFACEDOWN_IS_DISABLE_VIBRATION = "ONFACEDOWN_IS_DISABLE_VIBRATION";
	public static final String ONFACEDOWN_IS_SOFT_VIBRATION_ENABLED = "ONFACEDOWN_IS_SOFT_VIBRATION_ENABLED";
	public static final String ONFACEDOWN_SOFT_VIBRATION_INTERVAL = "ONFACEDOWN_SOFT_VIBRATION_INTERVAL";
	public static final String ONFACEDOWN_IS_WAKE_UP_DELAY_ENABLED = "ONFACEDOWN_IS_WAKE_UP_DELAY_ENABLED";
	public static final String ONFACEDOWN_WAKE_UP_DELAY = "ONFACEDOWN_WAKE_UP_DELAY";

	public static final String OUTOF_IS_OUT = "OUTOF_IS_OUT";
	public static final String OUTOF_IS_OUT_IS_CUSTOM_SOUND = "OUTOF_IS_OUT_IS_CUSTOM_SOUND";
	public static final String OUTOF_IS_OUT_CUSTOM_SOUND = "OUTOF_IS_OUT_CUSTOM_SOUND";
	public static final String OUTOF_IS_IN = "OUTOF_IS_IN";
	public static final String OUTOF_IS_IN_IS_CUSTOM_SOUND = "OUTOF_IS_IN_IS_CUSTOM_SOUND";
	public static final String OUTOF_IS_IN_CUSTOM_SOUND = "OUTOF_IS_IN_CUSTOM_SOUND";


	public static final PreferencesBox createPreferencesBox(Context context) {

		PreferencesBox preferencesBox = new PreferencesBox(context);

		preferencesBox.putToMap(new PreferencesBoxItem(IS_RUNNING, false));

		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEUP_IS_MUTE_RING_STREAM, false));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEUP_IS_MUTE_OTHER_AUDIO_STREAMS, false));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEUP_IS_DISABLE_VIBRATION, true));

		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_IS_MUTE_RING_STREAM, true));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_IS_MUTE_OTHER_AUDIO_STREAMS, false));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_IS_DISABLE_VIBRATION, true));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_IS_SOFT_VIBRATION_ENABLED, true));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_SOFT_VIBRATION_INTERVAL, 10));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_IS_WAKE_UP_DELAY_ENABLED, true));
		preferencesBox.putToMap(new PreferencesBoxItem(ONFACEDOWN_WAKE_UP_DELAY, 10));

		preferencesBox.putToMap(new PreferencesBoxItem(OUTOF_IS_OUT, false));
		preferencesBox.putToMap(new PreferencesBoxItem(OUTOF_IS_OUT_IS_CUSTOM_SOUND, false));
		preferencesBox.putToMap(new PreferencesBoxItem(OUTOF_IS_OUT_CUSTOM_SOUND,
				"content://settings/system/notification_sound"));
		preferencesBox.putToMap(new PreferencesBoxItem(OUTOF_IS_IN, false));
		preferencesBox.putToMap(new PreferencesBoxItem(OUTOF_IS_IN_IS_CUSTOM_SOUND, false));
		preferencesBox.putToMap(new PreferencesBoxItem(OUTOF_IS_IN_CUSTOM_SOUND,
				"content://settings/system/notification_sound"));

		return preferencesBox;

	}
}
