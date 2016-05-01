package com.remotehcs.remotehcs.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.record.PatientData;
import com.remotehcs.remotehcs.record.PatientSearch;
import com.remotehcs.remotehcs.record.Visit;
import com.remotehcs.remotehcs.record.VisitResponse;


public class NextPatientFragment extends Fragment {

    private List<PatientData> results;
    private ListView list;
    ArrayAdapter<PatientData> adapter;

    private View mSearch;
    private View mResults;
    private View mNoMatches;
    private View mNewPatient;

    public NextPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_next_patient, container, false);

        mResults = rootView.findViewById(R.id.resultsView);
        mSearch = rootView.findViewById(R.id.searchView);
        mNoMatches = rootView.findViewById(R.id.noMatchesView);
        mNewPatient = rootView.findViewById(R.id.newPatientView);

        results = new ArrayList<PatientData>();

        list = (ListView) rootView.findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.mainfab);
        fab.setVisibility(View.GONE);

        populateListView();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.patient.setPatientData(results.get(position));
                MainActivity.patient.getMetadata().setPatient_exists("Yes");
                new VisitRequestTask().execute("http://52.36.163.49:8000/api/records/" + MainActivity.patient.getPatientData().getPubpid() + "/visits");
                //((MainActivity) getActivity()).displayView(1);
            }
        });

        searchButtonListener();
        clearListener();

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

    public void searchButtonListener() {
        Button searchButton = (Button) mSearch.findViewById(R.id.searchPatientsButton);
        searchButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mResults.findViewById(R.id.showMoreButton).setVisibility(View.GONE);
                        results.clear();
                        search();
                    }
                }
        );
    }

    public void search() {

        mResults.setVisibility(View.GONE);
        mNewPatient.setVisibility(View.GONE);
        mNoMatches.setVisibility(View.GONE);

        EditText fnameEditText = (EditText) mSearch.findViewById(R.id.fnameEditText);
        EditText lnameEditText = (EditText) mSearch.findViewById(R.id.lnameEditText);

        String URL = "http://52.36.163.49:8000/api/records/patient-data";

        Boolean first = true;

        int count = 0;

        if (!fnameEditText.getText().toString().matches("")) {
//            if (first) {
//                URL += "?fname=" + fnameEditText.getText().toString();
//                first = false;
//            } else {
//                URL += "&fname=" + fnameEditText.getText().toString();
//            }
            URL += "?fname=" + fnameEditText.getText().toString();
            count++;
        }

        if (!lnameEditText.getText().toString().matches("")) {
//            if (first) {
//                URL += "?lname=" + lnameEditText.getText().toString();
//                first = false;
//            } else {
//                URL += "&lname=" + lnameEditText.getText().toString();
//            }
            URL += "&lname=" + lnameEditText.getText().toString();
            count++;
        }

        if (count == 2) {
            new HttpRequestTask().execute(URL);
        } else {
            Toast.makeText(getActivity(), "All fields must be filled.", Toast.LENGTH_LONG).show();
        }
    }

    private class HttpRequestTask extends AsyncTask<String, Void, PatientSearch> {
        @Override
        protected PatientSearch doInBackground(String... params) {
            try {
                final String url = params[0];

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + MainActivity.token);
                HttpEntity entity = new HttpEntity(headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<PatientSearch> response = restTemplate.exchange(url, HttpMethod.GET, entity, PatientSearch.class, params);
                return response.getBody();

            } catch (Exception e) {
                Log.e("Spring Problem", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(PatientSearch response) {
            if (response.getCount() == 0) {
                noMatches();
            } else {
                showResults(response);
            }
        }
    }

    private void showResults(PatientSearch response) {

        //Hide the keyboard if its open
        View view = this.getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        //Add the matches to results to be displayed by listView
        for (int i = 0; i < response.getResults().length; i++) {
            results.add(response.getResults(i));
        }
        adapter.notifyDataSetChanged();

        //if there are more results display show more button and call listener
        if (response.getNext() != null && !response.getNext().isEmpty() && !response.getNext().equals("null")) {
            mResults.findViewById(R.id.showMoreButton).setVisibility(View.VISIBLE);
            showMoreListener(response.getNext());
        }

        mSearch.setVisibility(View.GONE);
        mResults.setVisibility(View.VISIBLE);

        editSearchListener();
        newPatientListener();

    }

    private void populateListView() {
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    private class ListAdapter extends ArrayAdapter {

        ListAdapter() {
            super(getActivity(), R.layout.patient_search_row);
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int i) {
            return results.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.patient_search_row, parent, false);

            }

            final Boolean expanded = false;

            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView address = (TextView) convertView.findViewById(R.id.address);
            TextView cityState = (TextView) convertView.findViewById(R.id.cityState);
            TextView gender = (TextView) convertView.findViewById(R.id.gender);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView pubpid = (TextView) convertView.findViewById(R.id.pubpid);
            final ImageView expandButton = (ImageView) convertView.findViewById(R.id.plusImage);
            final View mExpanded = convertView.findViewById(R.id.expandedView);
            final Animation forwardRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate);
            final Animation reverseRotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_reverse);

            name.setText(results.get(i).getFname() + " " + results.get(i).getLname());
            address.setText(results.get(i).getAddress());
            cityState.setText(results.get(i).getCity() + ", " + results.get(i).getState() + " " + results.get(i).getPostal_code());
            gender.setText(results.get(i).getSex());
            date.setText(results.get(i).getDate());
            pubpid.setText(results.get(i).getPubpid());


            expandButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (mExpanded.getVisibility() == View.VISIBLE) {
                        collapse(mExpanded, expandButton);
                    } else if (mExpanded.getVisibility() == View.GONE) {
                        expand(mExpanded, expandButton);
                    }
                }
            });

            return convertView;
        }
    }

    public void showMoreListener(final String url) {
        mResults.findViewById(R.id.showMoreButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new HttpRequestTask().execute(url);
                    }
                }
        );
    }

    public void clearListener() {
        mSearch.findViewById(R.id.clearButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText fnameEditText = (EditText) mSearch.findViewById(R.id.fnameEditText);
                        EditText lnameEditText = (EditText) mSearch.findViewById(R.id.lnameEditText);


                        fnameEditText.getText().clear();
                        lnameEditText.getText().clear();
                    }
                }
        );
    }

    public void newPatientListener() {
        mResults.findViewById(R.id.newPatientLabel).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPatient();
                    }
                }
        );
    }

    public void editSearchListener() {
        mResults.findViewById(R.id.editSearchButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mResults.setVisibility(View.GONE);
                        mSearch.setVisibility(View.VISIBLE);
                    }
                }
        );
    }

    public void newPatient() {

        mResults.setVisibility(View.GONE);
        mSearch.setVisibility(View.GONE);
        mNoMatches.setVisibility(View.GONE);

        EditText fnameEditTextSearch = (EditText) mSearch.findViewById(R.id.fnameEditText);
        EditText lnameEditTextSearch = (EditText) mSearch.findViewById(R.id.lnameEditText);
        EditText dobEditTextSearch = (EditText) mSearch.findViewById(R.id.dobEditText);

        EditText fnameEditText = (EditText) mNewPatient.findViewById(R.id.fnameEditText);
        EditText lnameEditText = (EditText) mNewPatient.findViewById(R.id.lnameEditText);
        EditText dobEditText = (EditText) mNewPatient.findViewById(R.id.dobEditText);

        fnameEditText.setText(fnameEditTextSearch.getText().toString());
        lnameEditText.setText(lnameEditTextSearch.getText().toString());
        dobEditText.setText(dobEditTextSearch.getText().toString());

        mNewPatient.setVisibility(View.VISIBLE);

        mNewPatient.findViewById(R.id.createButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText fnameEditText = (EditText) mNewPatient.findViewById(R.id.fnameEditText);
                        EditText lnameEditText = (EditText) mNewPatient.findViewById(R.id.lnameEditText);
                        EditText dobEditText = (EditText) mNewPatient.findViewById(R.id.dobEditText);
                        EditText maritalStatuEditText = (EditText) mNewPatient.findViewById(R.id.maritalStatusEditText);
                        EditText genderEditText = (EditText) mNewPatient.findViewById(R.id.genderEditText);
                        EditText addressEditText = (EditText) mNewPatient.findViewById(R.id.addressEditText);
                        EditText cityEditText = (EditText) mNewPatient.findViewById(R.id.cityEditText);
                        EditText stateEditText = (EditText) mNewPatient.findViewById(R.id.stateEditText);
                        EditText postalCodeEditText = (EditText) mNewPatient.findViewById(R.id.zipEditText);
                        EditText countryCodeEditText = (EditText) mNewPatient.findViewById(R.id.countryCodeEditText);
                        EditText emailEditText = (EditText) mNewPatient.findViewById(R.id.emailEditText);
                        EditText homePhoneEditText = (EditText) mNewPatient.findViewById(R.id.homePhoneEditText);
                        EditText cellPhoneEditText = (EditText) mNewPatient.findViewById(R.id.cellPhoneEditText);

                        MainActivity.patient.getPatientData().setFname(fnameEditText.getText().toString());
                        MainActivity.patient.getPatientData().setLname(lnameEditText.getText().toString());
                        MainActivity.patient.getPatientData().setDob(dobEditText.getText().toString());
                        MainActivity.patient.getPatientData().setStatus(maritalStatuEditText.getText().toString());
                        MainActivity.patient.getPatientData().setSex(genderEditText.getText().toString());
                        MainActivity.patient.getPatientData().setAddress(addressEditText.getText().toString());
                        MainActivity.patient.getPatientData().setCity(cityEditText.getText().toString());
                        MainActivity.patient.getPatientData().setState(stateEditText.getText().toString());
                        MainActivity.patient.getPatientData().setPostal_code(postalCodeEditText.getText().toString());
                        MainActivity.patient.getPatientData().setCountry_code(countryCodeEditText.getText().toString());
                        MainActivity.patient.getPatientData().setEmail(emailEditText.getText().toString());
                        MainActivity.patient.getPatientData().setPhone_contact(homePhoneEditText.getText().toString());
                        MainActivity.patient.getPatientData().setPhone_cell(cellPhoneEditText.getText().toString());
                        MainActivity.patient.getPatientData().setGov_id("000003454");
                        MainActivity.patient.getPatientData().setDate(timestamp());

                        MainActivity.patient.getMetadata().setPatient_exists("No");

                        ((MainActivity)getActivity()).displayView(1);
                    }
                }
        );

    }

    public void noMatches() {
        mSearch.setVisibility(View.GONE);
        mResults.setVisibility(View.GONE);
        mNewPatient.setVisibility(View.GONE);
        mNoMatches.setVisibility(View.VISIBLE);

        mNoMatches.findViewById(R.id.newPatientButton).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newPatient();
                    }
                }
        );

        mNoMatches.findViewById(R.id.searchAgain).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        search();
                    }
                }
        );

    }

    public static void expand(final View v, final View symbol) {
        v.measure(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RelativeLayout.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(2*(int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        final Animation forwardRotate = AnimationUtils.loadAnimation(symbol.getContext(), R.anim.rotate);
        forwardRotate.setDuration(2*(int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        symbol.startAnimation(forwardRotate);
        v.startAnimation(a);
    }

    public static void collapse(final View v, final View symbol) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(2*(int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        final Animation reverseRotate = AnimationUtils.loadAnimation(symbol.getContext(), R.anim.rotate_reverse);
        reverseRotate.setDuration(2*(int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        symbol.startAnimation(reverseRotate);
        v.startAnimation(a);
    }

    private class VisitRequestTask extends AsyncTask<String, Void, VisitResponse> {
        @Override
        protected VisitResponse doInBackground(String... params) {
            try {
                final String url = params[0];

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", "Token " + MainActivity.token);
                HttpEntity entity = new HttpEntity(headers);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<VisitResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, VisitResponse.class, params);
                return response.getBody();

            } catch (Exception e) {
                Log.e("Spring Problem", e.getMessage(), e);
                return null;
            }
        }

        @Override
        protected void onPostExecute(VisitResponse visitResponse) {
            showVisitResults(visitResponse);
        }
    }

    private void showVisitResults(VisitResponse visitResponse) {

        MainActivity.patient.setVisits(new ArrayList<Visit>(Arrays.asList(visitResponse.getVisits())));

        for (int i = 0; i < MainActivity.patient.getVisits().size(); i++) {
            Log.d("Joseph", MainActivity.patient.getVisit(i).getDate());
        }

        ((MainActivity) getActivity()).displayView(1);
    }

    public String timestamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar timestamp = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(timestamp.getTime());
    }

}

