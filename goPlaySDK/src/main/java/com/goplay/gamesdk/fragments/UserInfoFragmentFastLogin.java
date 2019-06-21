package com.goplay.gamesdk.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.goplay.gamesdk.R;
import com.goplay.gamesdk.helper.FirebaseHelper;
import com.goplay.gamesdk.helper.ToastHelper;
import com.goplay.gamesdk.models.GoPlaySession;
import com.goplay.gamesdk.models.GoPlayUserInfo;
import com.goplay.gamesdk.validator.Form;
import com.goplay.gamesdk.validator.Validate;
import com.goplay.gamesdk.validator.validator.NotEmptyValidator;
import com.goplay.gamesdk.validator.validator.RegExpValidator;
import com.goplay.gamesdk.validator.validator.SimplePasswordValidator;
import com.goplay.gamesdk.widgets.ScoinButton;
import com.goplay.gamesdk.widgets.ScoinEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoFragmentFastLogin extends BaseFragment implements OnClickListener {
    private ScoinEditText editUsername;
    private ScoinEditText editPassword;

    private ScoinButton btnUpdate;

    private Form form;
    private Validate usernameValid;

    private Validate passwordValid;
    private ImageButton btnClose;
    private ImageButton btnBack;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mParent = inflater.inflate(R.layout.fragment_quick_login_user_info, container, false);

        editUsername = ((ScoinEditText) mParent.findViewById(R.id.edt_username));
        editPassword = ((ScoinEditText) mParent.findViewById(R.id.edt_password));

        btnUpdate = ((ScoinButton) mParent.findViewById(R.id.btn_update));

        btnBack = ((ImageButton) mParent.findViewById(R.id.btn_back));
        btnBack.setVisibility(View.INVISIBLE);
        btnClose = ((ImageButton) mParent.findViewById(R.id.btn_close));

        btnUpdate.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        return mParent;

    }

    @Override
    protected void initVariables() {
        // TODO Auto-generated method stub
    }

    @Override
    protected void initActions() {
        // TODO Auto-generated method stub
        form = new Form();
        usernameValid = new Validate(editUsername);
        usernameValid.addValidator(new NotEmptyValidator(mContext));
        RegExpValidator userNameRegex = new RegExpValidator(mContext, R.string.validator_user_name_string_length);
        userNameRegex.setPattern("^[A-Za-z0-9._]{4,30}$");
        usernameValid.addValidator(userNameRegex);

        passwordValid = new Validate(editPassword);
        passwordValid.addValidator(new NotEmptyValidator(mContext));
        RegExpValidator regex = new RegExpValidator(mContext, R.string.validator_string_length);
        regex.setPattern("^.{6,16}$");
        passwordValid.addValidator(regex);
        SimplePasswordValidator simplePw = new SimplePasswordValidator(mContext);
        passwordValid.addValidator(simplePw);
        form.addValidates(passwordValid);

        form.addValidates(usernameValid);
        FirebaseHelper.sendScreenView(getActivity(), "CREATE_USER_NAME_FASTLOGIN");
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();
        if (id == R.id.btn_update) {

            if (form.validate()) {
                if (!((Activity) mContext).isFinishing()) {
                    dialog.show();
                    dialog.setContentView(dl);
                }


                nwHelper.checkUserInfo(TAG, editUsername.getText().toString(), onCheckInfoSuccess(), onCheckInfoError());
//                nwHelper.updateQuickLogIn(TAG, preferenceHelper.getUserId(), editUsername.getText().toString(), editPassword.getText().toString(), "", onUpdateInfoSuccess(), onUpdateInfoError());
            }
        } else if (id == R.id.btn_close) {
            getActivity().finish();
        }
    }


    private Response.Listener<JSONObject> onCheckInfoSuccess() {
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
                        FirebaseHelper.sendEvent("CHECK_USER_INFO", "SUCCESS", "SUCCESS");

                        GoPlayUserInfo user = GoPlayUserInfo.deserialize(response);

                        if (!TextUtils.isEmpty(user.userName)) {
                            if (TextUtils.equals(user.userName, editUsername.getText().toString())) {


                                ToastHelper.showToastError(mContext, "Tài khoản đã tồn tại. Vui lòng kiểm tra lại !");
                                editUsername.setError("Tài khoản đã tồn tại");

                            }


                        }

                    } else {

                        nwHelper.updateQuickLogIn(TAG, preferenceHelper.getUserId(), editUsername.getText().toString(), editPassword.getText().toString(), "", onUpdateInfoSuccess(), onUpdateInfoError());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Response.ErrorListener onCheckInfoError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                // error.printStackTrace();
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("CHECK_USER_INFO", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("CHECK_USER_INFO", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }

                ToastHelper.showToastError(mContext, mContext.getString(R.string.maintain_error_message));
            }
        };
    }


    private Response.Listener<JSONObject> onUpdateInfoSuccess() {
        return new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                logger.info(UserInfoFragmentFastLogin.this.TAG, response.toString());
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                try {
                    boolean status = response.getBoolean("status");
                    int errorCode = response.getInt("error_code");
                    if ((status) && (errorCode == 0)) {

                        GoPlaySession session = GoPlaySession.deserialize(response);
                        if (session != null) {

                            preferenceHelper.setSession(session);
                            FirebaseHelper.sendEvent("CREATE_USERNAME", "SUCCESS", "SUCCESS");
                            ToastHelper.showToastError(mContext, "Tạo tài khoản thành công !");
                            getActivity().finish();
//

                        }
                    } else {
                        String message = response.getString("message");
                        FirebaseHelper.sendEvent("CREATE_USERNAME", "ERROR", message);
                        ToastHelper.showToastError(mContext, message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    private Response.ErrorListener onUpdateInfoError() {
        return new Response.ErrorListener() {
            public void onErrorResponse(VolleyError error) {
                if (dialog != null && !((Activity) mContext).isFinishing() && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    FirebaseHelper.sendEvent("CREATE_USERNAME", "CONNECTION_ERROR", error.getMessage());
                } else {
                    FirebaseHelper.sendEvent("CREATE_USERNAME", "CONNECTION_ERROR", "CONNECTION_ERROR");
                }
                ToastHelper.showToastError(mContext, mContext.getString(R.string.maintain_error_message));
            }

        };
    }

}

