package com.fit5046.paindiary.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit5046.paindiary.Dashboard;
import com.fit5046.paindiary.Login;
import com.fit5046.paindiary.R;
import com.fit5046.paindiary.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.location.Location;

import android.Manifest;
import android.content.Context;
import android.widget.Toast;

//layout consulted internet resources
public class HomeFragment extends Fragment implements View.OnClickListener {
    private FragmentHomeBinding binding;

    //openWeather api related
    private final String AppId = getResources().getString(R.string.openmap_api_key);
    private final String weatherUrl = "https://api.openweathermap.org/";

    //location manager related
    String locationProvider = LocationManager.GPS_PROVIDER;
    LocationManager locationManager;
    LocationListener locationListener;
    private final int locationRequestCode = 100;

    //update location every 2 seconds or 5kms
    private final long minTime = 1000;
    private final float minDistance = 1000;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        // onclick listener
        binding.dashboardLogout.setOnClickListener(this);
        binding.dashboardDataEntry.setOnClickListener(this);
        binding.dashboardDailyRecords.setOnClickListener(this);
        binding.dashboardReport.setOnClickListener(this);
        binding.dashboardMap.setOnClickListener(this);
        binding.dashboardWorker.setOnClickListener(this);
        // get weather information every time home fragment is initialized
        getWeatherInfo();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.dashboardLogout:
                if (FirebaseAuth.getInstance().getCurrentUser() != null){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(getActivity(), Login.class));
                    break;
                }
            case R.id.dashboardDataEntry:
                switchFragment(new DataEntryFragment());
                break;
            case R.id.dashboardDailyRecords:
                switchFragment(new DailyRecordsFragment());
                break;
            case R.id.dashboardReport:
                switchFragment(new ReportFragment());
                break;
            case R.id.dashboardMap:
                switchFragment(new MapFragment());
            case R.id.dashboardWorker:
                switchFragment(new WorkerFragment());
            default:
                Toast.makeText(getActivity(), "An error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private void getWeatherInfo() {

        // initialize location manager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // add location listener
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // get lon and lat from location object
                String lat = String.valueOf(location.getLatitude());
                String lon = String.valueOf(location.getLongitude());

                //use Retrofit to get response from open weather api
                Retrofit retrofit = new Retrofit.Builder().baseUrl(weatherUrl).addConverterFactory(GsonConverterFactory.create()).build();
                WeatherQuery query = retrofit.create(WeatherQuery.class);

                // execute query to get response
                Call call = query.getCurrentWeatherData(lat, lon, AppId);
                call.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.code() == 200) {
                            WeatherResponse weatherResponse = (WeatherResponse) response.body();
                            assert weatherResponse != null;
                            // convert to celsius
                            String weatherTemperatureCelsius = String.valueOf(Math.round(weatherResponse.main.temp - 273.15));
                            //update UI
                            if (weatherResponse.main != null && weatherResponse.name != null){
                                System.out.println(weatherResponse.name);
                                binding.dashboardWeatherCity.setText(weatherResponse.name);
                                binding.dashboardWeatherDegree.setText(weatherTemperatureCelsius + "°C");
                                binding.dashboardWeatherHumidity.setText(String.valueOf(weatherResponse.main.humidity));
                                binding.dashboardWeatherPressure.setText(String.valueOf(weatherResponse.main.pressure));

                                //call activity method to store weather information
                                Bundle bundle = new Bundle();
                                bundle.putString("Temperature", weatherTemperatureCelsius);
                                bundle.putString("Humidity", String.valueOf(weatherResponse.main.humidity));
                                bundle.putString("Pressure", String.valueOf(weatherResponse.main.pressure));
                                Dashboard dashboard = (Dashboard) getActivity();
                                dashboard.saveWeatherData(bundle);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        // print error message
                        binding.dashboardWeatherStatus.setText(t.getMessage());
                    }
                });
            }
        };

        //ask for device permission
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            //request user permission
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, locationRequestCode);
            return;
        }
        locationManager.requestLocationUpdates(locationProvider, minTime, minDistance, locationListener);
    }

    // check request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == locationRequestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(getActivity(), "Access to location info granted", Toast.LENGTH_SHORT).show();
                getWeatherInfo();
            }else {
                Toast.makeText(getActivity(), "Access to location info denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //switch fragment
    public void switchFragment(Fragment fragment){

        getParentFragmentManager().beginTransaction().replace(R.id.navigationHostFragment, fragment)
                .addToBackStack(null).commit();
    }

}