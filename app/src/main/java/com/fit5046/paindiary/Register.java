package com.fit5046.paindiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.fit5046.paindiary.databinding.ActivityLoginBinding;
import com.fit5046.paindiary.databinding.ActivityMainBinding;
import com.fit5046.paindiary.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    FirebaseAuth fAuth;
    private final String TAG = "RegisterActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        fAuth = FirebaseAuth.getInstance();

        //if the user is already logged in, redirect to user dashboard
        if (fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }

        binding.registerEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateEmail();
                }
            }
        });

        binding.registerPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validatePassword();
                }
            }
        });

        binding.registerConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    validateConfirmPassword();
                }
            }
        });
    }

    public void registerBack(View view){
        Intent intent = new Intent(getApplicationContext(), StartUp.class);
        startActivity(intent);
    }

    public void callLogin(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    //validate user inputs
    private boolean validateEmail(){
        String email = binding.registerEmail.getText().toString();
        String email_pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        if (email.isEmpty()){
            binding.registerEmailLayout.setErrorEnabled(true);
            binding.registerEmailLayout.setError("This field cannot be empty.");
            return false;
        } else if (!email.matches(email_pattern)){
            binding.registerEmailLayout.setErrorEnabled(true);
            binding.registerEmailLayout.setError("Invalid Email format.");
            return false;
        } else {
            binding.registerEmailLayout.setError(null);
            binding.registerEmailLayout.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword(){
        String password = binding.registerPassword.getText().toString();
        if (password.isEmpty()){
            binding.registerPasswordLayout.setErrorEnabled(true);
            binding.registerPasswordLayout.setError("This field cannot be empty.");
            return false;
        } else if (password.length() < 6){
            binding.registerPasswordLayout.setErrorEnabled(true);
            binding.registerPasswordLayout.setError("Password must be >- 6 characters.");
            return false;
        }
        else {
            binding.registerPasswordLayout.setError(null);
            binding.registerPasswordLayout.setErrorEnabled(false);
            return true;
        }
    }


    private boolean validateConfirmPassword(){
        String password = binding.registerPassword.getText().toString();
        String confirmPassword = binding.registerConfirmPassword.getText().toString();
        if (confirmPassword.isEmpty()){
            binding.registerConfirmPasswordLayout.setErrorEnabled(true);
            binding.registerConfirmPasswordLayout.setError("This field cannot be empty.");
            return false;
        } else if(!confirmPassword.equals(password)){
            binding.registerConfirmPasswordLayout.setErrorEnabled(true);
            binding.registerConfirmPasswordLayout.setError("Passwords do not match");
            return false;
        } else {
            binding.registerConfirmPasswordLayout.setError(null);
            binding.registerConfirmPasswordLayout.setErrorEnabled(false);
            return true;
        }
    }

    public void registerClicked(View view){
        if(!validateEmail() | !validatePassword() | !validateConfirmPassword()){
            return;
        }else {
            String email = binding.registerEmail.getText().toString().trim();
            String password = binding.registerPassword.getText().toString().trim();
            binding.registerProgressBar.setVisibility(View.VISIBLE);

            fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(Register.this, "User registered", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Dashboard.class));
                    }else {
                        Toast.makeText(Register.this, "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}