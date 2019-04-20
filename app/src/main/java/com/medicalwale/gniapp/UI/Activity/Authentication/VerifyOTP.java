package com.medicalwale.gniapp.UI.Activity.Authentication;

import android.Manifest;
import android.accounts.NetworkErrorException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.pwittchen.prefser.library.rx2.Prefser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.medicalwale.gniapp.AppConfiguration;
import com.medicalwale.gniapp.Model.UserBean;
import com.medicalwale.gniapp.Model.VerifyOTPResponse;
import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.UI.Activity.HomeActivity;
import com.medicalwale.gniapp.Utilities.AppApplication;
import com.medicalwale.gniapp.Utilities.IntentParam;
import com.medicalwale.gniapp.Utilities.MyPrefser;
import com.medicalwale.gniapp.Utilities.NetworkUtilities;
import com.medicalwale.gniapp.Utilities.NoInternetConnectionHandler;
import com.medicalwale.gniapp.WebAPI.ApiClientMain;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class VerifyOTP extends AppCompatActivity {

    String user_otp;
    Timer timer = new Timer();
    boolean is_sms;
    Context context;
    OTPResponseReceiver receiver;
    LinearLayout main_layout_id;
    private TextView tvCreatingOtp;
    private TextView tvSendingOtp;
    private TextView tvWaitingOtp;
    private TextView tvVerifyingOtp;
    private boolean isRegisterReciever;
    //private String numberString;
    String enterMobileNumber;
    MyPrefser myPrefser;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verifying_otp_layout);
        context = this;

        enterMobileNumber = getIntent().getExtras().getString("MobileNumber");

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CAMERA)
                .check();
        myPrefser = new MyPrefser(this);
        main_layout_id = (LinearLayout) findViewById(R.id.main_layout_id);
        tvCreatingOtp = (TextView) findViewById(R.id.tvCreatingOtp);
        tvSendingOtp = (TextView) findViewById(R.id.tvSendingOtp);
        tvWaitingOtp = (TextView) findViewById(R.id.tvWaitingOtp);
        tvVerifyingOtp = (TextView) findViewById(R.id.tvVerifyingOtp);
        if (enterMobileNumber != null && new NetworkUtilities(this).haveNetworkConnection()) {
            main_layout_id.setVisibility(View.VISIBLE);
            callAPI();

        } else if (!new NetworkUtilities(this).haveNetworkConnection()) {

            main_layout_id.setVisibility(View.GONE);
            new NoInternetConnectionHandler(this, (LinearLayout) findViewById(R.id.ll_no_internet_available), new NoInternetConnectionHandler.OnRefreshClickInterface() {
                @Override
                public void onRefresh() {
                    if (new NetworkUtilities(VerifyOTP.this).haveNetworkConnection()) {
                        main_layout_id.setVisibility(View.VISIBLE);
                        ((LinearLayout) findViewById(R.id.ll_no_internet_available)).setVisibility(View.GONE);
                        callAPI();
                    } else {
                        main_layout_id.setVisibility(View.GONE);
                    }

                }
            });
        }

        TextView numberTextView = (TextView) findViewById(R.id.numberTextView);
        if (numberTextView != null) {
            numberTextView.setText("+91 " + enterMobileNumber);
        }
    }

    public void recivedSms(String message) {
        try {
//            Toast.makeText(VerifyOTP.this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //smsVerifyCatcher.onStop();
    }

    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            Toast.makeText(VerifyOTP.this, "Permission Granted", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(VerifyOTP.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
        }
    };

    private String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{6}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    UserBean userBean;

    private void callAPI() {

        main_layout_id.setVisibility(View.VISIBLE);
        tvCreatingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.green));
        tvSendingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.text_color));
        String registrationId = FirebaseInstanceId.getInstance().getToken();


        Map<String, String> body = new HashMap<>();
        body.put("phone", enterMobileNumber);
        body.put("token", registrationId);
        body.put("agent", "android");
        body.put("hash_key", AppApplication.HASH_KEY);

        Call<VerifyOTPResponse> response = new ApiClientMain(AppConfiguration.MAIN_URL_AUTH, context, AppConfiguration.isCertificate)
                .getApiClient()
                .getResponseOTP(AppConfiguration.TOKEN, body);


        response.enqueue(new Callback<VerifyOTPResponse>() {

            @Override
            public void onResponse(Call<VerifyOTPResponse> call, final Response<VerifyOTPResponse> response) {

                if(response!=null && response.body()!=null)
                {
                final VerifyOTPResponse jsonResponseOTP = response.body();
                if (jsonResponseOTP.getData() != null) {
                    if (jsonResponseOTP.getStatus() == 200) {

                        Thread thread = new Thread(new MyThread());
                        thread.start();

                        tvCreatingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.text_color));
                        tvSendingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.green));

                        for (UserBean responseOTP : jsonResponseOTP.getData()) {
                            userBean=responseOTP;
                            user_otp = String.valueOf(responseOTP.getOtp_code());
                            myPrefser.put(MyPrefser.UserType,userBean.getUser());
                        }
                    } else {
                        Toast.makeText(VerifyOTP.this, "No Data Found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(VerifyOTP.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(VerifyOTP.this, "Try Again.", Toast.LENGTH_SHORT).show();
            }
            }

            @Override
            public void onFailure(final Call<VerifyOTPResponse> call, Throwable t) {
                main_layout_id.setVisibility(View.VISIBLE);
                if (t instanceof SocketTimeoutException) {

                    Log.d("Throwable", "SocketTimeoutException");

                } else if (t instanceof NetworkErrorException) {
                    Log.d("Throwable", "NetworkErrorException");

                } else if (t instanceof ParseException) {
                    Log.d("Throwable", "ParseException");

                } else if (t instanceof UnknownHostException) {

                    Log.d("Throwable", "UnknownHostException");
                }

                Toast.makeText(context, context.getResources().getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show();

            }
        });
    }


    public void NoConnection() {

        new NoInternetConnectionHandler(this, (LinearLayout) findViewById(R.id.ll_no_internet_available), new NoInternetConnectionHandler.OnRefreshClickInterface() {
            @Override
            public void onRefresh() {
                if (new NetworkUtilities(VerifyOTP.this).haveNetworkConnection()) {
                    main_layout_id.setVisibility(View.VISIBLE);
                    ((LinearLayout) findViewById(R.id.ll_no_internet_available)).setVisibility(View.GONE);
                    callAPI();
                } else {
                    main_layout_id.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();


        IntentFilter broadcastFilter = new IntentFilter(OTPResponseReceiver.OTP_RESPONSE);
        broadcastFilter.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new OTPResponseReceiver();
        isRegisterReciever = true;
        registerReceiver(receiver, broadcastFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (isRegisterReciever) {
                isRegisterReciever = false;
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (isRegisterReciever) {
                isRegisterReciever = false;
                unregisterReceiver(receiver);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public class OTPResponseReceiver extends BroadcastReceiver {

        public static final String OTP_RESPONSE = "com.example.admin.otpResponseCheck.ALL_DONE";

        @Override
        public void onReceive(Context context, Intent intent) {
            String OTP = intent.getStringExtra("otp").trim();

            if (TextUtils.equals(OTP, user_otp)) {
                is_sms = true;

                if (TextUtils.equals(myPrefser.getString(MyPrefser.UserType), "new")) {
                    tvSendingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.text_color));
                    tvWaitingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.text_color));
                    tvWaitingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.text_color));
                    tvVerifyingOtp.setTextColor(VerifyOTP.this.getResources().getColor(R.color.green));

                    try {
                        if (isRegisterReciever) {
                            isRegisterReciever = false;
                            unregisterReceiver(receiver);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Bundle bundle = new Bundle();
                    Intent i = new Intent(VerifyOTP.this, SignUpInformation.class);
                    bundle.putString(IntentParam.MobileNumber, ""+enterMobileNumber);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();

                } else {
                    Log.i("else","ok");
                    myPrefser.setUserLogin(true);
                    Intent i = new Intent(VerifyOTP.this, HomeActivity.class);
                    startActivity(i);
                    finish();

                }
            }
        }
    }

    private class MyThread implements Runnable {

        @Override
        public void run() {
            try {
                Thread.sleep(10000);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {

                myPrefser.saveUserData(userBean);

                if (is_sms) {
                    finish();
                } else {
                    Log.i("else sms","ok");
                    Intent i = new Intent(VerifyOTP.this, EnterOTP.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("OTP", user_otp);
                    bundle.putString("MobileNumber", enterMobileNumber);
                    System.out.println("@@@ OTP: " + user_otp);
                    i.putExtras(bundle);
                    startActivity(i);
                    finish();
                }

            }
        }

    }
}