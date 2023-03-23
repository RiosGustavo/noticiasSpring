/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news;

import com.egg.news.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author User
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SeguridadWeb extends WebSecurityConfigurerAdapter {

    @Autowired
    public UsuarioServicio usuarioServicio;
    
    
    /// con este metodo configuramos el manejador de seguridad que posse Spring Security
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        
        
        /// este es el metodo hecho en usuarioServicio para autenticar los usuarios
        // y asi tambien poder codificar "encriptar" el password con la funcion BCryptPasswordEncoder()
        auth.userDetailsService(usuarioServicio).passwordEncoder(new BCryptPasswordEncoder());
        
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
        http
                .authorizeRequests()
                        // con esta linea se condiciona el acceso a toda la calse admin desde la seguridad
                        // SOLO LOS QUE TIENEN EL ROL ADMIN PUENDEN 
                        .antMatchers("/admin/*").hasRole("ADMIN")
                        .antMatchers("/css/*", "/js/*", "/img/*", "/**")
                        .permitAll()
                
                /// aca vamos a configurar el ingreso al sistema log in
                .and().formLogin()
                        .loginPage("/login")
                        .loginProcessingUrl("/logincheck")
                        .usernameParameter("nombreUsuario")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/inicio")
                        .permitAll()
        
                 /// aca vamos a configurar la salidad del sistema log aut
                 .and().logout()
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                .and().csrf()
                        .disable();
        
        /// con estos comandos se agrega la configuracion para logearnos
                        
        
    }
}
