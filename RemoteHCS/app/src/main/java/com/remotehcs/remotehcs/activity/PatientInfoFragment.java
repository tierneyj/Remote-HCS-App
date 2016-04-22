package com.remotehcs.remotehcs.activity;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.record.PostResponse;


public class PatientInfoFragment extends Fragment {

    private Button postButton;

    private PostResponse response;

    public PatientInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_patient_info, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.mainfab);
        fab.setVisibility(View.VISIBLE);

        TextView name = (TextView) rootView.findViewById(R.id.nameValue);
        TextView dob = (TextView) rootView.findViewById(R.id.dobValue);
        TextView gender = (TextView) rootView.findViewById(R.id.genderValue);
        TextView maritalStatus = (TextView) rootView.findViewById(R.id.maritalStatusValue);
        TextView email = (TextView) rootView.findViewById(R.id.emailValue);
        TextView address = (TextView) rootView.findViewById(R.id.addressValue);
        TextView cityState = (TextView) rootView.findViewById(R.id.cityStateValue);
        TextView contactNumber= (TextView) rootView.findViewById(R.id.contactNumberValue);
        TextView cellPhoneNumber = (TextView) rootView.findViewById(R.id.cellPhoneValue);

        postButton = (Button) rootView.findViewById(R.id.post);

        name.setText(MainActivity.patient.getFname() + " " + MainActivity.patient.getLname());
        dob.setText(MainActivity.patient.getDob());
        gender.setText(MainActivity.patient.getSex());
        maritalStatus.setText(MainActivity.patient.getStatus());
        email.setText(MainActivity.patient.getEmail());
        address.setText(MainActivity.patient.getAddress());
        cityState.setText(MainActivity.patient.getCity() + ", " + MainActivity.patient.getState() + ", " + MainActivity.patient.getPostal_code());
        contactNumber.setText(MainActivity.patient.getPhone_contact());
        cellPhoneNumber.setText(MainActivity.patient.getPhone_cell());

        postButtonListener();

        // Inflate the layout for this fragment
        return rootView;
    }

    public void postButtonListener() {
        postButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Authenticate().execute();
                        //Log.d("Joseph", MainActivity.patient.toString());
                    }
                }
        );
    }

    private class Authenticate extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {

            try {
                final String url = ("https://www.remotehcs.com/api/test/");
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpEntity<String> request = new HttpEntity(MainActivity.patient.toString());

                //HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

                response = restTemplate.postForObject(url, request, PostResponse.class);

                Log.d("Joseph", response.getMessage());

                return null;

            } catch (Exception e) {
                Log.d("Joseph", "error");
                Log.e("Spring Problem", e.getMessage(), e);
            }

            return null;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
