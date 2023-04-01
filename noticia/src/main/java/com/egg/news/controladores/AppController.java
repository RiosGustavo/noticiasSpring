/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.egg.news.controladores;

import java.util.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;

/**
 *
 * @author User
 */
@Controller
public class AppController {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String from, String to) {
// use mailSender here...
       
SimpleMailMessage message = new SimpleMailMessage();
        
        
      //  Properties props = mailSender.getJavaMailProperties();
  // props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
                
        message.setFrom(from);
        message.setTo(to);
        message.setSubject("Asunto del correo");
        message.setText("Este es un correo automático!");
        
        mailSender.send(message); //método Send(envio), propio de Java Mail Sender.

    }

}
