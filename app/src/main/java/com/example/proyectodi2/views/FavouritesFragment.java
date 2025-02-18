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

import com.example.proyectodi2.Adapters.FavoritesAdapter;
import com.example.proyectodi2.R;
import com.example.proyectodi2.databinding.FragmentFavouritesBinding;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.viewmodels.FavoriteViewModel;

public class FavouritesFragment extends Fragment implements FavoritesAdapter.OnItemClickListener {
    private FavoriteViewModel viewModel;
    private FragmentFavouritesBinding binding;
    private FavoritesAdapter adapter;
    private boolean isDarkMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);

        // Note: We don't set theme in Fragment, it should be done in the hosting Activity
        viewModel = new ViewModelProvider(this).get(FavoriteViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFavouritesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView2.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new FavoritesAdapter(this);
        binding.recyclerView2.setAdapter(adapter);

        viewModel.getFavoritesLiveData().observe(getViewLifecycleOwner(), favorites -> {
            if (favorites != null) {
                adapter.updateList(favorites);
            }
        });

        viewModel.getErrorLiveData().observe(getViewLifecycleOwner(), error -> {
            if (error != null) {
                Toast.makeText(requireContext(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });

        viewModel.loadFavorites();
    }

    @Override
    public void onItemClick(Animals animal) {

        DetailFragment detailFragment = new DetailFragment();

        // Pasar datos con Bundle
        Bundle bundle = new Bundle();
        bundle.putString("animalId", animal.getId());
        detailFragment.setArguments(bundle);

        // Reemplazar el fragment actual con DetailFragment
        FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
        if (!fragmentManager.isStateSaved()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragmentContainer, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}