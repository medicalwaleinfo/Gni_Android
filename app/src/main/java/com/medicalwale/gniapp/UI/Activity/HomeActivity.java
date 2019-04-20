package com.medicalwale.gniapp.UI.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.zagum.speechrecognitionview.RecognitionProgressView;
import com.github.zagum.speechrecognitionview.adapters.RecognitionListenerAdapter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.medicalwale.gniapp.AppConfiguration;
import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.Utilities.IntentParam;
import com.medicalwale.gniapp.Utilities.MyPrefser;
import com.medicalwale.gniapp.Utilities.Utilities;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {

    SpeechRecognizer speech;
    SpeechRecognizer speechHeyGni;
    CircleImageView ivUserImage;
    MyPrefser myPrefser;
    ImageView btnSpeak;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        speech = SpeechRecognizer.createSpeechRecognizer(this);
        speechHeyGni = SpeechRecognizer.createSpeechRecognizer(this);
        myPrefser = new MyPrefser(this);
        requestPermissonAudio();

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

        int[] colors = {
                ContextCompat.getColor(this, R.color.color1),
                ContextCompat.getColor(this, R.color.color2),
                ContextCompat.getColor(this, R.color.color3),
                ContextCompat.getColor(this, R.color.color4),
                ContextCompat.getColor(this, R.color.color5)
        };

        int[] heights = { 20, 24, 18, 23, 16 };

        final RecognitionProgressView recognitionProgressView = (RecognitionProgressView) findViewById(R.id.recognition_view);
        recognitionProgressView.setSpeechRecognizer(speech);

        speechHeyGni.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        recognitionProgressView.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
                startRecognitionGni();
            }

            @Override
            public void onError(int error) {
                speech.destroy();
                speech = null;
                startRecognitionQuery();
            }

        });
        recognitionProgressView.setColors(colors);
        recognitionProgressView.setBarMaxHeightsInDp(heights);
        recognitionProgressView.setCircleRadiusInDp(2);
        recognitionProgressView.setSpacingInDp(2);
        recognitionProgressView.setIdleStateAmplitudeInDp(2);
        recognitionProgressView.setRotationRadiusInDp(10);
        recognitionProgressView.play();


        String str = Utilities.readRawTextFile(this, R.raw.custom_wakeup_call);

        Log.i("HEYGNI",str);


        if(TedPermission.isGranted(HomeActivity.this,Manifest.permission.RECORD_AUDIO))
        {
            startRecognitionQuery();
            recognitionProgressView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startRecognitionQuery();
                }
            }, 50);
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
                    /*startRecognitionQuery();
                    recognitionProgressView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startRecognitionQuery();
                        }
                    }, 50);*/

                    //Show Bottom Dialog here

                }
                else
                {
                    requestPermissonAudio();
                }
            }
        });
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
        if (speech != null) {
            speech.destroy();
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
        speech.startListening(intent);*/


        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speech = SpeechRecognizer.createSpeechRecognizer(this);

        AudioManager am=(AudioManager)getBaseContext().getSystemService(getApplicationContext().AUDIO_SERVICE);
        am.setStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);

        speech.setRecognitionListener(new RecognitionListenerAdapter() {
            @Override
            public void onResults(Bundle results) {
                showResults(results);
                startRecognitionQuery();
            }

            @Override
            public void onError(int error) {
                super.onError(error);
                speech.destroy();
                speech = null;
                startRecognitionQuery();
            }
        });
        intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en");
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);

        //if setting.SpeechEnable
        speech.startListening(intent);
    }

    private void startRecognitionGni() {
        /*Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
        speech.startListening(intent);*/


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
}