package com.medicalwale.gniapp.UI.Activity.Authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.UI.Activity.HomeActivity;
import com.medicalwale.gniapp.Utilities.IntentParam;
import com.medicalwale.gniapp.Utilities.MyPrefser;

public class EnterOTP extends AppCompatActivity {

    private EditText enterCodeEditText;
    Bundle bundle;
    Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enter_otp);

        context = this;
        bundle = getIntent().getExtras();
        enterCodeEditText = (EditText) findViewById(R.id.enterCodeEditText);

        TextView numbertv = (TextView) findViewById(R.id.numbertv);
        final ImageView editImageView = (ImageView) findViewById(R.id.editImageView);

        //  Log.e("sunil",bundle.getString("OTP"));
        //  Toast.makeText(context, ""+bundle.getString("OTP"), Toast.LENGTH_SHORT).show();
        Intent intent = getIntent();
        if(intent != null){
            String login = intent.getStringExtra("Login");
            if(login !=null) {
                if (login.equals("EmailLogin")) {
                    numbertv.setVisibility(View.GONE);
                    editImageView.setVisibility(View.GONE);
                }
            }
        }


        if (numbertv != null) {
            numbertv.setText("+91 " + bundle.getString("MobileNumber"));
        }


        if (editImageView != null) {
            editImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EnterOTP.this, VerifyNumber.class);
                    intent.putExtra("fromEnterOTP", true);
                    startActivity(intent);
                    finish();
                }
            });
        }

        final Button resendOTP = (Button) findViewById(R.id.bResendOTP);
        resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intentVerifyOTP = new Intent(EnterOTP.this,
                        VerifyOTP.class);
                startActivity(intentVerifyOTP);
                finish();

            }
        });

        final TextView resendTime = (TextView) findViewById(R.id.resendTime);
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                resendTime.setText("Resend in : " + (millisUntilFinished / 1000) % 60);

            }

            public void onFinish() {
                resendTime.setText("");
                editImageView.setVisibility(View.VISIBLE);
                resendTime.setVisibility(View.GONE);
                resendOTP.setGravity(Gravity.CENTER);
                resendOTP.setEnabled(true);
            }
        }.start();

        Button submitButton = (Button) findViewById(R.id.submitButton);
        if (submitButton != null) {
            submitButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  goMainActivity(enterCodeEditText.getText().toString());
                }
            });
        }

        enterCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 6) {
                    goMainActivity(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void goMainActivity(String text_otp) {

        String bundel_otpn = bundle.getString("OTP"); //String enterOtp = enterCodeEditText.getText().toString().trim();

        if (bundel_otpn != null && !bundel_otpn.isEmpty() && bundel_otpn.equals(text_otp)) {
            if (TextUtils.equals(new MyPrefser(EnterOTP.this).getString(MyPrefser.UserType), "new")) {
                //Intent i = new Intent(EnterOTP.this, InformationActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString(IntentParam.MobileNumber, ""+bundle.getString("MobileNumber"));
                Intent i = new Intent(EnterOTP.this, SignUpInformation.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtras(bundle1);
                startActivity(i);
                finish();
            } else {
                new MyPrefser(context).setUserLogin(true);
                Bundle bundle = new Bundle();
                Intent i = new Intent(EnterOTP.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtras(bundle);
                startActivity(i);
                finish();
                Toast.makeText(context, "Old User", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(EnterOTP.this, "OTP not match...", Toast.LENGTH_SHORT).show();
        }
    }
}