package com.medicalwale.gniapp.Utilities;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicBoolean;

public class Utilities {

    private static final String TIME24HOURS_PATTERN =
            "([01]?[0-9]|2[0-3]):[0-5][0-9]";

    public static void hideKeypadIfOpen(Activity context) {

        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);

        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(context.getCurrentFocus().getWindowToken(), 0);
        } else {

        }
    }


    public static void showSnackbar(View view, String message) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    public static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }

    public static void handleNoInternetConnection(Activity activity) {

    }

    public static void newDateValidation(final EditText etDay, final EditText etMonth, final EditText etYear) {
        final Calendar cal = Calendar.getInstance();
        final int[] day = {0};
        final int[] mon = {0};
        final int[] year = {0};
        String finaldate = "";


        etDay.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etDay.getText().toString().length() == 2)     //size as per your requirement
                {
                    etMonth.requestFocus();
                    day[0] = Integer.parseInt(String.valueOf(etDay.getText().toString()));
                }
                if (before == 2)     //size as per your requirement
                {
                    etMonth.requestFocus();
                }
                if (mon[0] != 0 && mon[0] == 2) {
                    if (day[0] > 0 && day[0] <= 28) {
                        // etDay.setText(day);
                    } else {
                        etDay.setError("Enter Valid Day");
                    }
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub


            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                //////
                String strYears = etYear.getText().toString();
                String strMonth = etMonth.getText().toString();

                if (!TextUtils.equals(strMonth, "") && !TextUtils.equals(strYears, "")) {

                    int months = Integer.parseInt(strMonth);
                    int years = Integer.parseInt(strYears);


                    if (years > 1900 && years <= Calendar.getInstance().get(Calendar.YEAR)) {
                        cal.set(Calendar.YEAR, years);


                        if (months == 2) {
                            if (years % 4 == 0) {
                                if (day[0] > 0 && day[0] <= 29) {
                                    // etDay.setText(day);
                                } else {
                                    etDay.setError("Enter Valid Day");
                                }
                            } else {
                                if (day[0] > 0 && day[0] <= 28) {
                                    // etDay.setText(day);
                                } else {
                                    etDay.setError("Enter Valid Day");
                                }
                            }
                        } else if (months == 1 || months == 3 || months == 5 || months == 7 || months == 8 || months == 10 || months == 12) {
                            if (day[0] > 0 && day[0] <= 31) {
                                //   etDay.setText(day);
                            } else {
                                etDay.setError("Enter Valid Day");
                            }

                        } else if (months == 4 || months == 6 || months == 5 || months == 9 || months == 11) {
                            if (day[0] > 0 && day[0] <= 30) {
                                // etDay.setText(day);
                            } else {
                                etDay.setError("Enter Valid Day");
                            }
                        }

                    }
                }
            }

        });

        etMonth.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (etMonth.getText().toString().length() == 2)     //size as per your requirement
                {
                    etYear.requestFocus();
                    mon[0] = Integer.parseInt(String.valueOf(etMonth.getText().toString()));

                    if (mon[0] >= 1 && mon[0] <= 12) {
                        cal.set(Calendar.MONTH, mon[0] - 1);
                    } else {
                        etMonth.setError("Enter Valid Month");
                    }

                }


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub


            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

        etYear.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

                if (!TextUtils.equals(etYear.getText().toString(), "") && !TextUtils.equals(etYear.getText().toString(), null)) {
                    year[0] = Integer.parseInt(String.valueOf(etYear.getText().toString()));

                    if (year[0] > 1900 && year[0] <= Calendar.getInstance().get(Calendar.YEAR)) {
                        cal.set(Calendar.YEAR, year[0]);
                    } else {
                        etYear.setError("Enter Valid Year");
                    }


                }


            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

                if (year[0] > 1900 && year[0] <= Calendar.getInstance().get(Calendar.YEAR)) {
                    cal.set(Calendar.YEAR, year[0]);

                    String strDay = etDay.getText().toString();
                    int days = Integer.parseInt(strDay);
                    String strMonth = etMonth.getText().toString();
                    int months = Integer.parseInt(strMonth);

                    if (months == 2) {
                        if (year[0] % 4 == 0) {
                            if (days > 0 && days <= 29) {
                                //    etDay.setText(days);
                            } else {
                                etDay.setError("Enter Valid Day");
                            }
                        } else {
                            if (days > 0 && days <= 28) {
                                //  etDay.setText(days);
                            } else {
                                etDay.setError("Enter Valid Day");
                            }
                        }
                    } else if (months == 1 || months == 3 || months == 5 || months == 7 || months == 8 || months == 10 || months == 12) {
                        if (days > 0 && days <= 31) {
                            // etDay.setText(days);
                        } else {
                            etDay.setError("Enter Valid Day");
                        }

                    } else if (months == 4 || months == 6 || months == 5 || months == 9 || months == 11) {
                        if (days > 0 && days <= 30) {
                            // etDay.setText(days);
                        } else {
                            etDay.setError("Enter Valid Day");
                        }
                    }

                }


            }

        });


        etYear.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace

                    if (etYear.getText().toString().length() == 0) {

                        etMonth.requestFocus();
                        etMonth.setSelection(etMonth.getText().toString().length());

                        etYear.clearFocus();

                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //this is for backspace

                    if (etYear.getText().toString().length() == 0) {

                        etMonth.requestFocus();
                        etMonth.setSelection(etMonth.getText().toString().length());

                        etYear.clearFocus();

                    }
                }
                return false;
            }
        });

        etMonth.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_


                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace

                    if (etMonth.getText().toString().length() == 0) {
                        etDay.requestFocus();
                        etDay.setSelection(etDay.getText().toString().length());
                        etMonth.clearFocus();
                        return true;

                    } else if (etMonth.getText().toString().length() > 0) {
                        etMonth.setSelection(etMonth.getText().toString().length());
                    }
                } else if (keyCode == KeyEvent.KEYCODE_BACK) {
                    //this is for backspace

                    if (etMonth.getText().toString().length() == 0) {
                        etDay.requestFocus();
                        etDay.setSelection(etDay.getText().toString().length());

                        etMonth.clearFocus();
                        return true;
                    } else if (etMonth.getText().toString().length() > 0) {
                        etMonth.setSelection(etMonth.getText().toString().length());
                    }
                }
                return false;
            }
        });


    }

    public static String generatedDate(final EditText etDay, final EditText etMonth, final EditText etYear) {
        String finaldate = "";
        if (!TextUtils.equals(etDay.getText().toString(), "") && !TextUtils.equals(etMonth.getText().toString(), "") && !TextUtils.equals(etYear.getText().toString(), "")) {


            String d = (String) etDay.getError();
            String m = (String) etMonth.getError();
            String y = (String) etYear.getError();

            if (TextUtils.equals(d, null) && TextUtils.equals(m, null) && TextUtils.equals(y, null)) {
                finaldate = etDay.getText().toString() + "/" + etMonth.getText().toString() + "/" + etYear.getText().toString();
            } else {
                finaldate = "";
            }
        }
        return finaldate;
    }



    public static void hint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            boolean hint;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 0) {
                    // no text, hint is visible
                    hint = true;
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

                } else if (hint) {
                    // no hint, text is visible
                    hint = false;
                    editText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
/*

    public static boolean isLoggedIn() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public static void FbShare(Activity activity, String shareUrl, String Post) {
        ShareDialog shareDialog = new ShareDialog(activity);  // initialize facebook shareDialog.
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle("Healthwall")
                    .setImageUrl(Uri.parse("https://www.studytutorial.in/wp-content/uploads/2017/02/FacebookLoginButton-min-300x136.png"))
                    .setContentDescription(
                            "This tutorial explains how to integrate Facebook and Login through Android Application")
                    .setContentUrl(Uri.parse(shareUrl))
                    .setQuote(Post)
                    .build();
            shareDialog.show(linkContent);  // Show facebook ShareDialog
        }
    }
*/

    public static String readRawTextFile(Context ctx, int resId)
    {
        InputStream inputStream = ctx.getResources().openRawResource(resId);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1)
            {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            return null;
        }
        return byteArrayOutputStream.toString();
    }



}