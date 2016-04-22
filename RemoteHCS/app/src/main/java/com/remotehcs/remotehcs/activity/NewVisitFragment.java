package com.remotehcs.remotehcs.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.remotehcs.remotehcs.R;

import static com.remotehcs.remotehcs.R.*;




public class NewVisitFragment extends Fragment {

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
}
