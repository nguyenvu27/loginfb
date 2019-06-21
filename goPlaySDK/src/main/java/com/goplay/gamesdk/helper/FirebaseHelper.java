package com.goplay.gamesdk.helper;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
//import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseHelper {

    // The following line should be changed to include the correct property id.
    private static final String TAG = "FirebaseHelper";


    private static Context sAppContext = null;

    private static FirebaseAnalytics mFirebaseAnalytics;

//	public static synchronized void setTracker(Tracker tracker) {
//		mTracker = tracker;
//	}

    private static boolean canSend() {
        // We can only send Google Analytics when ALL the following conditions
        // are true:
        // 1. This module has been initialized.
        // 2. The user has accepted the ToS.
        // 3. Analytics is enabled in Settings.
        return sAppContext != null && mFirebaseAnalytics != null;
    }

    public static void sendScreenView(Activity activity, String screenName) {
        if (canSend()) {

            mFirebaseAnalytics.setCurrentScreen(activity, screenName, null /* class override */);

        } else {
        }
    }

//    public static void sendRefererUrl(String screenName, String campaignData) {
//        if (canSend()) {
//
//            mTracker.setScreenName(screenName);
//
//            // Campaign data sent with this hit.
//            mTracker.send(new HitBuilders.ScreenViewBuilder().setCampaignParamsFromUrl(campaignData).build());
//
//        } else {
//        }
//    }

    public static void sendEvent(String category, String action, String label, long value) {
        if (canSend()) {


            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, label);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, action);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, category);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        }
    }

    public static void sendEvent(String category, String action, String label) {
        sendEvent(category, action, label, 0);
    }

//    public Tracker getTracker() {
//        return mTracker;
//    }

    public static synchronized void initializeAnalyticsTracker(Context context) {

        try {
            // Obtain the FirebaseAnalytics instance.
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        } catch (Exception e) {
            Log.e(TAG, "Cant to load meta-data, NullPointer: " + e.getMessage());
        }

        sAppContext = context;
    }
}
