package com.example.proyectodi2.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.proyectodi2.Adapters.AnimalsAdapter;
import com.example.proyectodi2.R;
import com.example.proyectodi2.databinding.ActivityDashboardBinding;
import com.example.proyectodi2.databinding.FragmentDashboardBinding;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.viewmodels.DashboardViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DashboardFragment extends Fragment implements AnimalsAdapter.OnItemClickListener {
    private DashboardViewModel viewModel;
    private FragmentDashboardBinding binding;
    private AnimalsAdapter adapter;
    private FloatingActionButton themeButton;
    private boolean isDarkMode;

    public DashboardFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);
        requireActivity().setTheme(isDarkMode ? R.style.ThemeOscuro : R.style.ThemeClaro);

        // Inicializar ViewModel
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);

    }

    //Se infla el layout del fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Infla el layot del fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.logoutButton.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        binding.favoritesButton.setOnClickListener(v -> {
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, new FavouritesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AnimalsAdapter(this);
        binding.recyclerView.setAdapter(adapter);

        // Observar cambios en los datos
        viewModel.getAnimalsLiveData().observe(getViewLifecycleOwner(), animals -> {
            if (animals != null) {
                adapter.updateList(animals);
            }
        });

        // Observar errores
        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        themeButton = binding.darkMode;
        updateButtonAppearance();
        themeButton.setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("darkMode", isDarkMode);
            editor.apply();

            //Toast.makeText(this, !isDarkMode ? "Modo oscuro activado" : "Modo claro activado", Toast.LENGTH_SHORT).show();

            requireActivity().recreate();
        });

        viewModel.loadAnimals();

    }

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

        // Usar el método newInstance para crear el fragmento
        DetailFragment detailFragment = DetailFragment.newInstance(animal.getId());

        // Reemplazar el fragment actual con DetailFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
