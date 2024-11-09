package org.openapitools.api;

import org.DBAccess.*;
import org.openapitools.model.Comments;
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

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;

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

	@Override
	public ResponseEntity<Video> getVideoByid(@PathVariable("id") String id) {
		System.out.println("---getVideoByid API---");

		try {
			int videoId = Integer.parseInt(id); // Convierte el ID a entero
			if (videoId <= 0) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Devuelve 400 si el ID es inválido
			}

			VideoAccess cliente = new VideoAccess();
			System.out.println("Iniciando programa");

			// Conectar a la base de datos
			if (!cliente.dbConectar()) {
				System.out.println("Conexion fallida");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Consultar el video
			Video video = cliente.dbConsultarVideoPorId(videoId); // Asume que dbConsultarVideoPorId devuelve un objeto Video
			if (!cliente.dbDesconectar()) {
				System.out.println("Desconexión fallida");
			}

			// Verificar si el video se encontró
			if (video != null) {
				return new ResponseEntity<>(video, HttpStatus.OK); // Devuelve 200 si el video se encuentra
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Devuelve 404 si el video no se encuentra
			}

		} catch (NumberFormatException e) {
			System.out.println("ID no válido: " + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Devuelve 400 si el ID no es numérico
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // Devuelve 500 en caso de error del servidor
		}
	}

	@Override
	public ResponseEntity<Void> updateVideo(@PathVariable("id") String id, @Valid @RequestBody(required = false) Video video) {
		System.out.println("---updateVideo API---");

		try {
			int videoId = Integer.parseInt(id); // Convertir el ID a entero
			if (videoId <= 0) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
			}

			if (video == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el objeto video no está presente
			}

			VideoAccess cliente = new VideoAccess();
			System.out.println("Iniciando actualización de video");

			// Conectar a la base de datos
			if (!cliente.dbConectar()) {
				System.out.println("Conexión fallida");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Asignar el ID al objeto video para actualizar el registro correspondiente
			video.setId((long) videoId);

			// Intentar actualizar el video
			boolean actualizado = cliente.dbActualizarVideo(video);

			if (!cliente.dbDesconectar()) {
				System.out.println("Desconexión fallida");
			}

			if (actualizado) {
				return new ResponseEntity<>(HttpStatus.OK); // 200 si la actualización fue exitosa
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si el video no se encontró
			}

		} catch (NumberFormatException e) {
			System.out.println("ID no válido: " + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID no es numérico
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
		}
	}

	@Override
	public ResponseEntity<Void> addComment(Comments comment
			) {
		System.out.println("---addComment API---");

		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el comment video no está presente
		}

		try {
			int videoId = comment.getVideo();
			if (videoId <= 0) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
			}

			CommentAccess cliente = new CommentAccess();
			System.out.println("Iniciando inserción de comentario");

			// Conectar a la base de datos
			if (!cliente.dbConectar()) {
				System.out.println("Conexión fallida");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}


			// Intentar actualizar el video
			cliente.dbAddComment(comment);

			if (!cliente.dbDesconectar()) {
				System.out.println("Desconexión fallida");
			}

			return new ResponseEntity<>(HttpStatus.OK); // 200 si la actualización fue exitosa


		} catch (NumberFormatException e) {
			System.out.println("ID no válido: " + comment.getVideo());
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID no es numérico
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
		}
	}

	@Override
	public ResponseEntity<List<Comments>> getCommentsForVideo(@PathVariable("id") int id) {
		System.out.println("---getCommentsForVideo API---");
		List<Comments> commentsList = new ArrayList<>();

		try {
			int videoId = id; // Convertir el ID a entero
			if (videoId <= 0) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
			}

			CommentAccess cliente = new CommentAccess();
			System.out.println("Iniciando busqueda comentarios");

			// Conectar a la base de datos
			if (!cliente.dbConectar()) {
				System.out.println("Conexión fallida");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Asignar el ID al objeto video para actualizar el registro correspondiente

			// Intentar actualizar el video
			commentsList = cliente.dbGetAllCommentsFromVideo(id);

			if (!cliente.dbDesconectar()) {
				System.out.println("Desconexión fallida");
			}

			if (commentsList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no hay comentarios
			}
			return new ResponseEntity<>(commentsList,HttpStatus.OK); // 200 si la actualización fue exitosa

		} catch (NumberFormatException e) {
			System.out.println("ID no válido: " + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID no es numérico
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
		}
	}
	
	@Override
	public ResponseEntity<List<Video>> getVideosbyUser(int id) {
		
		System.out.println("---getVideosbyUser API---");
		List<Video> videosList = new ArrayList<>();

		try {
			int videoId = id; // Convertir el ID a entero
			if (videoId <= 0) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
			}

			VideoAccess cliente = new VideoAccess();
			System.out.println("Iniciando busqueda comentarios");

			// Conectar a la base de datos
			if (!cliente.dbConectar()) {
				System.out.println("Conexión fallida");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			// Asignar el ID al objeto video para actualizar el registro correspondiente

			// Intentar actualizar el video
			videosList = cliente.dbConsultarVideosByUser(id);

			if (!cliente.dbDesconectar()) {
				System.out.println("Desconexión fallida");
			}

			if (videosList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no hay comentarios
			}
			return new ResponseEntity<>(videosList,HttpStatus.OK); // 200 si la actualización fue exitosa

		} catch (NumberFormatException e) {
			System.out.println("ID no válido: " + id);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID no es numérico
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
		}
	}

}
