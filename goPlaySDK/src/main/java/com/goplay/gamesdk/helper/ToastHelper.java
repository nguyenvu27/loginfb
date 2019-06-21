package com.goplay.gamesdk.helper;

import android.content.Context;
import android.widget.Toast;

import com.goplay.gamesdk.R;

public class ToastHelper {
	public static void showToastError(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showToastError(Context context, int errorCode) {
		Toast.makeText(context, String.format(context.getString(R.string.error_message), new Object[] { Integer.valueOf(errorCode) }), Toast.LENGTH_LONG).show();
	}

}
