package com.fit5046.paindiary;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.fit5046.paindiary.databinding.ActivityMainBinding;

public class StartUp extends AppCompatActivity {

    private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
//        setContentView(R.layout.activity_main);
        setContentView(view);
    }

    public void user_login(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View,String>(binding.loginBtn, "transition_login");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartUp.this, pairs);
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

    public void user_register(View view){
        Intent intent = new Intent(getApplicationContext(), Register.class);
        Pair[] pairs = new Pair[1];
        pairs[0] = new Pair<View,String>(binding.registerBtn, "transition_register");
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(StartUp.this, pairs);
            startActivity(intent, options.toBundle());
        }else{
            startActivity(intent);
        }
    }

}