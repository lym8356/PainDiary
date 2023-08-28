package com.fit5046.paindiary.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fit5046.paindiary.Dashboard;
import com.fit5046.paindiary.R;
import com.fit5046.paindiary.databinding.FragmentDataEntryBinding;
import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.adapter.CustomSpinnerAdapter;
import com.fit5046.paindiary.helper.CustomSpinnerItem;
import com.fit5046.paindiary.helper.DatePickerHelper;
import com.fit5046.paindiary.viewmodel.PainDataViewModel;
import com.google.android.material.slider.Slider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;


public class DataEntryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {

    private FragmentDataEntryBinding binding;
    private PainDataViewModel painDataViewModel;
    ArrayList<CustomSpinnerItem> itemList;
    AutoCompleteTextView entryMood;
    private int painLevel;
    private String userEmail;


    public DataEntryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDataEntryBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // initialize view model
        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainDataViewModel.class);

        // set onclick listener
        binding.entryDateBtn.setOnClickListener(this);
        binding.entryBackBtn.setOnClickListener(this);
        binding.entrySaveBtn.setOnClickListener(this);
        binding.entryAlarmBtn.setOnClickListener(this);


        //by default, the date picker starts with today's date
        Calendar calendar = Calendar.getInstance();
        String monthVal = "" + calendar.get(Calendar.DAY_OF_MONTH) , dayVal = "" + calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        if (calendar.get(Calendar.MONTH)+1 < 10){
            monthVal = "0" + month;
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10){
            dayVal = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        }

        String currentDate = dayVal + "/" + monthVal + "/" + calendar.get(Calendar.YEAR);
        binding.entryDateTextLayout.getEditText().setText(currentDate);

        //get weather data from the dashboard activity
        Dashboard dashboard = (Dashboard) getActivity();
        Bundle weatherInfo = dashboard.getWeatherData();
        if (weatherInfo != null){
            String temperature = weatherInfo.getString("Temperature");
            String humidity = weatherInfo.getString("Humidity");
            String pressure = weatherInfo.getString("Pressure");
            userEmail = weatherInfo.getString("Email");
            binding.entryTemperatureTextLayout.getEditText().setText(temperature);
            binding.entryHumidityTextLayout.getEditText().setText(humidity);
            binding.entryPressureTextLayout.getEditText().setText(pressure);
        }


        //setting custom spinner
        entryMood = binding.entryMood;
        itemList = getCustomList();
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getActivity(), itemList);
        if (entryMood != null){
            entryMood.setAdapter(adapter);
            entryMood.setOnItemClickListener(this);
        }
        //setting pain location array adapter
        ArrayList<String> painLocationList = new ArrayList<>();
        painLocationList.add("Back");
        painLocationList.add("Neck");
        painLocationList.add("Knees");
        painLocationList.add("Hips");
        painLocationList.add("Abdomen");
        painLocationList.add("Elbows");
        painLocationList.add("Shoulders");
        painLocationList.add("Shins");
        painLocationList.add("Jaw");
        painLocationList.add("Facial");
        ArrayAdapter<String> painLocationAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, painLocationList);
        binding.entryPainLocation.setAdapter(painLocationAdapter);

        // set slider listener
        binding.entryPainLevel.addOnChangeListener(new Slider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull @NotNull Slider slider, float value, boolean fromUser) {
                painLevel = (int) value;
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //set image and item pair for custom spinner list
    private ArrayList<CustomSpinnerItem> getCustomList() {
        itemList = new ArrayList<>();
        itemList.add(new CustomSpinnerItem("Very Low", R.drawable.sad));
        itemList.add(new CustomSpinnerItem("Low", R.drawable.unhappy));
        itemList.add(new CustomSpinnerItem("Average", R.drawable.average));
        itemList.add(new CustomSpinnerItem("Good", R.drawable.smile));
        itemList.add(new CustomSpinnerItem("Very Good", R.drawable.happy));
        return itemList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.entryDateBtn:
                DialogFragment datepickerFragment = new DatePickerHelper();
                datepickerFragment.show(getParentFragmentManager(), "DatePickerHelper");
                break;
            case R.id.entryBackBtn:
                Intent intent = new Intent(getActivity(), Dashboard.class);
                startActivity(intent);
                break;
            case R.id.entryAlarmBtn:
                DialogFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment.show(getParentFragmentManager(), "TimePicker");
                break;
            case R.id.entrySaveBtn:
                // check for device version
                String dateData = binding.entryDateTextLayout.getEditText().getText().toString();
                if (android.os.Build.VERSION.SDK_INT >=
                        android.os.Build.VERSION_CODES.N){
                    // check if the selected date already exists
                    CompletableFuture<PainData> dataToChecked = painDataViewModel.findByDateFuture(dateData);
                    dataToChecked.thenApply(painData -> {
                        if (painData != null){
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getContext(), "Pain data for this date already exists.", Toast.LENGTH_LONG).show();
                                }
                            });
                        }else{
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // validation
                                    if (!validateExerciseGoal() | !validatePainLocation() | !validateStepsWalked() | !validateUserMood()){
                                        return;
                                    }else{
                                        // get selected values and insert into database
                                        String painLocation = binding.entryPainLocationLayout.getEditText().getText().toString();
                                        String userMood = binding.entryMoodLayout.getEditText().getText().toString();
                                        int userExerciseGoal = Integer.parseInt(binding.entryExerciseGoalLayout.getEditText().getText().toString());
                                        int userStepsWalked = Integer.parseInt(binding.entryStepsWalkedLayout.getEditText().getText().toString());
                                        float dayTemperature = Float.parseFloat(String.valueOf(binding.entryTemperatureTextLayout.getEditText().getText()));
                                        float dayHumidity = Float.parseFloat(String.valueOf(binding.entryHumidityTextLayout.getEditText().getText()));
                                        float dayPressure = Float.parseFloat(String.valueOf(binding.entryPressureTextLayout.getEditText().getText()));
                                        PainData painData = new PainData(dateData, userEmail, painLevel, painLocation,
                                                userMood, userExerciseGoal, userStepsWalked, dayTemperature, dayHumidity, dayPressure);
                                        painDataViewModel.insert(painData);
                                        Toast.makeText(getContext(), "Pain data created.", Toast.LENGTH_SHORT).show();
                                        //incomplete methods
                                        resetFields();
                                    }
                                }
                            });
                        }
                    return true;
                    });
                }
        }
    }

    //populate the item selected from the custom spinner
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        try {
            LinearLayout linearLayout = getView().findViewById(R.id.customSpinnerItemLayout);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        CustomSpinnerItem item = (CustomSpinnerItem) adapterView.getItemAtPosition(position);
        CustomSpinnerAdapter adapter = (CustomSpinnerAdapter) binding.entryMood.getAdapter();
        //reset the adapter
        entryMood.setAdapter(null);
        entryMood.setText(item.getSpinnerItemName());
        entryMood.setAdapter(adapter);
    }

    private boolean validatePainLocation(){
        String painLocation = binding.entryPainLocationLayout.getEditText().getText().toString();
        if (painLocation.isEmpty()) {
            binding.entryPainLocationLayout.setErrorEnabled(true);
            binding.entryPainLocationLayout.setError("This field cannot be empty.");
            return false;
        }else {
            binding.entryPainLocationLayout.setError(null);
            binding.entryPainLocationLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUserMood(){
        String userMood = binding.entryMoodLayout.getEditText().getText().toString();
        if (userMood.isEmpty()) {
            binding.entryMoodLayout.setErrorEnabled(true);
            binding.entryMoodLayout.setError("This field cannot be empty.");
            return false;
        }else {
            binding.entryMoodLayout.setError(null);
            binding.entryMoodLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateExerciseGoal(){
        String exerciseGoalString = binding.entryExerciseGoalLayout.getEditText().getText().toString();
        if (exerciseGoalString.isEmpty()) {
            binding.entryExerciseGoalLayout.setErrorEnabled(true);
            binding.entryExerciseGoalLayout.setError("This field cannot be empty.");
            return false;
        }else {
            binding.entryExerciseGoalLayout.setError(null);
            binding.entryExerciseGoalLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateStepsWalked(){
        String stepsWalkedString = binding.entryStepsWalkedLayout.getEditText().getText().toString();
        if (stepsWalkedString.isEmpty()) {
            binding.entryStepsWalkedLayout.setErrorEnabled(true);
            binding.entryStepsWalkedLayout.setError("This field cannot be empty.");
            return false;
        }else {
            binding.entryStepsWalkedLayout.setError(null);
            binding.entryStepsWalkedLayout.setErrorEnabled(false);
            return true;
        }
    }

    private void resetFields(){

    }
}