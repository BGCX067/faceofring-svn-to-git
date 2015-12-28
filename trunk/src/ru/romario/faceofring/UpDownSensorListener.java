package ru.romario.faceofring;


import ru.romario.faceofring.util.Log;
import android.hardware.SensorListener;


public class UpDownSensorListener implements SensorListener {

	private static final int DEGREE_ERROR = 20;
	private static final int DEGREE_ERROR_S = DEGREE_ERROR * DEGREE_ERROR;
	private static final int MAX_AVERAGE_LENGTH = 20;
	private static final int MIN_OF_STATISTICS_DATA = MAX_AVERAGE_LENGTH / 2;

	public static final int STATE_DEFAULT = 0;
	public static final int STATE_FACE_UP = 1;
	public static final int STATE_FACE_DOWN = 2;
	public static final int STATE_UNKNOWN = 3;

	public static final int ACCURACY_BAD = 0;
	public static final int ACCURACY_GOOD = 1;
	public static final int ACCURACY_EXCELLENT = 2;

	private PhoneEventsHandler _phoneEventsHandler;
	
	private int _lastDeviationLengthCursor = 0;
	private float _lastDeviationLength[][] = new float[MAX_AVERAGE_LENGTH][4];
	private double _lastVectors[][] = new double[MAX_AVERAGE_LENGTH][4];

	private int _lastPhoneState = -1;
	private boolean _isScreenOn = true;


	protected UpDownSensorListener(PhoneEventsHandler phoneEventsHandler) {

		_phoneEventsHandler = phoneEventsHandler;

		dropStatistics();

	}


	public void onSensorChanged(int sensor, float[] values) {

		// Log.d(TAG, "onSensorChanged " + sensor + " " + values[1] + " " +
		// values[2]);

		synchronized (_phoneEventsHandler) {

			// Log.d(TAG, "onSensorChanged " + sensor + " " + values[1] + " " +
			// values[2]);

			// Log.d(TAG,
			// "onSensorChanged.preferencesBox.isSetingsOtherIgnoreSoftShake() "
			// + preferencesBox.isSetingsOtherIgnoreSoftShake());

			float accuracy = analizeValues(values);
			// Log.d(TAG, "deviation = " + deviation);

			int accuracyState = calculateAccuracyState(accuracy);

			int phoneState;
			phoneState = calculatePhoneState(values, accuracyState);

			// Log.d(TAG, "onSensorChanged " + values[1] + " " + values[2] + "
			// accuracy = " + accuracy
			// + " lastDeviationLengthCursor = " + lastDeviationLengthCursor + "
			// phoneState = " + phoneState + " accuracyState " + accuracyState);

			if (phoneState == _lastPhoneState) {
				return;
			}

			if (!calculateAverageDegrees(values)) {
				return;
			}

			phoneState = calculatePhoneState(values, accuracyState);

			// Log.d(TAG, "onSensorChanged " + values[1] + " " + values[2] + "
			// phoneState = " + phoneState + " lastPhoneState = " +
			// lastPhoneState);

			if (phoneState == _lastPhoneState) {
				return;
			}

			_lastPhoneState = phoneState;

			switch (phoneState) {
				case STATE_DEFAULT: {
					_phoneEventsHandler.onDefaultState(accuracyState);
					return;
				}
				case STATE_FACE_DOWN: {
					_phoneEventsHandler.onFaceDownState(accuracyState);
					return;
				}
				case STATE_FACE_UP: {
					_phoneEventsHandler.onFaceUpState(accuracyState);
					return;
				}
				case STATE_UNKNOWN: {
					_phoneEventsHandler.onUnknownState(accuracyState);
					return;
				}
			}

		}
	}


	private int calculateAccuracyState(float accuracy) {

		if (accuracy < DEGREE_ERROR) {
			return ACCURACY_EXCELLENT;
		}

		if (accuracy < DEGREE_ERROR_S) {
			return ACCURACY_GOOD;
		}

		return ACCURACY_BAD;
	}


	private int calculatePhoneState(float[] values, int accuracyState) {

		final float absoluteValue2 = (values[2] < 0) ? -values[2] : values[2];

		if (absoluteValue2 >= DEGREE_ERROR) {
			// phone is not lying

			if (accuracyState == ACCURACY_GOOD || accuracyState == ACCURACY_EXCELLENT) {
				return STATE_DEFAULT;
			}

			return STATE_UNKNOWN;
		}

		final float absoluteValue1 = (values[1] < 0) ? -values[1] : values[1];

		if (absoluteValue1 < DEGREE_ERROR) {
			return STATE_FACE_UP;
		}

		if (absoluteValue1 < 180 + DEGREE_ERROR && absoluteValue1 > 180 - DEGREE_ERROR) {
			return STATE_FACE_DOWN;
		}

		if (accuracyState == ACCURACY_GOOD || accuracyState == ACCURACY_EXCELLENT) {
			return STATE_DEFAULT;
		}

		return STATE_UNKNOWN;
	}


	private synchronized boolean calculateAverageDegrees(float[] values) {

		double vector1X = 0;
		double vector1Y = 0;

		double vector2X = 0;
		double vector2Y = 0;

		int i = 0;
		for (; i < MAX_AVERAGE_LENGTH; i++) {

			if (_lastDeviationLength[i][0] < -1000) {
				break;
			}

			vector1X += _lastVectors[i][0];
			vector1Y += _lastVectors[i][1];

			vector2X += _lastVectors[i][2];
			vector2Y += _lastVectors[i][3];
		}

		if (i < MIN_OF_STATISTICS_DATA) {
			return false;
		}

		double R1R1 = vector1X * vector1X + vector1Y * vector1Y;
		double R2R2 = vector2X * vector2X + vector2Y * vector2Y;

		if (R1R1 == 0 || R2R2 == 0) {
			// Undefined result;
			if (Log.DEBUG) {
				Log.d("Undefined result in average degree process");
			}

			return false;
		}

		double degree1 = Math.acos(vector1X / Math.sqrt(R1R1));

		if (vector1Y < 0) {
			degree1 = -degree1;
		}

		values[1] = (float) Math.toDegrees(degree1);

		double degree2 = Math.acos(vector2X / Math.sqrt(R2R2));

		if (vector2Y < 0) {
			degree2 = -degree2;
		}

		values[2] = (float) Math.toDegrees(degree2);

		return true;

	}


	private synchronized float analizeValues(float[] values) {

		int lastVLC = _lastDeviationLengthCursor - 1;

		if (lastVLC < 0 && _lastDeviationLength[MAX_AVERAGE_LENGTH - 1][0] < -1000) {

			_lastDeviationLength[_lastDeviationLengthCursor][0] = 0;
			_lastDeviationLength[_lastDeviationLengthCursor][1] = 0;

		} else {
			if (lastVLC < 0) {

				lastVLC = MAX_AVERAGE_LENGTH - 1;

			}

			_lastDeviationLength[_lastDeviationLengthCursor][0] = getDelta(_lastDeviationLength[lastVLC][2], values[1]);
			_lastDeviationLength[_lastDeviationLengthCursor][1] = getDelta(_lastDeviationLength[lastVLC][3], values[2]);
		}

		_lastDeviationLength[_lastDeviationLengthCursor][2] = values[1];
		_lastDeviationLength[_lastDeviationLengthCursor][3] = values[2];

		double degree;

		degree = Math.toRadians(values[1]);
		_lastVectors[_lastDeviationLengthCursor][0] = Math.cos(degree);
		_lastVectors[_lastDeviationLengthCursor][1] = Math.sin(degree);

		degree = Math.toRadians(values[2]);
		_lastVectors[_lastDeviationLengthCursor][2] = Math.cos(degree);
		_lastVectors[_lastDeviationLengthCursor][3] = Math.sin(degree);

		_lastDeviationLengthCursor++;

		if (_lastDeviationLengthCursor == MAX_AVERAGE_LENGTH) {
			_lastDeviationLengthCursor = 0;
		}

		float sum1 = 0;
		float sum2 = 0;

		for (int i = 0; i < MAX_AVERAGE_LENGTH; i++) {

			if (_lastDeviationLength[i][0] < -1000) {
				break;
			}
			sum1 += _lastDeviationLength[i][0];
			sum2 += _lastDeviationLength[i][1];
		}

		return sum1 + sum2;

	}


	private float getDelta(float value1, float value2) {

		float result = Math.abs(value1 - value2);

		if (result > 180) {
			result = 360 - result;
		} else {
			if (result < -180) {
				result = 360 + result;
			}

		}

		return result * result;
	}


	protected synchronized void dropStatistics() {

		if (_isScreenOn) {
			return;
		}

		if (Log.DEBUG)
			Log.d("");

		_lastDeviationLengthCursor = 0;

		for (int i = 0; i < MAX_AVERAGE_LENGTH; i++) {
			_lastDeviationLength[i][0] = -10000;
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


	@Override
	public void onAccuracyChanged(int sensor, int accuracy) {

		// Log.d(TAG, "onAccuracyChanged " + sensor + " " + accuracy);

	}

}
