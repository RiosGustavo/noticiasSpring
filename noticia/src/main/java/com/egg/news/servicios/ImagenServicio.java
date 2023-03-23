/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.servicios;

import com.egg.news.entidades.Imagen;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.repositorios.ImagenRepositorio;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class ImagenServicio {

    @Autowired
    private ImagenRepositorio imgRepo;

    public Imagen guardar(MultipartFile archivo) throws MiExcepcion {

        if (archivo != null) {
            try {

                Imagen img = new Imagen();

                img.setMime(archivo.getContentType());
                
                img.setNombre(archivo.getName());
                
                img.setContenido(archivo.getBytes());

                return imgRepo.save(img);

            } catch (Exception e) {
                System.err.println(e.getMessage());
            }

        }
        return null;

    }
    
    
    public Imagen actualizar(MultipartFile archivo, String idImagen) throws MiExcepcion{
        
        if (archivo != null) {
            
            try {
                
                 Imagen img = new Imagen();
                 
                 if (idImagen != null) {
                     
                     Optional<Imagen> respuesta = imgRepo.findById(idImagen);
                     
                     if (respuesta.isPresent()) {
                         img = respuesta.get();
                     }
                    
                }

                img.setMime(archivo.getContentType());
                img.setNombre(archivo.getName());
                img.setContenido(archivo.getBytes());

                return imgRepo.save(img);
                
            } catch (Exception e) {
                
                System.err.println(e.getMessage());
            }
            
        }
        
        
        return null;
        
    }
    
     @Transactional(readOnly = true)
	public List<Imagen> listarTodos() {
            
           
		return  imgRepo.findAll();
	}

}
