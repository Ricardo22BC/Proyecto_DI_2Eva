package com.example.proyectodi2.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectodi2.Adapters.FavoritesAdapter;
import com.example.proyectodi2.R;
import com.example.proyectodi2.databinding.ActivityFavoritesBinding;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.viewmodels.FavoriteViewModel;

public class FavouritesActivity extends AppCompatActivity implements FavoritesAdapter.OnItemClickListener {
    private FavoriteViewModel viewModel;
    private ActivityFavoritesBinding binding;
    private FavoritesAdapter adapter;
    private boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);
        setTheme(isDarkMode ? R.style.ThemeOscuro : R.style.ThemeClaro);

        super.onCreate(savedInstanceState);
        binding = ActivityFavoritesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FavoritesAdapter(this);
        binding.recyclerView2.setAdapter(adapter);
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);

        viewModel.getFavoritesLiveData().observe(this, favorites -> {
            if (favorites != null) {
                adapter.updateList(favorites);
            }
        });

        viewModel.getErrorLiveData().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });



        viewModel.loadFavorites();
    }

    @Override
    public void onItemClick(Animals animal) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("animalId", animal.getId());
        startActivity(intent);
    }
}
