/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.controladores;

import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import com.egg.news.servicios.PeriodistaServicio;
import com.egg.news.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/imagen")
public class imagenControlador {

    @Autowired
    UsuarioServicio userServ;
   // PeriodistaServicio periodistaServicio;

    /// trae el perfil del ususario atraves del id
    @GetMapping("/perfil/{id}")              /// recibe el id de la Imagen
    public ResponseEntity<byte[]> imagenUsuario(@PathVariable String id) {

        Usuario user = userServ.getOne(id);
   //     Periodista perio = periodistaServicio.getOne(id);
   
        /// netbeans graba las imagenes en un arreglo de bytes

        byte[] imagen = user.getImagen().getContenido();

        /// se crea la instancia cabecera (headers) que provee spring 
        HttpHeaders headers = new HttpHeaders();
        
        /// le seteamos el contenido

        /// con esto le estamos diciendo al navegador que lo que estamos devolviendo es una Imagen
        /// ademas que es del tipo jpeg
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(imagen, headers, HttpStatus.OK);

    }

}
