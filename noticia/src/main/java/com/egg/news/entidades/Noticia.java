package com.egg.news.entidades;

import javax.persistence.Entity;

import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;




/**
 * import javax.persistence.Entity;
 *
 * @author User
 */

/// indicamos que es una entidad
@Entity
public class Noticia {

    
    // con este comando generamos automaticamente el id 
    @Id
    private String id;
    
    private String titulo;
    

    private String Cuerpo;

   
    @ManyToOne
    private Periodista periodista;
     
    public Noticia() {
    }

    public Noticia(String id, String titulo, String Cuerpo, Periodista periodista) {
        this.id = id;
        this.titulo = titulo;
        this.Cuerpo = Cuerpo;
        this.periodista = periodista;
    }

   
    
    
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {
        return Cuerpo;
    }

    public void setCuerpo(String Cuerpo) {
        this.Cuerpo = Cuerpo;
    }

    public Periodista getPeriodista() {
        return periodista;
    }

    public void setPeriodista(Periodista periodista) {
        this.periodista = periodista;
    }

    
    
    
    

}
