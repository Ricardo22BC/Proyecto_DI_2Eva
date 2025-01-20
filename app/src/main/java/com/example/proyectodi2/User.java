package com.example.proyectodi2;  // El paquete debe ser el mismo que el de la actividad

public class User {
    public String uid;
    public String nombre;
    public String email;
    public String phone;
    public String direccion;

    public User() {
        // Constructor vacío necesario para Firebase
    }

    public User(String uid, String nombre, String email, String phone, String direccion) {
        this.uid = uid;
        this.nombre = nombre;
        this.email = email;
        this.phone = phone;
        this.direccion = direccion;
    }
}
