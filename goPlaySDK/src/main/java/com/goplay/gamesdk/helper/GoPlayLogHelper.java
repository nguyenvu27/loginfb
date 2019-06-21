package com.goplay.gamesdk.helper;

import android.content.Context;
import android.util.Log;


public class GoPlayLogHelper {
	private boolean isEnableLogging = false;

	public GoPlayLogHelper(Context context) {
		GoPlayPreferenceHelper preferenceHelper = GoPlayPreferenceHelper.getInstance().init(context);
		this.isEnableLogging = preferenceHelper.isEnableLogging();
	}

	public void verbose(String tag, String message) {
		if (this.isEnableLogging)
			Log.v(tag, message);
	}

	public void debug(String tag, String message) {
		if (this.isEnableLogging)
			Log.d(tag, message);
	}

	public void info(String tag, String message) {
		if (this.isEnableLogging)
			Log.i(tag, message);
	}

	public void error(String tag, String message) {
		if (this.isEnableLogging)
			Log.e(tag, message);
	}

}