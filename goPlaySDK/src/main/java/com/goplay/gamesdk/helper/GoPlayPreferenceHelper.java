package com.goplay.gamesdk.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.goplay.gamesdk.core.GoPlaySecuredPrefs;
import com.goplay.gamesdk.models.GoPlaySession;

public class GoPlayPreferenceHelper {

	private SharedPreferences preferences;
	private SharedPreferences.Editor editor;
	private static GoPlayPreferenceHelper defaultInstance;

	public static GoPlayPreferenceHelper getInstance() {
		if (defaultInstance == null) {
			synchronized (GoPlayPreferenceHelper.class) {
				if (defaultInstance == null) {
					defaultInstance = new GoPlayPreferenceHelper();
				}
			}
		}
		return defaultInstance;
	}

	public GoPlayPreferenceHelper init(Context context) {
		if (this.preferences == null) {
			this.preferences = new GoPlaySecuredPrefs(context);
		}
		if (this.editor == null) {
			this.editor = this.preferences.edit();
		}
		return this;
	}

	public synchronized void setUserDeviceInfo(String json) {
		this.editor.putString("com.goplay.gamesdk.user", json);
		this.editor.commit();
	}

	public synchronized String getUserDeviceInfo() {
		return this.preferences.getString("com.goplay.gamesdk.user", "");
	}

	public synchronized void setSession(GoPlaySession session) {
		this.editor.putString("com.goplay.gamesdk.pref.accesstoken", session.accessToken);
		this.editor.putString("com.goplay.gamesdk.pref.refreshtoken", session.refreshToken);
		this.editor.putString("com.goplay.gamesdk.pref.expire", session.expireDate);
		this.editor.putString("com.goplay.gamesdk.pref.userid", session.userId);
		this.editor.putString("com.goplay.gamesdk.pref.username", session.userName);
		this.editor.putInt("com.goplay.gamesdk.pref.accountype", session.accountType);
		this.editor.putBoolean("com.goplay.gamesdk.pref.login", true);
		this.editor.commit();
	}

	public synchronized void clear() {
		this.editor.putString("com.goplay.gamesdk.pref.accesstoken", "");
		this.editor.putString("com.goplay.gamesdk.pref.reftoken", "");
		this.editor.putString("com.goplay.gamesdk.pref.expire", "");
		this.editor.putString("com.goplay.gamesdk.pref.userid", "");
		this.editor.putString("com.goplay.gamesdk.pref.username", "");
		this.editor.putInt("com.goplay.gamesdk.pref.accountype", -1);
		this.editor.putBoolean("com.goplay.gamesdk.pref.login", false);
		this.editor.commit();
	}

	public synchronized String getAgencyId() {
		return this.preferences.getString("com.goplay.gamesdk.agencyId", "");
	}

	public synchronized void setAgencyId(String agencyId) {
		this.editor.putString("com.goplay.gamesdk.agencyId", agencyId);
		this.editor.commit();
	}

	public synchronized String getGAId() {
		return this.preferences.getString("com.goplay.gamesdk.gaId", "");
	}

	public synchronized void setGAId(String gaId) {
		this.editor.putString("com.goplay.gamesdk.gaId", gaId);
		this.editor.commit();
	}

	public synchronized String getUserId() {
		return this.preferences.getString("com.goplay.gamesdk.pref.userid", "");
	}

	public synchronized String getUserName() {
		return this.preferences.getString("com.goplay.gamesdk.pref.username", "");
	}

	public synchronized String getDeviceId() {
		// TODO Auto-generated method stub
		return this.preferences.getString("com.goplay.gamesdk.did", "");
	}

	public synchronized void setDeviceId(String deviceId) {
		this.editor.putString("com.goplay.gamesdk.did", deviceId);
		this.editor.commit();

	}

	public synchronized void setEnableLogging(boolean isEnableLogging) {
		this.editor.putBoolean("com.goplay.gamesdk.pref.enablelog", isEnableLogging);
		this.editor.commit();
	}

	public synchronized boolean isEnableLogging() {
		return this.preferences.getBoolean("com.goplay.gamesdk.pref.enablelog", false);
	}

	public synchronized void setApiKey(String apiKey) {
		this.editor.putString("com.goplay.gamesdk.pref.apikey", apiKey);
		this.editor.commit();
	}

	public synchronized String getApiKey() {
		return this.preferences.getString("com.goplay.gamesdk.pref.apikey", "");
	}



	public synchronized void setSandboxApiKey(String sandboxApiKey) {
		this.editor.putString("com.goplay.gamesdk.pref.sb_apikey", sandboxApiKey);
		this.editor.commit();
	}

	public synchronized String getSandboxApiKey() {
		return this.preferences.getString("com.goplay.gamesdk.pref.sb_apikey", "");
	}


	public synchronized String getAccessToken() {
		return this.preferences.getString("com.goplay.gamesdk.pref.accesstoken", "");
	}

	public synchronized int getAccountType() {
		return this.preferences.getInt("com.goplay.gamesdk.pref.accountype", -1);
	}





	public synchronized void setAutoLogin(boolean isAutoLogin) {
		this.editor.putBoolean("com.goplay.gamesdk.pref.auto.login", isAutoLogin);
		this.editor.commit();
	}

	public synchronized boolean isAutoLogin() {
		return this.preferences.getBoolean("com.goplay.gamesdk.pref.auto.login", true);
	}


	public synchronized String getUserPhone() {
		return this.preferences.getString("com.goplay.gamesdk.pref.phone", "");
	}

	public synchronized void setUserFbEmail(String fbEmail) {
		this.editor.putString("com.goplay.gamesdk.pref.email.fb", fbEmail);
		this.editor.commit();
	}

	public synchronized String getUserFbEmail() {
		return this.preferences.getString("com.goplay.gamesdk.pref.email.fb", "");
	}

	public synchronized void setUserGGEmail(String fbEmail) {
		this.editor.putString("com.goplay.gamesdk.pref.email.gg", fbEmail);
		this.editor.commit();
	}

	public synchronized String getUserGGEmail() {
		return this.preferences.getString("com.goplay.gamesdk.pref.email.gg", "");
	}

	public synchronized void setForgotPW(String link) {
		this.editor.putString("com.goplay.gamesdk.pref.forgot.pw.link", link);
		this.editor.commit();
	}

	public synchronized String getForgotPW() {
		return this.preferences.getString("com.goplay.gamesdk.pref.forgot.pw.link", "");
	}
}
