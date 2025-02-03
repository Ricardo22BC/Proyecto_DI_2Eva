package com.example.proyectodi2.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.proyectodi2.repositories.UserRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {
    private final FirebaseAuth auth;
    private final MutableLiveData<String> loginStatus = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private final UserRepository userRepository;

    public LoginViewModel() {
        auth = FirebaseAuth.getInstance();
        userRepository = new UserRepository();
    }

    public LiveData<String> getLoginStatus() {
        return loginStatus;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loginUser(String email, String password, UserRepository.OnAuthCompleteListener listener) {
        if (email.isEmpty() || password.isEmpty()) {
            loginStatus.setValue("Completa todos los campos.");
            return;
        }

        isLoading.setValue(true);
        userRepository.loginUser(email, password, new UserRepository.OnAuthCompleteListener() {
            @Override
            public void onSuccess(FirebaseUser user) {
                isLoading.setValue(false);
                loginStatus.setValue("Iniciado sesión.");
                listener.onSuccess(user);
            }

            @Override
            public void onFailure(Exception e) {
                isLoading.setValue(false);
                loginStatus.setValue("Error en autenticación: " + e.getMessage());
                listener.onFailure(e);
            }
        });
    }

}
