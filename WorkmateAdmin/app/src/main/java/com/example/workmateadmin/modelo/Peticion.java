package com.example.workmateadmin.modelo;

import com.google.firebase.firestore.DocumentReference;

public class Peticion {
    private String nombreEmpresa, idEmpresa, logo;

    public Peticion(String nombreEmpresa, String idEmpresa, String logo) {
        this.nombreEmpresa = nombreEmpresa;
        this.idEmpresa = idEmpresa;
        this.logo = logo;
    }

    public Peticion(){

    }

    public String getNombreEmpresa() {
        return nombreEmpresa;
    }

    public void setNombreEmpresa(String nombreEmpresa) {
        this.nombreEmpresa = nombreEmpresa;
    }

    public String getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(String idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
