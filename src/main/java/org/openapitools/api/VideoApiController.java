package org.openapitools.api;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Generated;
import javax.validation.Valid;

import org.DBAccess.CommentAccess;
import org.DBAccess.VideoAccess;
import org.openapitools.model.Comments;
import org.openapitools.model.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

import io.swagger.v3.oas.annotations.Parameter;
import service.UserService;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-10-15T15:54:30.961535300+02:00[Europe/Madrid]", comments = "Generator version: 7.9.0")
@Controller
@RequestMapping("${openapi.tubeFlixGestionDeVideosOpenAPI30.base-path:}")
public class VideoApiController implements VideoApi {

	private final NativeWebRequest request;
	
	@Autowired
	private UserService userservice;

	@Autowired
	public VideoApiController(NativeWebRequest request) {
		this.request = request;
	}

	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.ofNullable(request);
	}
	
    // Endpoint de prueba
    @GetMapping("/health")
    public String healthCheck() {
        return "health";
    }
    

	@Override
	public ResponseEntity<Video> createVideo(@Valid @RequestBody Video video) {
		System.out.println("-------- VideoApiController -> createVideo() -----------");

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
		System.out.println("-------- VideoApiController -> getVideoByid() -----------");

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
	public ResponseEntity<Void> updateVideo(@Valid @RequestBody(required = false) Video video) {
		System.out.println("-------- VideoApiController -> updateVideo() -----------");

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

			// Intentar actualizar el video
			Video videoAEditar = cliente.dbConsultarVideoByUserAndTitle(video.getUser(), video.getTitle());
			
			videoAEditar.setTitle(video.getTitle());
			videoAEditar.setDuration(video.getDuration());
			videoAEditar.setUploadDate(video.getUploadDate());
			videoAEditar.setDescription(video.getDescription());
			videoAEditar.setGenre(video.getGenre());
			videoAEditar.setLikes(0);
			videoAEditar.setAgeRestricted(video.getAgeRestricted());
			videoAEditar.setCountryRestricted((ArrayList<String>) video.getCountryRestricted());
			
			
			boolean actualizado = cliente.dbActualizarVideo(videoAEditar);

			if (!cliente.dbDesconectar()) {
				System.out.println("Desconexión fallida");
			}

			if (actualizado) {
				return new ResponseEntity<>(HttpStatus.OK); // 200 si la actualización fue exitosa
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si el video no se encontró
			}


	}

	@Override
	public ResponseEntity<Void> addComment(Comments comment) {
		System.out.println("-------- VideoApiController -> addComment() -----------");
		
		//System.out.println("---addComment API-------------- Usuario que hace el comentario:" + comment.getUsername());
		if (comment == null) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el comment video no está presente
		}

		try {
			Long videoId = comment.getVideo();
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
		System.out.println("-------- VideoApiController -> getCommentsForVideo() -----------");
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
		
		System.out.println("-------- VideoApiController -> getVideosbyUser() -----------");
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
	
	@Override
	public ResponseEntity<Void> likeVideo(@RequestBody Map<String, Long> requestBody) {
		System.out.println("-------- VideoApiController -> likeVideo() -----------");
		
	    Long videoId = requestBody.get("videoId");
	    System.out.println("VideoMicroservice -> Recibiendo 'Me gusta' para el video con ID: " + videoId);

	    if (videoId == null || videoId <= 0) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 si el ID es inválido
	    }

	    VideoAccess cliente = new VideoAccess();

	    try {
		        // Conectar a la base de datos
		        if (!cliente.dbConectar()) {
		            System.out.println("Conexión fallida");
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }
		        
                boolean exito = cliente.dbAddLike(videoId);
                
                if (!cliente.dbDesconectar()) {
                    System.out.println("Desconexión fallida");
                }
                
                if (exito) {
                    return ResponseEntity.ok().build();
                } else {
                    System.out.println("Fallo al registrar la vista en la base de datos");
                    return ResponseEntity.status(400).build();
                }
	    } catch (Exception e) {
	        System.err.println("VideoMicroservice -> Error al incrementar 'Me gusta': " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
	@Override
	public ResponseEntity<Void> unlikeVideo(@RequestBody Map<String, Long> requestBody) {
		System.out.println("-------- VideoApiController -> unlikeVideo() -----------");
	    Long videoId = requestBody.get("videoId");
	    System.out.println("VideoMicroservice -> Recibiendo 'Ya no me gusta' para el video con ID: " + videoId);

	    if (videoId == null || videoId <= 0) {
	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // 400 si el ID es inválido
	    }

	    VideoAccess cliente = new VideoAccess();

	    try {
		        // Conectar a la base de datos
		        if (!cliente.dbConectar()) {
		            System.out.println("Conexión fallida");
		            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		        }
		        
                boolean exito = cliente.dbAddUnlike(videoId);
                
                if (!cliente.dbDesconectar()) {
                    System.out.println("Desconexión fallida");
                }
                
                if (exito) {
                    return ResponseEntity.ok().build();
                } else {
                    System.out.println("Fallo al registrar la vista en la base de datos");
                    return ResponseEntity.status(400).build();
                }
	    } catch (Exception e) {
	        System.err.println("VideoMicroservice -> Error al incrementar 'Me gusta': " + e.getMessage());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
	    }
	}
	
    @Override
    public ResponseEntity<Void> deleteVideo(@PathVariable("id") Long id) {
		System.out.println("-------- VideoApiController -> deleteVideo() -----------");
        System.out.println("Video a borrar: " + id);

        if (id != null && id >= 0) {
            VideoAccess cliente = new VideoAccess();
            CommentAccess cliente2 = new CommentAccess();
            
            // Conectar a la base de datos
            if (!cliente.dbConectar() & !cliente2.dbConectar()) {
                System.out.println("Conexión fallida");
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Consultar y borrar el video
            System.out.println("Service: Servicio a usuario para borrar el historial");
            userservice.deleteHistory(id);
            
            cliente2.dbDeleteComments(id);
            
            boolean exito = cliente.dbBorrarVideoById(id);

            if (!cliente.dbDesconectar() & !cliente2.dbDesconectar()) {
                System.out.println("Desconexión fallida");
            }

            return exito ? new ResponseEntity<>(HttpStatus.OK) 
                         : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.badRequest().build();
    }
	

    /* ------------------------------ NUEVOS METODOS PARA RECOMENDACIONES ------------------------------------*/
    
    @Override
	public ResponseEntity<List<Video>> getVideosByGenre(
	        @Parameter(name = "genre", description = "Género de los videos", required = true) 
	        @RequestParam String genre
	) {
		System.out.println("-------- VideoApiController -> getVideosByGenre() -----------");
	    List<Video> videos;

	    try {
	        // Validar que el género no sea nulo o vacío
	        if (genre == null || genre.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el género es inválido
	        }

	        // Instancia para acceder a la base de datos
	        VideoAccess cliente = new VideoAccess();

	        // Conectar a la base de datos
	        if (!cliente.dbConectar()) {
	            System.out.println("Conexión fallida");
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
	        }

	        // Consultar la lista de videos por género
	        videos = cliente.dbConsultarVideosByGenre(genre);
	        if (videos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentran videos
	        }

	        // Desconectar la base de datos
	        if (!cliente.dbDesconectar()) {
	            System.out.println("Desconexión fallida");
	        }

	        System.out.println("Devolviendo lista de videos");
	        // Devolver la lista de videos con status 200
	        return new ResponseEntity<>(videos, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
	    }
	}
	
	@Override
	public ResponseEntity<List<Video>> getVideosByTitle(String query) {
		System.out.println("-------- VideoApiController -> getVideosByTitle() -----------");
	    List<Video> videos;

	    try {
	        // Validar que el título no sea nulo o vacío
	        if (query == null || query.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el título es inválido
	        }

	        // Instancia para acceder a la base de datos
	        VideoAccess cliente = new VideoAccess();

	        // Conectar a la base de datos
	        if (!cliente.dbConectar()) {
	            System.out.println("Conexión fallida");
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
	        }

	        // Consultar la lista de videos por título parcial
	        videos = cliente.dbConsultarVideosPorTitulo(query);
	        if (videos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentran videos
	        }

	        // Desconectar la base de datos
	        if (!cliente.dbDesconectar()) {
	            System.out.println("Desconexión fallida");
	        }

	        System.out.println("Devolviendo lista de videos");
	        // Devolver la lista de videos con status 200
	        return new ResponseEntity<>(videos, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
	    }
	}
	
	@Override
	public ResponseEntity<Integer> countLikesByVideoId(
	    @Parameter(name = "id", description = "ID del video", required = true)
	    @PathVariable Long id
	) {
		System.out.println("-------- VideoApiController -> countLikesByVideoId() -----------");
	    int likes;

	    try {
	        // Validar que el ID no sea nulo o menor a 1
	        if (id == null || id <= 0) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
	        }

	        // Instancia para acceder a la base de datos
	        VideoAccess cliente = new VideoAccess();

	        // Conectar a la base de datos
	        if (!cliente.dbConectar()) {
	            System.out.println("Conexión fallida");
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
	        }

	        // Consultar el número de likes del video por su ID
	        likes = cliente.dbCountLikesByVideoId(id);
	        if (likes == 0) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentra el video
	        }

	        // Desconectar la base de datos
	        if (!cliente.dbDesconectar()) {
	            System.out.println("Desconexión fallida");
	        }

	        System.out.println("Devolviendo número de likes");
	        // Devolver el número de likes con status 200
	        return new ResponseEntity<>(likes, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
	    }
	}
	
	@Override
	public ResponseEntity<Long> countViewsByVideoId(
	    @Parameter(name = "id", description = "ID del video", required = true)
	    @PathVariable Long id
	) {
		System.out.println("-------- VideoApiController -> countViewsByVideoId() -----------");
	    Long views;

	    try {
	        // Validar que el ID no sea nulo o menor a 1
	        if (id == null || id <= 0) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
	        }

	        // Instancia para acceder a la base de datos
	        VideoAccess cliente = new VideoAccess();

	        // Conectar a la base de datos
	        if (!cliente.dbConectar()) {
	            System.out.println("Conexión fallida");
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
	        }

	        // Consultar el número de vistas del video por su ID
	        views = cliente.dbCountViewsByVideoId(id);
	        if (views == 0) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no se encuentra el video o no tiene vistas
	        }

	        // Desconectar la base de datos
	        if (!cliente.dbDesconectar()) {
	            System.out.println("Desconexión fallida");
	        }

	        System.out.println("Devolviendo número de vistas");
	        // Devolver el número de vistas con status 200
	        return new ResponseEntity<>(views, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
	    }
	}
	
	@Override
	public ResponseEntity<List<Video>> getTopVideos() {
		System.out.println("-------- VideoApiController -> getTopVideos() -----------");

	    try {
	        // Instancia para acceder a la base de datos
	        VideoAccess cliente = new VideoAccess();

	        // Conectar a la base de datos
	        if (!cliente.dbConectar()) {
	            System.out.println("Conexión fallida");
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
	        }

	        // Consultar el top de videos ordenados por vistas
	        List<Video> videos = cliente.dbGetVideosOrderedByViews();

	        // Desconectar la base de datos
	        if (!cliente.dbDesconectar()) {
	            System.out.println("Desconexión fallida");
	        }

	        // Verificar si la lista está vacía (no hay videos en la base de datos)
	        if (videos.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no hay videos
	        }

	        System.out.println("Devolviendo el top de videos");
	        // Devolver la lista de videos con status 200
	        return new ResponseEntity<>(videos, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
	    }
	}
	
	@Override
	public ResponseEntity<List<Video>> getRandomVideosByUserId(@PathVariable Long id) {
		System.out.println("-------- VideoApiController -> getRandomVideosByUserId() -----------");

	    try {
	        // Validar que el ID del usuario no sea nulo o negativo
	        if (id == null || id <= 0) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 si el ID es inválido
	        }

	        // Instancia para acceder a la base de datos
	        VideoAccess cliente = new VideoAccess();

	        // Conectar a la base de datos
	        if (!cliente.dbConectar()) {
	            System.out.println("Conexión fallida");
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 si no se pudo conectar
	        }

	        // Consultar videos aleatorios del usuario
	        List<Video> videos = cliente.dbConsultarRandomVideosByUserId(id.intValue());

	        // Desconectar la base de datos
	        if (!cliente.dbDesconectar()) {
	            System.out.println("Desconexión fallida");
	        }

	        // Verificar si la lista está vacía (no hay videos para el usuario)
	        if (videos.isEmpty()) {
	            System.out.println("No se encontraron videos para el usuario con ID: " + id);
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 si no hay videos
	        }

	        System.out.println("Devolviendo videos aleatorios para el usuario con ID: " + id);
	        // Devolver la lista de videos con status 200
	        return new ResponseEntity<>(videos, HttpStatus.OK);

	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 en caso de error del servidor
	    }
	}
	
    @Override
    public ResponseEntity<Void> addView(@RequestBody Map<String, Long> payload) {
		System.out.println("-------- VideoApiController -> addView() -----------");

        Long videoId = payload.get("videoId");
        VideoAccess cliente = new VideoAccess();
        
        System.out.println("Registrando vista para el video con ID: " + videoId);
        if(!cliente.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    
    	
    	boolean exito = cliente.dbAddView(videoId);
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
        
        if (exito) {
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Fallo al registrar la vista en la base de datos");
            return ResponseEntity.status(400).build();
        }
    }

    @Override
    public ResponseEntity<Void> deleteVideosByUserId(@RequestBody Long userId) {
		System.out.println("-------- VideoApiController -> deleteVideosByUserId() -----------");

    	VideoAccess cliente = new VideoAccess();
    	CommentAccess cliente2 = new CommentAccess();
    	
        System.out.println("Borrando videos del usuario : " + userId);
        if(!cliente.dbConectar() & !cliente2.dbConectar()) {
            System.out.println("Conexion fallida");
        }
    
        List<Video> videosABorrar = cliente.dbConsultarVideosByUser(userId.intValue());
        
        for(Video v: videosABorrar) {
        	System.out.println("Borrando los comentarios del video : " + v.getId());
        	cliente2.dbDeleteComments(v.getId());
        	
        }
    	
    	boolean exito = cliente.dbDeleteVideos(userId);
   	 

        if(!cliente.dbDesconectar() & !cliente2.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
        
        if (exito) {
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Fallo al borrar los videos del usuario" + userId);
            return ResponseEntity.status(400).build();
        }
    }
    
    @Override
    public ResponseEntity<Void> deleteCommentsByUserId(@RequestBody Long userId) {
		System.out.println("-------- VideoApiController -> deleteCommentsByUserId() -----------");

    	CommentAccess cliente = new CommentAccess();
    	
        System.out.println("Borrando comentarios del usuario : " + userId);
        if(!cliente.dbConectar() ) {
            System.out.println("Conexion fallida");
        }
   	
    	boolean exito = cliente.dbDeleteCommentsByUser(userId);
   	 

        if(!cliente.dbDesconectar()) {
            System.out.println("Desconexión fallida");
           
        }
        
        if (exito) {
            return ResponseEntity.ok().build();
        } else {
            System.out.println("Fallo al borrar los videos del usuario" + userId);
            return ResponseEntity.status(400).build();
        }
    }
}
