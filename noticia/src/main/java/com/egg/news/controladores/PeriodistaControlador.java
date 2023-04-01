/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.controladores;

import com.egg.news.entidades.Noticia;
import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.servicios.PeriodistaServicio;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/periodista")
public class PeriodistaControlador {

    @Autowired
    private PeriodistaServicio periodistaServicio;

    @GetMapping("/registrar")
    public String registrar() {
        return "periodista_form.html";
    }

    @PostMapping("/registro")
    public String registro( @RequestParam String nombreUsuario, MultipartFile archivo, @RequestParam Integer SueldoMensual,
            @RequestParam String password, String password2, ModelMap modelo) {

        try {
            periodistaServicio.registrar(archivo, nombreUsuario, SueldoMensual, password, password2);

            modelo.put("exito", "Periodista Registrado Exitosamente");

        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            
            modelo.put("nombreUsuario", nombreUsuario);

            return "periodista_form.html";
        }
        return "index.html";
    }

    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Periodista> periodistas = periodistaServicio.listarPeriodistas();

        modelo.addAttribute("periodistas", periodistas);

        return "periodista_list.html";
    }

    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("periodista", periodistaServicio.getOne(id));

        return "periodista_modificar.html";
    }
//public void actualizar(MultipartFile archivo, String id, String nombreUsuario, Integer SueldoMensual

    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String id, String nombreUsuario,
           String password, String password2,Integer SueldoMensual, ModelMap modelo) {
        try {
             //// ACA TENGO UNA DUDA EN EL CONSTRUCTOR DE ACTUALIZAR 
            periodistaServicio.actualizar(archivo, id, nombreUsuario, SueldoMensual, password, password2);

            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "periodista_modificar.html";
        }

    }

}
