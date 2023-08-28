package com.fit5046.paindiary.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.paindiary.databinding.FragmentPainLocationChartBinding;
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
import java.util.List;

public class PainLocationChartFragment extends Fragment {
    private FragmentPainLocationChartBinding binding;
    private PieChart pieChart;
    private PainDataViewModel painDataViewModel;

    public PainLocationChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPainLocationChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // get pie chart object
        pieChart = (PieChart) binding.painLocationChart;
        // initialize pain data view model
        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainDataViewModel.class);
        painDataViewModel.getAllPainData().observe(getViewLifecycleOwner(), new Observer<List<PainData>>() {
            @Override
            public void onChanged(List<PainData> painData) {
                // initialize counters for every pain location
                int backCount , neckCount, headCount, kneesCount, hipsCount,
                        abdomenCount, elbowsCount, shouldersCount, shinsCount, jawCount, facialCount;
                backCount = neckCount = headCount = kneesCount = hipsCount = abdomenCount = elbowsCount = shouldersCount = shinsCount = jawCount = facialCount = 0;
                for (int i=0; i<painData.size(); i++){
                    // adding counters
                    if (painData.get(i).getPainLocation().equals("Back")){
                        backCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Neck")){
                        neckCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Head")){
                        headCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Knees")){
                        kneesCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Hips")){
                        hipsCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Abdomen")){
                        abdomenCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Elbows")){
                        elbowsCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Shoulders")){
                        shouldersCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Shins")){
                        shinsCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Jaw")){
                        jawCount += 1;
                    }else if (painData.get(i).getPainLocation().equals("Facial")){
                        facialCount += 1;
                    }
                }

                // adding data to entry for every count greater than 0
                ArrayList<PieEntry> entries = new ArrayList<>();
                if (backCount > 0 ) {entries.add(new PieEntry(backCount, "Back"));}
                if (neckCount > 0 ) {entries.add(new PieEntry(neckCount, "Neck"));}
                if (headCount > 0 ) {entries.add(new PieEntry(headCount, "Neck"));}
                if (kneesCount > 0 ) {entries.add(new PieEntry(kneesCount, "Knees"));}
                if (hipsCount > 0) {entries.add(new PieEntry(hipsCount, "Hips"));}
                if (abdomenCount > 0) {entries.add(new PieEntry(abdomenCount, "Abdomen"));}
                if (elbowsCount > 0) {entries.add(new PieEntry(elbowsCount, "Elbows"));}
                if (shouldersCount > 0) {entries.add(new PieEntry(shouldersCount, "Shoulders"));}
                if (shinsCount > 0) {entries.add(new PieEntry(shinsCount, "Shins"));}
                if (jawCount > 0) {entries.add(new PieEntry(jawCount, "Jaw"));}
                if (facialCount > 0) {entries.add(new PieEntry(facialCount, "Facial"));}

                // set up color
                ArrayList<Integer> colors = new ArrayList<>();
                for (int color: ColorTemplate.MATERIAL_COLORS){
                    colors.add(color);
                }
                for (int color: ColorTemplate.VORDIPLOM_COLORS){
                    colors.add(color);
                }
                PieDataSet dataSet = new PieDataSet(entries, "");
                dataSet.setColors(colors);

                //chart data options
                PieData data = new PieData(dataSet);
                data.setDrawValues(true);
                data.setValueTextSize(16);
                data.setValueFormatter(new PercentFormatter(pieChart));

                //set up legends and options
                Legend l = pieChart.getLegend();
                l.setTextSize(14);
                pieChart.setUsePercentValues(true);
                pieChart.setEntryLabelTextSize(16);
                pieChart.setEntryLabelColor(Color.BLACK);
                pieChart.setCenterText("Pain Location Report");
                pieChart.setCenterTextSize(18);
                pieChart.getDescription().setEnabled(false);
                pieChart.setData(data);
                pieChart.invalidate();

            }
        });
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}