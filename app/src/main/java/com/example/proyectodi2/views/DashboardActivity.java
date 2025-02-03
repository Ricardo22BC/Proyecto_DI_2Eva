package com.example.proyectodi2.views;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectodi2.Adapters.AnimalsAdapter;
import com.example.proyectodi2.databinding.ActivityDashboardBinding;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.viewmodels.DashboardViewModel;

public class DashboardActivity extends AppCompatActivity implements AnimalsAdapter.OnItemClickListener {

    private DashboardViewModel viewModel;
    private ActivityDashboardBinding binding;
    private AnimalsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(DashboardActivity.this, LoginActivity.class));
            finish();
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
    }

    @Override
    public void onItemClick(Animals animal) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("animalId", animal.getId());
        startActivity(intent);
    }
}

