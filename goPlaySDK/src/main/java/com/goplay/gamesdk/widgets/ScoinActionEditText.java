package com.goplay.gamesdk.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.utils.TypefaceUtils;

public class ScoinActionEditText extends EditText {

	private EdittextActionListener listener;
	private Drawable mActionIcon = getCompoundDrawables()[2];
	private int actionIconId = R.drawable.ic_next;

	public ScoinActionEditText(Context context) {
		super(context);
	}

	public ScoinActionEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (!isInEditMode())
			TypefaceUtils.setTypeface(attrs, this);
	}

	public ScoinActionEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		if (!isInEditMode())
			TypefaceUtils.setTypeface(attrs, this);
	}

	public void setListener(EdittextActionListener listener) {
		this.listener = listener;
	}

	public int getActionIconId() {
		return actionIconId;
	}

	public void setActionIconId(int actionIconId) {
		this.actionIconId = actionIconId;
		mActionIcon = getCompoundDrawables()[2];
		Drawable[] temp = this.getCompoundDrawables();
		Drawable edit = getResources().getDrawable(actionIconId);
		this.setCompoundDrawablesWithIntrinsicBounds(temp[0], temp[1], edit, temp[3]);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {

		if (getCompoundDrawables()[2] != null&& mActionIcon!=null) {
			boolean tappedX = paramMotionEvent.getX() > (getWidth() - getPaddingRight() - mActionIcon.getIntrinsicWidth());
			if (tappedX) {
				if (paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
					// setText("");
					if (listener != null) {
						listener.onActionClick(ScoinActionEditText.this);
					}
				}
				return true;
			}
		}

		return super.dispatchTouchEvent(paramMotionEvent);
	}
}
