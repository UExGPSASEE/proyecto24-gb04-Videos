package org.openapitools.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import java.io.IOException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.DBAccess.*;
import org.openapitools.model.Video;

/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {
	
    @RequestMapping("/")
    public String index() {
    	
    	VideoAccess cliente = new VideoAccess();
    	System.out.println("Iniciando programa");
    	if(!cliente.dbConectar()) {
    		System.out.println("Conexion fallida");
    	}
    	
        ArrayList<Video> videos = new ArrayList<Video>();
        videos = cliente.dbGetAllVideos();
    	for(Video v : videos) {
    		System.out.println("ID " + v.getId());
    		System.out.println("TITULO " + v.getTitle());
    		System.out.println("-----------------------------------");
    	}
    	
    	if(!cliente.dbDesconectar()) {
    		System.out.println("Desconexión fallida");
    	}
        return "redirect:swagger-ui.html";
    }

}