package com.remotehcs.remotehcs.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.api.LoginResponse;
import com.remotehcs.remotehcs.services.LocationService;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.loginButton);

        loginButtonListener();


    }

    public void loginButtonListener() {
        loginButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Authenticate().execute();
                        Intent intent = new Intent("com.remotehcs.remotehcs.activity.MainActivity");
                        startActivity(intent);
                        //new LocationService(getCurrentFocus().getContext());
                    }
                }
        );
    }

    private void setToken(String token, String username) {
        boolean offlineMode = false;
        Log.d("Joseph", "Token  " + token);
        Intent intent = new Intent("com.remotehcs.remotehcs.activity.MainActivity");
        intent.putExtra("token", token);
        intent.putExtra("user", username);
        intent.putExtra("offlineMode", offlineMode);
        startActivity(intent);
    }

    private void exceptionFound() {
        Toast.makeText(getApplicationContext(), "Incorrect username or password.", Toast.LENGTH_LONG).show();
        //Log.d("Joseph", "Exception");
    }

    private class Authenticate extends AsyncTask<Void, Void, LoginResponse> {

        private EditText usernameEditText;
        private EditText passwordEditText;

        private String username;
        private String password;

        private Throwable exceptionToBeThrown;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            usernameEditText = (EditText) findViewById(R.id.usernameEditText);
            passwordEditText = (EditText) findViewById(R.id.passwordEditText);

            username = usernameEditText.getText().toString();
            password = passwordEditText.getText().toString();
        }

        @Override
        protected LoginResponse doInBackground(Void... params) {

            try {
                MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
                map.add("username", username);
                map.add("password", password);
                //final String url = "https://54.165.25.47/remotehcs/api/token";
                final String url = "http://52.36.163.49:8000/api/token/";


                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

                RestTemplate restTemplate = new RestTemplate();

                final List<HttpMessageConverter<?>> messageConverters = new ArrayList< HttpMessageConverter<?> >();

                messageConverters.add(new FormHttpMessageConverter());
                messageConverters.add(new MappingJackson2HttpMessageConverter());
                messageConverters.add(new StringHttpMessageConverter());

                restTemplate.setMessageConverters(messageConverters);

                LoginResponse response = restTemplate.postForObject(url, request, LoginResponse.class);

                return response;

            } catch (Exception e) {
                Log.d("Joseph", "Login Error");
                Log.e("Joseph", e.getMessage(), e);
                exceptionToBeThrown = e;
                return null;
            }
        }

        @Override
        protected void onPostExecute(LoginResponse response) {
            super.onPostExecute(response);
            if (exceptionToBeThrown != null){
                exceptionFound();
            } else {
                setToken(response.getToken(), username);
            }
        }
    }
}
