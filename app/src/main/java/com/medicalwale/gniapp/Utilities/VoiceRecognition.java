package com.medicalwale.gniapp.Utilities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.widget.Toast;

import com.medicalwale.gniapp.R;

import java.util.ArrayList;

public class VoiceRecognition implements RecognitionListener {
    /* renamed from: a */
    private final Context f14431a;
    /* renamed from: b */
    private SpeechRecognizer f14432b;
    /* renamed from: c */
    private Intent f14433c;
    /* renamed from: d */
    private boolean f14434d;
    /* renamed from: e */
    private C5557a f14435e;

    public enum VoiceCommand {
        ERROR,
        LISTENING_STARTED,
        SPEECH_STARTED,
        SPEECH_ENDED,
        RESULT
    }

    /* renamed from: com.managers.VoiceRecognition$a */
    public interface C5557a {
        /* renamed from: a */
        void mo9944a(VoiceCommand voiceCommand, String str);

        /* renamed from: a */
        void mo9946a(ArrayList<String> arrayList);
    }

    public void onBufferReceived(byte[] bArr) {
    }

    public void onEvent(int i, Bundle bundle) {
    }

    public void onPartialResults(Bundle bundle) {
    }

    public void onRmsChanged(float f) {
    }

    public VoiceRecognition(Context context) {
        this.f14431a = context;
        m17278d();
        m17279e();
    }

    /* renamed from: a */
    public void m17282a(C5557a c5557a) {
        this.f14435e = c5557a;
    }

    /* renamed from: d */
    private void m17278d() {
        this.f14433c = new Intent("android.speech.action.RECOGNIZE_SPEECH");
        this.f14433c.putExtra("android.speech.extra.LANGUAGE_PREFERENCE", "en");
        this.f14433c.putExtra("calling_package", this.f14431a.getPackageName());
        this.f14433c.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
        this.f14433c.putExtra("android.speech.extra.LANGUAGE", "en-US");
        this.f14433c.putExtra("android.speech.extra.MAX_RESULTS", 3);
    }

    /* renamed from: e */
    private void m17279e() {
        this.f14432b = SpeechRecognizer.createSpeechRecognizer(this.f14431a, ComponentName.unflattenFromString("com.google.android.googlequicksearchbox/com.google.android.voicesearch.serviceapi.GoogleRecognitionService"));
        this.f14432b.setRecognitionListener(this);
    }

    /* renamed from: a */
    public void m17281a() {
        if (this.f14432b != null) {
            this.f14432b.startListening(this.f14433c);
            this.f14434d = true;
        }
        //C2061u.a().a("VoiceInteraction", "Listening", "Start");
    }

    /* renamed from: b */
    public boolean m17283b() {
        return this.f14434d;
    }

    /* renamed from: c */
    public void m17284c() {
        if (this.f14432b != null) {
            this.f14432b.destroy();
        }
    }

    public void onBeginningOfSpeech() {
        m17277a(VoiceCommand.SPEECH_STARTED, null);
    }

    public void onEndOfSpeech() {
        this.f14434d = false;
        m17277a(VoiceCommand.SPEECH_ENDED, null);
    }

    public void onError(int i) {
        this.f14434d = false;
        m17277a(VoiceCommand.ERROR, m17280a(i));
    }

    /* renamed from: a */
    private void m17277a(VoiceCommand voiceCommand, String str) {
        if (this.f14435e != null) {
            this.f14435e.mo9944a(voiceCommand, str);
        }
    }

    public void onReadyForSpeech(Bundle bundle) {
        m17277a(VoiceCommand.SPEECH_STARTED, null);
    }

    public void onResults(Bundle bundle) {
        /*bundle = bundle.getStringArrayList("results_recognition");
        if (this.f14435e != null) {
            this.f14435e.mo9946a(bundle);
        }*/
        Toast.makeText(f14431a, "onResults", Toast.LENGTH_SHORT).show();
    }

    /* renamed from: a */
    public String m17280a(int i) {
        String string = "Oops";
        String string2 = "Check your network connection and try again";
        switch (i) {
            case 1:
                return string2;
            case 2:
                return string2;
            case 3:
                return string;
            case 4:
                return string;
            case 5:
                return string;
            case 6:
                return string;
            case 7:
                return string;
            case 8:
                return string;
            case 9:
                return string;
            default:
                return string;
        }
    }
}