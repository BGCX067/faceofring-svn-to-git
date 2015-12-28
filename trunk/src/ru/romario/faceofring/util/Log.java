package ru.romario.faceofring.util;


public class Log {

	public static final boolean DEBUG = false;

	private static final boolean shortClassName = true;
	private static final String TAG = ">>>>>>> ";


	public static void d(String msg) {

		android.util.Log.d(TAG, "" + getContextStr(2) + (msg == null ? "" : ": " + msg));
	}


	public static void d() {

		android.util.Log.d(TAG, "" + getContextStr(2));
	}


	public static void e(String msg, Throwable ex) {

		android.util.Log.e(TAG, "" + getContextStr(2) + (msg == null ? "" : ": " + msg) + " :: " + ex.getMessage(), ex);
	}


	public static String getContextStr(int deep) {

		Exception ex = new Exception();

		int i = 0;
		for (StackTraceElement elem : ex.getStackTrace()) {
			if (i == deep) {
				String className = elem.getClassName();
				if (shortClassName) {
					int k = className.lastIndexOf(".");
					if (k != -1) {
						className = className.substring(k);
					}
				}
				return className + "." + elem.getMethodName() + "(" + elem.getLineNumber() + ")";
			}
			i++;
		}

		return null;
	}


	public static void main(String[] args) {

		System.err.println(getContextStr(1));
	}
}
