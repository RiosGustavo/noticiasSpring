package com.egg.news.controladores;

import com.egg.news.entidades.Usuario;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.servicios.UsuarioServicio;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

// con ewto configuramos cual es la url que va a escuchar a este controlador
// que en este caso va hacer la "/"
@Controller
@RequestMapping("/")
public class PortalControlador {

    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private AppController  appController;

    /// con est metodo le decimos que cuando ejecutemos la / en el marcador de las direcciones 
    // ejecuta todo el cuerpo de este metodo Html
    @GetMapping("/")
    public String index() {
        
       appController.sendEmail("gustavosuperlangorios@outlook.com", "gustavosuperlango@gmail.com");

        /// con este metodo devolvemos la vista de la pagina web que se hara en html
        return "index.html";

    }

    @GetMapping("/registrar")
    public String registrar() {
        return "registro.html";
    }

    @PostMapping("/registro")
    public String registro( @RequestParam String nombreUsuario,
            @RequestParam String password, String password2, ModelMap modelo,
            MultipartFile archivo) {

        try {
            usuarioServicio.registrar(archivo, nombreUsuario, password, password2);
                    

            modelo.put("exito", "Usuario Registrado Exitosamente");

            return "index.html";
        } catch (MiExcepcion ex) {

            modelo.put("error", ex.getMessage());
            
            modelo.put("nombreUsuario", nombreUsuario);

            return "registro.html";

        }

    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo) {

        if (error != null) {

            modelo.put("error", "Usuario o Password incorrectos");

        }

        return "login.html";
    }

    /// con esta anotacion @PreAuthorize se le indica al metodo de inicio que solo puede dar
    // acceso a un usuiario logfeado sin esto estrn cuaquuier usuairo sin la necesidad de loguearse
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session) {

        /// se castean para que el tipo de datos sea igual
        Usuario logeado = (Usuario) session.getAttribute("usuariosession");

        /// si esl usuario es admin lo redireccionamos a esta pagina
        if (logeado.getRol().toString().equals("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        return "inicio.html";

    }

    //// con este comando se autoriza a los usuarios o administradores que ya esten logueados el 
    // ingreso
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session) {

        Usuario usuario = (Usuario) session.getAttribute("usuariosession");

        modelo.put("usuario", usuario);

        return "usuario_modificar.html";

    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    @PostMapping("/perfil/(id)")
    public String actualizar(MultipartFile archivo, @PathVariable String id, @RequestParam String nombreUsuario,
            @RequestParam String password, String password2, ModelMap modelo) {

        try {
            usuarioServicio.actualizar(archivo, id, nombreUsuario, password, password2);

            modelo.put("exito", "usuario acutalizado Exitosamente!");
            return "inicio.html";

        } catch (MiExcepcion ex) {

            modelo.put("error", ex.getMessage());
            modelo.put("nombreUsuario", nombreUsuario);

            return "usuario_modificar.html";
        }

    }

}
