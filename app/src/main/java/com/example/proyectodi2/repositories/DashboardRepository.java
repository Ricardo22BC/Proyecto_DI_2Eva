package com.example.proyectodi2.repositories;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.proyectodi2.models.Animals;

import java.util.ArrayList;
import java.util.List;

public class DashboardRepository {

    private DatabaseReference animalsRef;

    public DashboardRepository() {
        animalsRef = FirebaseDatabase.getInstance().getReference("items");
    }

    public interface OnDataLoadedListener {
        void onDataLoaded(List<Animals> animals);
        void onDataFailed(Exception e);
    }

    public void loadAnimals(OnDataLoadedListener listener) {
        animalsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Animals> animals = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Animals animal = child.getValue(Animals.class);
                    if (animal != null) {
                        animal.setId(child.getKey()); //Creo el id
                        animals.add(animal);

                    }

                }
                listener.onDataLoaded(animals);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                listener.onDataFailed(error.toException());
            }
        });
    }


}
