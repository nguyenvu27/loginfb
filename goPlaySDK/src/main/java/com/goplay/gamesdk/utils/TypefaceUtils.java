package com.goplay.gamesdk.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.goplay.gamesdk.R;

public class TypefaceUtils {
	private static final String TAG = TypefaceUtils.class.getSimpleName();
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String name) {
		synchronized (cache) {
			if (!cache.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(c.getAssets(), String.format("fonts/%s.OTF", name));
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}

	public static void setTypeface(AttributeSet attrs, TextView textView) {
		Context context = textView.getContext();
		Typeface typeface = null;
		TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.ScoinTextView);
		String typefaceName = "";

		try {
			typefaceName = values.getString(R.styleable.ScoinTextView_typeface);

		} finally {
			values.recycle();
		}

		if (typefaceName != null && !("").equalsIgnoreCase(typefaceName) && cache.containsKey(typefaceName)) {
			textView.setTypeface(cache.get(typefaceName));
		} else {
			try {

				/* 0x7f050007 */

				if (typefaceName != null && !("").equalsIgnoreCase(typefaceName)) {
					int id = context.getResources().getIdentifier(typefaceName, "raw", context.getPackageName());
					typeface = getFontFromRes(context, id);
				} else {
					typeface = getFontFromRes(context, R.raw.roboto_light);
					typefaceName = "roboto_light";
				}

			} catch (Exception e) {

				return;
			}
			if (typefaceName != null && typeface != null) {
				cache.put(typefaceName, typeface);
				textView.setTypeface(typeface);
			}
		}
	}

	public static Typeface fileStreamTypeface(Context context, int resource) {
		Typeface tf = null;

		InputStream is = context.getResources().openRawResource(resource);
		String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/gmg_underground_tmp";
		File f = new File(path);
		if ((!f.exists()) && (!f.mkdirs())) {
			return null;
		}

		String outPath = path + "/tmp.raw";
		try {
			byte[] buffer = new byte[is.available()];
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

			int l = 0;
			while ((l = is.read(buffer)) > 0) {
				bos.write(buffer, 0, l);
			}
			bos.close();

			tf = Typeface.createFromFile(outPath);

			File f2 = new File(outPath);
			f2.delete();
		} catch (IOException e) {
			return null;
		}

		return tf;
	}

	public static Typeface getFontFromRes(Context context, int resource) {
		Typeface tf = null;
		InputStream is = null;
		try {
			is = context.getResources().openRawResource(resource);
		} catch (NotFoundException e) {
			Log.e(TAG, "Could not find font in resources!");
		}

		String outPath = context.getCacheDir() + "/tmp" + System.currentTimeMillis() + ".raw";

		try {
			byte[] buffer = new byte[is.available()];
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outPath));

			int l = 0;
			while ((l = is.read(buffer)) > 0)
				bos.write(buffer, 0, l);

			bos.close();

			tf = Typeface.createFromFile(outPath);

			// clean up
			new File(outPath).delete();
		} catch (IOException e) {
			Log.e(TAG, "Error reading in font!");
			return null;
		}


		return tf;
	}
}
