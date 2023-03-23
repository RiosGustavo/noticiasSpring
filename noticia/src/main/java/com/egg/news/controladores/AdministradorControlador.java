/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.controladores;

import com.egg.news.entidades.Administrador;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.servicios.AdministradorServicio;
import java.util.List;
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
@RequestMapping("/administrador")
public class AdministradorControlador {
    
    @Autowired
    private AdministradorServicio administradorServicio;
    
     @GetMapping("/registrar")
    public String registrar() {
        return "administrador_form.html";
    }
    
    @PostMapping("/registro")
    public String registro(@RequestParam String id, @RequestParam String nombreUsuario, MultipartFile archivo,
            @RequestParam String password, String password2, ModelMap modelo) {

        try {
            administradorServicio.Registrar(archivo, id, nombreUsuario, password, password2);
                    

            modelo.put("exito", "Administrador Registrado Exitosamente");

        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            modelo.put("id", id);
            modelo.put("nombreUsuario", nombreUsuario);

            return "administrador_form.html";
        }
        return "index.html";
    }
    
    @GetMapping("/lista")
    public String listar(ModelMap modelo) {

        List<Administrador> administradores = administradorServicio.listarAdministradores();

        modelo.addAttribute("administradores", administradores);

        return "administrador_list.html";
    }
    
     @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {
        modelo.put("administrador", administradorServicio.getOne(id));

        return "administrador_modificar.html";
    }
    
    @PostMapping("/modificar/{id}")
    public String modificar(MultipartFile archivo, @PathVariable String id, String nombreUsuario,
             ModelMap modelo) {
        try {
            //// ACA TENGO UNA DUDA EN EL CONSTRUCTOR DE ACTUALIZAR 
            administradorServicio.
                    actualizar(archivo, id, nombreUsuario, id, id);

            return "redirect:../lista";
        } catch (MiExcepcion ex) {
            modelo.put("error", ex.getMessage());
            return "administrador_modificar.html";
        }

    }
    
    
}
