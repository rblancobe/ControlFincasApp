package com.hortichuelas.controlfincas.Models;

public class Empresas {
    private int codigo;
    private String empresa;
    private int actividad;

    public Empresas(int codigo, String empresa, int actividad) {
        this.codigo = codigo;
        this.empresa = empresa;
        this.actividad = actividad;
    }

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getEmpresa() {
        return empresa;
    }

    public void setEmpresa(String empresa) {
        this.empresa = empresa;
    }

    public int getActividad() {
        return actividad;
    }

    public void setActividad(int actividad) {
        this.actividad = actividad;
    }
}
