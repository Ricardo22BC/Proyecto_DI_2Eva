package com.example.proyectodi2.views;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.proyectodi2.R;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mAuth = FirebaseAuth.getInstance();
        itemImageView = findViewById(R.id.image);
        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        Button logoutButton = findViewById(R.id.logout);

        String animalId = getIntent().getStringExtra("animalId");
        if (animalId == null) {
            Toast.makeText(this, "Error: ID no encontrado", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(DetailActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

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
    }
}

