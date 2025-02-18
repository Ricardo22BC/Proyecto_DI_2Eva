package com.example.proyectodi2.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.example.proyectodi2.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailFragment extends Fragment {
    private FirebaseAuth mAuth;
    private ImageView itemImageView;
    private TextView titleTextView, descriptionTextView;
    private FloatingActionButton favorite;
    private DatabaseReference favoritesRef;
    private String userId;
    private String animalId;
    private boolean isFavorite = false;
    private boolean isDarkMode;
    private Button logoutButton;


    public interface OnDetailFragmentInteractionListener {
        void onLogout();
    }

    private OnDetailFragmentInteractionListener mListener;

    public static DetailFragment newInstance(String animalId) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putString("ITEM_ID", animalId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        favoritesRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("favorites");

        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        isDarkMode = prefs.getBoolean("darkMode", false);


// Verificar si se ha pasado un animalId a través de los argumentos del Fragment
        if (getArguments() != null) {
            animalId = getArguments().getString("ITEM_ID");
        }

        if (animalId == null || animalId.isEmpty()) {
            Toast.makeText(requireContext(), "Error: ID no encontrado", Toast.LENGTH_SHORT).show();
            if (getActivity() != null) {
                getActivity().onBackPressed();
            }
            return;
        }


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        itemImageView = view.findViewById(R.id.image);
        titleTextView = view.findViewById(R.id.title);
        descriptionTextView = view.findViewById(R.id.description);
        logoutButton = view.findViewById(R.id.logout);
        favorite = view.findViewById(R.id.favorite);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Solo continuamos si animalId no es null ni vacío
        if (animalId != null && !animalId.isEmpty()) {
            checkIfFavorite();

            // Obtener datos de la base de datos para este animal
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("items").child(animalId);
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.exists()) {
                        Toast.makeText(requireContext(), "Animal no encontrado", Toast.LENGTH_SHORT).show();
                        if (getActivity() != null) {
                            getActivity().onBackPressed();
                        }
                        return;
                    }

                    String titulo = dataSnapshot.child("title").getValue(String.class);
                    String descripcion = dataSnapshot.child("description").getValue(String.class);
                    String imagen = dataSnapshot.child("imageUrl").getValue(String.class);

                    titleTextView.setText(titulo);
                    descriptionTextView.setText(descripcion);
                    if (imagen != null && isAdded()) {
                        Glide.with(requireContext())
                                .load(imagen)
                                .error(R.drawable.ic_error_placeholder)
                                .into(itemImageView);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    if (getActivity() != null) {
                        getActivity().onBackPressed();
                    }
                }
            });
        }

        favorite.setOnClickListener(view1 -> toggleFavorite());

        logoutButton.setOnClickListener(v -> {
            mAuth.signOut();
            if (mListener != null) {
                mListener.onLogout();
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context + " must implement OnDetailFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void checkIfFavorite() {
        if (animalId != null && !animalId.isEmpty()) {
            favoritesRef.child(animalId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (isAdded()) {
                        if (snapshot.exists()) {
                            isFavorite = true;
                            favorite.setImageResource(R.drawable.favorite);
                        } else {
                            isFavorite = false;
                            favorite.setImageResource(R.drawable.favorite_border);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    if (isAdded()) {
                        Toast.makeText(requireContext(), "Error al obtener favoritos", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.e("DetailFragment", "El animalId es null o vacío");
        }
    }


    private void toggleFavorite() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(userId)
                .child("favorites");

        favoritesRef.child(animalId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!isAdded()) return;

                if (snapshot.exists()) {
                    // Si el elemento ya está en favoritos, eliminarlo
                    favoritesRef.child(animalId).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && isAdded()) {
                            isFavorite = false;
                            favorite.setImageResource(R.drawable.favorite_border);
                            Toast.makeText(requireContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Si no está en favoritos, agregarlo
                    favoritesRef.child(animalId).setValue(true).addOnCompleteListener(task -> {
                        if (task.isSuccessful() && isAdded()) {
                            isFavorite = true;
                            favorite.setImageResource(R.drawable.favorite);
                            Toast.makeText(requireContext(), "Agregado a favoritos", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error al modificar favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}