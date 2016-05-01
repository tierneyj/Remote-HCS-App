package com.remotehcs.remotehcs.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.remotehcs.remotehcs.record.PostRequest;
import com.remotehcs.remotehcs.record.Visit;

public class NewVisitFragment extends Fragment {

    private ImageButton nextButton;
    private ImageButton previousButton;
    private RadioGroup radioGroup;
    private View[] views;
    private RadioButton[] progress;

    private Button uploadButton;

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

                    MainActivity.patient.getVisits().add(0,new Visit());

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

                    MainActivity.patient.getVisit(0).setBmi(23);
                    MainActivity.patient.getVisit(0).setGlucose(80);
                    MainActivity.patient.getVisit(0).setBps(120);
                    MainActivity.patient.getVisit(0).setBpd(80);
                    MainActivity.patient.getHistoryData().setTobacco("No");
                    MainActivity.patient.getVisit(0).setPulse(77);
                    MainActivity.patient.getVisit(0).setUser("admin");
                    MainActivity.patient.getVisit(0).setDate("New");
                    MainActivity.patient.getHistoryData().setDate("2016-04-30T20:18:25Z");

                    MainActivity.patient.getMetadata().setName("admin");
                    MainActivity.patient.getMetadata().setDate("2016-04-30T20:18:25Z");
                    MainActivity.patient.getMetadata().setPatient_exists("Yes");
                    MainActivity.patient.getMetadata().setLat(42.3492813);
                    MainActivity.patient.getMetadata().setLon(-71.106701);
                    MainActivity.patient.getMetadata().setDuration("00:25:34");
                    MainActivity.patient.getMetadata().setInternet(3);
                    MainActivity.patient.getMetadata().setPubpid(MainActivity.patient.getPatientData().getPubpid());

                    for (int i = 0; i < MainActivity.patient.getVisits().size(); i++) {
                        Log.d("Joseph", MainActivity.patient.getVisit(i).getDate());
                    }

                    PostRequest request = new PostRequest();
                    request.setVisit(MainActivity.patient.getVisit(0));
                    request.setHistory_data(MainActivity.patient.getHistoryData());
                    request.setMetadata(MainActivity.patient.getMetadata());
                    request.setPatient_data(MainActivity.patient.getPatientData());

                    ObjectMapper mapper = new ObjectMapper();

                    try {
                        String jsonString = mapper.writeValueAsString(request);
                        Log.d("Joseph", jsonString);
                    } catch (Exception e) {

                    }

                }
            }
        );
    }

    public void radioGroupListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("Joseph", Integer.toString(checkedId));
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
                    TextView smokingValue = (TextView) views[0].findViewById(R.id.smokingRadioYes);
                    TextView diabetesValue = (TextView) views[6].findViewById(R.id.diabetesValue);
                    TextView pregnantValue = (TextView) views[6].findViewById(R.id.pregnantValue);
                    TextView dryMouthValue = (TextView) views[6].findViewById(R.id.dryMouthValue);
                    TextView dizzinessValue = (TextView) views[6].findViewById(R.id.dizzinessValue);
                    TextView numbnessValue = (TextView) views[6].findViewById(R.id.numbnessValue);
                    TextView glucoseValue = (TextView) views[6].findViewById(R.id.glucoseValue);
                    TextView bpValue = (TextView) views[6].findViewById(R.id.bpValue);
                    TextView pulseValue = (TextView) views[6].findViewById(R.id.pulseValue);

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
                        heightValue.setText(" " + heightEditText.getText().toString() + "in.");
                    } else {
                        heightValue.setText(" " + heightEditText.getText().toString() + "cm.");
                    }

                    if (weightRadioPounds.isChecked()) {
                        weightValue.setText(" " + weightEditText.getText().toString() + "lbs.");
                    } else {
                        weightValue.setText(" " + weightEditText.getText().toString() + "kg.");
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
                        numbnessValue.setText(" No");
                    }

                    bmiValue.setText(": 45");
                    glucoseValue.setText(": 80gm");
                    bpValue.setText(": 120/34");
                    pulseValue.setText(": 60bps");

                }
            }
        });
    }
}
