package ru.romario.faceofring;


import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import ru.romario.faceofring.util.Log;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class HelpActivity extends Activity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);

		if (Log.DEBUG)
			Log.d("");

		final String data = loadResToString(R.raw.help);
		final TextView textView = (TextView) findViewById(R.id.help_text);
		textView.append(data);
		textView.refreshDrawableState();

	}


	public String loadResToString(int resId) {

		try {
			InputStream is = getResources().openRawResource(resId);

			byte[] buffer = new byte[4096];
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			while (true) {
				int read = is.read(buffer);

				if (read == -1) {
					break;
				}

				baos.write(buffer, 0, read);
			}

			baos.close();
			is.close();

			String data = baos.toString();

			return data;
		} catch (Exception e) {
			return null;
		}

	}

}
