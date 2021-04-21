package com.example.workmateadmin.modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class Empresa implements Serializable {
    private String idEmpresa, nombre, logo, localidad, provincia, direccion,descripcion,email,web,direccionGMaps, usernamecode;
    private ArrayList<String> categorias, imagenes;

    public Empresa(String idEmpresa, String nombre, String logo, String direccion, String descripcion, String email, ArrayList<String> categorias, ArrayList<String> imagenes, String usernamecode) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.logo = logo;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.categorias = categorias;
        this.imagenes = imagenes;
        this.email = email;
        this.usernamecode = usernamecode;
    }
    public Empresa(){

    }

    public Empresa(String idEmpresa, String nombre, String logo, String direccion, String descripcion, String email, String usernamecode) {
        this.idEmpresa = idEmpresa;
        this.nombre = nombre;
        this.logo = logo;
        this.direccion = direccion;
        this.descripcion = descripcion;
        this.email = email;
        this.categorias = new ArrayList<>();
        this.imagenes = new ArrayList<>();
        this.usernamecode = usernamecode;
    }

    public String getUsernamecode() {
        return usernamecode;
    }

    public void setUsernamecode(String usernamecode) {
        this.usernamecode = usernamecode;
    }

    public String getWeb() {
        return web;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getDireccionGMaps() {
        return direccionGMaps;
    }

    public void setDireccionGMaps(String direccionGMaps) {
        this.direccionGMaps = direccionGMaps;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public ArrayList<String> getCategorias() {
        return categorias;
    }

    public void setCategorias(ArrayList<String> categorias) {
        this.categorias = categorias;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }
}
