package com.remotehcs.remotehcs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;


public class FullscreenActivity extends AppCompatActivity {

    private View mControlsView;
    private View mLogin;
    private boolean mVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_fullscreen);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf" );
        TextView textViewPlus = (TextView)findViewById(R.id.plus_sign);
        textViewPlus.setTypeface(typeface);

        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "Montserrat-Bold.ttf" );
        TextView textViewRemote = (TextView)findViewById(R.id.remote);
        textViewRemote.setTypeface(typeface2);

        TextView textViewHCS = (TextView)findViewById(R.id.hcs);
        textViewHCS.setTypeface(typeface2);

        Typeface typeface3 = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf" );
        Button loginButton = (Button)findViewById(R.id.dummy_button);
        loginButton.setTypeface(typeface3);

        Button submitButton = (Button)findViewById(R.id.submit);
        submitButton.setTypeface(typeface3);

        EditText username = (EditText)findViewById(R.id.username);
        username.setTypeface(typeface3);

        EditText password = (EditText)findViewById(R.id.password);
        password.setTypeface(typeface3);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mLogin = findViewById(R.id.login);
        mLogin.setVisibility(View.GONE);

        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        toggle();
                    }
                }
        );

        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent("com.remotehcs.remotehcs.ViewRecordActivity");
                        startActivity(intent);
                    }
                }
        );


    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {

        mControlsView.setVisibility(View.GONE);
        mLogin.setVisibility(View.VISIBLE);
        mVisible = false;
    }


    @SuppressLint("InlinedApi")
    private void show() {
        mControlsView.setVisibility(View.VISIBLE);
        mLogin.setVisibility(View.GONE);
        mVisible = true;

    }


}
