package com.medicalwale.gniapp.Utilities;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;

import com.medicalwale.gniapp.AppServices.SMSRetriverAPI.AppSignatureHelper;
import com.medicalwale.gniapp.UI.Activity.HomeActivity;

import java.util.ArrayList;

public class AppApplication extends Application {

    public static final int INSTAGRAM_IMAGE_SIZE = 640;

    public static String HASH_KEY = "HASHKEY";

    public static final int INSTAGRAM_AVATAR_SIZE = 150;

    public static boolean mPrefetchImages;

    private static AppApplication mainApplication;


    @Override
    public void onCreate() {
        super.onCreate();
        mainApplication = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        AppSignatureHelper appSignature = new AppSignatureHelper(this);
        ArrayList<String> hashList = appSignature.getAppSignatures();
        Log.i("hashList","hashList size = "+hashList.size()+" hash0 = "+hashList.get(0));
        HASH_KEY=hashList.get(0);


    }


    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    public static synchronized AppApplication getInstance() {
        return mainApplication;
    }


}

