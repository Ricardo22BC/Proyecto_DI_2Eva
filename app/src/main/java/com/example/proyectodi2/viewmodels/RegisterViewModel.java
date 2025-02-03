package com.example.proyectodi2.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

//El RegisterViewModel actúa como intermediario entre el repositorio y la vista.
// Expone los datos observables de los productos mediante LiveData.
public class RegisterViewModel extends ViewModel {
    private final FirebaseAuth auth;
    private final MutableLiveData<String> registerStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isRegistered = new MutableLiveData<>();


    public RegisterViewModel() {
        auth = FirebaseAuth.getInstance();
    }

    public LiveData<String> getRegisterStatus() {
        return registerStatus;
    }

    public LiveData<Boolean> getIsRegistered() {
        return isRegistered;
    }


    public void registerUser(String nombre, String email, String password, String conPassword, String phone, String direccion) {
        if (email.isEmpty() || password.isEmpty() || conPassword.isEmpty() || nombre.isEmpty() || phone.isEmpty() || direccion.isEmpty()) {
            registerStatus.setValue("Por favor, completa todos los campos.");
            return;
        }

        if (!password.equals(conPassword)) {
            registerStatus.setValue("Las contraseñas no coinciden.");
            return;
        }

        if (password.length() < 6) {
            registerStatus.setValue("La contraseña debe tener al menos 6 caracteres.");
            return;
        }

        isRegistered.setValue(true);

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    isRegistered.setValue(false);
                    if (task.isSuccessful()) {
                        registerStatus.setValue("Usuario registrado correctamente.");
                    } else {
                        registerStatus.setValue("Error en el registro: " + task.getException().getMessage());
                    }
                });
    }

}
