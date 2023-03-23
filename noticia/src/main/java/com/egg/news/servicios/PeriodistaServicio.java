/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.servicios;

import com.egg.news.entidades.Imagen;
import com.egg.news.entidades.Noticia;
import com.egg.news.entidades.Periodista;
import com.egg.news.entidades.Usuario;
import com.egg.news.enumeraciones.Rol;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.repositorios.NoticiaRepositorio;
import com.egg.news.repositorios.PeriodistaRepositorio;
import com.egg.news.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author User
 */
@Service
public class PeriodistaServicio implements UserDetailsService {

    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void registrar(MultipartFile archivo, String id, String nombreUsuario, Integer SueldoMensual,
            String password, String password2) throws MiExcepcion {
        
        validar( id, nombreUsuario, SueldoMensual, password, password2);

        Periodista periodista = new Periodista();

        periodista.setId(id);
        periodista.setNombreUsuario(nombreUsuario);
        periodista.setFecha_alta(new Date());
        periodista.setSueldoMensual(SueldoMensual);
        periodista.setPassword(new BCryptPasswordEncoder().encode(password));
        periodista.setRol(Rol.USER);

        Imagen imagen = imagenServicio.guardar(archivo);

        periodista.setImagen(imagen);

        periodistaRepositorio.save(periodista);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombreUsuario, Integer SueldoMensual,
            String password, String password2) throws MiExcepcion {
        
        validar( id, nombreUsuario, SueldoMensual, password, password2);

        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Periodista periodista = (Periodista) respuesta.get();
            periodista.setId(id);
            periodista.setNombreUsuario(nombreUsuario);
            periodista.setFecha_alta(new Date());
            periodista.setSueldoMensual(SueldoMensual);
            periodista.setPassword(new BCryptPasswordEncoder().encode(password));
            periodista.setRol(Rol.USER);

            String idImagen = null;

            if (periodista.getImagen() != null) {

                idImagen = periodista.getImagen().getId();

            }
            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            periodista.setImagen(imagen);

            periodistaRepositorio.save(periodista);

        }

    }
    
    
     public Periodista getOne(String id){
        return (Periodista) periodistaRepositorio.getOne(id);
    }
    
     @Transactional(readOnly=true)
    public List<Periodista> listarPeriodistas() {

        List<Periodista> periodistas = new ArrayList();

        periodistas = periodistaRepositorio.findAll();

        return periodistas;
    }
    
    
    private void validar(String id, String nombreUsuario, Integer SueldoMensual, String password, String password2) throws MiExcepcion {

        if (id == null) {
            throw new MiExcepcion("Debe ingrear un ID de usuario ");

        }
        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiExcepcion("Debe ingrear nombre Usuario");
        }
        
        if (SueldoMensual == null) {
            throw new MiExcepcion("Debe ingrear sueldo");
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("Debe ingrear un password y de mas de 5 caracteres");
        }

        if (!password.equals(password2)) {
            throw new MiExcepcion("Los password ingresados deben ser iguales");
        }

    }

     @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

        Usuario periodista = periodistaRepositorio.buscarPornombrePeriodista(nombreUsuario);

        if (periodista != null) {

            /// aca comenzamos a trabajar con Spring Security
            List<GrantedAuthority> permisos = new ArrayList();
            // con esto creamos los permisos que le damos a este usuario
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + periodista.getRol().toString());

            permisos.add(p);

            /// aca se atrapa el usuario que ha sido autenticado para poder mostarlo
            ServletRequestAttributes atrr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            // session es un objeto de la interfas HttpSession
            HttpSession session = atrr.getRequest().getSession(true);
            // se setan los atributos del usuario
            // esta variable guarda los datos del objeto  usuario quehemos traido y esta autenticado
            session.setAttribute("usuariosession", periodista);

            return new User(periodista.getNombreUsuario(), periodista.getPassword(), permisos);

        } else {
            return null;
        }
    }

}
