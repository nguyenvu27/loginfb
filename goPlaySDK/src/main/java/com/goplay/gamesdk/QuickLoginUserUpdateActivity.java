package com.goplay.gamesdk;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.goplay.gamesdk.fragments.UserInfoFragmentFastLogin;

public class QuickLoginUserUpdateActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle data = getIntent().getExtras();
		if (savedInstanceState == null) {
			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			UserInfoFragmentFastLogin loginFm = new UserInfoFragmentFastLogin();
			loginFm.setArguments(data);
			fragmentTransaction.add(R.id.container, loginFm).commit();
		}
		// ViewServer.get(this).addWindow(this);
	}

}
