package com.example.loginfacebook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LoginGoogleActivity extends AppCompatActivity {
    private TextView mTvInfo;
    private TextView tvId;
    private TextView tvId2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_google);

        tvId = (TextView) findViewById(R.id.tv_show) ;
        tvId2 = (TextView) findViewById(R.id.tv_show2) ;

//
        Bundle bundle = getIntent().getExtras();
        String title = bundle.getString("key_1");
        String title2 = bundle.getString("key_2");

        tvId.setText(title );
        tvId2.setText(title2 );
    }
}
