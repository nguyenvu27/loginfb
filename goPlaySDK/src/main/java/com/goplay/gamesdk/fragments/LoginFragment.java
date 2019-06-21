package com.goplay.gamesdk.fragments;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.goplay.gamesdk.R;
import com.goplay.gamesdk.common.GoPlayAction;
import com.goplay.gamesdk.helper.FirebaseHelper;
import com.goplay.gamesdk.helper.ToastHelper;
import com.goplay.gamesdk.helper.VolleyHelper;
import com.goplay.gamesdk.models.GoPlaySession;
import com.goplay.gamesdk.models.GoogleSession;
import com.goplay.gamesdk.utils.Constants;
import com.goplay.gamesdk.validator.Form;
import com.goplay.gamesdk.validator.Validate;
import com.goplay.gamesdk.validator.validator.NotEmptyValidator;
import com.goplay.gamesdk.widgets.DialogManager;
import com.goplay.gamesdk.widgets.ScoinButton;
import com.goplay.gamesdk.widgets.ScoinEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginFragment extends BaseFragment implements OnClickListener, OnTouchListener {
    private ScoinEditText editUsername;
    private ScoinEditText editPassword;
    private ScoinButton btnLogin;
    private ScoinButton btnQuickLogin;

    private ImageButton btnBack;
    private ImageButton btnClose;

    private TextView btnForgot;
    private TextView btnRegister;
    private ImageButton btnLoginGoogle;
    private ImageButton btnLoginFacebook;
    private ArrayList<String> loginMethods;
    private Form form;
    private String googleToken;
    private AccountManager mAccountManager;
    private Bundle arg;
    private CallbackManager callbackManager;
    private String forgotURL = "";
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mParent = inflater.inflate(R.layout.fragment_login, container, false);

        editUsername = mParent.findViewById(R.id.edt_username);
        editPassword = mParent.findViewById(R.id.edt_password);

        btnLogin = mParent.findViewById(R.id.btn_login);
        btnBack = mParent.findViewById(R.id.btn_back);
        btnClose = mParent.findViewById(R.id.btn_close);
        btnQuickLogin = mParent.findViewById(R.id.btn_quick_login);

        btnLoginGoogle = mParent.findViewById(R.id.btn_gg);
        btnLoginFacebook = mParent.findViewById(R.id.btn_fb);

        btnForgot = mParent.findViewById(R.id.txt_forgot_pass);
        btnRegister = mParent.findViewById(R.id.txt_register);

        btnBack.setVisibility(View.INVISIBLE);

        callbackManager = CallbackManager.Factory.create();


// region Login Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // [START build_client]
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

//endregion


        if (loginMethods != null) {
            if (loginMethods.contains("LOGIN_FAST")) {
                btnQuickLogin.setVisibility(View.VISIBLE);
                btnQuickLogin.setOnClickListener(this);
            } else {
                btnQuickLogin.setVisibility(View.GONE);
            }
            if (loginMethods.contains("LOGIN_GG")) {
                btnLoginGoogle.setVisibility(View.VISIBLE);
                btnLoginGoogle.setOnClickListener(this);
                btnLoginGoogle.setOnTouchListener(this);
            } else {

                btnLoginGoogle.setVisibility(View.GONE);
            }
            if (loginMethods.contains("LOGIN_FB")) {
                btnLoginFacebook.setVisibility(View.VISIBLE);
                btnLoginFacebook.setOnClickListener(this);
                btnLoginFacebook.setOnTouchListener(this);
            } else {
                btnLoginFacebook.setVisibility(View.GONE);
            }

        }


        btnLogin.setOnClickListener(this);
        btnForgot.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        return mParent;

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(mContext.getApplicationContext());
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            nwHelper.loginGoogle(TAG, account.getId(), account.getEmail(), account.getDisplayName(), "", onLoginSuccess("Google"), onLoginError());

        }
    }

    @Override
    protected void initVariables() {

        TextView temp = dl.findViewById(R.id.progress_dialog_msg);
        temp.setText(mContext.getString(R.string.logging_in));
        processArguments();

        mAccountManager = AccountManager.get(mContext);

    }

    private void processArguments() {
        if (getArguments() != null) {
            arg = getArguments();
            if (arg.containsKey(Constants.SCOIN_LOGIN_METHODS)) {

                loginMethods = arg.getStringArrayList(Constants.SCOIN_LOGIN_METHODS);
            }
            if (arg.containsKey("FORGOTURL")) {

                forgotURL = arg.getString("FORGOTURL");
            } else {
                forgotURL = preferenceHelper.getForgotPW();
            }
        }
    }

    @Override
    protected void initActions() {

        form = new Form();
        Validate usernameValid = new Validate(this.editUsername);
        usernameValid.addValidator(new NotEmptyValidator(mContext));


        Validate passwordValid = new Validate(this.editPassword);
        passwordValid.addValidator(new NotEmptyValidator(mContext));


        form.addValidates(usernameValid);
        form.addValidates(passwordValid);

        FirebaseHelper.sendScreenView(getActivity(), "LOGIN");

    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (responseCode == Activity.RESULT_OK) {


                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                handleSignInResult(task);

            } else if (responseCode == Activity.RESULT_CANCELED) {
                Toast.makeText(mContext, "Bạn đã hủy yêu cầu !", Toast.LENGTH_SHORT).show(); // User canceled

            } else {
                Toast.makeText(mContext, "Có lỗi xảy ra. Vui lòng thử lại sau !", Toast.LENGTH_SHORT).show(); // Unknow error. Please try again later
            }
        }

    }

    // [START handleSignInResult]
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    // [END handleSignInResult]
//    @Override
//    public void onActivityResult(int requestCode, int responseCode, Intent data) {
//        super.onActivityResult(requestCode, responseCode, data);
//        callbackManager.onActivityResult(requestCode, responseCode, data);
//        if (requestCode == Constants.RC_SIGN_IN) {
//            if (responseCode == Activity.RESULT_OK) {
//
//                Account[] accs = mAccountManager.getAccounts();
//
//                if (accs == null || accs.length == 0) {
//
//
//                    Toast.makeText(mContext, "Không tìm thấy tài khoản trên máy của bạn. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
//
//                } else {
//
//                    String selectedAccountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
//                    String selectedAccountType = data.getStringExtra(AccountManager.KEY_ACCOUNT_TYPE);
//                    boolean accountFound = false;
//                    for (Account item : accs
//                            ) {
//                        if (TextUtils.equals(item.name, selectedAccountName) && TextUtils.equals(item.type, selectedAccountType)) {
//                            accountFound = true;
//                            Bundle options = new Bundle();
//                            mAccountManager
//                                    .getAuthToken(
//                                            item,
//                                            "oauth2:https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
//                                            options, LoginFragment.this.getActivity(), new LoginFragment.OnGGTokenAcquired(), new Handler());
//                        }
//
//                    }
//                    if (!accountFound) {
//                        Toast.makeText(mContext, "Không tìm thấy tài khoản trên máy của bạn. Vui lòng kiểm tra lại !", Toast.LENGTH_SHORT).show();
//                    }
//
//                }
//
//
//            } else if (responseCode == Activity.RESULT_CANCELED) {
//                Toast.makeText(mContext, "Bạn đã hủy yêu cầu !", Toast.LENGTH_SHORT).show(); // User canceled
//
//            } else {
//                Toast.makeText(mContext, "Có lỗi xảy ra. Vui lòng thử lại sau !", Toast.LENGTH_SHORT).show(); // Unknow error. Please try again later
//            }
//
//
//        }
//
//    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.btn_login) {
            if (form.validate()) {

                showLoading("", true);

                FirebaseHelper.sendEvent("LOGIN_SCOIN", "START", "START");
                nwHelper.login(TAG, editUsername.getText().toString().trim(), editPassword.getText().toString(), onLoginSuccess("Scoin"), onLoginError());

            }

        } else if (id == R.id.btn_quick_login) {
            showLoading("", true);
            nwHelper.guestLogin(TAG, onLoginSuccess("Guest_Login"), onLoginError());
            FirebaseHelper.sendEvent("Guest_Login", "START", "START");
        } else if (id == R.id.btn_gg) {

            signIn();
////            if (Utils.verifyContactPermissions(getActivity())) {
//            FirebaseHelper.sendEvent("LOGIN_GG", "START", "START");
//            if (Utils.verifyContactPermissions(getActivity())) {
//                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    // Do something for Oreo and above versions
//                    pickUserAccount();
//                } else {
//                    // do something for phones running an SDK before Oreo
//
//                    final Account[] accs = Utils.getAccounts(mAccountManager);
//                    String[] accounts = Utils.getAccountNames(accs);
//                    if (accounts == null || accounts.length == 0) {
//
//
//                        DialogManager.showConfirmDialog(mContext, "Thông báo", getString(R.string.no_account_found), new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which) {
//                                    case DialogInterface.BUTTON_POSITIVE:
//                                        startAddGoogleAccountIntent(mContext);
//                                        break;
//                                    case DialogInterface.BUTTON_NEGATIVE:
//                                        dialog.dismiss();
//                                        break;
//                                }
//                            }
//                        });
//                    } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                        builder.setTitle(mContext.getString(R.string.title_gg_dialog));
//
//                        builder.setItems(accounts, new Dialog.OnClickListener() {
//
//                            @Override
//                            public void onClick(DialogInterface dialogI, int which) {
//                                showLoading("", true);
//                                Bundle options = new Bundle();
//                                mAccountManager
//                                        .getAuthToken(
//                                                accs[which],
//                                                "oauth2:https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
//                                                options, LoginFragment.this.getActivity(), new LoginFragment.OnGGTokenAcquired(), new Handler());
//
//                            }
//                        });
//
//                        builder.show();
//                    }
//                }
//
////                }
//
//
//            }

        } else if (id == R.id.txt_register) {
            Fragment registerFragment = new RegisterFragment();

            FragmentTransaction transaction = getFragmentManager().beginTransaction();

            transaction.replace(R.id.container, registerFragment);
            transaction.addToBackStack(null);

            transaction.commit();

        } else if (id == R.id.btn_close) {
            // ==== modify by netmarble ====
            getActivity().setResult(Activity.RESULT_OK);
            // =============================
            getActivity().finish();
            Intent broadcast = new Intent(GoPlayAction.LOGIN_CANCEL_ACTION);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
        } else if (id == R.id.btn_fb) {
            LoginManager.getInstance().logOut();
            callbackManager = CallbackManager.Factory.create();
            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    Log.e("Invite_Facebook", "onSuccess");

                    GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject user, GraphResponse response) {

                            try {
                                nwHelper.loginFacebook(TAG, user.optString("id"), user.optString("email"), user.optString("name"), user.optString("name"), user.optString("gender"),
                                        loginResult.getAccessToken().getToken(), user.optString("token_for_business"), onLoginFBSuccess(), onLoginFBError());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id,email,gender,name,token_for_business");
                    request.setParameters(parameters);
                    request.executeAsync();


                }

                @Override
                public void onCancel() {
                    // TODO: this
                    Log.e("Invite_Facebook", "User_Canceled");

                }

                @Override
                public void onError(FacebookException exception) {
                    // TODO: this
                    Log.e("Invite_Facebook", "" + exception.toString());

                }
            });

            LoginManager.getInstance().logInWithReadPermissions((Activity) mContext, Arrays.asList("public_profile", "email"));

        } else if (id == R.id.txt_forgot_pass) {

            if (!TextUtils.isEmpty(forgotURL)) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(forgotURL));
                startActivity(i);
            }


        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        ImageButton button = (ImageButton) view;
        switch (motionEvent.getAction()) {
            case 0:
                button.getBackground().setColorFilter(this.mContext.getResources().getColor(R.color.black_trans), PorterDuff.Mode.SRC_ATOP);
                break;
            case 1:
                button.getBackground().clearColorFilter();
        }

        return false;
    }

    private Response.Listener<JSONObject> onLoginSuccess(final String type) {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(TAG, response.toString());
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    boolean status = response.getBoolean("status");
                    int errorCode = response.getInt("error_code");
                    LoginManager.getInstance().logOut();
                    if ((status) && (errorCode == 0)) {
                        FirebaseHelper.sendEvent("LOGIN", "SUCCESS", "SUCCESS");

                        GoPlaySession session = GoPlaySession.deserialize(response);
                        if (session != null) {
                            preferenceHelper.setSession(session);


                            Intent broadcast = new Intent(GoPlayAction.LOGIN_SUCCESS_ACTION);
                            Bundle b = new Bundle();
                            b.putParcelable(Constants.SCOIN_USER_SESSION, session);
                            broadcast.putExtras(b);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
                            if (mContext != null && !((Activity) mContext).isFinishing()) {
                                // ==== modify by netmarble ====
                                ((Activity) mContext).setResult(Activity.RESULT_OK);
                                // ==============================
                                ((Activity) mContext).finish();
                            }

                        }
                    } else {
                        String message = response.optString("message");
                        FirebaseHelper.sendEvent("LOGIN", "ERROR", "ERROR " + message);
                        mGoogleSignInClient.signOut();
                        // TODO need confirm
//                        Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
//                        Bundle b = new Bundle();
//                        b.putString(Constants.ERROR_STRING, "Server_Error " + message);
//                        data.putExtras(b);
//                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(data);

                        DialogManager.showPositiveDialog(mContext, mContext.getResources().getString(R.string.title_login_err), message, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                    }
                } catch (JSONException e) {
                    // TODO need confirm
//                    Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
//                    Bundle b = new Bundle();
//                    b.putString(Constants.ERROR_STRING, "Server_Error: JsonException");
//                    data.putExtras(b);
//                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(data);
                    mGoogleSignInClient.signOut();
                    e.printStackTrace();
                }
            }

        };
    }

    private Response.ErrorListener onLoginError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                mGoogleSignInClient.signOut();
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("LOGIN", "CONNECTION_ERROR", "CONNECTION_ERROR " + error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("LOGIN", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }

                ToastHelper.showToastError(mContext, mContext.getString(R.string.maintain_error_message));

                // TODO need confirm
//                Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
//                Bundle b = new Bundle();
//                b.putString(Constants.ERROR_STRING, "Network_Error " + error.getMessage());
//                data.putExtras(b);
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(data);

            }
        };
    }


    private Response.Listener<JSONObject> onLoginFBSuccess() {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(TAG, response.toString());
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    boolean status = response.getBoolean("status");
                    int errorCode = response.getInt("error_code");
                    if ((status) && (errorCode == 0)) {

                        FirebaseHelper.sendEvent("LOGIN_FB", "SUCCESS", "SUCCESS");
                        GoPlaySession session = GoPlaySession.deserialize(response);
                        if (session != null) {

                            preferenceHelper.setSession(session);

                            Intent broadcast = new Intent(GoPlayAction.LOGIN_SUCCESS_ACTION);
                            Bundle b = new Bundle();
                            b.putParcelable(Constants.SCOIN_USER_SESSION, session);
                            broadcast.putExtras(b);
                            LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
                            if (isAdded()) {
                                // ==== modify by netmarble ====
                                ((Activity) mContext).setResult(Activity.RESULT_OK);
                                // =============================
                                ((Activity) mContext).finish();
                            }
                        }

                    } else {
                        String message = response.optString("message");
                        // TODO need confirm
//                        Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
//                        Bundle b = new Bundle();
//                        b.putString(Constants.ERROR_STRING, "Server_Error " + message);
//                        data.putExtras(b);
//                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(data);


                        FirebaseHelper.sendEvent("LOGIN_FB", "ERROR", message);
                        ToastHelper.showToastError(mContext, "" + message);
                    }
                } catch (JSONException e) {
                    // TODO need confirm
//                    Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
//                    Bundle b = new Bundle();
//                    b.putString(Constants.ERROR_STRING, "Server_Error: JsonException");
//                    data.putExtras(b);
//                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(data);

                    e.printStackTrace();
                }
            }

        };
    }

    private Response.ErrorListener onLoginFBError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("LOGIN_FB", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("LOGIN_FB", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }
                ToastHelper.showToastError(mContext, mContext.getString(R.string.maintain_error_message));
                // TODO need confirm
//                Intent data = new Intent(GoPlayAction.LOGIN_ERROR_ACTION);
//                Bundle b = new Bundle();
//                b.putString(Constants.ERROR_STRING, "Network_Error " + error.getMessage());
//                data.putExtras(b);
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(data);

            }
        };
    }

//    private class OnGGTokenAcquired implements AccountManagerCallback<Bundle> {
//        private OnGGTokenAcquired() {
//        }
//
//        public void run(AccountManagerFuture<Bundle> result) {
//            try {
//                Intent launch = (Intent) result.getResult().get("intent");
//                if (launch != null) {
//                    LoginFragment.this.startActivityForResult(launch, 18);
//                    return;
//                }
//                Bundle bundle = result.getResult();
//                googleToken = bundle.getString("authtoken");
//
//                JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.GET, "https://www.googleapis.com/userinfo/v2/me?access_token=" + googleToken, null,
//                        onCallGoogleApiSuccess(), onCallGoogleApiFail());
//                VolleyHelper.getRequestQueue().add(request);
//
//            } catch (OperationCanceledException e) {
//                e.printStackTrace();
//            } catch (AuthenticatorException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private com.android.volley.Response.Listener<JSONObject> onCallGoogleApiSuccess() {
        return new Listener<JSONObject>() {

            public void onResponse(JSONObject arg0) {
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                GoogleSession session = GoogleSession.deserialize(arg0);
                if (session != null) {
                    nwHelper.loginGoogle(TAG, session.id, session.email, session.name, session.gender, onLoginSuccess("Google"), onLoginError());

                    FirebaseHelper.sendEvent("LOGIN_GG", "START", "START");
                }
            }
        };
    }

    private com.android.volley.Response.ErrorListener onCallGoogleApiFail() {
        return new ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("LOGIN_GG_API", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("LOGIN_GG_API", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }

            }
        };
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

//        switch (requestCode) {
//
//            case Utils.REQUEST_CONTACT: {
//
//                // If request is cancelled, the result arrays are empty.
//
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                    FirebaseHelper.sendEvent("LOGIN_GG", "START", "START");
//
//                    {
//                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                            // Do something for Oreo and above versions
//                            pickUserAccount();
//                        } else {
//                            // do something for phones running an SDK before Oreo
//
//                            final Account[] accs = Utils.getAccounts(mAccountManager);
//                            String[] accounts = Utils.getAccountNames(accs);
//                            if (accounts == null || accounts.length == 0) {
//
//
//                                DialogManager.showConfirmDialog(mContext, "Thông báo", getString(R.string.no_account_found), new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        switch (which) {
//                                            case DialogInterface.BUTTON_POSITIVE:
//                                                startAddGoogleAccountIntent(mContext);
//                                                break;
//                                            case DialogInterface.BUTTON_NEGATIVE:
//                                                dialog.dismiss();
//                                                break;
//                                        }
//                                    }
//                                });
//                            } else {
//                                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//                                builder.setTitle(mContext.getString(R.string.title_gg_dialog));
//
//                                builder.setItems(accounts, new Dialog.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialogI, int which) {
//                                        showLoading("", true);
//                                        Bundle options = new Bundle();
//                                        mAccountManager
//                                                .getAuthToken(
//                                                        accs[which],
//                                                        "oauth2:https://www.googleapis.com/auth/plus.login https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile",
//                                                        options, LoginFragment.this.getActivity(), new LoginFragment.OnGGTokenAcquired(), new Handler());
//
//                                    }
//                                });
//
//                                builder.show();
//                            }
//                        }
//
//                    }
//
//                } else {
//
//                    Toast.makeText(mContext, "Bạn cần cung cấp quyền cho ứng dụng. Vui lòng liên hệ GM để được hỗ trợ", Toast.LENGTH_LONG).show();
//
//                }
//
//                return;
//
//            }
//            // other 'case' lines to check for other
//
//            // permissions this app might request
//
//        }

    }

//    private void startAddGoogleAccountIntent(Context context) {
//        Intent addAccountIntent = new Intent(android.provider.Settings.ACTION_ADD_ACCOUNT)
//                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        addAccountIntent.putExtra(Settings.EXTRA_ACCOUNT_TYPES, new String[]{"com.google"});
//        context.startActivity(addAccountIntent);
//    }
//
//    private void pickUserAccount() {
//        try {
//            String[] accountTypes = new String[]{"com.google"};
//            Intent intent = AccountManager.newChooseAccountIntent(null, null,
//                    accountTypes, true, null, null, null, null);
//            Activity activity = getActivity();
//            if (activity == null)
//                return;
//
//            activity.startActivityForResult(intent, Constants.RC_SIGN_IN);
//        } catch (ActivityNotFoundException ex) {
////            logger.debug("Google-play-services are missing? cannot login by google");
//            Toast.makeText(mContext, "Có lỗi xảy ra. Vui lòng thử lại sau !", Toast.LENGTH_SHORT).show();
//        }
//    }
}
