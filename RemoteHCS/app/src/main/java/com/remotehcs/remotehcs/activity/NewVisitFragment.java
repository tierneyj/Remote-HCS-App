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
import android.widget.ImageButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.remotehcs.remotehcs.R;

import static com.remotehcs.remotehcs.R.*;




public class NewVisitFragment extends Fragment {

    private ImageButton nextButton;
    private ImageButton previousButton;
    private RadioGroup radioGroup;
    private View[] views;
    private RadioButton[] progress;

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
        View rootView = inflater.inflate(layout.fragment_new_visit, container, false);

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
        titles[0] = getString(string.medical_history);
        titles[1] = getString(string.medical_conditions);
        titles[2] = getString(string.medical_conditions);
        titles[3] = getString(string.height_weight);
        titles[4] = getString(string.glucose_test);
        titles[5] = getString(string.bp_test);
        titles[6] = getString(string.review_visit);

        progress = new RadioButton[7];
        progress[0] = (RadioButton) rootView.findViewById(id.radioOne);
        progress[1] = (RadioButton) rootView.findViewById(id.radioTwo);
        progress[2] = (RadioButton) rootView.findViewById(id.radioThree);
        progress[3] = (RadioButton) rootView.findViewById(id.radioFour);
        progress[4] = (RadioButton) rootView.findViewById(id.radioFive);
        progress[5] = (RadioButton) rootView.findViewById(id.radioSix);
        progress[6] = (RadioButton) rootView.findViewById(id.radioSeven);

        position = 0;

        radioGroup = (RadioGroup) rootView.findViewById(id.progressGroup);

        previousButton = (ImageButton) rootView.findViewById(R.id.previousButton);
        nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);

        if (MainActivity.patient.getSex().equals("Male")) {

            Log.d("Joseph", "hims is a boy");
            views[1].findViewById(id.topView).setVisibility(View.GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) views[1].findViewById(id.middleView).getLayoutParams();
            params.addRule(RelativeLayout.BELOW,0);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//
//            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) rootView.findViewById(id.conditionsView).getLayoutParams();
//            params2.addRule(RelativeLayout.MA);
//            params2.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        }

        previousButtonListener();
        nextButtonListener();
        radioGroupListener();

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
                            progress[position-1].setChecked(true);

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

    public void radioGroupListener() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("Joseph", Integer.toString(checkedId));
                views[position].setVisibility(View.GONE);
                position = checkedId - progress[0].getId();
                views[position].setVisibility(View.VISIBLE);
                title.setText(titles[position]);
            }
        });
    }
}
