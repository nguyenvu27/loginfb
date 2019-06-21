package com.goplay.gamesdk.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.goplay.gamesdk.R;
import com.goplay.gamesdk.common.GoPlayAction;
import com.goplay.gamesdk.helper.FirebaseHelper;
import com.goplay.gamesdk.helper.ToastHelper;
import com.goplay.gamesdk.models.GoPlaySession;
import com.goplay.gamesdk.utils.Constants;
import com.goplay.gamesdk.validator.Form;
import com.goplay.gamesdk.validator.Validate;
import com.goplay.gamesdk.validator.validator.NotEmptyValidator;
import com.goplay.gamesdk.validator.validator.RegExpValidator;
import com.goplay.gamesdk.validator.validator.SimplePasswordValidator;
import com.goplay.gamesdk.widgets.DialogManager;
import com.goplay.gamesdk.widgets.ScoinButton;
import com.goplay.gamesdk.widgets.ScoinEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterFragment extends BaseFragment implements OnClickListener {
    private ScoinEditText editUsername;
    private ScoinEditText editPassword;

    private ScoinButton btnRegister;
    private ImageButton btnBack;
    private ImageButton btnClose;

    private TextView btnOwnedAcc;
    private Form form;
    private String username = "";
    private String password = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mParent = inflater.inflate(R.layout.fragment_register, container, false);

        editUsername = ((ScoinEditText) mParent.findViewById(R.id.edt_username));
        editPassword = ((ScoinEditText) mParent.findViewById(R.id.edt_password));

        btnRegister = ((ScoinButton) mParent.findViewById(R.id.btn_register));
        btnBack = ((ImageButton) mParent.findViewById(R.id.btn_back));
        btnClose = ((ImageButton) mParent.findViewById(R.id.btn_close));
        btnOwnedAcc = ((TextView) mParent.findViewById(R.id.lbl));

        btnRegister.setOnClickListener(this);
        btnOwnedAcc.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        return mParent;

    }

    @Override
    protected void initVariables() {
        // TODO Auto-generated method stub

        FirebaseHelper.sendScreenView((Activity) mContext, "REGISTER");

    }

    @Override
    protected void initActions() {
        // TODO Auto-generated method stub
        form = new Form();
        Validate usernameValid = new Validate(editUsername);
        usernameValid.addValidator(new NotEmptyValidator(mContext));
        RegExpValidator userNameRegex = new RegExpValidator(mContext, R.string.validator_user_name_string_length);
        userNameRegex.setPattern("^[A-Za-z0-9._]{4,30}$");
        usernameValid.addValidator(userNameRegex);

        Validate passwordValid = new Validate(editPassword);
        passwordValid.addValidator(new NotEmptyValidator(mContext));
        RegExpValidator regex = new RegExpValidator(mContext, R.string.validator_string_length);
        regex.setPattern("^.{6,16}$");
        passwordValid.addValidator(regex);
        SimplePasswordValidator simplePw = new SimplePasswordValidator(mContext);
        passwordValid.addValidator(simplePw);

        form.addValidates(usernameValid);
        form.addValidates(passwordValid);

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        if (id == R.id.btn_register) {
            if (form.validate()) {
                FirebaseHelper.sendEvent("REGISTER", "START", "START");
                showLoading("", true);
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                nwHelper.register(TAG, username.trim(), password, onRegisterSuccess(), onRegisterError());

            }

        } else if (id == R.id.lbl) {

            getFragmentManager().popBackStack();
        } else if (id == R.id.btn_back) {

            getFragmentManager().popBackStack();

        } else if (id == R.id.btn_close) {
            Intent broadcast = new Intent(GoPlayAction.LOGIN_CANCEL_ACTION);
            LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
            getActivity().finish();
        }
    }

    private Response.Listener<JSONObject> onRegisterSuccess() {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(TAG, response.toString());
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    boolean status = response.getBoolean("status");
                    int errorCode = response.getInt("error_code");
                    int firstLogin = 0;
                    if (response.has("first_login")) {
                        firstLogin = response.getInt("first_login");
                    }
                    if ((status) && (errorCode == 0)) {

                        FirebaseHelper.sendEvent("REGISTER", "SUCCESS", "SUCCESS");

                        GoPlaySession session = GoPlaySession.deserialize(response);

                        preferenceHelper.setSession(session);


//                        if (firstLogin == 1) {
                            showDialog(session);
//                        }


                    } else {
                        String message = response.optString("message");
                        FirebaseHelper.sendEvent("REGISTER", "ERROR ", message);
                        DialogManager.showPositiveDialog(mContext, mContext.getResources().getString(R.string.title_register_err), message, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener onRegisterError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }

                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("REGISTER", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("REGISTER", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }
                ToastHelper.showToastError(mContext, mContext.getString(R.string.maintain_error_message));
            }
        };
    }


    private void showDialog(GoPlaySession user) {
        Fragment updatePhoneFragment = new RegisterSuccessFragment();

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();

        bundle.putString(Constants.SCOIN_USER_NAME, username);
        bundle.putString(Constants.SCOIN_USER_PW, password);
        bundle.putParcelable(Constants.SCOIN_USER_SESSION, user);
        updatePhoneFragment.setArguments(bundle);
        transaction.replace(R.id.container, updatePhoneFragment);
        transaction.addToBackStack(null);

        transaction.commit();

    }


}

