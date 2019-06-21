package com.goplay.gamesdk.core;

import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class goPlayConfig {
    private boolean isAutoUpdate;
    private List<String> loginMethods;
    private String noticeUrl;
    private String rating;
    private String reminder;
    private String appStatus;
    private boolean showFloatButton;
    private boolean showLog;

    private String forgotPwLink = "";

    public String getForgotPwLink() {
        return forgotPwLink;
    }

    public void setForgotPwLink(String forgotPwLink) {
        this.forgotPwLink = forgotPwLink;
    }

    public String getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(String appStatus) {
        this.appStatus = appStatus;
    }

    public boolean isShowLog() {
        return showLog;
    }

    public void setShowLog(boolean showLog) {
        this.showLog = showLog;
    }

    public boolean isShowFloatButton() {
        return showFloatButton;
    }

    public void setShowFloatButton(boolean showFloatButton) {
        this.showFloatButton = showFloatButton;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public String getNoticeUrl() {
        return this.noticeUrl;
    }

    public void setNoticeUrl(String noticeUrl) {
        this.noticeUrl = noticeUrl;
    }

    public boolean isAutoCheckUpdate() {
        return this.isAutoUpdate;
    }

    public void setAutoCheckUpdate(boolean isAutoCheckUpdate) {
        this.isAutoUpdate = isAutoCheckUpdate;
    }

    public List<String> getLoginMethods() {
        return this.loginMethods;
    }

    public void setLoginMethods(List<String> loginMethods) {
        this.loginMethods = loginMethods;
    }


    public static goPlayConfig parseConfiguration(JSONObject json) {
        goPlayConfig config = new goPlayConfig();
        try {
            if (json.has("data")) {
                json = json.getJSONObject("data");
            }
            config.setForgotPwLink(json.optString("urlforgetpassword"));
            if (json.has("checkUpdate"))
                config.setAutoCheckUpdate(json.getBoolean("checkUpdate"));
            if (json.has("rating"))
                config.setRating(json.getString("rating"));

            if (json.has("reminder"))
                config.setReminder(json.getString("reminder"));
            if (json.has("btnprofile")) {
                if (TextUtils.equals("hide", json.getString("btnprofile"))) {
                    config.setShowFloatButton(false);
                } else if (TextUtils.equals("show", json.getString("btnprofile"))) {
                    config.setShowFloatButton(true);
                }
            }
            if (json.has("app_status")) {
                config.setAppStatus(json.getString("app_status"));
                if (TextUtils.equals("InReview", json.getString("app_status"))) {
                    config.setShowFloatButton(false);
                }
            }

            if (json.has("showLog"))
                config.setShowLog(json.getBoolean("showLog"));
            if (json.has("loginMethods")) {
                JSONArray loginMethodArray = json.getJSONArray("loginMethods");

                List<String> loginMethods = new ArrayList<String>();
                int len = loginMethodArray.length();
                for (int i = 0; i < len; i++) {
                    String loginMethod = loginMethodArray.getString(i);
                    loginMethods.add(loginMethod);
                }
                config.setLoginMethods(loginMethods);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return config;
    }
}