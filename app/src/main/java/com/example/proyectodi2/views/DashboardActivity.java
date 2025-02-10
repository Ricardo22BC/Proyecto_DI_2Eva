package com.example.proyectodi2.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectodi2.Adapters.AnimalsAdapter;
import com.example.proyectodi2.R;
import com.example.proyectodi2.databinding.ActivityDashboardBinding;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.viewmodels.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardActivity extends AppCompatActivity implements AnimalsAdapter.OnItemClickListener {

    private DashboardViewModel viewModel;
    private ActivityDashboardBinding binding;
    private AnimalsAdapter adapter;
    private FloatingActionButton themeButton;
    private boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);
        setTheme(isDarkMode ? R.style.ThemeOscuro : R.style.ThemeClaro);

        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        binding.logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
        });

        binding.favoritesButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, FavouritesActivity.class));
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AnimalsAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

        // Observar cambios en los datos
        viewModel.getAnimalsLiveData().observe(this, animals -> {
            if (animals != null) {
                adapter.updateList(animals);
            }
        });

        // Observar errores
        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        // Cargar datos
        viewModel.loadAnimals();



        themeButton = findViewById(R.id.darkMode);
        updateButtonAppearance();
        themeButton.setOnClickListener(view -> {
            isDarkMode  = !isDarkMode;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("darkMode", isDarkMode);
            editor.apply();

            //Toast.makeText(this, !isDarkMode ? "Modo oscuro activado" : "Modo claro activado", Toast.LENGTH_SHORT).show();

            recreate();
        });
    }
        //función cambia la apariencia del botón al cambiar de modo oscuro a claro
    private void updateButtonAppearance() {
        if (isDarkMode) {
            themeButton.setImageResource(R.drawable.moon);  // Icono de luna para modo oscuro
            themeButton.setBackgroundTintList(getResources().getColorStateList(R.color.black));  // Fondo negro
            themeButton.setColorFilter(getResources().getColor(R.color.white));  // Icono blanco
        } else {
            themeButton.setImageResource(R.drawable.sunny);  // Icono de sol para modo claro
            themeButton.setBackgroundTintList(getResources().getColorStateList(R.color.white));  // Fondo blanco
            themeButton.setColorFilter(getResources().getColor(R.color.black));  // Icono negro
        }
    }
    @Override
    public void onItemClick(Animals animal) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("animalId", animal.getId());
        startActivity(intent);
    }


}

