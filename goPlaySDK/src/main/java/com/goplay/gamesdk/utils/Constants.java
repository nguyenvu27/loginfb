package com.goplay.gamesdk.utils;

import android.os.Build;
import android.os.Environment;

public class Constants {
	public static final String SCOIN_USER_SESSION = "com.goplay.gamesdk.user";
	public static final String SCOIN_TRANS_RESULT = "com.goplay.gamesdk.trans.result";
	public static final String SCOIN_USER_PW = "com.goplay.gamesdk.user_pw";
	public static final String SCOIN_USER_NAME = "com.goplay.gamesdk.user_name";
	public static final String SCOIN_USER_PHONE = "com.goplay.gamesdk.user_phone";
	public static final String SCOIN_UPDATE_TYPE = "com.goplay.gamesdk.update_type";
	public static final String SCOIN_LOGIN_METHODS = "com.goplay.gamesdk.login.methods";
	public static final String SCOIN_PARTNER_INFO = "com.goplay.gamesdk.partner.info";
	public static final String SCOIN_PAYMENT_NOTICE = "com.goplay.gamesdk.payment.notice";
	public static final String SCOIN_USER_NAME_TEMP = "com.goplay.gamesdk.user.name.temp";

	public static final String SCOIN_CARD_PAYMENT_RATE = "com.goplay.gamesdk.payment.card";
	public static final String SCOIN_SCOIN_PAYMENT_RATE = "com.goplay.gamesdk.payment.scoin";
	public static final String SCOIN_GG_PAYMENT_RATE = "com.goplay.gamesdk.payment.gp";

	public static final String ERROR_STRING = "com.goplay.gamesdk.error";

	public static final String SCOIN_PREF_APIKEY = "com.goplay.gamesdk.pref.apikey";
	public static final String SCOIN_PREF_SB_APIKEY = "com.goplay.gamesdk.pref.sb_apikey";
	public static int SCOIN_LOGIN_UPDATE = 0;
	public static int SCOIN_PROFILE_UPDATE = 1;

	public static final String SCOIN_ACC_TYPE = "com.goplay.gamesdk.acc.type";
	public static final String SCOIN_APP_STATUS = "com.goplay.gamesdk.app.status";
	public static int SCOIN_PROFILE_CHECK = 0;
	public static int SCOIN_PAYMENT_CHECK = 1;
	public static final String OS_VERSION = Build.MANUFACTURER + " " + Build.MODEL + " " + Build.VERSION.RELEASE;
	private static final String SDCARD_ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
	public static final String FOLDER = SDCARD_ROOT + "Scoin/Updates/";
	public static int SCOIN_LOGIN_COUNT_CHECK = 20;

	public static final String OPEN_ID__ACC_TYPE = "com.vtcmobile.openid.acc.type";

	public static final int RC_SIGN_IN = 9001;
	public static final int REQUEST_LOGIN = 18710;

}
