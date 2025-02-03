package com.example.proyectodi2.models;  // El paquete debe ser el mismo que el de la actividad
        //Representa los datos del usuario.
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


            public void setUid(String uid) {
                this.uid = uid;
            }

            public void setNombre(String nombre) {
                this.nombre = nombre;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public void setDireccion(String direccion) {
                this.direccion = direccion;
            }

            public String getEmail() {
                return email;
            }

            public String getNombre() {
                return nombre;
            }
            public String getUid() {
                return uid;
            }

            public String getPhone() {
                return phone;
            }

            public String getDireccion() {
                return direccion;
            }


        }