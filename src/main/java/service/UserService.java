package service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {
	
	private final RestTemplate restTemplate = new RestTemplate();
	private final String userServiceUrl = "http://localhost:8081"; // URL del microservicio de users actualizado

    /**
     * Llama al microservicio de usuarios para eliminar un video del historial de todos los usuarios.
     *
     * @param videoId El ID del video que se debe eliminar del historial.
     */
    public void deleteHistory(Long videoId) {
        String url = userServiceUrl + "/user/deleteHistory"; // Construye la URL del endpoint

        try {
            // Llama al endpoint con un método POST, pasando el videoId en el cuerpo
            restTemplate.postForObject(url, videoId, Void.class);
        } catch (HttpClientErrorException e) {
            // Manejo de errores, puedes lanzar una excepción personalizada o registrar el error
            System.err.println("Error al llamar al servicio de usuarios para eliminar el historial: " + e.getMessage());
        }
}

}
