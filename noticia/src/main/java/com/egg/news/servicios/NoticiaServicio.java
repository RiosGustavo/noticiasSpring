package com.egg.news.servicios;

import com.egg.news.entidades.Noticia;
import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.repositorios.NoticiaRepositorio;
import com.egg.news.repositorios.PeriodistaRepositorio;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author User
 */
/// Esto esta en el video 5
@Service
public class NoticiaServicio {

    /// con este comando creamos el atributo NoticiaRepositorio
    // se le indica al sistema que esta variable va ha ser inicializada por id
    // esto es INYECCION DE DEPENDENCIAS
    @Autowired
    private NoticiaRepositorio noticaRepositorio;
    
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;

    /// con la siguiente anotiacion se realizan cambios en la base de datos al enviar un "commit" que afecta la
    /// base de datos   @Transactional 
    /// esta anotacion solo se pone en los metodos que modifican la base de datos 
    /// no en los de consulta
    
    
    @Transactional
    public void crearNoticia( String titulo, String cuerpo, String idPeriodista) throws MiExcepcion {

/// ESTO ESTA EN EL VIDEO 8 DE EXCEPCIONES ERRORES
        validar(   titulo, cuerpo, idPeriodista);
       
        Optional<Periodista> respuestaPeriodista = periodistaRepositorio.findById(idPeriodista);
        
        Periodista periodista = new Periodista();
        
        if (respuestaPeriodista.isPresent()) {
            
            periodista = (Periodista) respuestaPeriodista.get();
            
        }
        
        
        Noticia noti = new Noticia();

       
        noti.setTitulo(titulo);
        noti.setCuerpo(cuerpo);
        noti.setPeriodista(periodista);
                

        // persistir
        // con este atributo podemos llamar a todos los metodos que tiene el JpaRepositorio 
        // en este caso la funcion save para grabar la noticia que acabamos de crear
        noticaRepositorio.save(noti);

    }

    /// esto esta en el video 6
    // con este metodo  listamos todas la noticias
    @Transactional(readOnly = true)
    public List<Noticia> listarNoticias() {

        List<Noticia> noticias = new ArrayList();

        //esto lo trae el JpaRepositario con esta funcion que busca todo 
        /// buca todas las noticias que esten en noticiaRepositorio
        noticias = noticaRepositorio.findAll();
        return noticias;

    }

    /// esto esta en el video 7
    @Transactional
    public void modificarNotica( String id, String Titulo, String Cuerpo, String idPeriodista) throws MiExcepcion {
        
        if (id == null || id.isEmpty()) {
            throw new MiExcepcion("Debe ingrear un id de la noticia");
            
        }
        validar(   Titulo, Cuerpo, idPeriodista);

        /// con el comando optional prevenimos fallos en el id que no exista o no este relacionado a una noticia
        /// Optional es un objeto contenedor que puede cuyo valor no sea nulo
        Optional<Noticia> respuesta = noticaRepositorio.findById(id);
        Optional<Periodista> respuestaPeriodista = periodistaRepositorio.findById(id);
        
        Periodista periodista = new Periodista();
        
        if (respuestaPeriodista.isPresent()) {
            periodista = (Periodista) respuestaPeriodista.get();
            
        }

        // con este if no aseguramos que la respuesta traiga alguna informacion y no sea nulo
        // para ahi asi poder modificar el parametro que deseamos
        if (respuesta.isPresent()) {

            Noticia noti = respuesta.get();
            noti.setTitulo(Titulo);
            noti.setCuerpo(Cuerpo);
            noti.setPeriodista(periodista);
                    

            ///persistimos la noticia modificada guardandola en el repositorio
            noticaRepositorio.save(noti);
        }

    }
    
        ///con este comando busca la noticia en el repositorio por id
    @Transactional(readOnly = true)
    public Noticia getOne(String id){
       return noticaRepositorio.getOne(id);
    }
    
    /// este metodo es private porque solo lo utilizan los metodos de esta clase 
    private void validar( String titulo, String cuerpo, String idPeriodista) throws MiExcepcion{
        
       
        if (titulo.isEmpty() || titulo == null) {
            throw new MiExcepcion("Debe ingrear un Titulo de la noticia");
        }

        if (cuerpo.isEmpty() || cuerpo == null) {
            throw new MiExcepcion("Debe ingrear un Cuerpo de la noticia");
        }
        
         if(idPeriodista.isEmpty() || idPeriodista == null){
            throw new MiExcepcion("el Periodista no puede ser nulo o estar vacio");
        }
        
        
        
    }

}
