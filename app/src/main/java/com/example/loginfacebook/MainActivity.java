package com.example.loginfacebook;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.goplay.gamesdk.core.GoPlayReceiver;
import com.goplay.gamesdk.core.GoPlaySDK;
import com.goplay.gamesdk.models.GoPlaySession;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MainActivity extends AppCompatActivity {
    private TextView mTvInfo;
    private LoginButton mBtnLoginFacebook;
    private CallbackManager mCallbackManager;
    int RC_SIGN_IN = 0;
    SignInButton signInButton;
    GoogleSignInClient mGoogleSignInClient;


    private GoPlaySDK sdk;
    // private String apiKey = "your api key";

    private String apiKey = "03554252E3734F9C90C8"; // Au2
    private String sandboxApiKey = "c6cd95e0a76cc58eb745c260585da190";
    private YourReceiver receiver;
    private TextView userName;
    private TextView userId;
    private TextView token;
    private TextView screenName;

    private TableLayout table;
    private Button btnLogin;
    private Button btnLogout;

    private Button btnUpdateFastLogin;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCallbackManager = CallbackManager.Factory.create();
        mTvInfo = (TextView) findViewById(R.id.tv_info);
        mBtnLoginFacebook = (LoginButton) findViewById(R.id.login_button);
        signInButton = findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

       




        mBtnLoginFacebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Intent intent = new Intent(MainActivity.this, showActivity.class ) ;
//                startActivity(intent);
//                mTvInfo.setText("User ID: " + loginResult.getAccessToken().getUserId() + "\n" +
//                        "Auth Token: " + loginResult.getAccessToken().getToken());

                Intent intent = new Intent(MainActivity.this,LoginGoogleActivity.class);
                Bundle bundle = new Bundle();
// đóng gói kiểu dữ liệu String, Boolean
                bundle.putString("key_1", "User ID: " + loginResult.getAccessToken().getUserId());

                bundle.putString("key_2",  "Auth Token: " + loginResult.getAccessToken().getToken());
// đóng gói bundle vào intent
                intent.putExtras(bundle);
// start SecondActivity
                startActivity(intent);


            }

            @Override
            public void onCancel() {
                mTvInfo.setText("Login canceled");

            }

            @Override
            public void onError(FacebookException error) {
                mTvInfo.setText("Login failed");
            }
        });


        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.loginfacebook",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        sdk.handleActivityResult(MainActivity.this, requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }

    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        super.onDestroy();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {


    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(MainActivity.this, showActivity.class));
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In Error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_LONG).show();
        }}
    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            startActivity(new Intent(MainActivity.this, showActivity.class));
        }
        super.onStart();
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public class YourReceiver extends GoPlayReceiver {

        @Override
        public void onLoginSuccess(final GoPlaySession user) {
            sdk.showWelcomMessage(user, MainActivity.this);

            // START Sample. REMOVE in RELEASE BUILD
            btnLogin.setVisibility(View.GONE);
            btnLogout.setVisibility(View.VISIBLE);
            btnUpdateFastLogin.setVisibility(View.VISIBLE);

            screenName.setText("Main Screen");
            table.setVisibility(View.VISIBLE);
//            userName.setText(user.userName);
            userName.setText("deviceID: "+ sdk.getDeviceId(MainActivity.this) + ", username: "+ user.userName);
            userId.setText(user.userId);
            token.setText(user.accessToken);
        }

        @Override
        public void onLoginError(String error) {

        }

        @Override
        public void onLogoutSuccess() {
            // START Sample. REMOVE in RELEASE BUILD
            btnLogin.setVisibility(View.VISIBLE);
            btnLogout.setVisibility(View.GONE);
            screenName.setText("Splash Screen");
            table.setVisibility(View.GONE);
            btnUpdateFastLogin.setVisibility(View.GONE);

            userName.setText("");
            userId.setText("");
            token.setText("");
            // End Sample. REMOVE in RELEASE BUILD
        }

        @Override
        public void onLogoutError(String error) {
            Toast.makeText(MainActivity.this, "Logout error" + error, Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onLoginCancel() {
            Toast.makeText(MainActivity.this, "Login cancelled", Toast.LENGTH_SHORT).show();

        }
    }


}
