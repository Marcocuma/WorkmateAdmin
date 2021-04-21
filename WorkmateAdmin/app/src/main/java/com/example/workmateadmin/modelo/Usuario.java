package com.example.workmateadmin.modelo;

public class Usuario {
    private String nombre, foto,email,idUsuario, usernamecode;

    public Usuario(){

    }

    public Usuario(String nombre, String email, String idUsuario, String usernamecode) {
        this.nombre = nombre;
        this.email = email;
        this.idUsuario = idUsuario;
        this.usernamecode = usernamecode;
    }

    public Usuario(String nombre, String foto, String email, String idUsuario, String usernamecode) {
        this.nombre = nombre;
        this.foto = foto;
        this.email = email;
        this.idUsuario = idUsuario;
        this.usernamecode = usernamecode;
    }

    public String getUsernamecode() {
        return usernamecode;
    }

    public void setUsernamecode(String usernamecode) {
        this.usernamecode = usernamecode;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
