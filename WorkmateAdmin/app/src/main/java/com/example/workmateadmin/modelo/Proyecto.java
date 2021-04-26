package com.example.workmateadmin.modelo;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;
import java.util.ArrayList;

public class Proyecto implements Serializable {
    private DocumentReference usuario;
    private String presentacion, descripcion, idProyecto,localidad,provincia, direccion, direccionGMaps;
    private Long metrosCuadrados,precioMax,creado;
    private ArrayList<String> imagenes, imagenesTerminado;
    private String estado;
    private ArrayList<String> caracteristicas, empresas;
    private ArrayList<Peticion> solicitudes;



    public Proyecto(){

    }

    public Proyecto(DocumentReference usuario, String presentacion, String descripcion, String idProyecto, String localidad, String provincia, Long metrosCuadrados, Long precioMax, ArrayList<String> imagenes, ArrayList<String> imagenesTerminado, String estado, ArrayList<String> caracteristicas, ArrayList<String> empresas, ArrayList<Peticion> solicitudes, Long creado) {
        this.usuario = usuario;
        this.presentacion = presentacion;
        this.descripcion = descripcion;
        this.idProyecto = idProyecto;
        this.localidad = localidad;
        this.provincia = provincia;
        this.metrosCuadrados = metrosCuadrados;
        this.precioMax = precioMax;
        this.imagenes = imagenes;
        this.imagenesTerminado = imagenesTerminado;
        this.estado = estado;
        this.caracteristicas = caracteristicas;
        this.empresas = empresas;
        this.solicitudes = solicitudes;
        this.creado = creado;
    }

    public String getDireccion() {
        return direccion;
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

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDireccionGMaps() {
        return direccionGMaps;
    }

    public void setDireccionGMaps(String direccionGMaps) {
        this.direccionGMaps = direccionGMaps;
    }

    public DocumentReference getUsuario() {
        return usuario;
    }

    public void setUsuario(DocumentReference usuario) {
        this.usuario = usuario;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIdProyecto() {
        return idProyecto;
    }

    public void setIdProyecto(String idProyecto) {
        this.idProyecto = idProyecto;
    }

    public Long getMetrosCuadrados() {
        return metrosCuadrados;
    }

    public void setMetrosCuadrados(Long metrosCuadrados) {
        this.metrosCuadrados = metrosCuadrados;
    }

    public Long getPrecioMax() {
        return precioMax;
    }

    public void setPrecioMax(Long precioMax) {
        this.precioMax = precioMax;
    }

    public ArrayList<String> getImagenes() {
        return imagenes;
    }

    public void setImagenes(ArrayList<String> imagenes) {
        this.imagenes = imagenes;
    }

    public ArrayList<String> getImagenesTerminado() {
        return imagenesTerminado;
    }

    public void setImagenesTerminado(ArrayList<String> imagenesTerminado) {
        this.imagenesTerminado = imagenesTerminado;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public ArrayList<String> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(ArrayList<String> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public ArrayList<Peticion> getSolicitudes() {
        return solicitudes;
    }

    public void setSolicitudes(ArrayList<Peticion> solicitudes) {
        this.solicitudes = solicitudes;
    }

    public ArrayList<String> getEmpresas() {
        return empresas;
    }

    public void setEmpresas(ArrayList<String> empresas) {
        this.empresas = empresas;
    }

    public Long getCreado() {
        return creado;
    }

    public void setCreado(Long creado) {
        this.creado = creado;
    }
}
