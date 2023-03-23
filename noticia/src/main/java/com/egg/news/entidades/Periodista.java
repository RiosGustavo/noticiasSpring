/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.entidades;

import com.egg.news.enumeraciones.Rol;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.OneToMany;



/**
 *
 * @author User
 */
@Entity
public class Periodista extends Usuario {
    
   
    
   
    private Integer SueldoMensual ;

    public Periodista() {
    }

//    public Periodista(List<Noticia> misNoticias, Integer SueldoMensual) {
//        this.misNoticias = misNoticias;
//        this.SueldoMensual = SueldoMensual;
//    }
    
    
    

//    public Periodista( Integer SueldoMensual, String id, String nombreUsuario, String password, Date fecha_alta, Boolean activo, Rol rol) {
//        super(id, nombreUsuario, password, fecha_alta, activo, rol);
//        
//        this.SueldoMensual = SueldoMensual;
//    }

   
    
    
   

    public Integer getSueldoMensual() {
        return SueldoMensual;
    }

    public void setSueldoMensual(Integer SueldoMensual) {
        this.SueldoMensual = SueldoMensual;
    }
    
}
