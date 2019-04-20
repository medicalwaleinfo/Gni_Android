package com.medicalwale.gniapp.UI.Activity.Authentication;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.medicalwale.gniapp.AppConfiguration;
import com.medicalwale.gniapp.Model.JsonResponseSignUp;
import com.medicalwale.gniapp.R;
import com.medicalwale.gniapp.UI.Activity.HomeActivity;
import com.medicalwale.gniapp.Utilities.Commons;
import com.medicalwale.gniapp.Utilities.IntentParam;
import com.medicalwale.gniapp.Utilities.MyPrefser;
import com.medicalwale.gniapp.Utilities.NetworkUtilities;
import com.medicalwale.gniapp.Utilities.Utilities;
import com.medicalwale.gniapp.WebAPI.ApiClientMain;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpInformation extends AppCompatActivity implements View.OnClickListener {

    String enterMobileNumber;
    EditText mobileEditText,nameEditText,etDay,etMonth,etYear,emailEditText;
    ImageView profileImage;
    private LinearLayout femaleLayout, maleLayout;
    private TextView tvMale;
    private TextView tvFemale;
    private ImageView ivMale;
    private ImageView ivFemale;
    private ImageView imgSelectProfileImage,getProfileImage;
    Button submitButton, backButton;
    private boolean isLeapYearCalculated = false;
    String[] date;
    String gender = "";
    MyPrefser myPrefser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_information);
        initView();

    }

    public void initView() {

        enterMobileNumber = getIntent().getExtras().getString(IntentParam.MobileNumber);

        myPrefser = new MyPrefser(this);

        etDay = (EditText) findViewById(R.id.etDay);
        etMonth = (EditText) findViewById(R.id.etMonth);
        etYear = (EditText) findViewById(R.id.etYear);

        profileImage = (ImageView) findViewById(R.id.profileImage);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        LinearLayout llEmailLayout = (LinearLayout) findViewById(R.id.llEmailLayout);
        llEmailLayout.setVisibility(View.VISIBLE);
        TextView tvEmailLabel = (TextView) findViewById(R.id.tvEmailLabel);
        tvEmailLabel.setVisibility(View.VISIBLE);

        EditText emailEditText = (EditText) findViewById(R.id.emailEditText);

        Picasso.with(this).load(R.drawable.user_img).into((ImageView) findViewById(R.id.getProfileImage));


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        String date1 = simpleDateFormat.format(calendar.getTime());
        date = date1.split("/");

        etDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    etDay.setSelection(charSequence.toString().length());
                    etMonth.setFocusable(true);
                    etMonth.setFocusableInTouchMode(true);
                    if (!etMonth.getText().toString().isEmpty()) {
                        etYear.setFocusable(true);
                        etYear.setFocusableInTouchMode(true);
                    }

                    if (charSequence.toString().length() == 2) {
                        if (Integer.parseInt(charSequence.toString()) > 31 || Integer.parseInt(charSequence.toString()) <= 0) {
                            etDay.setError("Enter Valid Day");
                        } else {
                            if (isLeapYearCalculated) {
                                etYear.setFocusable(true);
                                etYear.setFocusableInTouchMode(true);
                                switch (etMonth.getText().toString()) {
                                    case "01":
                                        validateOddMonth();
                                        break;
                                    case "02":
                                        validateFebMonth();
                                        break;
                                    case "03":
                                        validateOddMonth();
                                        break;
                                    case "04":
                                        validateEvenMonth();
                                        break;
                                    case "05":
                                        validateOddMonth();
                                        break;
                                    case "06":
                                        validateEvenMonth();
                                        break;
                                    case "07":
                                        validateOddMonth();
                                        break;
                                    case "08":
                                        validateOddMonth();
                                        break;
                                    case "09":
                                        validateEvenMonth();
                                        break;
                                    case "10":
                                        validateOddMonth();
                                        break;
                                    case "11":
                                        validateEvenMonth();
                                        break;
                                    case "12":
                                        validateOddMonth();
                                        break;
                                    default:
                                        etMonth.setError("Enter Valid Month");
                                        break;
                                }
                            } else {
                                etDay.clearFocus();
                                etMonth.requestFocus();
                            }

                        }
                    } else {
                        etDay.setError("Enter Valid Day");
                    }

                } else {
                    etDay.setError("Enter Valid Day");
                    etMonth.setFocusable(false);
                    etYear.setFocusable(false);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (etDay.getText().length() < 2) {
                        etDay.setError("Enter 2 Digit Day Example 01");
                    }
                }
            }
        });

        etMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!charSequence.toString().isEmpty()) {
                    etMonth.setSelection(charSequence.toString().length());
                    etYear.setFocusableInTouchMode(true);
                    etYear.setFocusable(true);
                    if (charSequence.toString().length() == 2) {
                        if (Integer.parseInt(charSequence.toString()) > 12) {
                            etMonth.setError("Enter Valid Month");
                        } else {
                            if (!isLeapYearCalculated) {
                                switch (charSequence.toString()) {
                                    case "01":
                                        validateOddMonth();
                                        break;
                                    case "02":
                                        etMonth.clearFocus();
                                        etYear.requestFocus();
                                        break;
                                    case "03":
                                        validateOddMonth();
                                        break;
                                    case "04":
                                        validateEvenMonth();
                                        break;
                                    case "05":
                                        validateOddMonth();
                                        break;
                                    case "06":
                                        validateEvenMonth();
                                        break;
                                    case "07":
                                        validateOddMonth();
                                        break;
                                    case "08":
                                        validateOddMonth();
                                        break;
                                    case "09":
                                        validateEvenMonth();
                                        break;
                                    case "10":
                                        validateOddMonth();
                                        break;
                                    case "11":
                                        validateEvenMonth();
                                        break;
                                    case "12":
                                        validateOddMonth();
                                        break;
                                    default:
                                        etMonth.setError("Enter Valid Month");
                                        break;
                                }
                            }

                        }
                    } else {
                        etMonth.setError("Enter Valid Month");
                    }

                } else {
                    // etMonth.clearFocus();
                    //etDay.requestFocus();
                    etMonth.setError("Enter Valid Month");
                    etYear.setFocusable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {

                    if (etMonth.getText().length() < 2) {
                        etMonth.setError("Enter 2 Digit Month Example 02");
                    }
                }
            }
        });

        etYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().isEmpty()) {
                    //logic while deleting year entry
                    //etYear.clearFocus();
                    //etMonth.requestFocus();
                    etYear.setError("Enter Valid Year");
                } else {
                    etYear.setSelection(charSequence.toString().length());
                    if (charSequence.toString().length() == 4) {

                        Calendar cal = Calendar.getInstance();

                        if (Integer.parseInt(charSequence.toString()) <= 1900 ||
                                Integer.parseInt(charSequence.toString()) > Integer.parseInt(date[2])) {
                            etYear.setError("Enter Valid Year");
                        } else if (etMonth.getText().toString() != null && !etMonth.getText().toString().isEmpty()) {
                            if (etMonth.getText().toString().equals("02")) {
                                validateFebMonth();
                            }
                        }
                    } else {
                        etYear.setError("Enter Valid Year");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    if (etYear.getText().length() < 4) {
                        etDay.setError("Enter Valid Year");
                    }
                }
            }
        });

        //gender = "Male";
        femaleLayout = (LinearLayout) findViewById(R.id.femaleLayout);
        femaleLayout.setOnClickListener(this);

        maleLayout = (LinearLayout) findViewById(R.id.maleLayout);
        maleLayout.setOnClickListener(this);
        maleLayout.setSelected(true);

        ivMale = (ImageView) findViewById(R.id.ivMale);
        ivFemale = (ImageView) findViewById(R.id.ivFemale);
        tvMale = (TextView) findViewById(R.id.tvMale);
        tvFemale = (TextView) findViewById(R.id.tvFemale);


        submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(this);
        backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(this);

        getProfileImage = (ImageView) findViewById(R.id.getProfileImage);
        getProfileImage.setOnClickListener(this);

        imgSelectProfileImage = (ImageView) findViewById(R.id.selectProfileImage);
        imgSelectProfileImage.setOnClickListener(this);

        mobileEditText = (EditText) findViewById(R.id.mobileEditText);
        mobileEditText.setFocusable(true);
        nameEditText.clearFocus();

        maleLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.login_rectangle_drawable));
        femaleLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.login_rectangle_drawable));
        ivMale.setColorFilter(ContextCompat.getColor(this, R.color.text_gray), android.graphics.PorterDuff.Mode.SRC_IN);
        tvMale.setTextColor(ContextCompat.getColor(this, R.color.text_gray));

        ivFemale.setColorFilter(ContextCompat.getColor(this, R.color.text_gray), android.graphics.PorterDuff.Mode.SRC_IN);
        tvFemale.setTextColor(ContextCompat.getColor(this, R.color.text_gray));

            if(myPrefser.getString("Login_Email")!= null) {
                emailEditText.setText(myPrefser.getString("Login_Email"));
            }
            mobileEditText.setText(enterMobileNumber);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.getProfileImage:
            case R.id.selectProfileImage:
                //ChooseAction(getActivity());
                break;

            case R.id.femaleLayout:

                gender = "Female";
                updateGenderUi(gender);
                break;
            case R.id.maleLayout:
                gender = "Male";
                updateGenderUi(gender);
                break;
            case R.id.submitButton:
                Utilities.hideKeypadIfOpen((this));
                callMethodSubmit();
                break;
            case R.id.backButton:
                onBackPressed();
                break;
        }
    }

    public boolean validateDate() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date date1 = sdf.parse(date[0] + "/" + date[1] + "/" + date[2]);
            Date date2 = sdf.parse(etDay.getText().toString() + "/" + etMonth.getText().toString() + "/" + etYear.getText().toString());
            if (date2.after(date1)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    private void validateOddMonth() {
        if (Integer.parseInt(etDay.getText().toString()) > 31) {
            etDay.setError("Enter Valid Day");
            etDay.setFocusable(true);
        } else {
            etYear.requestFocus();
        }
    }

    private void validateEvenMonth() {
        if (Integer.parseInt(etDay.getText().toString()) > 31) {
            etDay.setError("Enter Valid Day");
            etDay.setFocusable(true);
        } else {
            etYear.requestFocus();
        }
    }

    private void validateFebMonth() {

        int year = Integer.parseInt(etYear.getText().toString());
        int day = Integer.parseInt(etDay.getText().toString());
        isLeapYearCalculated = true;
        if (year % 4 == 0) {
            if (day > 29) {
                etDay.setError("Enter Valid Day");
                etDay.requestFocus();
            } else {
                if (!isLeapYearCalculated) {
                    etMonth.clearFocus();
                    etYear.requestFocus();
                } else {
                    etDay.clearFocus();
                    etMonth.requestFocus();
                }
            }

        } else if (year % 400 == 0) {
            if (day > 29) {
                etDay.setError("Enter Valid Day");
                etDay.requestFocus();
            } else {
                etMonth.clearFocus();
                etYear.requestFocus();
            }
        } else if (year % 100 == 0) {
            if (day > 28) {
                etDay.setError("Enter Valid Day");
                etDay.requestFocus();
            } else {
                etMonth.clearFocus();
                etYear.requestFocus();
            }
        } else {
            if (day > 28) {
                etDay.setError("Enter Valid Day");
                etDay.requestFocus();
            } else {
                etMonth.clearFocus();
                etYear.requestFocus();
            }
        }


    }


    public void callMethodSubmit() {

            if (isEmpty(nameEditText)) {
                nameEditText.setError(IntentParam.nameError);
            } else if (isEmpty(emailEditText)) {
                emailEditText.setError(IntentParam.emailError);

            } else if (nameEditText.getText().toString().trim().equalsIgnoreCase(IntentParam.App_UserName)) {
                nameEditText.setError(IntentParam.AppUserNameError);
                //Toast.makeText(this, "name is require!", Toast.LENGTH_SHORT).show();
            }else if (TextUtils.equals(gender, "")) {
                Toast.makeText(this, "" + IntentParam.genderError, Toast.LENGTH_SHORT).show();
            } else {
                if (!isEmpty(emailEditText) && !isValidMail(emailEditText.getText().toString())) {
                    emailEditText.setError(IntentParam.validEmial);
                    Toast.makeText(this, "Please check email", Toast.LENGTH_SHORT).show();
                } else if (etDay.getText().toString().isEmpty() ||
                        etMonth.getText().toString().isEmpty() ||
                        etYear.getText().toString().isEmpty()) {
                    Toast.makeText(this, "Please Enter Date Of Birth", Toast.LENGTH_LONG).show();
                } else if (etDay.getError() != null) {
                    showKeyBoard(etDay);
                } else if (etMonth.getError() != null) {
                    showKeyBoard(etMonth);
                } else if (etYear.getError() != null) {
                    showKeyBoard(etYear);
                } else if (validateDate()) {
                    Toast.makeText(this, "Future Date Is Not Allowed", Toast.LENGTH_LONG).show();
                }
                else if(mobileEditText.getText().toString().trim().length()== 0 ||
                        mobileEditText.getText().toString().trim().length() > 15  ){
                    Toast.makeText(this, "Enter Valid Mobile Number.", Toast.LENGTH_LONG).show();
                }
                else {
                    callAPI();
                }
            }

    }

    private void callAPI() {
        if (new NetworkUtilities(this).haveNetworkConnection()) {

            String number = mobileEditText.getText().toString().trim();
            String registrationId = FirebaseInstanceId.getInstance().getToken();

            Map<String, String> body = new HashMap<>();
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("name", nameEditText.getText().toString());
            builder.addFormDataPart("phone", number);
            builder.addFormDataPart("email", emailEditText.getText().toString().length() == 0 ? "" : emailEditText.getText().toString());
            builder.addFormDataPart("gender", gender);
            builder.addFormDataPart("dob", Utilities.generatedDate(etDay, etMonth, etYear));

            // builder.addFormDataPart("password", passwordEditText.getText().toString());
            // builder.addFormDataPart("filter", base64String);
            builder.addFormDataPart("token", registrationId);
            builder.addFormDataPart("agent", "android");
            //builder.addFormDataPart("media_source", preferenceManager.getPreferenceValues(IntentParam.MEDIA_SOURCE));


            if(myPrefser.getString("is_mail").equals("yes")){
                myPrefser.put("is_mail","no");
                builder.addFormDataPart("is_mail", "android_mail");
            }else{
                myPrefser.put("is_mail","no");
                builder.addFormDataPart("is_mail", "android_phone");
            }



            String token = AppConfiguration.TOKEN;

            /*if (!TextUtils.isEmpty(ChildSingleton.getInstance().getProfileImage())) {
                File file = new File(ChildSingleton.getInstance().getProfileImage());
                RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                //body = MultipartBody.Part.createFormData("profile_pic", file.getName(), reqFile);
                builder.addFormDataPart("image", file.getName(), reqFile);
            }*/
            MultipartBody requestBody = builder.build();

            Call<JsonResponseSignUp> response = new ApiClientMain(AppConfiguration.MAIN_URL_AUTH, this, AppConfiguration.isCertificate)
                    .getApiClient()
                    .getResponseSignUp("usersignup", requestBody);
            response.enqueue(new Callback<JsonResponseSignUp>() {

                @Override
                public void onResponse(Call<JsonResponseSignUp> call, final Response<JsonResponseSignUp> response) {

                    if (response != null && response.body() != null) {
                        final JsonResponseSignUp rsp = response.body();

                        if (rsp.getStatus() == 201 || rsp.getStatus() == 200) {

                            new MyPrefser(SignUpInformation.this).setUserLogin(true);
                Bundle bundle = new Bundle();
                Intent i = new Intent(SignUpInformation.this, HomeActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.putExtras(bundle);
                startActivity(i);
                finish();

                        } else {
                            Toast.makeText(SignUpInformation.this, rsp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SignUpInformation.this, "Please Try Again", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<JsonResponseSignUp> call, Throwable t) {

                    Toast.makeText(SignUpInformation.this, "Please check your network connection and internet permission", Toast.LENGTH_LONG).show();

                }
            });
        }
    }

    public void showKeyBoard(EditText edittext) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edittext.getWindowToken(), 0);
    }

    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    private void updateGenderUi(String gender) {
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.login_rectangle_drawable).mutate();
        GradientDrawable drawable1 = (GradientDrawable) ContextCompat.getDrawable(this, R.drawable.login_rectangle_drawable).mutate();

        if (gender.equals("Male")) {
            drawable.setStroke((int) Commons.pxFromDp(this, 1), ContextCompat.getColor(this, R.color.colorPrimary));
            maleLayout.setBackground(drawable);
            ivMale.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            tvMale.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            ivFemale.setColorFilter(ContextCompat.getColor(this, R.color.text_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            tvFemale.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            drawable1.setStroke((int) Commons.pxFromDp(this, 1), ContextCompat.getColor(this, R.color.text_gray));
            femaleLayout.setBackground(drawable1);
        } else if (gender.equals("Female")) {
            drawable.setStroke((int) Commons.pxFromDp(this, 1), ContextCompat.getColor(this, R.color.colorPrimary));
            femaleLayout.setBackground(drawable);
            ivFemale.setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), android.graphics.PorterDuff.Mode.SRC_IN);
            tvFemale.setTextColor(ContextCompat.getColor(this, R.color.colorPrimary));

            ivMale.setColorFilter(ContextCompat.getColor(this, R.color.text_gray), android.graphics.PorterDuff.Mode.SRC_IN);
            tvMale.setTextColor(ContextCompat.getColor(this, R.color.text_gray));
            drawable1.setStroke((int) Commons.pxFromDp(this, 1), ContextCompat.getColor(this, R.color.text_gray));
            maleLayout.setBackground(drawable1);
        }
    }
}