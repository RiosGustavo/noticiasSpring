/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.servicios;

import com.egg.news.entidades.Usuario;
import com.egg.news.entidades.Imagen;
import com.egg.news.enumeraciones.Rol;
import com.egg.news.excepciones.MiExcepcion;
import com.egg.news.repositorios.ImagenRepositorio;
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
import org.springframework.web.servlet.support.RequestContext;

/**
 *
 * @author User
 */
@Service
public class UsuarioServicio implements UserDetailsService {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private ImagenServicio imgServ;
    
    
    @Transactional
    public void registrar(MultipartFile archivo, String id, String nombreUsuario, 
            String password, String password2) throws MiExcepcion {

        validar(id, nombreUsuario, password, password2);

        Usuario usuario = new Usuario();
   
        usuario.setId(id);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setFecha_alta(new Date());

        /// con estos comandos encritamos el password atreaves del metodo hecho en securityweb clase
        usuario.setPassword(new BCryptPasswordEncoder().encode(password));

        /// aca le eindicamos al programa que por defecto cada usuario qeu se registre se ha del tipo
        // usduario y no administrador
        usuario.setRol(Rol.USER);

        Imagen imagen = imgServ.guardar(archivo);

        usuario.setImagen(imagen);

        usuarioRepositorio.save(usuario);

    }

    @Transactional
    public void actualizar(MultipartFile archivo, String id, String nombreUsuario,
            String password, String password2) throws MiExcepcion {

        validar(id, nombreUsuario, password, password2);
        
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Usuario usuario = respuesta.get();
            
            usuario.setId(id);
            usuario.setNombreUsuario(nombreUsuario);
            usuario.setFecha_alta(new Date());

            /// con estos comandos encritamos el password atreaves del metodo hecho en securityweb clase
            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            /// aca le eindicamos al programa que por defecto cada usuario qeu se registre se ha del tipo
            // usduario y no administrador
            usuario.setRol(Rol.USER);
            
            String idImagen = null;
            
            if (usuario.getImagen() != null) {
                
                idImagen = usuario.getImagen().getId();
                
            }

            Imagen img = imgServ.actualizar(archivo, idImagen);

            usuario.setImagen(img);

            usuarioRepositorio.save(usuario);

        }

    }
    
      public Usuario getOne(String id){
        return usuarioRepositorio.getOne(id);
    }
      
      @Transactional(readOnly=true)
    public List<Usuario> listarUsuarios() {

        List<Usuario> usuarios = new ArrayList();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }

    @Transactional
    public void cambiarRol(String id){
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        
        if (respuesta.isPresent()) {
            
            Usuario usuario = respuesta.get();
            
            if (usuario.getRol().equals(Rol.USER)) {
                
                usuario.setRol(Rol.ADMIN);
                
            } else if(usuario.getRol().equals(Rol.ADMIN)){
                
                usuario.setRol(Rol.USER);
                
            }
            
        }
    }
    
    
    private void validar(String id, String nombreUsuario, String password, String password2) throws MiExcepcion {

        if (id == null) {
            throw new MiExcepcion("Debe ingrear un ID de usuario ");

        }
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

        Usuario usuario = usuarioRepositorio.buscarPornombreUsuario(nombreUsuario);

        if (usuario != null) {

            /// aca comenzamos a trabajar con Spring Security
            List<GrantedAuthority> permisos = new ArrayList();
            // con esto creamos los permisos que le damos a este usuario
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);

            /// aca se atrapa el usuario que ha sido autenticado para poder mostarlo
            ServletRequestAttributes atrr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();

            // session es un objeto de la interfas HttpSession
            HttpSession session = atrr.getRequest().getSession(true);
            // se setan los atributos del usuario
            // esta variable guarda los datos del objeto  usuario quehemos traido y esta autenticado
            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getNombreUsuario(), usuario.getPassword(), permisos);

        } else {
            return null;
        }
    }

}
