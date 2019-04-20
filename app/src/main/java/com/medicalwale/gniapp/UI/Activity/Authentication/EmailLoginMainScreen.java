package com.medicalwale.gniapp.UI.Activity.Authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.medicalwale.gniapp.AppConfiguration;
import com.medicalwale.gniapp.Model.VerifyOTPResponse;
import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.Utilities.MyPrefser;
import com.medicalwale.gniapp.WebAPI.ApiClientMain;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Admin on 8/14/2018.
 */

public class EmailLoginMainScreen extends AppCompatActivity {

    EditText email,password;
    Button login;
    MyPrefser myPrefser;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email_login_layout);
        context = this;
        myPrefser = new MyPrefser(context);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        password.setVisibility(View.GONE);
        login = (Button) findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidEmail(email.getText().toString().trim())) {
                    emailLogin(email.getText().toString().trim());
                }else{
                    Toast.makeText(context, "Please Fill Right Information", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void emailLogin(String email) {


        String registrationId = FirebaseInstanceId.getInstance().getToken();

        Map<String, String> body = new HashMap<>();
        body.put("phone", "");
        body.put("token",registrationId);
        body.put("mail",email);
        body.put("agent","android");
        body.put("is_mail","android_mail");
        body.put("password","");


        Call<VerifyOTPResponse> response = new ApiClientMain(AppConfiguration.MAIN_URL_INDEX, EmailLoginMainScreen.this, AppConfiguration.isCertificate)
                .getApiClient()
                .getResponseEmailLogin(AppConfiguration.TOKEN, body);


        response.enqueue(new Callback<VerifyOTPResponse>() {

            @Override
            public void onResponse(Call<VerifyOTPResponse> call, final Response<VerifyOTPResponse> response) {

                VerifyOTPResponse emailLoginMainModel = response.body();

                if (emailLoginMainModel.getStatus() == 200) {

                    myPrefser.saveUserData(emailLoginMainModel.getData().get(0));

                    myPrefser.put("Login_Email", String.valueOf(emailLoginMainModel.getData().get(0).getEmail()));
                    myPrefser.put("is_mail", String.valueOf(emailLoginMainModel.getData().get(0).getIs_mail()));
                    myPrefser.put(MyPrefser.UserType,emailLoginMainModel.getData().get(0).getUser());
                    Intent intentVerifyOTP = new Intent(EmailLoginMainScreen.this,
                            EnterOTP.class);
                    intentVerifyOTP.putExtra("Login","EmailLogin");
                    Bundle bundle = new Bundle();
                    bundle.putString("OTP",""+emailLoginMainModel.getData().get(0).getOtp_code());
                    bundle.putString("MobileNumber",""+emailLoginMainModel.getData().get(0).getEmail());
                    intentVerifyOTP.putExtras(bundle);
                    startActivity(intentVerifyOTP);

                }

                else {
                    Toast.makeText(EmailLoginMainScreen.this, "No Data Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(final Call<VerifyOTPResponse> call, Throwable t) {

            }
        });
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}


