package com.egg.news.repositorios;

import com.egg.news.entidades.Noticia;
import com.egg.news.entidades.Periodista;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String>{


    // @Query metodo propio de JpaRepository para hacer una consulta en MySQL
    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    /// Param metodo propio de JpaRepository para buscar 
    public Noticia buscarPorTitulo(@Param("titulo") String Titulo);
    
     @Query("SELECT u FROM Usuario u WHERE u.nombreUsuario = :nombreUsuario")
    public List<Noticia> buscarPornombrePeriodista(@Param("nombreUsuario") String nombreUsuario);


}