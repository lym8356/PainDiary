package com.fit5046.paindiary.helper;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import androidx.fragment.app.DialogFragment;

import com.fit5046.paindiary.R;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

//time picker fragment for work fragment
public class WorkRequestTimePickerHelper extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public WorkRequestTimePickerHelper(){

    }

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(), this,
                hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        //initialize time values with calendar
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        populateSetTime(hourOfDay, minute);

//        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()){
//            Toast.makeText(getActivity(), "You must select a time in the future.", Toast.LENGTH_LONG).show();
//        }else{
//
//        }
    }

    public void populateSetTime(int hour, int minute) {

        //format time string
        String hourVal, minuteVal = "";
        if (hour < 10){
            hourVal = "0" + hour;
        }else {
            hourVal = "" + hour;
        }
        if (minute < 10){
            minuteVal = "0" + minute;
        }else{
            minuteVal = "" + minute;
        }
        String timeVal = hourVal + ":" + minuteVal;

        TextInputLayout workerTimeLayout = getActivity().findViewById(R.id.workerTimeLayout);
        workerTimeLayout.getEditText().setText(timeVal);

    }
}
