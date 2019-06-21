package com.goplay.gamesdk.core;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.goplay.gamesdk.common.GoPlayAction;
import com.goplay.gamesdk.models.GoPlaySession;
import com.goplay.gamesdk.utils.Constants;

public abstract class GoPlayReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.equals(action, GoPlayAction.LOGIN_SUCCESS_ACTION)) {

            GoPlaySession user = intent.getExtras().getParcelable(Constants.SCOIN_USER_SESSION);

            onLoginSuccess(user);
        } else if (TextUtils.equals(action, GoPlayAction.LOGIN_ERROR_ACTION)) {
            String error = intent.getExtras().getString(Constants.ERROR_STRING);
            onLoginError(error);
        } else if (TextUtils.equals(action, GoPlayAction.LOGOUT_SUCCESS_ACTION)) {
            onLogoutSuccess();
        } else if (TextUtils.equals(action, GoPlayAction.LOGOUT_ERROR_ACTION)) {
            String error = intent.getExtras().getString(Constants.ERROR_STRING);
            onLogoutError(error);
        } else if (TextUtils.equals(action, GoPlayAction.LOGIN_CANCEL_ACTION)) {
            onLoginCancel();
        }
//

    }

    /**
     * Callback when user successfully authenticate with our server. You can do
     * your login game account from this point.
     *
     * @param user this scoin's user object. You can use object properties such
     *             as userId or userName
     **/
    public abstract void onLoginSuccess(GoPlaySession user);

    public abstract void onLoginError(String error);

    public abstract void onLogoutSuccess();

    public abstract void onLogoutError(String error);

    public abstract void onLoginCancel();

}
