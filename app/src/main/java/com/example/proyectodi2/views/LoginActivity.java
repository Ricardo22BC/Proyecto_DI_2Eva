package com.example.proyectodi2.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectodi2.R;
import com.example.proyectodi2.repositories.UserRepository;
import com.example.proyectodi2.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        EditText emailEditText = findViewById(R.id.emailText);
       EditText passwordEditText = findViewById(R.id.passwordText);

        findViewById(R.id.logButton).setOnClickListener(v-> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            loginViewModel.loginUser(email, password, new UserRepository.OnAuthCompleteListener() {
                @Override
                public void onSuccess(FirebaseUser user) {
                    startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
                    finish();
                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        });

        findViewById(R.id.regButton).setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        seeViewModel();
    }

    private void seeViewModel() {
        loginViewModel.getLoginStatus().observe(this, status ->{
            if (status != null) {
                Toast.makeText(LoginActivity.this, status, Toast.LENGTH_SHORT).show();

                if (status.equals("Inicio de sesión  con exito.")) {
                    Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        loginViewModel.getIsLoading().observe(this, isLoading -> {
            // Aquí puedes mostrar u ocultar un indicador de carga
        });
    }

}
