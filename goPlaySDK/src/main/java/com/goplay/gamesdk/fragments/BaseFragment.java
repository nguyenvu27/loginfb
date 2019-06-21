package com.goplay.gamesdk.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goplay.gamesdk.R;
import com.goplay.gamesdk.helper.FirebaseHelper;
import com.goplay.gamesdk.helper.GoPlayNetworkHelper;
import com.goplay.gamesdk.helper.GoPlayPreferenceHelper;
import com.goplay.gamesdk.helper.GoPlayLogHelper;

public abstract class BaseFragment extends Fragment {
    protected View mParent;
    protected Context mContext;
    protected GoPlayPreferenceHelper preferenceHelper;
    protected GoPlayLogHelper logger;
    protected GoPlayNetworkHelper nwHelper;
    protected ProgressDialog dialog;
    protected View dl;
    protected final String TAG = getClass().getSimpleName();

    protected abstract void initVariables();

    protected abstract void initActions();

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
        initActions();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = getActivity();
        FirebaseHelper.initializeAnalyticsTracker(this.mContext.getApplicationContext());
        logger = new GoPlayLogHelper(mContext);
        preferenceHelper = GoPlayPreferenceHelper.getInstance().init(mContext);
        nwHelper = GoPlayNetworkHelper.getInstance().init(mContext, preferenceHelper.getApiKey(), preferenceHelper.getSandboxApiKey());

        dialog = new ProgressDialog(this.mContext);
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dl = inflater.inflate(R.layout.progress_dialog_with_bg, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView temp = (TextView) dl.findViewById(R.id.progress_dialog_msg);
        temp.setText(mContext.getString(R.string.loading));
        initVariables();

    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        this.mContext = getActivity();
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDetach() {
        nwHelper.cancelRequests(TAG);
        super.onDetach();
    }

    public void showLoading(String message, boolean cancelable) {
        if (dialog == null) {
            dialog = new ProgressDialog(this.mContext);
            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            dl = inflater.inflate(R.layout.progress_dialog_with_bg, null);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        }
        if (!cancelable) {
            dialog.setCancelable(false);
        }
        if (!((Activity) mContext).isFinishing()) {
            dialog.show();
            TextView temp = (TextView) dl.findViewById(R.id.progress_dialog_msg);
            if (TextUtils.isEmpty(message))
                temp.setText(mContext.getString(R.string.loading));
            else
                temp.setText(message);
            dialog.setContentView(dl);
        }

    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        super.onDestroy();
    }
}