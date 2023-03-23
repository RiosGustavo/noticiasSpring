/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.repositorios;

import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface PeriodistaRepositorio extends JpaRepository<Periodista, String> {
    
     @Query("SELECT p FROM Periodista p WHERE p.id = :id")
    public Periodista buscarPornombrePeriodista(@Param("id") String id);
   
}
