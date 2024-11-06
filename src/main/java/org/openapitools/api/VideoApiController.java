package org.openapitools.api;

import org.DBAccess.VideoAccess;
import org.openapitools.model.GetDataVideoById200Response;
import org.openapitools.model.Video;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.constraints.*;
import javax.validation.Valid;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-15T15:54:30.961535300+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
@Controller
@RequestMapping("${openapi.tubeFlixGestionDeVideosOpenAPI30.base-path:}")
public class VideoApiController implements VideoApi {

    private final NativeWebRequest request;

    @Autowired
    public VideoApiController(NativeWebRequest request) {
        this.request = request;
    }

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.ofNullable(request);
    }
    
    @Override
    public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) {
        System.out.println("---createVideo API---");
        
        try {
        	VideoAccess cliente = new VideoAccess();
        	System.out.println("Iniciando programa");
        	if(!cliente.dbConectar()) {
        		System.out.println("Conexion fallida");
        	}
        	
        	cliente.dbAddVideo(video);
        	
        	if(!cliente.dbDesconectar()) {
        		System.out.println("Desconexión fallida");
        	}
            return new ResponseEntity<>(video, HttpStatus.CREATED); // Retorna el objeto video y estado 201 (Created)
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
