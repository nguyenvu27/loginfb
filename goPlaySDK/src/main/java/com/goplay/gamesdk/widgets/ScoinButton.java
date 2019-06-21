package com.goplay.gamesdk.widgets;

import java.util.Locale;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.utils.TypefaceUtils;

public class ScoinButton extends Button {

	public ScoinButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ScoinButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub

		boolean allCaps = false;
		TypedArray style = context.obtainStyledAttributes(attrs, R.styleable.ScoinButton, defStyle, 0);
		allCaps = style.getBoolean(R.styleable.ScoinButton_textUpperAll, false);
		style.recycle();
		// Framework impl also checks TextAppearance for textAllCaps. This isn't
		// needed for our
		// purposes so has been omitted.
		if (allCaps) {
			setTransformationMethod(new AllCapsTransformationMethod(context));
		}

		if (!isInEditMode())
			TypefaceUtils.setTypeface(attrs, this);
	}

	public ScoinButton(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
		// TODO Auto-generated constructor stub

		if (!isInEditMode())
			TypefaceUtils.setTypeface(attrs, this);
	}

	/**
	 * Transforms source text into an ALL CAPS string, locale-aware.
	 */
	private static class AllCapsTransformationMethod implements TransformationMethod {
		private final Locale mLocale;

		public AllCapsTransformationMethod(Context context) {
			mLocale = context.getResources().getConfiguration().locale;
		}

		@Override
		public CharSequence getTransformation(CharSequence source, View view) {
			return source != null ? source.toString().toUpperCase(mLocale) : null;
		}

		@Override
		public void onFocusChanged(View view, CharSequence charSequence, boolean b, int i, Rect rect) {
		}
	}
}
