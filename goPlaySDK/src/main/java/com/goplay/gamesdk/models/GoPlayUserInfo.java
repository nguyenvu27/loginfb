package com.goplay.gamesdk.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class GoPlayUserInfo implements Parcelable {

    public String userId;
    public String userName;
    public String email;
    public String mobile;
    public String passport;
    public String facebookEmail;
    public String googleEmail;
    public Boolean isForeigner;
    public Boolean isOpenID;
    public Boolean isLock = false;

    protected GoPlayUserInfo(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        email = in.readString();
        mobile = in.readString();
        passport = in.readString();
        facebookEmail = in.readString();
        googleEmail = in.readString();
        byte isForeignerVal = in.readByte();
        isForeigner = isForeignerVal == 0x02 ? null : isForeignerVal != 0x00;
        byte isOpenIDVal = in.readByte();
        isOpenID = isOpenIDVal == 0x02 ? null : isOpenIDVal != 0x00;
        byte isLockVal = in.readByte();
        isLock = isLockVal == 0x02 ? null : isLockVal != 0x00;
    }

    public GoPlayUserInfo() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(passport);
        dest.writeString(facebookEmail);
        dest.writeString(googleEmail);
        if (isForeigner == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isForeigner ? 0x01 : 0x00));
        }
        if (isOpenID == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isOpenID ? 0x01 : 0x00));
        }

        if (isLock == null) {
            dest.writeByte((byte) (0x02));
        } else {
            dest.writeByte((byte) (isLock ? 0x01 : 0x00));
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<GoPlayUserInfo> CREATOR = new Parcelable.Creator<GoPlayUserInfo>() {
        @Override
        public GoPlayUserInfo createFromParcel(Parcel in) {
            return new GoPlayUserInfo(in);
        }

        @Override
        public GoPlayUserInfo[] newArray(int size) {
            return new GoPlayUserInfo[size];
        }
    };

    public static GoPlayUserInfo deserialize(JSONObject data) {
        GoPlayUserInfo user = null;
        try {
            if (data.has("data")) {
                data = data.getJSONObject("data");
            }
            user = new GoPlayUserInfo();
            if (data.has("user_id")) {
                user.userId = data.getString("user_id");
            }
            if (data.has("username")) {
                user.userName = data.getString("username");
            }
            if (data.has("email")) {
                user.email = data.getString("email");
            }
            if (data.has("mobile")) {
                user.mobile = data.getString("mobile");
            }
            if (data.has("passport")) {
                user.passport = data.getString("passport") != null ? data.getString("passport") : "";
            }
            if (data.has("facebook_email")) {
                user.facebookEmail = data.getString("facebook_email");
            }
            if (data.has("google_email")) {
                user.googleEmail = data.getString("google_email");
            }
            if (data.has("foreign_customer")) {
                user.isForeigner = data.getBoolean("foreign_customer");
            }
            if (data.has("isopenid")) {
                user.isOpenID = data.getBoolean("isopenid");
            }
            if (data.has("lock_account")) {
                user.isLock = data.getBoolean("lock_account");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}