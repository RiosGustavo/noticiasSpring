package com.egg.news.controladores;

import com.egg.news.entidades.Noticia;
import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.servicios.NoticiaServicio;
import com.egg.news.servicios.PeriodistaServicio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author User
 */
// con ewto configuramos cual es la url que va a escuchar a este controlador
// que en este caso va hacer la "/"
@Controller
@RequestMapping("/noticia") // localhost:8080/noticia
public class NoticiaControlador {

    @Autowired
    private NoticiaServicio notiServicio;
    
    @Autowired
    private PeriodistaServicio periodistaServicio;

    @GetMapping("/registrar")// localhost:8080/noticia/registrar
    public String registrar(ModelMap modelo) {
        List<Periodista> periodistas =periodistaServicio.listarPeriodistas();
        
        modelo.addAttribute("periodistas", periodistas);

        return "noticia_form.html";

    }

    // hace los llamados necesarios para persistir la noticia
    /// el parametro nombre es el mismo que esta en noticia_fomr.html
    /// con el comando @REquestParam se le dice al programa este es un parametro requerido
    
    
    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) String id, @RequestParam String titulo,
            @RequestParam String cuerpo,  @RequestParam String idPeriodista,   ModelMap modelo) throws MiExcepcion {
        /// modelo sirve para mostrar la informacion por pantalla

        try {

            notiServicio.crearNoticia(id, titulo, cuerpo, idPeriodista);

            modelo.put("exito", "La noticia fue cargada Exitosamente "); // si todo va bien retornamos al index

        } catch (MiExcepcion ex) {
            List<Periodista> periodistas =periodistaServicio.listarPeriodistas();
            modelo.addAttribute("periodistas", periodistas);
            modelo.put("error", ex.getMessage());

            return "noticia_form.html"; // volvemos a cargar el formulario
        }

        return "index.html";

    }

    @GetMapping("/lista")
    public String Listar(ModelMap modelo) {

        List<Noticia> noticias = notiServicio.listarNoticias();

        modelo.addAttribute("noticias", noticias);

        return "noticia_list.html";

    }

    /// id es una path variable
    /// la cual viaja en la url /modificar/{id}
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo) {

        /// con este linea traemos del metodo getOne del repositorio la noticia correspondiente al id
        modelo.put("noticia", notiServicio.getOne(id));
        List<Periodista> periodistas = periodistaServicio.listarPeriodistas();
        
        modelo.addAttribute("periodistas", periodistas);

        return "noticia_modificar.html";

    }

    @PostMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, String titulo, String cuerpo,String idPeriodista, ModelMap modelo) throws MiExcepcion{
        try {
            List<Periodista> periodistas = periodistaServicio.listarPeriodistas();
            
            modelo.addAttribute("periodistas", periodistas);
            
            notiServicio.modificarNotica(id, titulo, cuerpo,idPeriodista);
            
            return "redirect:../lista";
            
        } catch (MiExcepcion ex) {
            
            List<Periodista> periodistas = periodistaServicio.listarPeriodistas();
            modelo.put("error", ex.getMessage());
            modelo.put("periodistas", periodistas);
            
            
            return "noticia_modificar.html";
        }
        
        
    }
}
