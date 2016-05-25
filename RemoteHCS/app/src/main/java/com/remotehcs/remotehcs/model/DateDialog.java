package com.remotehcs.remotehcs.model;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by jeltierney on 5/22/16.
 */
public class DateDialog extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    EditText dob;

    public DateDialog() {

    }

    public DateDialog(View view) {
        dob = (EditText) view;
    }

    public Dialog onCreateDialog (Bundle savedInstanceState) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog;
        dialog = new DatePickerDialog(getActivity(),this,year,month,day);

        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("Joseph", dayOfMonth + "/" + monthOfYear + "/" + year);
    }
}
