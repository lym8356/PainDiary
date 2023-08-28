package com.fit5046.paindiary.fragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fit5046.paindiary.databinding.FragmentWorkerBinding;
import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.helper.DatabaseWorker;
import com.fit5046.paindiary.helper.WorkRequestTimePickerHelper;
import com.fit5046.paindiary.viewmodel.PainDataViewModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.lang.reflect.Type;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class WorkerFragment extends Fragment {
    FragmentWorkerBinding binding;
    private WorkManager workManager;
    private WorkRequest workRequest;
    PainDataViewModel painDataViewModel;
    List<PainData> painDataList;

    public WorkerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkerBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // initialize worker manager and worker request
        workManager = WorkManager.getInstance(getContext());
        workRequest = new OneTimeWorkRequest.Builder(DatabaseWorker.class).build();

        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this.getActivity().getApplication()).create(PainDataViewModel.class);

        // update global paindata when there is a change in the database
        painDataViewModel.getAllPainData().observe(getViewLifecycleOwner(), new Observer<List<PainData>>() {
            @Override
            public void onChanged(List<PainData> painData) {
                painDataList = painData;
            }
        });

        binding.workerSetTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // setting the pain data list and pass the list to worker
                Map<String, Object> painListMap = new HashMap<>();
                for (int i=0; i<painDataList.size(); i++){
                    painListMap.put(i+"", painDataList.get(i));
                }
                // convert the list into json objects first
                Type listOfPainData = new TypeToken<List<PainData>>(){}.getType();
                // convert using gson
                Gson gson = new Gson();
                String s = gson.toJson(painDataList, listOfPainData);

                //get time now in milliseconds
                Calendar calendar = Calendar.getInstance();
                long nowMillis = calendar.getTimeInMillis();

                //get and split the time string into required format
                String timeString = binding.workerTimeLayout.getEditText().getText().toString();
                String[] timeSplit = timeString.split(":");
                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeSplit[0]));
                calendar.set(Calendar.MINUTE, Integer.parseInt(timeSplit[1]));
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MILLISECOND,0);

                //calculate the difference between now and the set time
                long timeDiff = calendar.getTimeInMillis() - nowMillis;

                //put the data list in a bundle using data builder
                Data uploadBuilder = new Data.Builder().putString("list", s).build();
//                OneTimeWorkRequest workRequest =
//                        new OneTimeWorkRequest.Builder(DatabaseWorker.class).setInputData(uploadBuilder).setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).build();

                // set up periodic work request to repeat every 24 hours with an initial delay of time difference between now and the set time
                PeriodicWorkRequest workRequest =
                        new PeriodicWorkRequest.Builder(DatabaseWorker.class, 24, TimeUnit.HOURS).setInputData(uploadBuilder).setInitialDelay(timeDiff, TimeUnit.MILLISECONDS).build();
                workManager.enqueue(workRequest);
                Toast.makeText(getActivity(), "Set Successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.workerCancelTaskBtn.setOnClickListener(new View.OnClickListener() {
            //cancel the work if the user clicks on the button
            @Override
            public void onClick(View v) {
                workManager.cancelWorkById(workRequest.getId());
                Toast.makeText(getActivity(), "Cancelled Successfully.", Toast.LENGTH_SHORT).show();
            }
        });

        binding.workerSelectTimeBtn.setOnClickListener(new View.OnClickListener() {
            //open up time picker
            @Override
            public void onClick(View v) {
                DialogFragment timepickerFragment = new WorkRequestTimePickerHelper();
                timepickerFragment.show(getParentFragmentManager(), "WorkerTimePickerHelper");
            }
        });

        return view;
    }
}