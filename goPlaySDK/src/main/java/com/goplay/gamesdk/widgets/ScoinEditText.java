package com.goplay.gamesdk.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

import com.goplay.gamesdk.utils.TypefaceUtils;

public class ScoinEditText extends EditText {

	public ScoinEditText(Context context) {
		super(context);
	}

	public ScoinEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			TypefaceUtils.setTypeface(attrs, this);
	}

	public ScoinEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (!isInEditMode())
			TypefaceUtils.setTypeface(attrs, this);
	}

}
