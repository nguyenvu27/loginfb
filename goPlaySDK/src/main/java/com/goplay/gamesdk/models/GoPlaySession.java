package com.goplay.gamesdk.models;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.goplay.gamesdk.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoPlaySession implements Parcelable {

	public String accessToken;
	public String refreshToken;
	public String expireDate;
	public String userId;
	public String userName;
	public String email;
	public int accountType;

	protected GoPlaySession(Parcel in) {
		accessToken = in.readString();
		refreshToken = in.readString();
		expireDate = in.readString();
		userId = in.readString();
		userName = in.readString();
		email = in.readString();
		accountType = in.readInt();
	}

	public GoPlaySession() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(accessToken);
		dest.writeString(refreshToken);
		dest.writeString(expireDate);
		dest.writeString(userId);
		dest.writeString(userName);
		dest.writeString(email);
		dest.writeInt(accountType);
	}

	public static final Parcelable.Creator<GoPlaySession> CREATOR = new Parcelable.Creator<GoPlaySession>() {
		@Override
		public GoPlaySession createFromParcel(Parcel in) {
			return new GoPlaySession(in);
		}

		@Override
		public GoPlaySession[] newArray(int size) {
			return new GoPlaySession[size];
		}
	};

	public static GoPlaySession deserialize(JSONObject data) {
		GoPlaySession session = null;
		try {
			if (data.has("data")) {
				data = data.getJSONObject("data");
			}
			session = new GoPlaySession();
			String accessToken = data.getString("access_token");
			String refreshToken = "";
			if (data.has("refresh_token")) {
				refreshToken = data.getString("refresh_token");
			}

			String expires = data.getString("expires_in");
			String username = "";
			if (data.has("username")) {
				username = data.getString("username");
			}

			if (data.has("username")) {

				String temp = data.getString("username");
				if (temp.startsWith("_f_")) {

					session.accountType = 1;
				}
//				else if (temp.startsWith("vtc_") || temp.startsWith("fb_") || temp.startsWith("gg_")) {
//					session.accountType = 2;
//				}
				else {
					session.accountType = 0;
				}
			}
			String userId = data.getString("user_id");
			if (("null").equals(userId))
				return null;
			if (data.has("email")) {
				session.email = data.getString("email");
			}
			session.accessToken = accessToken;
			session.refreshToken = refreshToken;
			session.expireDate = expires;
			session.userId = userId;
			session.userName = username;
//			if (data.has("hash")) {
//				String hash = data.getString("hash");
//				String input = accessToken + expires + refreshToken + userId + username;
//				String generatedHash = Utils.generateHashMD5(input);
//				if (TextUtils.equals(hash, generatedHash)) {
//					return session;
//				}
//				return null;
//			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return session;
	}

	public static String serialize(GoPlaySession user) {
		JSONObject json = new JSONObject();
		try {
			String userId = user.userId;
			String username = user.userName;
			String accessToken = user.accessToken;
			String refreshToken = user.refreshToken;
			String expires = user.expireDate;

			json.put("access_token", accessToken);
			json.put("refresh_token", refreshToken);
			json.put("expires_in", expires);
			json.put("username", username);
			json.put("user_id", userId);
			String input = accessToken + expires + refreshToken + userId + username;
			String hash = Utils.generateHashMD5(input);
			json.put("hash", hash);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public static List<GoPlaySession> getListSession(JSONObject json) {
		List<GoPlaySession> sessionList = new ArrayList<GoPlaySession>();
		try {
			JSONArray array = json.getJSONArray("update");

			for (int i = 0; i < array.length(); i++) {
				JSONObject obj = array.getJSONObject(i);
				GoPlaySession session = new GoPlaySession();
				session.userName = obj.getString("username");
				session.userId = obj.getString("user_id");
				JSONObject token = obj.getJSONObject("token");
				session.accessToken = token.getString("access_token");
				session.refreshToken = token.getString("refresh_token");
				session.expireDate = token.getString("expires_in");
				sessionList.add(session);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return sessionList;
	}

	public static GoPlaySession parseChecDevice(JSONObject data) {
		GoPlaySession session = null;
		try {

			session = new GoPlaySession();
			String accessToken = data.getString("AccessToken");
			String refreshToken = "";
			if (data.has("refresh_token")) {
				refreshToken = data.getString("refresh_token");
			}

			String expires = data.getString("Expires");
			String username = "";
			if (data.has("AccountName")) {
				username = data.getString("AccountName");
			}

			if (data.has("AccountName")) {

				String temp = data.getString("AccountName");
				if (temp.contains("fastlogin")) {

					session.accountType = 1;
				} else {
					session.accountType = 0;
				}
			}

			String userId = data.getString("AccountId");
			if (data.has("email")) {
				session.email = data.getString("email");
			}
			session.accessToken = accessToken;
			session.refreshToken = refreshToken;
			session.expireDate = expires;
			session.userId = userId;
			session.userName = username;
			if (data.has("hash")) {
				String hash = data.getString("hash");
				String input = accessToken + expires + refreshToken + userId + username;
				String generatedHash = Utils.generateHashMD5(input);
				if (TextUtils.equals(hash, generatedHash)) {
					return session;
				}
				return null;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return session;
	}

}