/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.servicios;

import com.egg.news.entidades.Administrador;
import com.egg.news.entidades.Imagen;
import com.egg.news.entidades.Usuario;
import com.egg.news.enumeraciones.Rol;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.repositorios.AdministradorRepositorio;
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
public class AdministradorServicio implements UserDetailsService {

    @Autowired
    private AdministradorRepositorio administradorRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imagenServicio;

    @Transactional
    public void Registrar(MultipartFile archivo,  String nombreUsuario,String password, String password2) throws MiExcepcion {

        validar( nombreUsuario, password, password2);

        Administrador administrador = new Administrador();

        
        administrador.setNombreUsuario(nombreUsuario);
        administrador.setFecha_alta(new Date());

        administrador.setPassword(new BCryptPasswordEncoder().encode(password));
        administrador.setRol(Rol.ADMIN);

        Imagen imagen = imagenServicio.guardar(archivo);

        administrador.setImagen(imagen);

        administradorRepositorio.save(administrador);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombreUsuario,
            String password, String password2) throws MiExcepcion {

         if (id == null || id.isEmpty()) {
            throw new MiExcepcion("Debe ingrear un id de la noticia");
            
        }
        
        validar( nombreUsuario, password, password2);

        Optional<Administrador> respuesta = administradorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Administrador administrador = (Administrador) respuesta.get();

            administrador.setId(id);
            administrador.setNombreUsuario(nombreUsuario);
            administrador.setFecha_alta(new Date());
            administrador.setPassword(new BCryptPasswordEncoder().encode(password));
            administrador.setRol(Rol.ADMIN);

            String idImagen = null;

            if (administrador.getImagen() != null) {

                idImagen = administrador.getImagen().getId();

            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            administrador.setImagen(imagen);

            administradorRepositorio.save(administrador);

        }

    }

    public Administrador getOne(String id) {
        return (Administrador) administradorRepositorio.getOne(id);
    }

    @Transactional(readOnly = true)
    public List<Administrador> listarAdministradores() {

        List<Administrador> administradores = new ArrayList();

        administradores = administradorRepositorio.findAll();

        return administradores;
    }

    @Transactional
    public void cambiarRol(String id) {
        Optional<Administrador> respuesta = administradorRepositorio.findById(id);

        if (respuesta.isPresent()) {

            Administrador administrador = (Administrador) respuesta.get();

            if (administrador.getRol().equals(Rol.USER)) {

                administrador.setRol(Rol.ADMIN);

            } else if (administrador.getRol().equals(Rol.ADMIN)) {

                administrador.setRol(Rol.USER);

            }

        }
    }

     private void validar( String nombreUsuario, String password, String password2) throws MiExcepcion {

        
        if (nombreUsuario.isEmpty() || nombreUsuario == null) {
            throw new MiExcepcion("Debe ingrear nombre Usuario");
        }

        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiExcepcion("Debe ingrear un password y de mas de 5 caracteres");
        }

        if (!password.equals(password2)) {
            throw new MiExcepcion("Los password ingresados deben ser iguales");
        }

    }

    /// metodo abstracto impleentado por  UserDetailsService
    /// con este metodo cuando un usuairo se loguea lelga aca y spring security le da los permisos a los que tiene lugar
    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {

        Usuario administrador = administradorRepositorio.buscarPornombreAdministrador(nombreUsuario);
                

        if (administrador != null) {

            /// aca comenzamos a trabajar con Spring Security
            List<GrantedAuthority> permisos = new ArrayList();
            // con esto creamos los permisos que le damos a este usuario
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + administrador.getRol().toString());

            permisos.add(p);

            /// aca se atrapa el usuario que ha sido autenticado para poder mostarlo
            ServletRequestAttributes atrr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            // session es un objeto de la interfas HttpSession
            HttpSession session = atrr.getRequest().getSession(true);
            // se setan los atributos del usuario
            // esta variable guarda los datos del objeto  usuario quehemos traido y esta autenticado
            session.setAttribute("usuariosession", administrador);

            return new User(administrador.getNombreUsuario(), administrador.getPassword(), permisos);

        } else {
            return null;
        }
    }
    
}
