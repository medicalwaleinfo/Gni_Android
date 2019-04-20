package com.medicalwale.gniapp.UI.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.medicalwale.gniapp.R;

public class CustomDialogForCancelAndSubmit extends Dialog {

    public interface OnButtonClickListner {

        void okOKClicked();

        void canceledClicked();
    }

    private String header, message;
    private CustomDialogForCancelAndSubmit.OnButtonClickListner buttonClickListner;
    private boolean onlyPositive = false, closeOnOutsideClick = true;
    private String negativeText;
    private String possitiveText;

    public CustomDialogForCancelAndSubmit(Context context, String header, String message,
                                          CustomDialogForCancelAndSubmit.OnButtonClickListner buttonClickListner) {
        super(context);
        this.header = header;
        this.message = message;
        this.buttonClickListner = buttonClickListner;
        negativeText = "";
        possitiveText = "";

    }


    public CustomDialogForCancelAndSubmit(Activity context, String header, String message,
                                          CustomDialogForCancelAndSubmit.OnButtonClickListner buttonClickListner, boolean onlyPositive) {
        super(context);
        this.header = header;
        this.message = message;
        this.buttonClickListner = buttonClickListner;
        this.onlyPositive = onlyPositive;
        negativeText = "";
        possitiveText = "";

    }

    public CustomDialogForCancelAndSubmit(Activity context, String header, String message,
                                          CustomDialogForCancelAndSubmit.OnButtonClickListner buttonClickListner, boolean onlyPositive, boolean isUpdatePopup) {
        super(context);
        this.header = header;
        this.message = message;
        this.buttonClickListner = buttonClickListner;
        this.onlyPositive = onlyPositive;
        negativeText = "";
        possitiveText = "";


    }

    public CustomDialogForCancelAndSubmit(Activity context, String header, String message,
                                          CustomDialogForCancelAndSubmit.OnButtonClickListner buttonClickListner,
                                          String negativeText, String posiitiveText, boolean onlyPositive, boolean closeOnOutsideClick) {
        super(context);
        this.header = header;
        this.message = message;
        this.buttonClickListner = buttonClickListner;
        this.onlyPositive = onlyPositive;
        this.negativeText = negativeText;
        this.possitiveText = posiitiveText;
        this.closeOnOutsideClick = closeOnOutsideClick;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.ok_cancel_custom_dialog_layout);

        this.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        this.setCanceledOnTouchOutside(closeOnOutsideClick);
        this.setCancelable(closeOnOutsideClick);

        TextView title = (TextView) findViewById(R.id.tvTitle);
        title.setText(header);


        TextView messageTV = (TextView) findViewById(R.id.tvMessage);
        messageTV.setMovementMethod(new ScrollingMovementMethod());
        messageTV.setText(Html.fromHtml(message));

        Button buttonOK = (Button) findViewById(R.id.bOk);
        if (!possitiveText.equalsIgnoreCase(""))
            buttonOK.setText(possitiveText);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListner.okOKClicked();
                CustomDialogForCancelAndSubmit.this.dismiss();
            }
        });

        Button buttonCancel = (Button) findViewById(R.id.bCancel);
        if (onlyPositive) {
            buttonCancel.setVisibility(View.GONE);

        }


        if (!negativeText.equalsIgnoreCase(""))
            buttonCancel.setText(negativeText);

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonClickListner.canceledClicked();
                CustomDialogForCancelAndSubmit.this.dismiss();
            }
        });

    }
}
