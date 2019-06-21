package com.goplay.gamesdk.helper;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goplay.gamesdk.utils.Constants;
import com.goplay.gamesdk.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class GoPlayNetworkHelper {
    private static final String TAG = GoPlayNetworkHelper.class.getSimpleName();
    private static GoPlayNetworkHelper defaultInstance;
    private GoPlayPreferenceHelper scoinPreferenceHelper;
    private String apiKey;
    private String apiDomain;
    private View dl;
    private Context context;
    protected static RequestQueue requestQueue;
    private boolean isSandboxMode;
    private String scoinAgencyId;
    private String clientAgencyId;
    private final int MY_SOCKET_TIMEOUT_MS = 10000;

    public final static int maxAttempt = 10;

    public static GoPlayNetworkHelper getInstance() {
        if (defaultInstance == null) {
            synchronized (GoPlayNetworkHelper.class) {
                if (defaultInstance == null) {
                    defaultInstance = new GoPlayNetworkHelper();
                }
            }
        }
        return defaultInstance;
    }

    public GoPlayNetworkHelper init(Context context, String apiKey, String sandboxApiKey) {
        this.context = context;
        scoinPreferenceHelper = GoPlayPreferenceHelper.getInstance().init(context);
        VolleyHelper.getInstance(context);
        requestQueue = VolleyHelper.getRequestQueue();
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            if (bundle != null) {
                isSandboxMode = bundle.getBoolean("sandbox");
                scoinAgencyId = bundle.getString("SCOIN_AGENCY_ID");
                if ((scoinAgencyId == null) || (TextUtils.isEmpty(scoinAgencyId))) {
                    scoinAgencyId = scoinPreferenceHelper.getAgencyId();
                    if (TextUtils.isEmpty(scoinAgencyId)) {
                        scoinAgencyId = Utils.getAgencyId(context);
                    }
                }
                clientAgencyId = Utils.getAgencyId(context);
                if (isSandboxMode) {
                    this.apiKey = sandboxApiKey;
                    apiDomain = "http://sandbox.graph.vtcmobile.vn";
                } else {
                    this.apiKey = apiKey;
//                    apiDomain = "https://graph.vtcmobile.vn";
                    apiDomain = "https://sdkv2.goplay.vn";

                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, "Cant load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e(TAG, "Cant to load meta-data, NullPointer: " + e.getMessage());
        }
        return this;
    }


    public void getRemoteConfiguration(Response.Listener<JSONObject> success, Response.ErrorListener error) {

        String endpoint = String.format("%1$s/getconfig", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getRemoteConfigurationParams(), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private JSONObject getRemoteConfigurationParams() {
        JSONObject params = new JSONObject();
        try {
            params.put("client_version", Utils.getAppVersionName(this.context));
            params.put("device_id", Utils.getDeviceId(context));
            params.put("device_os", "android");
            if (!TextUtils.isEmpty(scoinAgencyId)) {
                params.put("agency_id", this.scoinAgencyId);
            }
            params.put("api_key", apiKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public void checkDevice(Response.Listener<JSONObject> success, Response.ErrorListener error) {

        String endpoint = String.format("%1$s/checkdevice", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getCheckDeviceParams(), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    private JSONObject getCheckDeviceParams() {
        JSONObject params = new JSONObject();
        try {
            params.put("client_version", Utils.getAppVersionName(this.context));
            params.put("device_id", Utils.getDeviceId(context));
            if (!TextUtils.isEmpty(scoinAgencyId)) {
                params.put("agency_id", this.scoinAgencyId);
            }
            params.put("device_os", "android");
            params.put("api_key", apiKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public void login(String tag, String username, String password, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/logingoplay", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getLoginParams(username, password), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }

    private JSONObject getLoginParams(String username, String password) {
        JSONObject params = new JSONObject();

        try {
            if (!TextUtils.isEmpty(username)) {
                params.put("username", username);
            }
            if (!TextUtils.isEmpty(password)) {
                params.put("password", password);
            }

            params.put("device_id", Utils.getDeviceId(this.context));
            params.put("device_os", "android");
            params.put("device_os_version", Constants.OS_VERSION);
            params.put("client_version", Utils.getAppVersionName(this.context));

            if (!TextUtils.isEmpty(this.scoinAgencyId)) {
                params.put("agency_id", this.scoinAgencyId);
            }
//                if (!TextUtils.isEmpty(clientAgencyId)) {
//                    params.put("local_agency_id", this.clientAgencyId);
//                }
            params.put("api_key", apiKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }

    public void guestLogin(String tag, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/playnow", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getLoginParams("", ""), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }

    public void loginFacebook(String tag, String facebookId, String email, String facebookUserName, String facebookName, String facebookGender, String facebookToken, String tokenBusiness,
                              Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/playnowfb", apiDomain, apiKey);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getLoginFBParams(facebookId, email, facebookUserName, facebookName, facebookGender, facebookToken, tokenBusiness), success,
                error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }

    private JSONObject getLoginFBParams(String facebookId, String email, String facebookUserName, String facebookName, String facebookGender, String facebookToken, String tokenBusiness) {
        JSONObject params = new JSONObject();
        if (!TextUtils.isEmpty(facebookId)) {
            try {
                params.put("facebookId", facebookId);
                if (!TextUtils.isEmpty(email)) {
                    params.put("email", email);
                }

                if (!TextUtils.isEmpty(facebookUserName)) {

                    params.put("facebookUserName", facebookUserName);

                }
                if (!TextUtils.isEmpty(facebookName)) {

                    params.put("facebookName", facebookName);

                } else {
                    params.put("facebookName", facebookUserName);
                }
                if (!TextUtils.isEmpty(facebookGender)) {
                    params.put("facebookGender", facebookGender);
                }
                if (!TextUtils.isEmpty(facebookToken)) {
                    params.put("facebookToken", facebookToken);
                }
                if (!TextUtils.isEmpty(tokenBusiness)) {
                    params.put("businessToken", tokenBusiness);
                }
                params.put("device_id", Utils.getDeviceId(this.context));

                params.put("device_os", "android");

                params.put("device_os_version", Constants.OS_VERSION);


                params.put("client_version", Utils.getAppVersionName(this.context));

                if (!TextUtils.isEmpty(this.scoinAgencyId)) {
                    params.put("agency_id", this.scoinAgencyId);
                }
                if (!TextUtils.isEmpty(clientAgencyId)) {
                    params.put("local_agency_id", this.clientAgencyId);
                }
                params.put("api_key", apiKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return params;
    }

    public void loginGoogle(String tag, String googleId, String email, String userName, String gender, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/playnowgg", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getLoginGGParams(googleId, email, userName, gender), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }

    private JSONObject getLoginGGParams(String googleId, String email, String userName, String gender) {
        JSONObject params = new JSONObject();
        if (!TextUtils.isEmpty(googleId)) {
            try {
                params.put("googleId", googleId);
                if (!TextUtils.isEmpty(email)) {
                    params.put("googleEmail", email);
                }

                if (!TextUtils.isEmpty(userName)) {

                    params.put("googleName", userName);

                }

                // if (!TextUtils.isEmpty(gender)) {
                // params.put("googleGender", gender));
                // }

                params.put("device_id", Utils.getDeviceId(this.context));

                params.put("device_os", "android");

                params.put("device_os_version", Constants.OS_VERSION);


                params.put("client_version", Utils.getAppVersionName(this.context));

                if (!TextUtils.isEmpty(this.scoinAgencyId)) {
                    params.put("agency_id", this.scoinAgencyId);
                }
                if (!TextUtils.isEmpty(clientAgencyId)) {
                    params.put("local_agency_id", this.clientAgencyId);
                }
                params.put("api_key", apiKey);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return params;
    }

    public void register(String tag, String username, String password, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/register", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getRegisterParams(username, password), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }


    private JSONObject getRegisterParams(String username, String password) {

        JSONObject params = new JSONObject();

        try {
            params.put("username", username);
            params.put("password", password);
            params.put("device_id", Utils.getDeviceId(this.context));

            params.put("device_os", "android");

            params.put("device_os_version", Constants.OS_VERSION);
            params.put("client_version", Utils.getAppVersionName(this.context));
            if (!TextUtils.isEmpty(this.scoinAgencyId)) {
                params.put("agency_id", this.scoinAgencyId);
            }
            if (!TextUtils.isEmpty(clientAgencyId)) {
                params.put("local_agency_id", this.clientAgencyId);
            }
            params.put("api_key", apiKey);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return params;

    }

    public void logout(Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/logoutdevice", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, endpoint, getLogoutParams(), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

    public JSONObject getLogoutParams() {
        JSONObject params = new JSONObject();
        try {
            params.put("device_id", Utils.getDeviceId(this.context));

            params.put("api_key", apiKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return params;
    }


    public void updateQuickLogIn(String tag, String userId, String userName, String password, String email, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/updateplaynow", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, endpoint, getUpdateQuickLoginParams(userId, userName, password, email), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }

    private JSONObject getUpdateQuickLoginParams(String userid, String username, String password, String email) {
        JSONObject params = new JSONObject();
        try {
            if (!TextUtils.isEmpty(userid)) {
                params.put("user_id", userid);
            }
            if (!TextUtils.isEmpty(username)) {
                params.put("username", username);
            }
            if (!TextUtils.isEmpty(password)) {
                params.put("password", password);
            }
            if (!TextUtils.isEmpty(email)) {
                params.put("email", email);
            }
            params.put("device_id", Utils.getDeviceId(this.context));
            params.put("device_os", "android");
            params.put("device_os_version", Constants.OS_VERSION);
            if (!TextUtils.isEmpty(this.scoinAgencyId)) {
                params.put("agency_id", this.scoinAgencyId);
            }
            if (!TextUtils.isEmpty(clientAgencyId)) {
                params.put("local_agency_id", this.clientAgencyId);
            }
            params.put("api_key", apiKey);
        } catch (Exception ex) {

        }

        return params;
    }


    public void checkUserInfo(String tag, String username, Response.Listener<JSONObject> success, Response.ErrorListener error) {
        String endpoint = String.format("%1$s/getprofilewithaccountname", apiDomain);
        JsonObjectRequest request = new JsonObjectRequest(Method.POST, endpoint, getCheckInfoParams(username), success, error);
        request.setRetryPolicy(new DefaultRetryPolicy(MY_SOCKET_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        request.setTag(tag);
        requestQueue.add(request);
    }

    private JSONObject getCheckInfoParams(String username) {
        JSONObject params = new JSONObject();
        try {
            if (!TextUtils.isEmpty(username)) {
                params.put("username", username);
            }
            if (!TextUtils.isEmpty(this.scoinAgencyId)) {
                params.put("agency_id", this.scoinAgencyId);
            }
            params.put("api_key", apiKey);

        } catch (Exception ex) {

        }

        return params;
    }


    public void cancelRequests(Object paramObject) {
        if (requestQueue != null)
            requestQueue.cancelAll(paramObject);
    }
}
