package com.example.proyectodi2.repositories;

import androidx.annotation.NonNull;

import com.example.proyectodi2.models.Animals;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesRepository {

    private DatabaseReference favoritesRef;
    private DatabaseReference itemsRef;

    public FavoritesRepository() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        favoritesRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("favoritos");
        itemsRef = FirebaseDatabase.getInstance().getReference("items");
    }

    public interface OnfavoritesLoadedListener {
        void onFavoritesLoaded(List<Animals> favorites);
        void onDataFailed(Exception e);
    }

    public void loadFavorites(OnfavoritesLoadedListener listener) {
        favoritesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> favoriteIds = new ArrayList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    favoriteIds.add(child.getKey());
                }
                loadFavoritesDetails(favoriteIds, listener);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadFavoritesDetails(List<String> favoriteIds, OnfavoritesLoadedListener listener) {
        List<Animals> favorites = new ArrayList<>();
        for (String id : favoriteIds) {
            itemsRef.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Animals animal = snapshot.getValue(Animals.class);
                    if(animal != null) {
                        animal.setId(snapshot.getKey());
                        favorites.add(animal);
                        if (favorites.size() == favoriteIds.size()) {
                            listener.onFavoritesLoaded(favorites);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
