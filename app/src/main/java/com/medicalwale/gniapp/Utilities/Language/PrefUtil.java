package com.medicalwale.gniapp.Utilities.Language;

import android.content.Context;


public class PrefUtil {

    public static void setLanguage(LanguageType currentUser, Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        complexPreferences.putObject("user_pref_value", currentUser);
        complexPreferences.commit();
    }



    public static LanguageType getLanguage(Context ctx){
        ComplexPreferences complexPreferences = ComplexPreferences.getComplexPreferences(ctx, "user_pref", 0);
        LanguageType currentUser = complexPreferences.getObject("user_pref_value", LanguageType.class);
        return currentUser;
    }
}
