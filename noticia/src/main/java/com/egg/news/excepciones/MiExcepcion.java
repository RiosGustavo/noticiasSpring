package com.egg.news.excepciones;


public class MiExcepcion extends Exception {
    
/// creamos esta clase para diferenciar los errores propios del negocio (programna de nocicias
/// de los errores propios del sistema)
    
    public MiExcepcion(String msg) {
        super(msg);
    }
    
    
}
