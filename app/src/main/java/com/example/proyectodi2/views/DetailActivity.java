package com.example.proyectodi2.views;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectodi2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class DetailActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView itemImageView;
    private TextView titleTextView, descriptionTextView;
    private FloatingActionButton favorite;
    private DatabaseReference favoritesRef;
    private String userId;
    private String animalId;
    private boolean isFavorite = false;
    private boolean isDarkMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SharedPreferences prefs = getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);
        setTheme(isDarkMode ? R.style.ThemeOscuro : R.style.ThemeClaro);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        itemImageView = findViewById(R.id.image);
        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        Button logoutButton = findViewById(R.id.logout);
        favorite = findViewById(R.id.favorite);



        animalId = getIntent().getStringExtra("animalId");
        if (animalId == null) {
            Toast.makeText(this, "Error: ID no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        userId=FirebaseAuth.getInstance().getUid();

        favoritesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");

        checkIfFavorite();
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child(animalId);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    Toast.makeText(DetailActivity.this, "Animal no encontrado", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }

                String titulo = dataSnapshot.child("title").getValue(String.class);
                String descripcion = dataSnapshot.child("description").getValue(String.class);
                String imagen = dataSnapshot.child("imageUrl").getValue(String.class);

                titleTextView.setText(titulo);
                descriptionTextView.setText(descripcion);
                if (imagen != null) {
                    Glide.with(DetailActivity.this)
                            .load(imagen)
                            .error(R.drawable.ic_error_placeholder) // Asegúrate de tener este drawable
                            .into(itemImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                finish();
            }
        });

        favorite.setOnClickListener(view -> toggleFavorite());

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void checkIfFavorite() {
        favoritesRef.child(animalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    isFavorite = true;
                    favorite.setImageResource(R.drawable.favorite);
                } else {
                    isFavorite = false;
                    favorite.setImageResource(R.drawable.favorite_border);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Error al obtener favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleFavorite() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance()
                .getReference("usuarios")
                .child(userId)
                .child("favoritos");

        favoritesRef.child(animalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Si el elemento ya está en favoritos, eliminarlo
                    favoritesRef.child(animalId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            isFavorite = false;
                            favorite.setImageResource(R.drawable.favorite_border);
                            Toast.makeText(DetailActivity.this, "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Si no está en favoritos, agregarlo
                    favoritesRef.child(animalId).setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            isFavorite = true;
                            favorite.setImageResource(R.drawable.favorite);
                            Toast.makeText(DetailActivity.this, "Agregado a favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(DetailActivity.this, "Error al modificar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }

}