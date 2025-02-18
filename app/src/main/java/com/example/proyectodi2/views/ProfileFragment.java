package com.example.proyectodi2.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import com.example.proyectodi2.R;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileFragment extends Fragment {
    private EditText currentPasswordEditText, newPasswordEditText;
    private Switch darkModeSwitch;

    public ProfileFragment() { }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        currentPasswordEditText = view.findViewById(R.id.currentPasswordEditText);
        newPasswordEditText = view.findViewById(R.id.newPasswordEditText);
        darkModeSwitch = view.findViewById(R.id.darkModeSwitch);

        // Estado inicial del switch (obtener de SharedPreferences)
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        boolean isDarkMode = prefs.getBoolean("darkMode", false);
        darkModeSwitch.setChecked(isDarkMode);

        // Listener para botón "Cambiar contraseña"
        Button changePasswordButton = view.findViewById(R.id.changePasswordButton);
        changePasswordButton.setOnClickListener(v -> changePassword());

        // Listener para alternar modo oscuro/claro
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            toggleDarkMode(isChecked);
        });

        return view;
    }

    private void changePassword() {
        String currentPass = currentPasswordEditText.getText().toString();
        String newPass = newPasswordEditText.getText().toString();

        // Primero reautenticar al usuario (opcional) o directamente update:
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null && !newPass.isEmpty()) {
            // EJEMPLO: reautenticación con la credencial actual (si quieres ser estricto con la seguridad)
            AuthCredential credential = EmailAuthProvider
                    .getCredential(user.getEmail(), currentPass);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.updatePassword(newPass).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(getContext(), "Contraseña cambiada con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Error al cambiar la contraseña", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "La contraseña actual no es correcta", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void toggleDarkMode(boolean enableDarkMode) {
        // Guardamos la preferencia
        SharedPreferences prefs = requireActivity().getSharedPreferences("AppConfig", Context.MODE_PRIVATE);
        prefs.edit().putBoolean("darkMode", enableDarkMode).apply();

        // Aplicamos el tema
        if (enableDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        // Recreamos la activity para que se aplique el cambio
        requireActivity().recreate();
    }
}

