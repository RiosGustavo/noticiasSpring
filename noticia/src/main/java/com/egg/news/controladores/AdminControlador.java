/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.controladores;

import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import com.egg.news.servicios.PeriodistaServicio;
import com.egg.news.servicios.UsuarioServicio;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author User
 */
@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private UsuarioServicio userServ;
    
    @Autowired
    private PeriodistaServicio periodistaServicio;
    
    /// el dashboard es una convencion que su utiliza para motrar
    /// el panel de administracion 
    @GetMapping("/dashboard")
    public String panelAdministrativo(HttpSession session, ModelMap model){
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        model.addAttribute("logueado", logueado.getNombreUsuario());
        
        return "panel.html";
        
    }
    
    @GetMapping("/usuarios")
    public String listar(ModelMap modelo){
        List<Usuario> usuarios =  userServ.listarUsuarios();
       // List<Periodista> periodistas = periodistaServicio.listarPeriodistas();
        
        modelo.addAttribute("usuarios", usuarios);
    //    modelo.addAttribute("usuarios", periodistas);
        
        return "usuario_list.html";
        
    }
    
    @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        userServ.cambiarRol(id);
        //periodistaServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
        
    }
    
    
}
