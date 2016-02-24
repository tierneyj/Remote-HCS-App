package com.remotehcs.remotehcs;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.graphics.Typeface;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.util.Log;
import android.os.AsyncTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;


public class ViewRecordActivity extends AppCompatActivity {

    private View mInitialView;
    private ContactInfo record;
    private View mRecordView;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        setContentView(R.layout.activity_view_record);

        record = null;

        Typeface typeface = Typeface.createFromAsset(getAssets(), "Montserrat-Regular.ttf" );
        submitButton = (Button)findViewById(R.id.submit_button);
        submitButton.setTypeface(typeface);

        Button sendEmailButton = (Button)findViewById(R.id.send_email_button);
        sendEmailButton.setTypeface(typeface);

        EditText username = (EditText)findViewById(R.id.username);
        username.setTypeface(typeface);

        EditText password = (EditText)findViewById(R.id.password);
        password.setTypeface(typeface);

        mInitialView = findViewById(R.id.initialView);
        mRecordView = findViewById(R.id.recordView);
        mRecordView.setVisibility(View.GONE);
        submitButtonListener();
    }

    public void submitButtonListener() {
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HttpRequestTask().execute();
                    }
                }
        );
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, ContactInfo> {
        @Override
        protected ContactInfo doInBackground(Void... params) {
            try {
                final String url = ("http://52.36.163.49:8000/api/patient-data/3.json/");
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                //newPatient = restTemplate.getForObject(url, patientContainer.class);
                record = restTemplate.getForObject(url, ContactInfo.class);

                return record;
            } catch (Exception e) {
                Log.e("Spring Problem", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(ContactInfo newPatient) {
            displayRecord();
        }
    }

    private void displayRecord () {
        mInitialView.setVisibility(View.GONE);
        mRecordView.setVisibility(View.VISIBLE);
        submitButton.setText(record.getFname() + record.getLname());
        Button emailButton = (Button)findViewById(R.id.send_email_button);
        emailButton.setText(record.getFname() + " " + record.getLname());
        emailButtonListener();
    }

    public void emailButtonListener() {
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EmailPDF pdf = new EmailPDF(record);
                    }
                }
        );
    }
}
