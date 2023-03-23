/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.repositorios;


import com.egg.news.entidades.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author User
 */
@Repository
public interface AdministradorRepositorio extends JpaRepository<Administrador, String>{
    
    @Query("SELECT a FROM Administrador a WHERE a.id = :id")
    public Administrador buscarPornombreAdministrador(@Param("id") String id);
    
}
