package com.goplay.gamesdk.core;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.goplay.gamesdk.AuthenActivity;
import com.goplay.gamesdk.QuickLoginUserUpdateActivity;
import com.goplay.gamesdk.R;
import com.goplay.gamesdk.common.GoPlayAction;
import com.goplay.gamesdk.helper.FirebaseHelper;
import com.goplay.gamesdk.helper.GoPlayLogHelper;
import com.goplay.gamesdk.helper.GoPlayNetworkHelper;
import com.goplay.gamesdk.helper.GoPlayPreferenceHelper;
import com.goplay.gamesdk.helper.VolleyHelper;
import com.goplay.gamesdk.models.GoPlaySession;
import com.goplay.gamesdk.utils.Constants;
import com.goplay.gamesdk.utils.Utils;
import com.goplay.gamesdk.widgets.Crouton;
import com.goplay.gamesdk.widgets.DialogManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GoPlaySDK {

    private static GoPlaySDK defaultInstance;

    private Activity context;
    private String apiKey;
    private String sandboxApiKey;
    private GoPlayNetworkHelper nwHelper;
    private GoPlayPreferenceHelper preferenceHelper;
    private ProgressDialog dialog;
    private View dl;
    private goPlayConfig configuration;

    private GoPlayLogHelper logger;
    private final String TAG = getClass().getSimpleName();

    private boolean autoLogin = true;


    private static CallbackManager mFbCallBackManager;
    private AppEventsLogger fbLogger;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public static GoPlaySDK getInstance() {
        if (defaultInstance == null) {
            synchronized (GoPlaySDK.class) {
                if (defaultInstance == null) {
                    defaultInstance = new GoPlaySDK();
                }
            }
        }
        return defaultInstance;
    }

    public GoPlaySDK init(final Activity context, boolean isUseSDKButton, String apiKey, String sandboxApiKey) {
        this.context = context;
        VolleyHelper.getInstance(context);
        this.apiKey = apiKey;
        this.sandboxApiKey = sandboxApiKey;
        this.preferenceHelper = GoPlayPreferenceHelper.getInstance().init(context);

        dialog = new ProgressDialog(this.context);
        dialog.setMessage(this.context.getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        this.nwHelper = GoPlayNetworkHelper.getInstance().init(this.context, apiKey, sandboxApiKey);
//        if (verifyStoragePermissions(context)) {
//            if (verifyStoragePermissions(context)) {
        getConfig();
//            }
//
//        }
        FacebookSdk.sdkInitialize(context.getApplicationContext());
        fbLogger = AppEventsLogger.newLogger(context);
        FirebaseHelper.initializeAnalyticsTracker(context);
        this.logger = new GoPlayLogHelper(context);

        setApiKey(this.apiKey);
        setSandboxApiKey(this.sandboxApiKey);

        return this;
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case REQUEST_EXTERNAL_STORAGE: {
//                // If request is cancelled, the result arrays are empty.
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    // permission was granted, yay! Do the
//                    // contacts-related task you need to do.
//                    getConfig();
//
//                } else {
//                    DialogManager.showConfirmDialog(context, "Thông báo", "Xin vui lòng cấp quyền cho ứng dụng", new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            // TODO Auto-generated method stub
//
//                            switch (which) {
//                                case DialogInterface.BUTTON_POSITIVE:
//                                    if (verifyStoragePermissions(context)) {
//                                        getConfig();
//
//                                    }
//
//                                    break;
//
//                                default:
//                                    dialog.dismiss();
//                                    // TODO need confirm
////                                    context.finish();
//
//                                    // process kill
//                                    Intent intent = new Intent(Intent.ACTION_MAIN);
//                                    intent.addCategory(Intent.CATEGORY_HOME);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    context.startActivity(intent);
//
//                                    Thread thread = new Thread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            try {
//                                                Thread.sleep(300);
//                                            } catch (InterruptedException e) {
//                                                e.printStackTrace();
//                                            }
//
//                                            android.os.Process.killProcess(android.os.Process.myPid());
//                                        }
//                                    });
//
//                                    thread.start();
//                                    break;
//                            }
//
//                        }
//                    });
//
//
//                }
//                return;
//            }
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
    }

    public static boolean verifyStoragePermissions(Activity activity) {
        // Check if we have read or write permission
        int writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE);

        if (writePermission != PackageManager.PERMISSION_GRANTED || readPermission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            return false;
        } else
            return true;
    }

    @SuppressLint("NewApi")


//

    public void updateQuickLogin() {
        if (preferenceHelper != null) {
            int accountType = preferenceHelper.getAccountType();
            if (accountType == 1) {
                Intent intent = new Intent(context, QuickLoginUserUpdateActivity.class);
                intent.putExtra(Constants.SCOIN_ACC_TYPE, Constants.SCOIN_PAYMENT_CHECK);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Tài khoản của bạn không hợp lệ", Toast.LENGTH_SHORT).show();
            }

        }

    }


    public void setAutoLogin(boolean autoLogin) {
        this.autoLogin = autoLogin;
        preferenceHelper.setAutoLogin(autoLogin);
    }

    private void setEnableLog(boolean enableLog) {
        preferenceHelper.setEnableLogging(enableLog);

    }

    private void setApiKey(String apiKey) {
        preferenceHelper.setApiKey(apiKey);
    }

    private void setSandboxApiKey(String sandboxApiKey) {
        preferenceHelper.setSandboxApiKey(sandboxApiKey);
    }

    private void setPasswordLink(String link) {
        preferenceHelper.setForgotPW(link);
    }

    public void manualLogin() {
        getSavedUserServer();
    }


    // Facebook


    public static void handleActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {

        // ==== modify by netmarble ====
        if (requestCode == Constants.REQUEST_LOGIN) {
            if (resultCode == Activity.RESULT_CANCELED) {
                Intent broadcast = new Intent(GoPlayAction.LOGIN_CANCEL_ACTION);
                LocalBroadcastManager.getInstance(activity).sendBroadcast(broadcast);
            }
        } else {
            try {

                mFbCallBackManager.onActivityResult(requestCode, resultCode, data);
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        // ============================
    }

    // Facebook

    private void getConfig() {
        try {

            if (!context.isFinishing() && dialog != null) {
                dialog.show();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                dl = inflater.inflate(R.layout.progress_dialog_with_bg, null);
                TextView temp = dl.findViewById(R.id.progress_dialog_msg);
                temp.setText(context.getString(R.string.loading));
                dialog.setContentView(dl);
            }

            nwHelper.getRemoteConfiguration(onGetConfigSuccess(), onGetConfigError());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Response.Listener<JSONObject> onGetConfigSuccess() {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(TAG, response.toString());
                try {
                    if (!context.isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (!TextUtils.isEmpty(response.toString())) {
                    Utils.encrypt(response.toString(), "config.sdk", context);

                    FirebaseHelper.sendEvent("GET_CONFIG", "SUCCESS", "");

                    configuration = goPlayConfig.parseConfiguration(response);

                    setApiKey(apiKey);
                    setSandboxApiKey(sandboxApiKey);
                    setPasswordLink(configuration.getForgotPwLink());
                    setEnableLog(configuration.isShowLog());


                    if (autoLogin) {

                        getSavedUserServer();
                    }


                }

            }
        };
    }

    private Response.ErrorListener onGetConfigError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("GET_CONFIG", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("GET_CONFIG", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }

                try {
                    if (!context.isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    String configStr = Utils.decrypt("config.sdk", context);
                    if ((TextUtils.isEmpty(configStr)) || (configStr == null)) {
                        configuration = new goPlayConfig();
                        List<String> loginMethods = new ArrayList<String>();
                        loginMethods.add("LOGIN_SCOIN");
//
                        configuration.setLoginMethods(loginMethods);
                        // configuration.setNoticeUrl(.this.noticeUrl);
                        setApiKey(apiKey);
                        setSandboxApiKey(sandboxApiKey);
                        if (autoLogin) {
                            getSavedUserServer();
                        }
                    } else {
                        JSONObject json = new JSONObject(configStr);
                        configuration = goPlayConfig.parseConfiguration(json);
                        setApiKey(apiKey);
                        setSandboxApiKey(sandboxApiKey);

                        if (autoLogin) {
                            getSavedUserServer();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        };
    }

    public String getDeviceId(Context ctx) {
        return Utils.getDeviceId(ctx);
    }

    public void logout(boolean isShowLogin) {
        String userId = preferenceHelper.getUserId();
        FirebaseHelper.sendEvent("LOGOUT", "", "Success");

        if (!TextUtils.isEmpty(userId)) {
            LoginManager.getInstance().logOut();
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            // [START build_client]
            // Build a GoogleSignInClient with the options specified by gso.
            GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
            mGoogleSignInClient.signOut();
            this.nwHelper.logout(logoutSuccess(isShowLogin, this.context), logoutError());
        } else {
            Intent data = new Intent(GoPlayAction.LOGOUT_SUCCESS_ACTION);
            LocalBroadcastManager.getInstance(context).sendBroadcast(data);
        }
    }

    public void trackFBInstall(Context context) {

        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null) {

                // facebook tracker
                String fbId = bundle.getString("com.facebook.sdk.ApplicationId");
                FacebookSdk.sdkInitialize(context.getApplicationContext());
                AppEventsLogger.activateApp(context, fbId);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cant load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Cant to load meta-data, NullPointer: " + e.getMessage());
        }
    }


    private void getSavedUserServer() {
        try {

            if (configuration == null) {
                // TODO need confirm
                Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
                Bundle b = new Bundle();
                b.putString(Constants.ERROR_STRING, "Client_Error: configuration is null");
                data.putExtras(b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(data);
            } else {
                // if (autoLogin) {
                nwHelper.checkDevice(checkDeviceSuccessListener(), checkDeviceErrorListener());


            }
        } catch (Exception e) {
            if (!context.isFinishing()) {
                dialog = new ProgressDialog(this.context);
                dialog.setMessage(this.context.getString(R.string.loading));
                dialog.show();
            }
            nwHelper.checkDevice(checkDeviceSuccessListener(), checkDeviceErrorListener());

        }
    }

    private Response.Listener<JSONObject> checkDeviceSuccessListener() {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(TAG, response.toString());
                try {
                    if (!context.isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

                try {
                    boolean status = response.getBoolean("status");
                    int errorCode = response.getInt("error_code");

                    if ((status) && (errorCode == 0)) {

                        FirebaseHelper.sendEvent("CHECK_DEVICE", "SUCCESS", "SUCCESS");

                        GoPlaySession session = GoPlaySession.deserialize(response);
                        if (session != null) {

                            preferenceHelper.setSession(session);

                            Intent broadcast = new Intent(GoPlayAction.LOGIN_SUCCESS_ACTION);
                            Bundle b = new Bundle();
                            b.putParcelable(Constants.SCOIN_USER_SESSION, session);
                            broadcast.putExtras(b);
                            LocalBroadcastManager.getInstance(context).sendBroadcast(broadcast);
                        } else {
                            // TODO need confirm
                            preferenceHelper.clear();
                            Intent login = new Intent(GoPlaySDK.this.context, AuthenActivity.class);
                            Bundle bundle = new Bundle();

                            bundle.putStringArrayList(Constants.SCOIN_LOGIN_METHODS, (ArrayList<String>) configuration.getLoginMethods());
                            bundle.putString("FORGOTURL", configuration.getForgotPwLink());
                            if (configuration != null && !TextUtils.isEmpty(configuration.getAppStatus()))
                                bundle.putString(Constants.SCOIN_APP_STATUS, configuration.getAppStatus());
                            login.putExtras(bundle);
                            GoPlaySDK.this.context.startActivityForResult(login, Constants.REQUEST_LOGIN); // ==== Modify by netmarble
                        }
                    } else {
                        preferenceHelper.clear();
                        Intent login = new Intent(GoPlaySDK.this.context, AuthenActivity.class);
                        Bundle bundle = new Bundle();

                        bundle.putStringArrayList(Constants.SCOIN_LOGIN_METHODS, (ArrayList<String>) configuration.getLoginMethods());
                        bundle.putString("FORGOTURL", configuration.getForgotPwLink());
                        if (configuration != null && !TextUtils.isEmpty(configuration.getAppStatus()))
                            bundle.putString(Constants.SCOIN_APP_STATUS, configuration.getAppStatus());
                        login.putExtras(bundle);
                        GoPlaySDK.this.context.startActivityForResult(login, Constants.REQUEST_LOGIN);  // ==== Modify by netmarble
                    }
                } catch (JSONException e) {
                    // TODO need confirm
                    preferenceHelper.clear();
                    Intent login = new Intent(GoPlaySDK.this.context, AuthenActivity.class);
                    Bundle bundle = new Bundle();

                    bundle.putStringArrayList(Constants.SCOIN_LOGIN_METHODS, (ArrayList<String>) configuration.getLoginMethods());
                    bundle.putString("FORGOTURL", configuration.getForgotPwLink());
                    if (configuration != null && !TextUtils.isEmpty(configuration.getAppStatus()))
                        bundle.putString(Constants.SCOIN_APP_STATUS, configuration.getAppStatus());
                    login.putExtras(bundle);
                    GoPlaySDK.this.context.startActivityForResult(login, Constants.REQUEST_LOGIN); // ==== Modify by netmarble

                    e.printStackTrace();
                }

            }
        };
    }

    private Response.ErrorListener checkDeviceErrorListener() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                try {
                    if (!context.isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("CHECK_DEVICE", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("CHECK_DEVICE", "CONNECTION_ERROR", "");
                }
                preferenceHelper.clear();
                Intent login = new Intent(GoPlaySDK.this.context, AuthenActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(Constants.SCOIN_LOGIN_METHODS, (ArrayList<String>) configuration.getLoginMethods());
                bundle.putString("FORGOTURL", configuration.getForgotPwLink());
                if (configuration != null && !TextUtils.isEmpty(configuration.getAppStatus()))
                    bundle.putString(Constants.SCOIN_APP_STATUS, configuration.getAppStatus());
                login.putExtras(bundle);
                GoPlaySDK.this.context.startActivityForResult(login, Constants.REQUEST_LOGIN); // ==== Modify by netmarble
            }
        };
    }


    private Response.Listener<JSONObject> logoutSuccess(final boolean isShowLogin, final Context context) {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(TAG, response.toString());

                try {
                    String status = response.optString("error_code");
                    if (("0").equals(status)) {

                        FirebaseHelper.sendEvent("LOGOUT", "SUCCESS", "SUCCESS");

                        Toast.makeText(context, R.string.logout_success, Toast.LENGTH_LONG).show();
                        preferenceHelper.clear();

                        if (isShowLogin) {
                            Intent login = new Intent(context, AuthenActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putStringArrayList(Constants.SCOIN_LOGIN_METHODS, (ArrayList<String>) configuration.getLoginMethods());
                            if (configuration != null && !TextUtils.isEmpty(configuration.getAppStatus()))
                                bundle.putString(Constants.SCOIN_APP_STATUS, configuration.getAppStatus());
                            login.putExtras(bundle);
                            ((Activity) context).startActivityForResult(login, Constants.REQUEST_LOGIN); // ==== Modify by netmarble
                        }
                        Intent data = new Intent(GoPlayAction.LOGOUT_SUCCESS_ACTION);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(data);
                    } else {
                        String msg = response.optString("message");
                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                        Intent data = new Intent(GoPlayAction.LOGOUT_ERROR_ACTION);
                        Bundle b = new Bundle();
                        b.putString(Constants.ERROR_STRING, "Server_Error: " + msg);
                        data.putExtras(b);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(data);
                    }
                } catch (Exception e) {
                    Intent data = new Intent(GoPlayAction.LOGOUT_ERROR_ACTION);
                    Bundle b = new Bundle();
                    b.putString(Constants.ERROR_STRING, "Server_Error: " + e.getMessage());
                    data.putExtras(b);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(data);
                    e.printStackTrace();
                }
            }

        };
    }

    private Response.ErrorListener logoutError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                try {
                    if (!context.isFinishing() && dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }

                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("LOG_OUT", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("LOG_OUT", "CONNECTION_ERROR", "");
                }
                Intent data = new Intent(GoPlayAction.LOGOUT_ERROR_ACTION);
                Bundle b = new Bundle();
                b.putString(Constants.ERROR_STRING, "Network_Error");
                data.putExtras(b);
                LocalBroadcastManager.getInstance(context).sendBroadcast(data);
            }

        };
    }

    /**
     * Show a welcome message to logged user
     *
     * @param user     User session
     * @param activity Current activity
     */
    public void showWelcomMessage(GoPlaySession user, Activity activity) {
        if (user != null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.login_msg, null);
            TextView txtMessage = (TextView) view.findViewById(R.id.message);
            String temp = String.format(activity.getString(R.string.welcome_back_msg), user.userName);
            txtMessage.setText(temp);
            final Crouton crouton;

            crouton = Crouton.make(activity, view);

            crouton.show();
        }
    }

}


