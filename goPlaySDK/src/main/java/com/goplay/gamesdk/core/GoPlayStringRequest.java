package com.goplay.gamesdk.core;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.StringRequest;

import org.apache.http.protocol.HTTP;

import java.util.Map;

public class GoPlayStringRequest extends StringRequest {
	protected static final String TYPE_UTF8_CHARSET = "charset=UTF-8";
	private static final String TAG = GoPlayStringRequest.class.getSimpleName();
	private Map<String, String> params;

	public GoPlayStringRequest(int method, String url, Map<String, String> params, Listener<String> listener, ErrorListener errorListener) {
		super(method, url, listener, errorListener);
		// TODO Auto-generated constructor stub
		this.params = params;
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		try {
			if (response.data.length > 10000)
				setShouldCache(false);
			String type = response.headers.get(HTTP.CONTENT_TYPE);
			if (type == null) {
				Log.d(TAG, "content type was null");
				type = TYPE_UTF8_CHARSET;
				response.headers.put(HTTP.CONTENT_TYPE, type);
			} else if (!type.contains("UTF-8")) {
				Log.d(TAG, "content type had UTF-8 missing");
				type += ";" + TYPE_UTF8_CHARSET;
				response.headers.put(HTTP.CONTENT_TYPE, type);
			}
		} catch (Exception e) {
			// print stacktrace e.g.
		}
		return super.parseNetworkResponse(response);
	}

	/* For HTTPS */
	public Map<String, String> getParams() throws AuthFailureError {
		// Map map = new HashMap();
		// for (BasicNameValuePair pair : this.params) {
		// map.put(pair.getName(), pair.getValue());
		// }
		return params;
	}

}
