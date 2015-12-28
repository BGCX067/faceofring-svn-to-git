package ru.romario.faceofring;


import ru.romario.android.prefs.PreferencesBox;
import ru.romario.faceofring.util.Log;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;


public class Main extends Activity implements View.OnClickListener {

	private static final String INFO_STARTED = "Service is running...";
	private static final String INFO_STOPED = "Service is stopped.";

	public static final int MENU_SETTINGS = 1;
	public static final int MENU_HELP = 2;

	protected static final Intent INTEND_FOREGRAUND_SERVICE = new Intent(ForegroundService.class.getName());
	private static Bitmap _bmpImageLogo = null;
	private static Bitmap _bmpBackgroundV = null;
	private static Bitmap _bmpBackgroundH = null;

	// TODO through PreferenceBox
	private PreferencesBox _preferencesBox;

	boolean isServiceRunning = false;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		final CompoundButton button = (CompoundButton) findViewById(R.id.button_toggle);
		_preferencesBox = Prefs.createPreferencesBox(this.getApplicationContext());

		final ServiceConnection serviceConnection = new ServiceConnection() {

			public void onServiceConnected(ComponentName componentname, IBinder ibinder) {

				isServiceRunning = true;

				if (Log.DEBUG) {
					Log.d("");
				}

				button.setChecked(isServiceRunning);
				TextView infoText = (TextView) findViewById(R.id.main_info_text);
				// TODO get text from resources
				infoText.setText(INFO_STARTED);

			}


			public void onServiceDisconnected(ComponentName componentname) {

			}

		};

		try {

			Context appContext = getApplicationContext();
			if (appContext.bindService(new Intent(INTEND_FOREGRAUND_SERVICE), serviceConnection, 0)) {
				appContext.unbindService(serviceConnection);
			}

		} catch (SecurityException ex) {
			Log.e("Cannot bind to service", ex);
		}

		button.setOnClickListener(this);

		// BitmapDrawable drawable = (BitmapDrawable)
		// getResources().getDrawable(R.drawable.background_v);
		// LayerDrawable
		// PaintDrawables

		// canvas.drawBitmap(image, screenCoords[0], screenCoords[1], paint2);

		// TODO
		// http://android-developers.blogspot.com/2009/02/faster-screen-orientation-change.html
		if (_bmpImageLogo == null) {
			_bmpImageLogo = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		}

		final int orientation = getResources().getConfiguration().orientation;

		if (orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (_bmpBackgroundV == null) {
				_bmpBackgroundV = BitmapFactory.decodeResource(getResources(), R.drawable.background_v);
			}
		} else {
			if (_bmpBackgroundH == null) {
				_bmpBackgroundH = BitmapFactory.decodeResource(getResources(), R.drawable.background_h);
			}
		}

		final BitmapDrawable bitmapDrawable = new BitmapDrawable() {

			@Override
			public void draw(Canvas canvas) {

				Bitmap background = null;

				if (orientation == Configuration.ORIENTATION_PORTRAIT) {
					background = _bmpBackgroundV;
				} else {
					background = _bmpBackgroundH;
				}

				int canvasWidth = canvas.getWidth();
				int imageWidth = background.getWidth();

				int cursor = 0;
				while (cursor < canvasWidth) {
					canvas.drawBitmap(background, cursor, 0, null);
					cursor += imageWidth;
				}

				canvas.drawBitmap(_bmpImageLogo, 165, (orientation == Configuration.ORIENTATION_PORTRAIT) ? 70 : 40,
						null);

				// Log.d(TAG, "DRAW!!!! image.getWidth(); = " +
				// backgroundV.getWidth() + " canvas.getWidth() = "
				// + canvas.getWidth());
			}
		};

		final View mainView = (View) findViewById(R.id.layout_main);
		mainView.setBackgroundDrawable(bitmapDrawable);

		final Button buttonSettings = (Button) findViewById(R.id.button_settings);
		buttonSettings.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// startActivity(new Intent(Main.this, SettingsActivity.class));
				startActivity(new Intent(Main.this, AppPreferenceActivity.class));

			}

		});

		final Button buttonHelp = (Button) findViewById(R.id.button_help);
		buttonHelp.setOnClickListener(new Button.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				startActivity(new Intent(Main.this, HelpActivity.class));
			}

		});

	}


	@Override
	public void onClick(View view) {

		if (Log.DEBUG)
			Log.d("");

		if (!(view instanceof CompoundButton)) {
			return;
		}

		final CompoundButton button = (CompoundButton) view;

		final boolean isChecked = button.isChecked();

		_preferencesBox.setValue(Prefs.IS_RUNNING, isChecked);

		Context appContext = getApplicationContext();

		if (isChecked) {
			appContext.startService(INTEND_FOREGRAUND_SERVICE);
			TextView infoText = (TextView) findViewById(R.id.main_info_text);

			// TODO get text from resources
			infoText.setText(INFO_STARTED);

		} else {
			appContext.stopService(INTEND_FOREGRAUND_SERVICE);
			TextView infoText = (TextView) findViewById(R.id.main_info_text);

			// TODO get text from resources
			infoText.setText(INFO_STOPED);

			isServiceRunning = false;

		}

		if (Log.DEBUG)
			Log.d("-");

	}

}
