package com.example.proyectodi2;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class DashboardActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView itemImageView;
    private TextView titleTextView, descriptionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mAuth = FirebaseAuth.getInstance();
        itemImageView = findViewById(R.id.image);
        titleTextView = findViewById(R.id.title);
        descriptionTextView = findViewById(R.id.description);
        Button logoutButton = findViewById(R.id.logout);

        // Configurar botón de logout
        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(DashboardActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });

        //Creamos para que seleccione al azar uno de los 10 con ramdom.
        final int totalItems = 10;
        Random  random = new Random();
        int randomIndex  = random.nextInt(totalItems);
        // Obtener el único elemento desde Firebase
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child("item" + (randomIndex+1));

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String title = dataSnapshot.child("title").getValue(String.class);
                    String description = dataSnapshot.child("description").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    titleTextView.setText(title);
                    descriptionTextView.setText(description);
                    Glide.with(DashboardActivity.this).load(imageUrl).into(itemImageView);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(DashboardActivity.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

