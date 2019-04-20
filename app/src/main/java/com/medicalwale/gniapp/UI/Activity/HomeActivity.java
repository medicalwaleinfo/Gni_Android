package com.medicalwale.gniapp.UI.Activity;

import android.Manifest;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.Utilities.MyPrefser;
import com.medicalwale.gniapp.Utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    SpeechRecognizer speechQuery;
    SpeechRecognizer speechHeyGni;
    CircleImageView ivUserImage;
    MyPrefser myPrefser;
    ImageView btnSpeak;
    String wordingForWakeUpCall="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        requestPermissonAudio();

        speechHeyGni = SpeechRecognizer.createSpeechRecognizer(this);
        speechQuery = SpeechRecognizer.createSpeechRecognizer(this);
        myPrefser = new MyPrefser(this);

        wordingForWakeUpCall = Utilities.readRawTextFile(this, R.raw.custom_wakeup_call);

        setUserProfile();


        Log.i("HEYGNI",wordingForWakeUpCall);


        if(TedPermission.isGranted(HomeActivity.this,Manifest.permission.RECORD_AUDIO))
        {
            startRecognitionGni();
        }
        else
        {
            requestPermissonAudio();
        }

        btnSpeak = (ImageView) findViewById(R.id.btnSpeak);

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TedPermission.isGranted(HomeActivity.this,Manifest.permission.RECORD_AUDIO))
                {
                    //Show Bottom Dialog here
                    startRecognitionQuery();
                }
                else
                {
                    requestPermissonAudio();
                }
            }
        });
    }

    private void setUserProfile() {

        ivUserImage = (CircleImageView) findViewById(R.id.ivUserImage);

        if (!TextUtils.isEmpty(myPrefser.getUserData().getImage())) {

            Picasso.with(this)
                    .load(myPrefser.getUserData().getImage())
                    .placeholder(R.drawable.user_img)
                    .fit()
                    .into(ivUserImage);
        }
        else
        {
            Picasso.with(this)
                    .load("abcd")
                    .placeholder(R.drawable.user_img)
                    .fit()
                    .into(ivUserImage);
        }

    }

    private void requestPermissonAudio() {

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.RECORD_AUDIO)
                .check();

    }

    PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
            //onSpeakClick();
            startRecognitionGni();
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            requestPermissonAudio();
        }
    };

    private void showResults(Bundle results) {
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        Toast.makeText(this, matches.get(0), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onDestroy() {
        if (speechQuery != null) {
            speechQuery.destroy();
        }

        if (speechHeyGni != null) {
            speechHeyGni.destroy();
        }
        super.onDestroy();
    }

    private void startRecognitionQuery() {
        /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        speechQuery.startListening(intent);*/


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechQuery = SpeechRecognizer.createSpeechRecognizer(this);

        AudioManager am=(AudioManager)getBaseContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);

        speechQuery.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
                startRecognitionQuery();
            }

            @Override
            public void onError(int error) {
                super.onError(error);
                speechQuery.destroy();
                speechQuery = null;
                startRecognitionQuery();
            }
        });
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        //if setting.SpeechEnable
        speechQuery.startListening(intent);
    }

    private void startRecognitionGni() {
        /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        speechQuery.startListening(intent);*/


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechHeyGni = SpeechRecognizer.createSpeechRecognizer(this);

        AudioManager am=(AudioManager)getBaseContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);

        speechHeyGni.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                //showResults(results);
                startRecognitionQuery();
            }

            @Override
            public void onError(int error) {
                super.onError(error);
                speechHeyGni.destroy();
                speechHeyGni = null;
                startRecognitionGni();
            }
        });
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        //if setting.SpeechEnable
        speechHeyGni.startListening(intent);
    }

    private void stopRecognitionGni()
    {
        speechHeyGni.stopListening();
    }

    private void stopRecognitionQuery()
    {
        speechQuery.stopListening();
    }

}