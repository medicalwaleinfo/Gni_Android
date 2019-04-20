package com.medicalwale.gniapp.Utilities;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.medicalwale.gniapp.R;

public class NoInternetConnectionHandler {
    private Context activity;
    private OnRefreshClickInterface onRefreshClickInterface;
    private LinearLayout linearLayout;
    private View bNoInternetConnection;

    public NoInternetConnectionHandler(Activity activity, final LinearLayout linearLayout, int StringColorResource, String refrshButtonText, final OnRefreshClickInterface onRefreshClickInterface) {
        this.activity = activity;
        this.onRefreshClickInterface = onRefreshClickInterface;

        this.linearLayout = linearLayout;
        this.linearLayout.setVisibility(View.VISIBLE);

        this.bNoInternetConnection = (Button) linearLayout.findViewById(R.id.btnRefresh);
        this.bNoInternetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                NoInternetConnectionHandler.this.onRefreshClickInterface.onRefresh();
            }
        });

        //All customizations goes here
        TextView messageString;
        messageString = (TextView) linearLayout.findViewById(R.id.tvNoDataFound);
        messageString.setTextColor(StringColorResource);

        Button refreshButton;
        refreshButton = (Button) linearLayout.findViewById(R.id.btnRefresh);
        refreshButton.setText(refrshButtonText);
    }

    public NoInternetConnectionHandler(Context activity, final LinearLayout linearLayout, final OnRefreshClickInterface onRefreshClickInterface) {
        this.activity = activity;
        this.onRefreshClickInterface = onRefreshClickInterface;

        this.linearLayout = linearLayout;
        this.linearLayout.setVisibility(View.VISIBLE);

        this.bNoInternetConnection = (View) linearLayout.findViewById(R.id.btnRefresh);
        this.bNoInternetConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.setVisibility(View.GONE);
                NoInternetConnectionHandler.this.onRefreshClickInterface.onRefresh();
            }
        });
    }

    public interface OnRefreshClickInterface {
        void onRefresh();
    }
}