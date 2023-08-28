package com.fit5046.paindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.fit5046.paindiary.databinding.ActivityDashboardBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Dashboard extends AppCompatActivity {

    //view binding
    private ActivityDashboardBinding binding;
    private Bundle weatherBundle;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //display login user name
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = currentUser.getEmail();
        View menuHeaderView = binding.navigationView.getHeaderView(0);
        TextView menuHeaderUserEmail = (TextView) menuHeaderView.findViewById(R.id.menuUser);
        if (currentUser != null) {
            binding.dashboardUsername.setText(currentUser.getEmail());
            menuHeaderUserEmail.setText(currentUser.getEmail());
        } else {
            binding.dashboardUsername.setText("User");
        }

        //navigation related
        binding.navigationMenu.setOnClickListener((v) -> {
            binding.navigationDrawerLayout.openDrawer(GravityCompat.START);
        });
        binding.navigationView.setItemIconTintList(null);

        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menuLogout){
                    if (FirebaseAuth.getInstance().getCurrentUser() != null){
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                }
                return true;
            }
        });
        //set navigation drawer and host fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment) fragmentManager.findFragmentById(R.id.navigationHostFragment);

//        NavController navController = Navigation.findNavController(this, R.id.navigationHostFragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(binding.navigationView, navController);
    }

    //save main method to allow other fragments to save weather data
    public void saveWeatherData(Bundle data){
        data.putString("Email", userEmail);
        weatherBundle = data;
    }

    //allow other fragments to retrive weather data
    public Bundle getWeatherData(){
        return weatherBundle;
    }

}