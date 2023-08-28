package com.fit5046.paindiary.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fit5046.paindiary.databinding.FragmentStepsTakenChartBinding;
import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.viewmodel.PainDataViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.CompletableFuture;

public class StepsTakenChartFragment extends Fragment {
    private FragmentStepsTakenChartBinding binding;
    private PieChart pieChart;
    PainDataViewModel painDataViewModel;


    public StepsTakenChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentStepsTakenChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        pieChart = (PieChart) binding.stepsWalkedChart;

        setupChart(pieChart);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();

        setupChart(pieChart);
    }

    private void setupChart(PieChart pieChart){
        String currentDate = getCurrentDate();
        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainDataViewModel.class);
        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.N){
            //get the pain data for current date
            CompletableFuture<PainData> painDataCompletableFuture = painDataViewModel.findByDateFuture(currentDate);
            painDataCompletableFuture.thenApply(painData -> {
                if (painData != null){
                    int stepsWalked = painData.getUserStepsWalked();
                    int exerciseGoal = painData.getUserExerciseGoal();
                    // set up data entries using pain data for current date
                    ArrayList<PieEntry> entries = new ArrayList<>();
                    entries.add(new PieEntry(stepsWalked, "Steps Walked Today"));
                    entries.add(new PieEntry(exerciseGoal, "Exercise Goal"));
                    ArrayList<Integer> colors = new ArrayList<>();
                    // set up colors
                    for (int color: ColorTemplate.MATERIAL_COLORS){
                        colors.add(color);
                    }
                    PieDataSet dataSet = new PieDataSet(entries, "");
                    dataSet.setColors(colors);

                    PieData data = new PieData(dataSet);
                    data.setDrawValues(true);
                    data.setValueTextSize(16);
                    data.setValueFormatter(new PercentFormatter(pieChart));

                    //set up legend options and chart options
                    Legend l = pieChart.getLegend();
                    l.setTextSize(14);
                    pieChart.setUsePercentValues(true);
                    pieChart.setEntryLabelTextSize(16);
                    pieChart.setEntryLabelColor(Color.BLACK);
                    pieChart.setCenterText("Exercise Report");
                    pieChart.setCenterTextSize(18);
                    pieChart.getDescription().setEnabled(false);
                    pieChart.setData(data);
                    pieChart.invalidate();
                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getContext(), "No pain data for today.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return true;
            });
        }
    }

    private String getCurrentDate(){
        //use calendar to get current date data and return the formatted date
        Calendar calendar = Calendar.getInstance();
        String monthVal = "" + calendar.get(Calendar.DAY_OF_MONTH) , dayVal = "" + calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        if (calendar.get(Calendar.MONTH)+1 < 10){
            monthVal = "0" + month;
        }
        if (calendar.get(Calendar.DAY_OF_MONTH) < 10){
            dayVal = "0" + calendar.get(Calendar.DAY_OF_MONTH);
        }

        return dayVal + "/" + monthVal + "/" + calendar.get(Calendar.YEAR);
    }

}