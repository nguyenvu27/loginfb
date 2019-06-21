package com.goplay.gamesdk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.goplay.gamesdk.common.GoPlayAction;
import com.goplay.gamesdk.models.GoPlaySession;
import com.goplay.gamesdk.utils.Constants;
import com.goplay.gamesdk.widgets.ScoinButton;
import com.goplay.gamesdk.widgets.ScoinEditText;
import com.goplay.gamesdk.R;

public class RegisterSuccessFragment extends BaseFragment implements OnClickListener {
    private ScoinButton btnFinish;
    private ImageButton btnBack;
    private ImageButton btnClose;
    private GoPlaySession user;
    private int type;

    private ScoinEditText editUsername;
    private ScoinEditText editPassword;
    private Bundle args;
    private String username;
    private String password;

    // private final String TAG = getClass().getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable(Constants.SCOIN_USER_SESSION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mParent = inflater.inflate(R.layout.fragment_register_success, container, false);
        processArguments();
        editUsername = ((ScoinEditText) mParent.findViewById(R.id.edt_username));
        editUsername.setText(username);

        editPassword = ((ScoinEditText) mParent.findViewById(R.id.edt_password));
        editPassword.setText(password);

        btnFinish = ((ScoinButton) mParent.findViewById(R.id.btn_finish));
        btnBack = ((ImageButton) mParent.findViewById(R.id.btn_back));
        btnClose = ((ImageButton) mParent.findViewById(R.id.btn_close));
        btnClose.setVisibility(View.INVISIBLE);
        btnFinish.setOnClickListener(this);
        btnBack.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        return mParent;

    }

    @Override
    protected void initVariables() {

    }

    @Override
    protected void initActions() {

    }

    private void processArguments() {
        if (getArguments() != null) {
            args = getArguments();
            if (args.containsKey(Constants.SCOIN_USER_NAME)) {

                username = args.getString(Constants.SCOIN_USER_NAME);
            }
            if (args.containsKey(Constants.SCOIN_USER_PW)) {

                password = args.getString(Constants.SCOIN_USER_PW);
            }
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        int id = v.getId();

        if (id == R.id.btn_finish) {
            if (type == Constants.SCOIN_LOGIN_UPDATE) {
                Intent broadcast = new Intent(GoPlayAction.LOGIN_SUCCESS_ACTION);
                Bundle b = new Bundle();
                b.putParcelable(Constants.SCOIN_USER_SESSION, user);
                broadcast.putExtras(b);
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(broadcast);
                getActivity().finish();
            }

        } else if (id == R.id.btn_back) {
            getFragmentManager().popBackStack();
        }
    }

}
