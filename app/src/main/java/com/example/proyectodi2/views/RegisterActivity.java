package com.example.proyectodi2.views;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.proyectodi2.R;
import com.google.firebase.auth.FirebaseAuth;

import com.example.proyectodi2.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel registerViewModel;
    private FirebaseAuth mAuth;
    private EditText nombreEditText,emailEditText, passwordEditText, conPasswordEditText, phoneEditText, dirEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance(); // Devuelve la instancia de autenticación de Firebase.
        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        nombreEditText= findViewById(R.id.nombreEditText);
        emailEditText= findViewById(R.id.emailEditText);
        passwordEditText= findViewById(R.id.passwordEditText);
        conPasswordEditText= findViewById(R.id.conPasswordEditText);
        phoneEditText= findViewById(R.id.phoneEditText);
        dirEditText= findViewById(R.id.dirEditText);

        findViewById(R.id.registerButton).setOnClickListener(v -> {
            String name = nombreEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String conPassword = conPasswordEditText.getText().toString().trim();
            String phone = phoneEditText.getText().toString().trim();
            String direccion = dirEditText.getText().toString().trim();

            registerViewModel.registerUser(name,email,password,conPassword,phone,direccion);

        });

        findViewById(R.id.loginButton).setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);

        });

        seeViewModel();
//        //Consultar datos de una tabla en FireBase utilizamos un ValueEventListener. Lineas 38-55
//
//        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
//        //DatabaseReference: Representa una referencia a una ubicación en la base de datos.
//
//        ValueEventListener userListener = new ValueEventListener() {
//            //ValueEventListener: Es un oyente que se utiliza para leer datos de Firebase. Contiene dos métodos principales:
//            //onDataChange: Se ejecuta cuando se obtienen datos de la base de datos. Los datos se devuelven como un objeto DataSnapshot que permite acceder a los valores almacenados.
//            //onCancelled: Se ejecuta si ocurre algún error al intentar leer los datos.
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
//                    String userName = userSnapshot.child("name").getValue(String.class);
//                    Log.d("Firebase", "Nombre del usuario: " + userName);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.w("Firebase", "Error al leer datos", databaseError.toException());
//            }
//        };
//
//        databaseRef.addListenerForSingleValueEvent(userListener);
//        //addListenerForSingleValueEvent: Lee los datos de la base de datos una única vez en lugar de escuchar cambios en tiempo real.
//        //DataSnapshot: Proporciona acceso a los datos reales en el nodo de la base de datos al que se hace referencia.
    }

    private void seeViewModel() {
        registerViewModel.getRegisterStatus().observe(this, status -> {
            if (status != null) {
                Toast.makeText(RegisterActivity.this, status,Toast.LENGTH_SHORT).show();

                if (status.equals("Usuario registrado")) {
                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        registerViewModel.getIsRegistered().observe(this, isRegistered -> {

        });
//    private void registerUser() {
//        String nombre = ((EditText) findViewById(R.id.nombreEditText)).getText().toString();
//        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
//        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
//        String conPassword = ((EditText) findViewById(R.id.conPasswordEditText)).getText().toString();
//        String phone = ((EditText) findViewById(R.id.phoneEditText)).getText().toString();
//        String direccion = ((EditText) findViewById(R.id.dirEditText)).getText().toString();
//
//        registerViewModel.register(nombre, email, password, conPassword, phone, direccion);
//        if (validateInput(email, password, conPassword, nombre, phone, direccion)) {
//            mAuth.createUserWithEmailAndPassword(email, password) // Registra un nuevo usuario con el correo y contraseña proporcionados.
//                    .addOnCompleteListener(this, task -> { // addOnCompleteListener: Se ejecuta al completar el registro, indicando éxito o error.
//                        if (task.isSuccessful()) {
//                            Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
//                            Log.d("Firebase", "Usuario registrado correctamente");
//
//                            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
//                            String uid = mAuth.getCurrentUser().getUid();
//                            User newUser = new User(uid, nombre, email, phone, direccion);
//                            databaseRef.child(uid).setValue(newUser)
//                                    .addOnCompleteListener(dbTask -> {
//                                        if (dbTask.isSuccessful()) {
//                                            Log.d("Firebase", "Usuario añadido a Realtime Database con UID: " + uid);
//                                        } else {
//                                            Log.e("Firebase", "Error al añadir usuario a la base de datos", dbTask.getException());
//                                        }
//                                    });
//                        } else {
//                            Log.e("Firebase", "Error al registrar usuario", task.getException());
//                        }
//                    });
//        } else {
//            Toast.makeText(this, "Por favor, complete todos los campos correctamente.", Toast.LENGTH_SHORT).show();
//        }
    }

//    private boolean validateInput(String email, String password, String conPassword, String nombre, String phone, String direccion) {
//        return !email.isEmpty() && !password.isEmpty() && password.equals(conPassword) &&
//                password.length() >= 6 && !nombre.isEmpty() && !phone.isEmpty() && !direccion.isEmpty();
//    }

//        if(nombre.isEmpty() || email.isEmpty() || password.isEmpty() || conPassword.isEmpty() || phone.isEmpty() || direccion.isEmpty()) {
//          Toast.makeText(RegisterActivity.this,"Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show();
//          return;
//        }
//
//        if (password.length() < 6) {
//            Toast.makeText(RegisterActivity.this, "La contraseña debe tener al menos 6 caracteres.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if(!password.equals(conPassword)){
//            Toast.makeText(RegisterActivity.this, "Las contraseñas no coinciden.", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        mAuth.createUserWithEmailAndPassword(email, password) //Registra un nuevo usuario con el correo y contraseña proporcionados.
//                .addOnCompleteListener(this, task -> {//addOnCompleteListener: Se ejecuta al completar el registro, indicando éxito o error.
//                    if (task.isSuccessful()) {
//                        Toast.makeText(RegisterActivity.this, "Usuario registrado correctamente.", Toast.LENGTH_SHORT).show();
//                        Log.d("Firebase", "Usuario registrado correctamente");
//
//                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
//                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
//
//                        if (firebaseUser != null) {
//                            String uid = firebaseUser.getUid();
//                            User newUser = new User(uid, nombre, email, phone, direccion);
//                            //Tenemos que tener la clase User que es como se van a guardar los datos.
//                            databaseRef.child(uid).setValue(newUser)
//                                    .addOnCompleteListener(dbTask -> {
//                                        if (dbTask.isSuccessful()) {
//                                            Log.d("Firebase", "Usuario añadido a Realtime Database con UID: " + uid);
//                                        } else {
//                                            Log.e("Firebase", "Error al añadir usuario a la base de datos", dbTask.getException());
//                                        }
//                                    });
//                        }
//                    } else {
//                        Log.e("Firebase", "Error al registrar usuario", task.getException());
//                    }
//                });
//
//        //push(): Genera una clave única para cada nodo hijo, en el ejemplo usamos la Id única proporcionada por Authentication de Firebase. Si queremos añadir un elemento a otra tabla que no sea la de usuario, usaremos push().getKey()para hacerlo.
//        //setValue(Object value): Escribe un objeto en la base de datos.
//        //addOnCompleteListener: Informa si la operación fue exitosa o falló.


//    private void loginUser() {
//        String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
//        String password = ((EditText) findViewById(R.id.passwordEditText)).getText().toString();
//
//        mAuth.signInWithEmailAndPassword(email, password) // Autentica al usuario con las credenciales proporcionadas.
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        //Toast: Muestra un mensaje en pantalla para retroalimentar al usuario.
//                        Toast.makeText(RegisterActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "Error en autenticación." , Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
//    private void loginUser() {
//        String email = emailEditText.getText().toString();
//        String password = passwordEditText.getText().toString();
//
//        mAuth.signInWithEmailAndPassword(email, password) // Autentica al usuario con las credenciales proporcionadas.
//                .addOnCompleteListener(this, task -> {
//                    if (task.isSuccessful()) {
//                        // Toast: Muestra un mensaje en pantalla para retroalimentar al usuario.
//                        Toast.makeText(RegisterActivity.this, "Inicio de sesión exitoso.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(RegisterActivity.this, DashboardActivity.class);
//                        startActivity(intent);
//                        finish();
//                    } else {
//                        Toast.makeText(RegisterActivity.this, "Error en autenticación.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}


