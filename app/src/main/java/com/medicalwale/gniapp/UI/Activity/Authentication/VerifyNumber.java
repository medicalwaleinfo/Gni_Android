package com.medicalwale.gniapp.UI.Activity.Authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.UI.Activity.HomeActivity;
import com.medicalwale.gniapp.Utilities.MyPrefser;
import com.medicalwale.gniapp.Utilities.NetworkUtilities;
import com.medicalwale.gniapp.Utilities.PreferenceManager;


public class VerifyNumber extends AppCompatActivity {

    private EditText etVerifyNumber;
    PreferenceManager preferenceManager;
    Button login_email;
    Task<Void> task;
    String enterMobileNumber;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new MyPrefser(this).get(MyPrefser.isUserLogin,Boolean.class,false))
        {
            finish();

            new MyPrefser(this).setUserLogin(true);
            Bundle bundle = new Bundle();
            Intent i = new Intent(this, HomeActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtras(bundle);
            startActivity(i);
            finish();
            //Toast.makeText(this, "alredy login!!!", Toast.LENGTH_SHORT).show();

        }
        else{
        setContentView(R.layout.verify_number_layout);
        preferenceManager = new PreferenceManager(this);
        SmsRetrieverClient client = SmsRetriever.getClient(VerifyNumber.this);
        task = client.startSmsRetriever();

        Button btnVerifyingOTP = (Button) findViewById(R.id.btnVerifyingOTP);
        etVerifyNumber = (EditText) findViewById(R.id.etVerifyNumber);
        login_email = (Button) findViewById(R.id.login_email);
//        login_email.setVisibility(View.GONE);
        if (btnVerifyingOTP != null) {
            btnVerifyingOTP.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String number = etVerifyNumber.getText().toString();
                    if(new NetworkUtilities(VerifyNumber.this).haveNetworkConnection()){
                    if (number.length() == 10) {
                        enterMobileNumber=number;
                        Intent intentVerifyOTP = new Intent(VerifyNumber.this, VerifyOTP.class);
                        intentVerifyOTP.putExtra("MobileNumber",enterMobileNumber);
                        startActivity(intentVerifyOTP);
                        finish();
                    }  else{
                        Toast.makeText(VerifyNumber.this, "Please enter valid 10 digit mobile number", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(VerifyNumber.this,""+getResources().getString(R.string.no_internet_available), Toast.LENGTH_SHORT).show();
                }
                }
            });
        }

        login_email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(VerifyNumber.this, EmailLoginMainScreen.class);
                startActivity(intent);
            }
        });

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Successfully started retriever, expect broadcast intent
                // ...
                Log.d("GETSMS","start");
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("GETSMS","fail");
                // Failed to start retriever, inspect Exception for more details
                // ...
            }
        });


        boolean fromEnterOTP = getIntent().getBooleanExtra("fromEnterOTP", false);
        if (fromEnterOTP) {
            etVerifyNumber.setText(enterMobileNumber);
        }
    }
    }
}
