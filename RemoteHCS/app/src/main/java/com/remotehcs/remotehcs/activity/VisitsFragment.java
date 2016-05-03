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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.widget.Toolbar;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.record.PatientData;
import com.remotehcs.remotehcs.record.VisitResponse;
import com.remotehcs.remotehcs.record.Visit;

public class VisitsFragment extends Fragment {

    private View visisView;
    private View historyDataView;
    private Spinner spinner;
    private int count = 0;

    public VisitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_visits, container, false);

        visisView = rootView.findViewById(R.id.visitsView);
        historyDataView = rootView.findViewById(R.id.historyDataView);


        spinner = (Spinner) visisView.findViewById(R.id.spinner);

        ArrayList<String> dateList = new ArrayList<>();

        for (int i = 0; i < MainActivity.patient.getVisits().size(); i++) {
            dateList.add(MainActivity.patient.getVisit(i).getDate());
            Log.d("Joseph", MainActivity.patient.getVisit(i).getDate());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinner, dateList);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                TextView smokesValue = (TextView) historyDataView.findViewById(R.id.smokesValue);
                TextView familyDiabetesValue = (TextView) historyDataView.findViewById(R.id.relativesDiabetesValue);
                TextView familyHypertensionValue = (TextView) historyDataView.findViewById(R.id.relativesHypertensionValue);

                TextView heightValue = (TextView) visisView.findViewById(R.id.heightValue);
                TextView weightValue = (TextView) visisView.findViewById(R.id.weightValue);
                TextView bmiValue = (TextView) visisView.findViewById(R.id.bmiValue);
                TextView hypertensionValue = (TextView) visisView.findViewById(R.id.hypertensionValue);
                TextView diabetesValue = (TextView) visisView.findViewById(R.id.diabetesValue);
                TextView pregnantValue = (TextView) visisView.findViewById(R.id.pregnantValue);
                TextView dryMouthValue = (TextView) visisView.findViewById(R.id.dryMouthValue);
                TextView dizzinessValue = (TextView) visisView.findViewById(R.id.dizzinessValue);
                TextView numbnessValue = (TextView) visisView.findViewById(R.id.numbnessValue);
                TextView glucoseValue = (TextView) visisView.findViewById(R.id.glucoseValue);
                TextView bpValue = (TextView) visisView.findViewById(R.id.bpValue);
                TextView pulse = (TextView) visisView.findViewById(R.id.pulseValue);

                smokesValue.setText(MainActivity.patient.getHistoryData().getTobacco());
                familyDiabetesValue.setText(MainActivity.patient.getHistoryData().getRelatives_diabetes());
                familyHypertensionValue.setText(MainActivity.patient.getHistoryData().getRelatives_high_blood_pressure());

                heightValue.setText(Double.toString(MainActivity.patient.getVisit(arg2).getHeight()));
                weightValue.setText(Double.toString(MainActivity.patient.getVisit(arg2).getWeight()));
                bmiValue.setText(Double.toString(MainActivity.patient.getVisit(arg2).getBmi()));
                hypertensionValue.setText(MainActivity.patient.getVisit(arg2).getHigh_blood_pressure());
                diabetesValue.setText(MainActivity.patient.getVisit(arg2).getDiabetes());
                pregnantValue.setText(MainActivity.patient.getVisit(arg2).getPregnant());
                dryMouthValue.setText(MainActivity.patient.getVisit(arg2).getDry_mouth());
                dizzinessValue.setText(MainActivity.patient.getVisit(arg2).getDizziness());
                numbnessValue.setText(MainActivity.patient.getVisit(arg2).getNumbness());
                glucoseValue.setText(MainActivity.patient.getVisit(arg2).getGlucose() + " mmol/L");
                bpValue.setText(MainActivity.patient.getVisit(arg2).getBps() + "/" + MainActivity.patient.getVisit(arg2).getBpd() + " mmHg");
                pulse.setText(MainActivity.patient.getVisit(arg2).getPulse() + " bpm");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {


            }

        });

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

