package com.fit5046.paindiary.helper;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import com.fit5046.paindiary.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Calendar;

//date range picker class for creating graph
public class DateRangePickerHelper extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    private Button button;
    public DateRangePickerHelper(Button button){
        this.button = button;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    @Override
    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        populateSetDate(yy, mm+1, dd);
    }
    public void populateSetDate(int year, int month, int day) {
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

        if (button.getId() == R.id.lineStartDateBtn){
            TextInputLayout startDate = getActivity().findViewById(R.id.lineStartDateTextLayout);
            startDate.getEditText().setText(dateVal);
        }else if(button.getId() == R.id.lineEndDateBtn){
            TextInputLayout endDate = getActivity().findViewById(R.id.lineEndDateTextLayout);
            endDate.getEditText().setText(dateVal);
        }
    }
}
