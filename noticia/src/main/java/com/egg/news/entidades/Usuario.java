/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.entidades;

import com.egg.news.enumeraciones.Rol;
import java.io.Serializable;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author User
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public  class Usuario  {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    protected String id;
    
    protected String nombreUsuario;
    protected String password;
    
    @Temporal(TemporalType.DATE)
    protected Date fecha_alta;
    
    protected Boolean activo;
    
    @Enumerated(EnumType.STRING)
    protected Rol rol;
    
    @OneToOne
    protected Imagen Imagen;

    public Usuario() {
    }

    public Usuario(String id, String nombreUsuario, String password, Date fecha_alta, Boolean activo, Rol rol, Imagen Imagen) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.password = password;
        this.fecha_alta = fecha_alta;
        this.activo = activo;
        this.rol = rol;
        this.Imagen = Imagen;
    }

    

    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getFecha_alta() {
        return fecha_alta;
    }

    public void setFecha_alta(Date fecha_alta) {
        this.fecha_alta = fecha_alta;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Imagen getImagen() {
        return Imagen;
    }

    public void setImagen(Imagen Imagen) {
        this.Imagen = Imagen;
    }
    
    
    
}
