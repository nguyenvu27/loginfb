package com.goplay.gamesdk.models;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class GoogleSession implements Parcelable {

	public String picture;
	public String id;
	public String gender;
	public String name;
	public String email;

	public GoogleSession() {
		super();
		// TODO Auto-generated constructor stub
	}

	protected GoogleSession(Parcel in) {
		picture = in.readString();
		id = in.readString();
		gender = in.readString();
		name = in.readString();
		email = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(picture);
		dest.writeString(id);
		dest.writeString(gender);
		dest.writeString(name);
		dest.writeString(email);
	}

	public static final Parcelable.Creator<GoogleSession> CREATOR = new Parcelable.Creator<GoogleSession>() {
		@Override
		public GoogleSession createFromParcel(Parcel in) {
			return new GoogleSession(in);
		}

		@Override
		public GoogleSession[] newArray(int size) {
			return new GoogleSession[size];
		}
	};

	public static GoogleSession deserialize(JSONObject data) {
		GoogleSession session = null;
		try {

			session = new GoogleSession();
			// String picture = "";
			if (data.has("picture")) {
				session.picture = data.getString("picture");
			}
//			String id = "";
			if (data.has("id")) {
				session.id = data.getString("id");
			}

			// String email = data.getString("email");
			if (data.has("email")) {
				session.email = data.getString("email");
			}
			// String name = "";
			if (data.has("name")) {
				session.name = data.getString("name");
			}
			// String gender = data.getString("gender");
			if (data.has("gender")) {
				session.gender = data.getString("gender");
			}
			// session.picture = picture;
			// session.id = id;
			// session.gender = gender;
			// session.name = name;
			// session.email = email;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return session;
	}

	// public static String serialize(GoogleSession user) {
	// JSONObject json = new JSONObject();
	// try {
	// String userId = user.userId;
	// String username = user.userName;
	// String accessToken = user.accessToken;
	// String refreshToken = user.refreshToken;
	// String expires = user.expireDate;
	//
	// json.put("access_token", accessToken);
	// json.put("refresh_token", refreshToken);
	// json.put("expires_in", expires);
	// json.put("username", username);
	// json.put("user_id", userId);
	// String input = accessToken + expires + refreshToken + userId + username;
	// String hash = Utils.generateHashMD5(input);
	// json.put("hash", hash);
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// return json.toString();
	// }
}
