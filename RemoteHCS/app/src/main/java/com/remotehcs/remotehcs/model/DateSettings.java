package com.remotehcs.remotehcs.model;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.DatePicker;

/**
 * Created by jeltierney on 5/22/16.
 */
public class DateSettings implements DatePickerDialog.OnDateSetListener {

    Context context;

    public DateSettings(Context context) {
        this.context = context;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Log.d("Joseph", dayOfMonth + "/" + monthOfYear + "/" + year);
    }
}
