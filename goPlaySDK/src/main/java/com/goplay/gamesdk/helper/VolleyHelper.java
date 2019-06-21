package com.goplay.gamesdk.helper;

import android.content.Context;
import android.graphics.Bitmap.CompressFormat;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.goplay.gamesdk.core.DiskLruImageCache;

public class VolleyHelper {
	private static VolleyHelper defaultInstance;
	private static RequestQueue mRequestQueue;
	private static ImageLoader mImageLoader;
	private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
	private static Context ctx;

//	public static void init(Context context) {
//
//		/*
//		 * mRequestQueue = Volley.newRequestQueue(context, new
//		 * ExtHttpClientStack(new SslHttpClient(Util.getKeyStore(context),
//		 * "123465", 443)));
//		 */
//		// mRequestQueue = Volley.newRequestQueue(context, new
//		// ExtHttpClientStack(new DefaultHttpClient()));
//		ctx = context.getApplicationContext();
//		mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
//		mImageLoader = new ImageLoader(mRequestQueue, new DiskLruImageCache(context, "photos", DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, 100));
//		// mImageLoader = new ImageLoader(mRequestQueue, new BitmapLruCache());
//	}

	public static VolleyHelper getInstance(Context ctx) {
		if (defaultInstance == null) {
			synchronized (VolleyHelper.class) {
				if (defaultInstance == null) {
					defaultInstance = new VolleyHelper(ctx);
				}
			}
		}
		return defaultInstance;
	}

	public VolleyHelper(Context ctx) {
		mRequestQueue = Volley.newRequestQueue(ctx);
		mImageLoader = new ImageLoader(mRequestQueue, new DiskLruImageCache(ctx, "photos", DISK_IMAGECACHE_SIZE, DISK_IMAGECACHE_COMPRESS_FORMAT, 100));
	}

	public static RequestQueue getRequestQueue() {
		if (mRequestQueue != null) {
			return mRequestQueue;
		}
		// else {
		// return Volley.newRequestQueue(ctx);
		// }
		throw new IllegalStateException("RequestQueue not initialized");
	}

	public static ImageLoader getImageLoader() {
		if (mImageLoader != null) {
			return mImageLoader;
		}
		throw new IllegalStateException("ImageLoader not initialized");
	}
}