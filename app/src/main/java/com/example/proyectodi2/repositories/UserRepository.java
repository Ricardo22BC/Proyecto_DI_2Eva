package com.example.proyectodi2.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Gestiona la autenticación y almacenamiento de datos en Firebase,
public class UserRepository {

    private final FirebaseAuth mAuth;

    //DatabaseReference userRef: Referencia al nodo products en Firebase Realtime Database.
    public UserRepository() {

        mAuth = FirebaseAuth.getInstance();

    }

    public void registerUser(String email, String password, OnAuthCompleteListener listener) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        listener.onSuccess(user);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }


    public void loginUser(String email, String password, OnAuthCompleteListener listener) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        listener.onSuccess(user);
                    } else {
                        listener.onFailure(task.getException());
                    }
                });
    }

    public interface OnAuthCompleteListener {
        void onSuccess(FirebaseUser user);

        void onFailure(Exception e);
    }
}


//    public void getUsers(MutableLiveData<List<User>> userLiveData) {
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<User> users = new ArrayList<>();
//                for (DataSnapshot child : snapshot.getChildren()) {
//                    User user = child.getValue(User.class);
//                    users.add(user);
//                }
//                userLiveData.setValue(users);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Manejo de errores.
//                Log.e("Firebase", "Error al cargar usuarios", error.toException());
//            }
//        });
//    }



