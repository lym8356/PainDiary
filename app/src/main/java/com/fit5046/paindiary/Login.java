package com.fit5046.paindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.fit5046.paindiary.databinding.ActivityLoginBinding;
import com.fit5046.paindiary.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private ActivityLoginBinding binding;
    FirebaseAuth fAuth;
    private final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        fAuth = FirebaseAuth.getInstance();

        //if the user is already logged in, redirect to user dashboard
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }


        binding.loginEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });

        binding.loginPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validatePassword();
                }
            }
        });
    }

    public void loginBack(View view){
        Intent intent = new Intent(getApplicationContext(), StartUp.class);
        startActivity(intent);
    }

    public void callRegister(View view){
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }

    //validate user inputs
    private boolean validateEmail(){
        String email = binding.loginEmail.getText().toString();

        if (email.isEmpty()){
            binding.loginEmailLayout.setErrorEnabled(true);
            binding.loginEmailLayout.setError("This field cannot be empty.");
            return false;
        } else {
            binding.loginEmailLayout.setError(null);
            binding.loginEmailLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String password = binding.loginPassword.getText().toString();
        if (password.isEmpty()){
            binding.loginPasswordLayout.setErrorEnabled(true);
            binding.loginPasswordLayout.setError("This field cannot be empty.");
            return false;

        } else {
            binding.loginPasswordLayout.setError(null);
            binding.loginPasswordLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void loginClicked(View view){
        if(!validateEmail() | !validatePassword()){
            return;
        }else {
            String email = binding.loginEmail.getText().toString().trim();
            String password = binding.loginPassword.getText().toString().trim();
            binding.loginProgressBar.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Login.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    }else {
                        Toast.makeText(Login.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}