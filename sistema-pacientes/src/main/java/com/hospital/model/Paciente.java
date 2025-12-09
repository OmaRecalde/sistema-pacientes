package com.hospital.model;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Modelo Paciente - JavaBean
 * Representa un paciente en el sistema hospitalario
 */
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;

    // Atributos privados
    private Integer id;
    private String nombre;
    private String cedula;
    private String correo;
    private Integer edad;
    private String direccion;
    private Boolean activo;
    private Timestamp fechaRegistro;

    // Constructor vacío
    public Paciente() {
    }

    // Constructor con parámetros
    public Paciente(Integer id, String nombre, String cedula, String correo,
                    Integer edad, String direccion, Boolean activo) {
        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.correo = correo;
        this.edad = edad;
        this.direccion = direccion;
        this.activo = activo;
    }

    // Getters y Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Timestamp getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(Timestamp fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return "Paciente{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                ", correo='" + correo + '\'' +
                ", edad=" + edad +
                ", direccion='" + direccion + '\'' +
                ", activo=" + activo +
                ", fechaRegistro=" + fechaRegistro +
                '}';
    }
}
