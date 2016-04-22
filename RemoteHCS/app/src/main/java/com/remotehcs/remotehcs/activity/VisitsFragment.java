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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import com.remotehcs.remotehcs.R;
import com.remotehcs.remotehcs.record.VisitResponse;
import com.remotehcs.remotehcs.record.Visit;

import static com.remotehcs.remotehcs.R.*;


public class VisitsFragment extends Fragment {

    private List<Visit> results;
    private ListView list;
    ArrayAdapter<Visit> adapter;

    public VisitsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(layout.fragment_visits, container, false);

        String URL = "http://54.84.171.130/api/records/28005573/visits";

        results = new ArrayList<Visit>();

        list = (ListView) rootView.findViewById(R.id.listView);

        populateListView();

        new HttpRequestTask().execute(URL);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.mainfab);
        fab.setVisibility(View.VISIBLE);


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


    private class HttpRequestTask extends AsyncTask<String, Void, VisitResponse> {
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
        protected void onPostExecute(VisitResponse response) {
            showResults(response);
        }
    }

    private void populateListView() {
        adapter = new ListAdapter();
        list.setAdapter(adapter);
    }

    private class ListAdapter extends ArrayAdapter {

        ListAdapter() {
            super(getActivity(), layout.visit_row);
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
                convertView = getActivity().getLayoutInflater().inflate(R.layout.visit_row, parent, false);

            }

            TextView dateLabel = (TextView) convertView.findViewById(R.id.dateLabel);
            dateLabel.setText(results.get(i).getDate());

            return convertView;
        }
    }

    private void showResults(VisitResponse response) {

        for (int i = 0; i < response.getVisits().length; i++) {
            results.add(response.getVisits(i));
            Log.d("Joseph", response.getVisits(i).getDate());
        }

        adapter.notifyDataSetChanged();

        //if there are more results display show more button and call listener
//        if (response.getNext() != null && !response.getNext().isEmpty() && !response.getNext().equals("null")) {
//            mResults.findViewById(R.id.showMoreButton).setVisibility(View.VISIBLE);
//            showMoreListener(response.getNext());
//        }
//
//        mSearch.setVisibility(View.GONE);
//        mResults.setVisibility(View.VISIBLE);
//
//        editSearchListener();
//        newPatientListener();

    }

}

