package com.example.proyectodi2.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.proyectodi2.models.Animals;
import com.example.proyectodi2.repositories.DashboardRepository;
import java.util.List;

public class DashboardViewModel extends ViewModel {

    private DashboardRepository repository;
    private MutableLiveData<List<Animals>> animalsLiveData;
    private MutableLiveData<String> errorLiveData;

    public DashboardViewModel() {
        repository = new DashboardRepository();
       animalsLiveData = new MutableLiveData<>();
        errorLiveData = new MutableLiveData<>();
    }

    public void loadAnimals() {
        repository.loadAnimals(new DashboardRepository.OnDataLoadedListener() {
            @Override
            public void onDataLoaded(List<Animals> animals) {
                animalsLiveData.setValue(animals);
            }

            @Override
            public void onDataFailed(Exception e) {
                errorLiveData.setValue(e.getMessage());
            }
        });
    }

    public MutableLiveData<List<Animals>> getAnimalsLiveData() {
        return animalsLiveData;
    }

    public MutableLiveData<String> getErrorLiveData() {
        return errorLiveData;
    }
}