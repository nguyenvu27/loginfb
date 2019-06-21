package com.goplay.gamesdk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.goplay.gamesdk.fragments.LoginFragment;
import com.goplay.gamesdk.utils.Utils;

public class AuthenActivity extends AppCompatActivity {
    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_base);

        if (savedInstanceState == null) {

            data = getIntent().getExtras();
//            if (data != null && data.containsKey(Constants.SCOIN_APP_STATUS)) {
////                String temp = data.getString(Constants.SCOIN_APP_STATUS);
////                if (TextUtils.equals(temp, "InReview")) {
////                    InReviewLoginFragment loginFm = new InReviewLoginFragment();
////                    FragmentManager fragmentManager = getSupportFragmentManager();
////                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////                    loginFm.setArguments(data);
////                    fragmentTransaction.add(R.id.container, loginFm).commit();
////                } else
////                    if (TextUtils.equals(temp, "Release")) {
//                LoginFragment loginFm = new LoginFragment();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                loginFm.setArguments(data);
//                fragmentTransaction.add(R.id.container, loginFm).commit();
////                }
//            } else {
            LoginFragment loginFm = new LoginFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            loginFm.setArguments(data);
            fragmentTransaction.add(R.id.container, loginFm).commit();
//            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // ==== modify by netmarble ====
//        Intent broadcast = new Intent(GoPlayAction.LOGIN_CANCEL_ACTION);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcast);
        // ===============================
    }

    @SuppressLint("Override")
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if (requestCode == Utils.REQUEST_CONTACT) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            Fragment fragment = fragmentManager.findFragmentById(R.id.container);
//            if (fragment != null && fragment instanceof LoginFragment) {
//                fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
//            }
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment != null && fragment instanceof LoginFragment) {
            fragment.onActivityResult(requestCode, resultCode, data);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
