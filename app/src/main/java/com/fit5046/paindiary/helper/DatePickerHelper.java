package com.fit5046.paindiary.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;


import androidx.fragment.app.DialogFragment;

import com.fit5046.paindiary.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;
public class DatePickerHelper extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    private Dialog dialog;

    public DatePickerHelper(){

    };

    public DatePickerHelper(Dialog dialog){
        this.dialog = dialog;
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //initialize date values with calendar
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(android.widget.DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {

        //format date string
        String monthVal, dayVal = "" + day;
        if (month < 10){
            monthVal = "0" + month;
        }else {
            monthVal = "" + month;
        }
        if (day < 10){
            dayVal = "0" + day;
        }
        String dateVal = dayVal + "/" + monthVal + "/" + year;

        //check if the caller is a dialog or not
        if (dialog != null){
            //update the corresponding fields according to the caller
            TextInputLayout updateDateText = dialog.findViewById(R.id.updateDateTextLayout);
            updateDateText.getEditText().setText(dateVal);
        }else {
            TextInputLayout entryDateText = getActivity().findViewById(R.id.entryDateTextLayout);
            entryDateText.getEditText().setText(dateVal);
        }
    }
}

