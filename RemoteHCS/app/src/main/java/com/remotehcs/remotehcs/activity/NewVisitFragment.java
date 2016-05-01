package com.remotehcs.remotehcs.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.api.LoginResponse;
import com.remotehcs.remotehcs.record.PostRequest;
import com.remotehcs.remotehcs.record.PostResponse;
import com.remotehcs.remotehcs.record.Record;
import com.remotehcs.remotehcs.record.Visit;

import org.apache.http.entity.ContentType;
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

public class NewVisitFragment extends Fragment {

    private ImageButton nextButton;
    private ImageButton previousButton;
    private RadioGroup radioGroup;
    private View[] views;
    private View mGlucoseDisconnectedView;
    private View mBpDisconnectedView;
    private RadioButton[] progress;

    private Button uploadButton;
    private Button glucoseBeginTestButton;
    private Button bpBeginTestButton;

    private Integer position;
    private TextView title;
    private String[] titles;

    public NewVisitFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_new_visit, container, false);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.mainfab);
        fab.setVisibility(View.GONE);

        views = new View[7];
        views[0] = rootView.findViewById(R.id.historyView);
        views[1] = rootView.findViewById(R.id.conditionsView);
        views[2] = rootView.findViewById(R.id.recentConditionsView);
        views[3] = rootView.findViewById(R.id.heightWeightView);
        views[4] = rootView.findViewById(R.id.glucoseView);
        views[5] = rootView.findViewById(R.id.bpView);
        views[6] = rootView.findViewById(R.id.reviewVisitView);

        mGlucoseDisconnectedView = rootView.findViewById(R.id.glucoseDisconnectedView);
        mBpDisconnectedView = rootView.findViewById(R.id.bpDisconnectedView);

        title = (TextView) rootView.findViewById(R.id.titleLabel);

        titles = new String[7];
        titles[0] = getString(R.string.medical_history);
        titles[1] = getString(R.string.medical_conditions);
        titles[2] = getString(R.string.medical_conditions);
        titles[3] = getString(R.string.height_weight);
        titles[4] = getString(R.string.glucose_test);
        titles[5] = getString(R.string.bp_test);
        titles[6] = getString(R.string.review_visit);

        progress = new RadioButton[7];
        progress[0] = (RadioButton) rootView.findViewById(R.id.radioOne);
        progress[1] = (RadioButton) rootView.findViewById(R.id.radioTwo);
        progress[2] = (RadioButton) rootView.findViewById(R.id.radioThree);
        progress[3] = (RadioButton) rootView.findViewById(R.id.radioFour);
        progress[4] = (RadioButton) rootView.findViewById(R.id.radioFive);
        progress[5] = (RadioButton) rootView.findViewById(R.id.radioSix);
        progress[6] = (RadioButton) rootView.findViewById(R.id.radioSeven);

        position = 0;

        radioGroup = (RadioGroup) rootView.findViewById(R.id.progressGroup);

        previousButton = (ImageButton) rootView.findViewById(R.id.previousButton);
        nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);

        uploadButton = (Button) rootView.findViewById(R.id.uploadButton);
        glucoseBeginTestButton = (Button) views[4].findViewById(R.id.beginTestButton);
        bpBeginTestButton = (Button) views[5].findViewById(R.id.beginTestButton);

        if (MainActivity.patient.getPatientData().getSex().equals("Male")) {

            views[1].findViewById(R.id.topView).setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) views[1].findViewById(R.id.middleView).getLayoutParams();
            params.addRule(RelativeLayout.BELOW,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        }

        previousButtonListener();
        nextButtonListener();
        radioGroupListener();
        uploadButtonListener();
        glucoseBeginTestListener();
        bpBeginTestListener();

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

    public void previousButtonListener() {
        previousButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != 0) {
//                            views[position].setVisibility(View.GONE);
//                            position--;
//                            views[position].setVisibility(View.VISIBLE);
//                            progress[position].setChecked(true);
//                            title.setText(titles[position]);
                            progress[position - 1].setChecked(true);

                        }
                    }
                }
        );
    }

    public void nextButtonListener() {
        nextButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (position != (views.length-1)) {
//                            views[position].setVisibility(View.GONE);
//                            position++;
//                            views[position].setVisibility(View.VISIBLE);
//                            progress[position].setChecked(true);
//                            title.setText(titles[position]);
                            progress[position+1].setChecked(true);
                        }
                    }
                }
        );
    }

    public void glucoseBeginTestListener() {
        glucoseBeginTestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MainActivity.connectedToHub) {
                            views[4].setVisibility(View.GONE);
                            views[4] = mGlucoseDisconnectedView;
                            views[4].setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }

    public void bpBeginTestListener() {
        bpBeginTestButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!MainActivity.connectedToHub) {
                            views[5].setVisibility(View.GONE);
                            views[5] = mBpDisconnectedView;
                            views[5].setVisibility(View.VISIBLE);
                        }
                    }
                }
        );
    }


    public void uploadButtonListener() {
        uploadButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RadioButton smokingRadioYes = (RadioButton) views[0].findViewById(R.id.smokingRadioNo);
                        RadioButton relativesDiabetesRadioYes = (RadioButton) views[0].findViewById(R.id.diabetesRadioYes);
                        RadioButton relativesHypertensionRadioYes = (RadioButton) views[0].findViewById(R.id.hypertensionRadioYes);

                        RadioButton pregnantRadioYes = (RadioButton) views[1].findViewById(R.id.pregnantRadioYes);
                        RadioButton hypertensionRadioYes = (RadioButton) views[1].findViewById(R.id.hypertensionRadioYes);
                        RadioButton diabetesRadioYes = (RadioButton) views[1].findViewById(R.id.diabetesRadioYes);

                        RadioButton dryMouthRadioYes = (RadioButton) views[2].findViewById(R.id.dryMouthRadioYes);
                        RadioButton numbnessRadioYes = (RadioButton) views[2].findViewById(R.id.numbnessRadioYes);
                        RadioButton dizzinessRadioYes = (RadioButton) views[2].findViewById(R.id.dizzinessRadioYes);

                        EditText heightEditText = (EditText) views[3].findViewById(R.id.heightValue);
                        RadioButton heightRadioInches = (RadioButton) views[3].findViewById(R.id.heightRadioInches);
                        EditText weightEditText = (EditText) views[3].findViewById(R.id.weightValue);
                        RadioButton weightRadioPounds = (RadioButton) views[3].findViewById(R.id.weightRadioPounds);

                        if (heightRadioInches.isChecked()) {
                            MainActivity.patient.getVisit(0).setHeight(Double.parseDouble(heightEditText.getText().toString()));
                        } else {
                            MainActivity.patient.getVisit(0).setHeight(Double.parseDouble(heightEditText.getText().toString()));
                        }

                        if (weightRadioPounds.isChecked()) {
                            MainActivity.patient.getVisit(0).setWeight(Integer.parseInt(weightEditText.getText().toString()));
                        } else {
                            MainActivity.patient.getVisit(0).setWeight(Integer.parseInt(weightEditText.getText().toString()));
                        }

                        if (relativesHypertensionRadioYes.isChecked()) {
                            MainActivity.patient.getHistoryData().setRelatives_high_blood_pressure("Yes");
                        } else {
                            MainActivity.patient.getHistoryData().setRelatives_high_blood_pressure("No");
                        }

                        if (relativesDiabetesRadioYes.isChecked()) {
                            MainActivity.patient.getHistoryData().setRelatives_diabetes("Yes");
                        } else {
                            MainActivity.patient.getHistoryData().setRelatives_diabetes("No");
                        }

                        if (hypertensionRadioYes.isChecked()) {
                            MainActivity.patient.getVisit(0).setHigh_blood_pressure("Yes");
                        } else {
                            MainActivity.patient.getVisit(0).setHigh_blood_pressure("No");
                        }

                        if (diabetesRadioYes.isChecked()) {
                            MainActivity.patient.getVisit(0).setDiabetes("Yes");
                        } else {
                            MainActivity.patient.getVisit(0).setDiabetes("No");
                        }

                        if (smokingRadioYes.isChecked()) {
                            MainActivity.patient.getHistoryData().setTobacco("Yes");
                        } else {
                            MainActivity.patient.getHistoryData().setTobacco("No");
                        }

                        if (MainActivity.patient.getPatientData().getSex().equals("Male")) {
                            MainActivity.patient.getVisit(0).setPregnant("No");

                        } else {

                            if (pregnantRadioYes.isChecked()) {
                                MainActivity.patient.getVisit(0).setPregnant("Yes");
                            } else {
                                MainActivity.patient.getVisit(0).setPregnant("No");
                            }

                        }
                        if (dryMouthRadioYes.isChecked()) {
                            MainActivity.patient.getVisit(0).setDry_mouth("Yes");
                        } else {
                            MainActivity.patient.getVisit(0).setDry_mouth("No");
                        }

                        if (dizzinessRadioYes.isChecked()) {
                            MainActivity.patient.getVisit(0).setDizziness("Yes");
                        } else {
                            MainActivity.patient.getVisit(0).setDizziness("No");
                        }

                        if (numbnessRadioYes.isChecked()) {
                            MainActivity.patient.getVisit(0).setNumbness("Yes");
                        } else {
                            MainActivity.patient.getVisit(0).setNumbness("No");
                        }

                        if (MainActivity.connectedToHub) {

                        } else {
                            EditText glucoseEditText = (EditText) views[4].findViewById(R.id.glucoseValue);
                            EditText bpsEditText = (EditText) views[5].findViewById(R.id.bpsValue);
                            EditText bpdEditText = (EditText) views[5].findViewById(R.id.bpdValue);
                            EditText pulseEditText = (EditText) views[5].findViewById(R.id.pulseValue);

                            MainActivity.patient.getVisit(0).setGlucose(Integer.parseInt(glucoseEditText.getText().toString()));
                            MainActivity.patient.getVisit(0).setBps(Integer.parseInt(bpsEditText.getText().toString()));
                            MainActivity.patient.getVisit(0).setBpd(Integer.parseInt(bpdEditText.getText().toString()));
                            MainActivity.patient.getVisit(0).setPulse(Integer.parseInt(pulseEditText.getText().toString()));
                        }

                        MainActivity.patient.getVisit(0).setBmi(calculateBMI());
                        MainActivity.patient.getVisit(0).setUser(MainActivity.user);
                        MainActivity.patient.getHistoryData().setDate(MainActivity.patient.getVisit(0).getDate());

                        MainActivity.patient.getMetadata().setName(MainActivity.user);
                        MainActivity.patient.getMetadata().setDate(timestamp());
                        MainActivity.patient.getMetadata().setLat(42.3492813);
                        MainActivity.patient.getMetadata().setLon(-71.106701);
                        MainActivity.patient.getMetadata().setDuration("00:25:34");
                        MainActivity.patient.getMetadata().setInternet(3);
                        //MainActivity.patient.getMetadata().setPubpid(MainActivity.patient.getPatientData().getPubpid());
                        MainActivity.patient.getMetadata().setPubpid("");

                        new UploadRecord().execute();

                    }
                }
        );
    }

    public void radioGroupListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                views[position].setVisibility(View.GONE);
                position = checkedId - progress[0].getId();
                views[position].setVisibility(View.VISIBLE);
                title.setText(titles[position]);

                if (position == 6) {

                    TextView heightValue = (TextView) views[6].findViewById(R.id.heightValue);
                    TextView weightValue = (TextView) views[6].findViewById(R.id.weightValue);
                    TextView bmiValue = (TextView) views[6].findViewById(R.id.bmiValue);
                    TextView familyHypertensionValue = (TextView) views[6].findViewById(R.id.familyHypertensionValue);
                    TextView familyDiabetesValue = (TextView) views[6].findViewById(R.id.familyDiabetesValue);
                    TextView hypertensionValue = (TextView) views[6].findViewById(R.id.hypertensionValue);
                    TextView smokingValue = (TextView) views[6].findViewById(R.id.smokesValue);
                    TextView diabetesValue = (TextView) views[6].findViewById(R.id.diabetesValue);
                    TextView pregnantValue = (TextView) views[6].findViewById(R.id.pregnantValue);
                    TextView dryMouthValue = (TextView) views[6].findViewById(R.id.dryMouthValue);
                    TextView dizzinessValue = (TextView) views[6].findViewById(R.id.dizzinessValue);
                    TextView numbnessValue = (TextView) views[6].findViewById(R.id.numbnessValue);
                    TextView glucoseValue = (TextView) views[6].findViewById(R.id.glucoseValue);
                    TextView bpValue = (TextView) views[6].findViewById(R.id.bpValue);
                    TextView pulseValue = (TextView) views[6].findViewById(R.id.pulseValue);

                    RadioButton smokingRadioYes = (RadioButton) views[0].findViewById(R.id.smokingRadioYes);
                    RadioButton relativesDiabetesRadioYes = (RadioButton) views[0].findViewById(R.id.diabetesRadioYes);
                    RadioButton relativesHypertensionRadioYes = (RadioButton) views[0].findViewById(R.id.hypertensionRadioYes);

                    RadioButton pregnantRadioYes = (RadioButton) views[1].findViewById(R.id.pregnantRadioYes);
                    RadioButton hypertensionRadioYes = (RadioButton) views[1].findViewById(R.id.hypertensionRadioYes);
                    RadioButton diabetesRadioYes = (RadioButton) views[1].findViewById(R.id.diabetesRadioYes);

                    RadioButton dryMouthRadioYes = (RadioButton) views[2].findViewById(R.id.dryMouthRadioYes);
                    RadioButton numbnessRadioYes = (RadioButton) views[2].findViewById(R.id.numbnessRadioYes);
                    RadioButton dizzinessRadioYes = (RadioButton) views[2].findViewById(R.id.dizzinessRadioYes);

                    EditText heightEditText = (EditText) views[3].findViewById(R.id.heightValue);
                    RadioButton heightRadioInches = (RadioButton) views[3].findViewById(R.id.heightRadioInches);
                    EditText weightEditText = (EditText) views[3].findViewById(R.id.weightValue);
                    RadioButton weightRadioPounds = (RadioButton) views[3].findViewById(R.id.weightRadioPounds);

                    if (heightRadioInches.isChecked()) {
                        heightValue.setText(": " + heightEditText.getText().toString() + " in.");
                    } else {
                        heightValue.setText(": " + heightEditText.getText().toString() + " cm.");
                    }

                    if (weightRadioPounds.isChecked()) {
                        weightValue.setText(": " + weightEditText.getText().toString() + " lbs.");
                    } else {
                        weightValue.setText(": " + weightEditText.getText().toString() + " kg.");
                    }

                    if (relativesHypertensionRadioYes.isChecked()) {
                        familyHypertensionValue.setText(": Yes");
                    } else {
                        familyHypertensionValue.setText(": No");
                    }

                    if (relativesDiabetesRadioYes.isChecked()) {
                        familyDiabetesValue.setText(": Yes");
                    } else {
                        familyDiabetesValue.setText(": No");
                    }

                    if (hypertensionRadioYes.isChecked()) {
                        hypertensionValue.setText(": Yes");
                    } else {
                        hypertensionValue.setText(": No");
                    }

                    if (diabetesRadioYes.isChecked()) {
                        diabetesValue.setText(": Yes");
                    } else {
                        diabetesValue.setText(": No");
                    }

                    if (smokingRadioYes.isChecked()) {
                        smokingValue.setText(": Yes");
                    } else {
                        smokingValue.setText(": No");
                    }

                    if (MainActivity.patient.getPatientData().getSex().equals("Male")) {
                        pregnantValue.setText(": No");
                    } else {
                        if (pregnantRadioYes.isChecked()) {
                            pregnantValue.setText(": Yes");
                        } else {
                            pregnantValue.setText(": No");
                        }
                    }

                    if (dryMouthRadioYes.isChecked()) {
                        dryMouthValue.setText(": Yes");
                    } else {
                        dryMouthValue.setText(": No");
                    }

                    if (dizzinessRadioYes.isChecked()) {
                        dizzinessValue.setText(": Yes");
                    } else {
                        dizzinessValue.setText(": No");
                    }

                    if (numbnessRadioYes.isChecked()) {
                        numbnessValue.setText(": Yes");
                    } else {
                        numbnessValue.setText(": No");
                    }

                    bmiValue.setText(": " + calculateBMI());

                    if (MainActivity.connectedToHub) {

                    } else {
                        EditText glucoseEditText = (EditText) views[4].findViewById(R.id.glucoseValue);
                        EditText bpsEditText = (EditText) views[5].findViewById(R.id.bpsValue);
                        EditText bpdEditText = (EditText) views[5].findViewById(R.id.bpdValue);
                        EditText pulseEditText = (EditText) views[5].findViewById(R.id.pulseValue);

                        glucoseValue.setText(": " + glucoseEditText.getText().toString() + " mmol/L.");
                        bpValue.setText(": " + bpsEditText.getText().toString());
                        bpValue.append("/" + bpdEditText.getText().toString() + " mmHg.");
                        pulseValue.setText(": " + pulseEditText.getText().toString() + " bpm.");
                    }
                }
            }
        });
    }

    public String timestamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar timestamp = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(timestamp.getTime());
    }

    public Double calculateBMI () {

        EditText heightEditText = (EditText) views[3].findViewById(R.id.heightValue);
        RadioButton heightRadioInches = (RadioButton) views[3].findViewById(R.id.heightRadioInches);
        EditText weightEditText = (EditText) views[3].findViewById(R.id.weightValue);
        RadioButton weightRadioPounds = (RadioButton) views[3].findViewById(R.id.weightRadioPounds);

        double height;
        double weight;
        double bmi;

        if (heightRadioInches.isChecked()) {
            height = 0.0254 * Double.parseDouble(heightEditText.getText().toString());
        } else {
            height = Double.parseDouble(heightEditText.getText().toString()) / 100;
        }

        if (weightRadioPounds.isChecked()) {
            weight = 0.453592 * Double.parseDouble(weightEditText.getText().toString());
        } else {
            weight = Double.parseDouble(weightEditText.getText().toString());
        }

        bmi = Math.round(((weight / (height * height)) * 10.0)/10.0);

        return bmi;
    }

    private class UploadRecord extends AsyncTask<Void, Void, String> {

        private PostRequest requestObj;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            requestObj = new PostRequest();

            requestObj.setVisit(MainActivity.patient.getVisit(0));
            requestObj.setHistory_data(MainActivity.patient.getHistoryData());
            requestObj.setMetadata(MainActivity.patient.getMetadata());
            requestObj.setPatient_data(MainActivity.patient.getPatientData());

        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                ObjectMapper mapper = new ObjectMapper();

                String jsonString;

                try {
                    jsonString = mapper.writeValueAsString(requestObj);
                    Log.d("Joseph", jsonString);
                } catch (Exception e) {

                    jsonString = "";
                    Log.d("Joseph", jsonString);

                }


                final String url = "http://52.36.163.49:8000/api/records/";

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + MainActivity.token);
                headers.setContentType(MediaType.APPLICATION_JSON);

                //HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

                HttpEntity<PostRequest> request = new HttpEntity<PostRequest>(requestObj, headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

//                final List<HttpMessageConverter<?>> messageConverters = new ArrayList< HttpMessageConverter<?> >();
//
//                messageConverters.add(new FormHttpMessageConverter());
//                messageConverters.add(new MappingJackson2HttpMessageConverter());
//                messageConverters.add(new StringHttpMessageConverter());
//
//                restTemplate.setMessageConverters(messageConverters);

                String response = restTemplate.postForObject(url, request, String.class);

                return response;

            } catch (Exception e) {
                Log.d("Joseph", "Post Error");
                String errorMessage = e.getMessage().toString();
                return errorMessage;
            }
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.d("Joseph", response);
        }
    }

}
