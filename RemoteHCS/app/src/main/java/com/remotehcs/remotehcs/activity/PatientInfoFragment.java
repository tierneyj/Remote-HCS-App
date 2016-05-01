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

        name.setText(MainActivity.patient.getPatientData().getFname() + " " + MainActivity.patient.getPatientData().getLname());
        dob.setText(MainActivity.patient.getPatientData().getDob());
        gender.setText(MainActivity.patient.getPatientData().getSex());
        maritalStatus.setText(MainActivity.patient.getPatientData().getStatus());
        email.setText(MainActivity.patient.getPatientData().getEmail());
        address.setText(MainActivity.patient.getPatientData().getAddress());
        cityState.setText(MainActivity.patient.getPatientData().getCity() + ", " + MainActivity.patient.getPatientData().getState() + ", " + MainActivity.patient.getPatientData().getPostal_code());
        contactNumber.setText(MainActivity.patient.getPatientData().getPhone_contact());
        cellPhoneNumber.setText(MainActivity.patient.getPatientData().getPhone_cell());


        // Inflate the layout for this fragment
        return rootView;
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
