package com.fit5046.paindiary.fragment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;
import android.widget.Toast;


import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import com.fit5046.paindiary.helper.AlarmReceiver;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    public TimePickerFragment(){

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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // initialize alarm manager
        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        // get calendar object to format the date data
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        //setting validation
        if (calendar.getTimeInMillis() > Calendar.getInstance().getTimeInMillis()){
            Toast.makeText(getActivity(), "Alarm set.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), 0, intent, 0);
            // set up alarm to appear 2 minutes before set time
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis()-120000, pendingIntent);
        }else {
            Toast.makeText(getActivity(), "You must select a time in the future.", Toast.LENGTH_LONG).show();
        }

    }
}
