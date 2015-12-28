package ru.romario.faceofring.ui;


import ru.romario.faceofring.R;
import ru.romario.faceofring.util.Log;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.RemoteViews.RemoteView;


/**
 * <p>
 * An image button displays an image that can be pressed, or clicked, by the
 * user.
 * </p>
 * 
 * <p>
 * <strong>XML attributes</strong>
 * </p>
 * <p>
 * See {@link android.R.styleable#ImageView Button Attributes},
 * {@link android.R.styleable#View View Attributes}
 * </p>
 */
@RemoteView
public class StartButton extends CompoundButton {

	private Drawable _buttonStart = null;
	private Drawable _buttonStop = null;
	private Drawable _buttonCurrent = null;

	private static final int NO_ALPHA = 0xFF;
	private float _disabledAlpha = 0.5f;


	public StartButton(Context context) {

		this(context, null);
		init();
	}


	public StartButton(Context context, AttributeSet attrs) {

		super(context, attrs);
		init();
	}


	public StartButton(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
		init();
	}


	private void init() {

		_buttonStart = getResources().getDrawable(R.drawable.button_start);
		_buttonStop = getResources().getDrawable(R.drawable.button_stop);

		setFocusable(true);
		setClickable(true);

		setChecked(false);

	}


	@Override
	protected boolean onSetAlpha(int alpha) {

		if (Log.DEBUG)
			Log.d("alfa = " + alpha);

		return true;
	}


	@Override
	public void setChecked(boolean checked) {

		super.setChecked(checked);

		if (Log.DEBUG)
			Log.d("cheched =  " + checked);

		setText("");

		if (checked) {
			setButtonDrawable(_buttonStop);
		} else {
			setButtonDrawable(_buttonStart);
		}
	}


	@Override
	protected void onFinishInflate() {

		super.onFinishInflate();

		if (Log.DEBUG)
			Log.d("");
	}


	@Override
	public void setButtonDrawable(Drawable d) {

		super.setButtonDrawable(d);

		this._buttonCurrent = d;

		if (Log.DEBUG)
			Log.d("d = " + d);
	}


	@Override
	protected void drawableStateChanged() {

		super.drawableStateChanged();

		if (Log.DEBUG)
			Log.d("");

		if (this._buttonCurrent == null) {
			return;
		}

		if (Log.DEBUG)
			Log.d("isPressed = " + isPressed());

		if (isChecked()) {
			_buttonCurrent.setAlpha(!isPressed() ? NO_ALPHA : (int) (NO_ALPHA * _disabledAlpha));
		} else {
			_buttonCurrent.setAlpha(!isPressed() ? NO_ALPHA : (int) (NO_ALPHA * _disabledAlpha));
		}

	}

}
