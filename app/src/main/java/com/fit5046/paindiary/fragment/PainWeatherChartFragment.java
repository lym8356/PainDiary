package com.fit5046.paindiary.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.fit5046.paindiary.R;
import com.fit5046.paindiary.databinding.FragmentPainWeatherChartBinding;
import com.fit5046.paindiary.entity.PainData;
import com.fit5046.paindiary.helper.DateRangePickerHelper;
import com.fit5046.paindiary.viewmodel.PainDataViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;

public class PainWeatherChartFragment<LIst> extends Fragment {
    private FragmentPainWeatherChartBinding binding;
    private PainDataViewModel painDataViewModel;
    private LineChart lineChart;
    private List<Float> globalWeatherInfo;
    private List<Integer> globalPainLevel;

    public PainWeatherChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPainWeatherChartBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        painDataViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(PainDataViewModel.class);
        lineChart = (LineChart) binding.lineChart;


        //set up spinner
        ArrayList<String> weatherAttributeList = new ArrayList<>();
        weatherAttributeList.add("Temperature");
        weatherAttributeList.add("Humidity");
        weatherAttributeList.add("Pressure");

        ArrayAdapter<String> weatherAttributeAdapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, weatherAttributeList);
        binding.lineWeatherAttribute.setAdapter(weatherAttributeAdapter);


        //invoke data range picker
        binding.lineStartDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepickerFragment = new DateRangePickerHelper(binding.lineStartDateBtn);
                datepickerFragment.show(getParentFragmentManager(), "DatePickerHelper");
            }
        });

        binding.lineEndDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datepickerFragment = new DateRangePickerHelper(binding.lineEndDateBtn);
                datepickerFragment.show(getParentFragmentManager(), "DatePickerHelper");
            }
        });

        binding.lineGenerateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateDate() | !validateWeatherAttribute()){
                    return;
                }else{
                    String weatherAttribute = binding.lineWeatherAttributeLayout.getEditText().getText().toString();
                    String startDateString = binding.lineStartDateTextLayout.getEditText().getText().toString();
                    String endDateString = binding.lineEndDateTextLayout.getEditText().getText().toString();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date startDate = null;
                    // convert string to date object
                    try {
                        startDate = format.parse(startDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date endDate = null;
                    try {
                        endDate = format.parse(endDateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    Date finalStartDate = startDate;
                    Date finalEndDate = endDate;
                    //observe changes in database
                    painDataViewModel.getAllPainData().observe(getViewLifecycleOwner(), new Observer<List<PainData>>() {
                        @Override
                        public void onChanged(List<PainData> painData) {
                            // initialize array lists for storing data
                            ArrayList<String> dateString = new ArrayList<>();
                            ArrayList<String> xValues = new ArrayList<>();
                            ArrayList<Entry> weatherYValues = new ArrayList<>();
                            ArrayList<Entry> painLevelYValues = new ArrayList<>();
                            ArrayList<Float> temperature = new ArrayList<>();
                            ArrayList<Float> humidity = new ArrayList<>();
                            ArrayList<Float> pressure = new ArrayList<>();
                            ArrayList<Integer> painLevel = new ArrayList<>();
                            String yLabel;
                            //put record dates in array list
                            for (int i=0; i<painData.size(); i++){
                                dateString.add(painData.get(i).getRecordDate());
                            }
                            ArrayList<Date> tempDates = new ArrayList<>();
                            ArrayList<Date> dates = new ArrayList<>();
                            //convert string date to Date objects
                            for (int i=0; i<dateString.size(); i++){
                                try {
                                    tempDates.add(format.parse(dateString.get(i)));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            //filter date
                            for (int i=0; i<tempDates.size(); i++){
                                if (tempDates.get(i).compareTo(finalStartDate)>=0 && tempDates.get(i).compareTo(finalEndDate)<=0){
                                    dates.add(tempDates.get(i));
                                    temperature.add(painData.get(i).getDayTemperature());
                                    humidity.add(painData.get(i).getDayHumidity());
                                    pressure.add(painData.get(i).getDayPressure());
                                    painLevel.add(painData.get(i).getPainLevel());
                                }
                            }
                            //convert date back to string
                            for (int i=0; i<dates.size(); i++){
                                xValues.add(format.format(dates.get(i)));
                            }
                            //get user selection and put corresponding selection data into an arraylist
                            switch (weatherAttribute){
                                case "Temperature":
                                    globalWeatherInfo = temperature;
                                    yLabel = "Temperature";
                                    for (int i=0; i<temperature.size(); i++){
                                        weatherYValues.add(new Entry(i, temperature.get(i)));
                                    }
                                    break;
                                case "Humidity":
                                    globalWeatherInfo = humidity;
                                    yLabel = "Humidity";
                                    for (int i=0; i<humidity.size(); i++){
                                        weatherYValues.add(new Entry(i, humidity.get(i)));
                                    }
                                    break;
                                case "Pressure":
                                    globalWeatherInfo = pressure;
                                    yLabel = "Pressure";
                                    for (int i=0; i<pressure.size(); i++){
                                        weatherYValues.add(new Entry(i, pressure.get(i)));
                                    }
                                    break;
                                default:
                                    yLabel = "";
                                    break;
                            }
                            globalPainLevel = painLevel;

                            //add y values to the data list
                            for (int i=0; i<painLevel.size(); i++){
                                painLevelYValues.add(new Entry(i, painLevel.get(i)));
                            }
                            LineDataSet weatherInfoSet = new LineDataSet(weatherYValues, yLabel);
                            LineDataSet painLevelSet = new LineDataSet(painLevelYValues, "Pain Level");

                            //set line options for weather info
                            weatherInfoSet.setColor(Color.RED);
                            weatherInfoSet.setLineWidth(2);
                            weatherInfoSet.setValueTextSize(14);
                            weatherInfoSet.setValueTextColor(Color.BLACK);

                            //set line options for pain level
                            painLevelSet.setColors(Color.DKGRAY);
                            painLevelSet.setLineWidth(2);
                            painLevelSet.setValueTextSize(10);
                            painLevelSet.setValueTextColor(Color.BLACK);

                            //set up legend
                            Legend l = lineChart.getLegend();
                            l.setTextSize(10);
                            l.setTextColor(Color.BLACK);
                            lineChart.getDescription().setText("Pain level vs Weather Report");
                            lineChart.getDescription().setTextSize(10);
                            lineChart.getXAxis().setTextSize(10);

                            //add lines to the chart
                            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                            dataSets.add(weatherInfoSet);
                            dataSets.add(painLevelSet);

                            LineData data = new LineData(dataSets);

                            //format x axis
                            XAxis xAxis = lineChart.getXAxis();
                            xAxis.setValueFormatter(new ValueFormatter() {
                                @Override
                                public String getFormattedValue(float value) {
                                    return xValues.get((int) value);
                                }
                            });
                            xAxis.setGranularity(1);

                            lineChart.setData(data);
                            lineChart.invalidate();
                        }
                    });
                }
            }
        });

        binding.linePerformTestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (globalPainLevel.isEmpty() || globalWeatherInfo.isEmpty()){
                    Toast.makeText(getContext(), "No data to perform test", Toast.LENGTH_SHORT).show();
                }else{
                    //put data in list and convert to float
                    ArrayList<Float> tempList = new ArrayList<>();
                    for (int i = 0; i < globalPainLevel.size(); i++) {
                        tempList.add((float) (globalPainLevel.get(i) * 1.0));
                        tempList.add(globalWeatherInfo.get(i));
                    }
                    //put data in two dimensional array
                    double[][] data = new double[globalPainLevel.size()][2];
                    int counter = 0;
                    for (int row = 0; row < globalPainLevel.size(); row++) {
                        for (int col = 0; col < data[row].length; col++) {
                            data[row][col] = tempList.get(counter);
                            counter += 1;
                        }
                    }
                    // create a realmatrix
                    RealMatrix m = MatrixUtils.createRealMatrix(data);
                    // measure all correlation test: x-x, x-y, y-x, y-x
                    for (int i = 0; i < m.getColumnDimension(); i++)
                        for (int j = 0; j < m.getColumnDimension(); j++) {
                            PearsonsCorrelation pc = new PearsonsCorrelation();
                            double cor = pc.correlation(m.getColumn(i), m.getColumn(j));
                            System.out.println(i + "," + j + "=[" + String.format(".%2f", cor) + "," + "]");
                        }
                    // correlation test (another method): x-y
                    PearsonsCorrelation pc = new PearsonsCorrelation(m);
                    RealMatrix corM = pc.getCorrelationMatrix();
                    // significant test of the correlation coefficient (p-value)
                    RealMatrix pM = pc.getCorrelationPValues();
                    double pValue = pM.getEntry(0, 1); //p value
                    double correlation = corM.getEntry(0,1); //correlation
                    Toast.makeText(getContext(),"p value is: " + pValue + "\n" + "correlation is: "+ correlation , Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    //validation
    private boolean validateDate() {
        String startDateString = binding.lineStartDateTextLayout.getEditText().getText().toString();
        String endDateString = binding.lineEndDateTextLayout.getEditText().getText().toString();
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (startDateString.isEmpty()){
            binding.lineStartDateTextLayout.setErrorEnabled(true);
            binding.lineStartDateTextLayout.setError("Please select a date.");
            return false;
        }else if(endDateString.isEmpty()){
            binding.lineEndDateTextLayout.setErrorEnabled(true);
            binding.lineEndDateTextLayout.setError("Please select a date.");
            return false;
        }else{
            Date startDate = null;
            try {
                startDate = format.parse(startDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Date endDate = null;
            try {
                endDate = format.parse(endDateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if( startDate.compareTo(endDate) >= 0){
                binding.lineStartDateTextLayout.setErrorEnabled(true);
                binding.lineStartDateTextLayout.setError("Start date must be less than end date.");
                binding.lineEndDateTextLayout.setErrorEnabled(true);
                binding.lineEndDateTextLayout.setError("Start date must be less than end date.");
                return false;
            }else{
                binding.lineStartDateTextLayout.setErrorEnabled(false);
                binding.lineEndDateTextLayout.setErrorEnabled(false);
                binding.lineStartDateTextLayout.setError(null);
                binding.lineEndDateTextLayout.setError(null);
                return true;
            }
        }
    }

    private boolean validateWeatherAttribute(){
        String weatherAttribute = binding.lineWeatherAttributeLayout.getEditText().getText().toString();
        if (weatherAttribute.isEmpty()){
            binding.lineWeatherAttributeLayout.setErrorEnabled(true);
            binding.lineWeatherAttributeLayout.setError("This field cannot be empty.");
            return false;
        }else{
            binding.lineWeatherAttributeLayout.setErrorEnabled(false);
            binding.lineWeatherAttributeLayout.setError(null);
            return true;
        }
    }

}