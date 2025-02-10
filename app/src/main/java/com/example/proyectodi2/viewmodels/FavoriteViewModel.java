package com.example.proyectodi2.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.repositories.FavoritesRepository;

import java.util.List;

public class FavoriteViewModel extends ViewModel {
    private FavoritesRepository repository;
    private MutableLiveData<List<Animals>> favoritesLiveData;
    private MutableLiveData<String> errorLiveData;

    public FavoriteViewModel () {
        repository = new FavoritesRepository();
        favoritesLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void loadFavorites () {
        repository.loadFavorites(new FavoritesRepository.OnfavoritesLoadedListener() {
            @Override
            public void onFavoritesLoaded(List<Animals> favorites) {
                favoritesLiveData.setValue(favorites);
            }

            @Override
            public void onDataFailed(Exception e) {

            }
        });
    }

    public MutableLiveData<List<Animals>> getFavoritesLiveData() {
        return favoritesLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}
