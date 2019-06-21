package com.goplay.gamesdk.widgets;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;

import com.goplay.gamesdk.R;

public class DialogManager {

	public static void showDialogWithItems(Context context, String message, String[] items, DialogInterface.OnClickListener listener) {
		// ==== modify by netmarble ====
		int theme = android.R.style.Theme_Holo_Light;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			theme = android.R.style.Theme_Material_Light;
		}
		// ===============================
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
		if (!TextUtils.isEmpty(message)) {
			builder.setTitle(message);
		}
		builder.setItems(items, listener);
		try {
			if (!((Activity) context).isFinishing()) {
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showConfirmDialog(Context context, int messageId, DialogInterface.OnClickListener listener) {
		// ==== modify by netmarble ====
		int theme = android.R.style.Theme_Holo_Light;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			theme = android.R.style.Theme_Material_Light;
		}
		// ===============================
		String message = context.getResources().getString(messageId);
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
		builder.setMessage(message);
		builder.setPositiveButton(R.string.ok, listener);
		builder.setNegativeButton(R.string.cancel, listener);
		builder.setCancelable(true); // ==== modify by netmarble ====
		try {
			if (!((Activity) context).isFinishing()) {
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showConfirmDialog(Context context, String title, CharSequence message, DialogInterface.OnClickListener listener) {
		// ==== modify by netmarble ====
		int theme = android.R.style.Theme_Holo_Light;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			theme = android.R.style.Theme_Material_Light;
		}
		// ===============================
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
//		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(R.string.ok, listener);
		builder.setNegativeButton(R.string.cancel, listener);
		builder.setCancelable(true); // ==== modify by netmarble ====
		try {
			if (!((Activity) context).isFinishing()) {
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showConfirmDialog(Context context, String title, CharSequence message, String possitveButton, String negativeButton, DialogInterface.OnClickListener listener) {
		// ==== modify by netmarble ====
		int theme = android.R.style.Theme_Holo_Light;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			theme = android.R.style.Theme_Material_Light;
		}
		// ===============================
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
		builder.setMessage(message);
		builder.setTitle(title);
		builder.setPositiveButton(possitveButton, listener);
		builder.setNegativeButton(negativeButton, listener);
		builder.setCancelable(true); // ==== modify by netmarble ====
		try {
			if (!((Activity) context).isFinishing()) {
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showPositiveDialog(Context context, String title, CharSequence message, DialogInterface.OnClickListener listener) {
		// ==== modify by netmarble ====
		int theme = android.R.style.Theme_Holo_Light;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			theme = android.R.style.Theme_Material_Light;
		}
		// ===============================
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
		builder.setTitle(title);
		builder.setMessage(message);
		if (listener == null) {
			listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
				}
			};
		}
		builder.setPositiveButton(R.string.ok, listener);
		builder.setCancelable(true); // ==== modify by netmarble ====
		try {
			if (!((Activity) context).isFinishing()) {
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void showScoinDialog(final Context context, String title, String message, DialogInterface.OnClickListener listener, DialogInterface.OnCancelListener cancel) {
		// ==== modify by netmarble ====
		int theme = android.R.style.Theme_Holo_Light;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			theme = android.R.style.Theme_Material_Light;
		}
		// ===============================
		AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, theme));
		builder.setTitle(title);
		builder.setMessage(message);
		if (listener == null) {
			listener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int arg1) {

					dialog.dismiss();
				}
			};
		}
		builder.setPositiveButton("Tải ứng dụng Scoin", listener);
		// builder.setOnDismissListener(new DialogInterface.OnDismissListener()
		// {
		//
		// @Override
		// public void onDismiss(DialogInterface dialog) {
		// // TODO Auto-generated method stub
		// Log.e("TAG", "DIsmiss");
		// }
		// });
		if (cancel != null)
			builder.setOnCancelListener(cancel);
		try {
			if (!((Activity) context).isFinishing()) {
				builder.show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		// builder.show();
	}

}