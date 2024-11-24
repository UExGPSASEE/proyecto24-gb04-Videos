package org.DBAccess;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openapitools.model.Video;

public class VideoAccess {
	 //SE DECLARA LA CONEXIÓN
    private static Connection conexion = null ; 
    /* ------------------------------------------------------------------ */
    /* --------------METODO PARA REALIZAR LA CONEXION-------------------- */
    /* ------------------------------------------------------------------ */
    public boolean dbConectar() {
        
        System.out.println("---dbConectar---");
        // Crear la conexion a la base de datos 
        String driver = "org.postgresql.Driver";
        String numdep = "localhost"; // Direccion IP
        String puerto = "5432";
        String database = "ASEE_Videos";
        String url = "jdbc:postgresql://" + numdep + ":" + puerto + "/" + database;
        String usuario = "postgres";
        String contrasena = "12345";

        try { 
             System.out.println("---Conectando a PostgreSQL---");
                Class.forName (driver); // Cargar el driver JDBC para PostgreSQL
             conexion = DriverManager.getConnection (url, usuario, contrasena); 
             System.out.println ("Conexion realizada a la base de datos " + conexion); 
             return true; 
         } catch (ClassNotFoundException e) { 
             // Error. No se ha encontrado el driver de la base de datos 
             e.printStackTrace(); 
             return false; 
         } catch (SQLException e) { 
             // Error. No se ha podido conectar a la BD 
             e.printStackTrace(); 
             return false; 
         } 
    }
    
    public boolean dbDesconectar() {
        System.out.println("---dbDesconectar---");

        try {
            //conexion.commit();// conexion.setAutoCommit(false); // en dbConectar()
            conexion.close();
            System.out.println("Desconexión realizada correctamente");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
	
    public List<Video> dbConsultarVideos() {
        List<Video> videos = new ArrayList<>();
        Statement st;
        
        System.out.println("---dbConsultarVideos---"); 
        
        try {
            st = conexion.createStatement();
            String query = "SELECT * FROM Video";
            ResultSet rset = st.executeQuery(query);
            
            System.out.println(query);
            System.out.println(" ");
            
            while (rset.next()) {
                Video video = new Video();
                video.setId(rset.getLong(1));
                video.setDuration(rset.getString(2));
                video.setTitle(rset.getString(3));
                video.setUploadDate(rset.getTimestamp(4));
                video.setDescription(rset.getString(5));
                video.setGenre(rset.getString(6));
                video.setLikes(rset.getInt(7));
                video.setAgeRestricted(rset.getBoolean(8));
                
                // Convertir array de SQL a ArrayList<String> para countryRestricted
                Array sqlArray = rset.getArray(9);
                if (sqlArray != null) {
                    String[] countries = (String[]) sqlArray.getArray();
                    video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                }
                
                videos.add(video);
                System.out.println("---------------------------------------");
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return videos;
    }

    
    public Video dbConsultarVideoPorId(int videoId) {
        PreparedStatement pst = null;
        Video video = null;
        
        System.out.println("---dbConsultarVideoPorId---"); 
        
        try {
            String query = "SELECT * FROM Video WHERE id = ?";
            pst = conexion.prepareStatement(query);
            pst.setInt(1, videoId);
            
            ResultSet rset = pst.executeQuery();
            
            System.out.println(query);
            System.out.println(" ");
            
            if (rset.next()) {
                video = new Video();
                video.setId(rset.getLong(1));
                video.setDuration(rset.getString(2));
                video.setTitle(rset.getString(3));
                video.setUploadDate(rset.getTimestamp(4));
                video.setDescription(rset.getString(5));
                video.setGenre(rset.getString(6));
                video.setLikes(rset.getInt(7));
                video.setAgeRestricted(rset.getBoolean(8));
                video.setViews(rset.getLong("views"));
                video.setUser(rset.getLong("video_user"));
                
                // Convertir array de SQL a ArrayList<String> para countryRestricted
                Array sqlArray = rset.getArray(9);
                if (sqlArray != null) {
                    String[] countries = (String[]) sqlArray.getArray();
                    video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                }
                
                System.out.println("---------------------------------------");
            } else {
                System.out.println("No se encontró un video con el ID: " + videoId);
            }
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return video;
    }
    
    public boolean dbActualizarVideo(Video video) {
        PreparedStatement pst = null;
        
        System.out.println("---dbActualizarVideo---");
        
        try {
            String query = "UPDATE Video SET duration = ?, title = ?, uploadDate = ?, description = ?, genre = ?, likes = ?, ageRestricted = ?, countryrestricted = ? WHERE id = ?";
            pst = conexion.prepareStatement(query);
            
            // Establecer los valores en la consulta
            pst.setString(1, video.getDuration());
            pst.setString(2, video.getTitle());
            pst.setTimestamp(3, video.getUploadDate());
            pst.setString(4, video.getDescription());
            pst.setString(5, video.getGenre());
            pst.setInt(6, video.getLikes());
            pst.setBoolean(7, video.getAgeRestricted());
            
         // Convertir ArrayList<String> a Array para PostgreSQL
            Array countryArray = conexion.createArrayOf("TEXT", video.getCountryRestricted().toArray());
            pst.setArray(8, countryArray);
            
            pst.setLong(9, video.getId()); // ID del video a actualizar

            // Ejecutar la consulta de actualización
            int rowsUpdated = pst.executeUpdate();
            
            System.out.println("Video actualizado con ID: " + video.getId());
            return rowsUpdated > 0; // Devuelve true si se actualizó al menos una fila
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    
    public void fromRstoVideoObject(ResultSet rset, Video video) throws SQLException {
    	video.setId(rset.getLong(1));
    	video.setDuration(rset.getString(2));
    	video.setTitle(rset.getString(3));
    	video.setUploadDate(rset.getTimestamp(4));
    	video.setDescription(rset.getString(5));
    	video.setGenre(rset.getString(6));
    	video.setLikes(rset.getInt(7));
    	video.setAgeRestricted(rset.getBoolean(8));
    	Array pgArray = rset.getArray(9);
    	if(pgArray != null){
    		String[] countryArray = (String[]) pgArray.getArray();
    		ArrayList<String> countryList = new ArrayList<>(Arrays.asList(countryArray));
    		video.setCountryRestricted(countryList);
    	}
    }
    
    public ArrayList<Video> dbGetAllVideos() {
        Statement st;
        
        System.out.println("---dbGetAllVideos---"); 
        ArrayList<Video> videos = new ArrayList<Video>();
        
        try {
            st = conexion.createStatement();
            // Obtener todos los videos
            String result = "SELECT * FROM Video";
            
            ResultSet rset = st.executeQuery(result);
            
            System.out.println(result);
            System.out.println(" ");
            
            while (rset.next()) {
            	Video video = new Video();
            	fromRstoVideoObject(rset, video);
            	videos.add(video);
                 /*System.out.println("ID: "+rset.getInt(1));
                 System.out.println("Duracion: "+rset.getString(2));
                 System.out.println("Titulo: "+rset.getString(3));
                 System.out.println("uploadDate: "+rset.getTimestamp(4));
                 System.out.println("Descripcion: "+rset.getString(5));
                 System.out.println("Genero: "+rset.getString(6));
                 System.out.println("Likes: "+rset.getInt(7));
                 System.out.println("RestriccionEdad: "+rset.getBoolean(8));
                 System.out.println("RestricciónPaises: "+rset.getArray(9));
                 
                 System.out.println("---------------------------------------");
                 */
            }
            rset.close();
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return videos;
    }
    
    public void dbAddVideo(Video video) {
        PreparedStatement ps;
        
        System.out.println("---dbAddVideo---");
        
        try {
            String insertQuery = "INSERT INTO Video (duration, title, uploadDate, description, genre, likes, ageRestricted, countryRestricted, video_user) VALUES (?,?,?,?,?,?,?,?,?)";
            ps = conexion.prepareStatement(insertQuery);
            
            // Asumiendo que el objeto Video tiene métodos getter para obtener sus atributos
            ps.setString(1, video.getDuration());
            ps.setString(2, video.getTitle());
            // Convertir `Date` a `Timestamp` para `uploadDate`
            ps.setTimestamp(3, video.getUploadDate());
            ps.setString(4, video.getDescription());
            ps.setString(5, video.getGenre());
            ps.setInt(6, video.getLikes());
            ps.setBoolean(7, video.getAgeRestricted());
            
            // Para countryRestricted, creamos un array en PostgreSQL con los valores de países
            Array countryArray = conexion.createArrayOf("text", video.getCountryRestricted().toArray(new String[0]));
            ps.setArray(8, countryArray);
            
            ps.setLong(9, video.getUser()); // ID del usuario que sube el video
            
            int rowsInserted = ps.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("¡El Video fue insertado exitosamente!");
            } else {
                System.out.println("ERROR: No se ha insertado el video");
            }
            
            ps.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Video> dbConsultarVideosByUser(int userId) {
        PreparedStatement pst = null;
        List<Video> videos = new ArrayList<>();
        
        System.out.println("---dbConsultarVideosPorUsuario---"); 
        
        try {
            String query = "SELECT * FROM Video WHERE video_user = ?";
            pst = conexion.prepareStatement(query);
            pst.setInt(1, userId);
            
            ResultSet rset = pst.executeQuery();
            
            System.out.println(query);
            System.out.println(" ");
            
            while (rset.next()) {
                Video video = new Video();
                video.setId(rset.getLong(1));
                video.setDuration(rset.getString(2));
                video.setTitle(rset.getString(3));
                video.setUploadDate(rset.getTimestamp(4));
                video.setDescription(rset.getString(5));
                video.setGenre(rset.getString(6));
                video.setLikes(rset.getInt(7));
                video.setAgeRestricted(rset.getBoolean(8));
                
                // Convertir array de SQL a ArrayList<String> para countryRestricted
                Array sqlArray = rset.getArray(9);
                if (sqlArray != null) {
                    String[] countries = (String[]) sqlArray.getArray();
                    video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                }
                
                videos.add(video);
                System.out.println("---------------------------------------");
            }
            
            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        
        return videos;
    }
    
    public Video dbConsultarVideoByUserAndTitle(Long userId, String title) {
        Video video = null;
        System.out.println("---dbConsultarVideoByUserAndTitle---");

        String query = "SELECT * FROM Video WHERE video_user = ? AND title = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setLong(1, userId);
            pst.setString(2, title);

            try (ResultSet rset = pst.executeQuery()) {
                if (rset.next()) {  // Si hay un resultado, inicializa el objeto Video
                    video = new Video();
                    video.setId(rset.getLong(1));
                    video.setDuration(rset.getString(2));
                    video.setTitle(rset.getString(3));
                    video.setUploadDate(rset.getTimestamp(4));
                    video.setDescription(rset.getString(5));
                    video.setGenre(rset.getString(6));
                    video.setLikes(rset.getInt(7));
                    video.setAgeRestricted(rset.getBoolean(8));
                    
                    // Convertir array de SQL a ArrayList<String> para countryRestricted
                    Array sqlArray = rset.getArray(9);
                    if (sqlArray != null) {
                        String[] countries = (String[]) sqlArray.getArray();
                        video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                    }
                    System.out.println("Video encontrado: " + video);
                } else {
                    System.out.println("No se encontró un video con los criterios especificados.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return video;
    }
    
    public boolean dbBorrarVideoById(Long videoId) {
        System.out.println("---dbBorrarVideoById---");

        String query = "DELETE FROM Video WHERE id = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            // Establecer el parámetro de la consulta
            pst.setLong(1, videoId);

            // Ejecutar la consulta
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Video con id " + videoId + " eliminado correctamente.");
                return true; // Indica que la operación fue exitosa
            } else {
                System.out.println("No se encontró un video con id " + videoId + " para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar eliminar el video con id " + videoId);
            e.printStackTrace();
        }

        return false; // Indica que no se pudo realizar la operación
    }


    public Video dbConsultarVideoByDateAndTitle(Timestamp uploadDate, String title) {
        Video video = null;

        String query = "SELECT id, duration, title, uploaddate, description, genre, likes, agerestricted, video_user, views FROM Video WHERE title = ? AND uploaddate = ?";
        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setString(1, title);
            pst.setTimestamp(2, uploadDate);

            try (ResultSet rset = pst.executeQuery()) {
                if (rset.next()) {
                    video = new Video();
                    video.setId(rset.getLong(1));
                    video.setDuration(rset.getString(2));
                    video.setTitle(rset.getString(3));
                    video.setUploadDate(rset.getTimestamp(4));
                    video.setDescription(rset.getString(5));
                    video.setGenre(rset.getString(6));
                    video.setLikes(rset.getInt(7));
                    video.setAgeRestricted(rset.getBoolean(8));
                    video.setUser(rset.getLong(9));
                    video.setViews(rset.getLong(10));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar el video: " + e.getMessage());
        }

        return video;
    }
    
    
    public boolean dbAddLike(Long videoId) {
        System.out.println("---dbAddLike---");

        String query = "UPDATE Video SET likes = likes + 1 WHERE id = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            // Establecer el parámetro de la consulta
            pst.setLong(1, videoId);

            // Ejecutar la consulta
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Video con id " + videoId + " eliminado correctamente.");
                return true; // Indica que la operación fue exitosa
            } else {
                System.out.println("No se encontró un video con id " + videoId + " para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar eliminar el video con id " + videoId);
            e.printStackTrace();
        }

        return false; // Indica que no se pudo realizar la operación
    }
    
    public boolean dbAddUnlike(Long videoId) {
        System.out.println("---dbAddLike---");

        String query = "UPDATE Video SET likes = likes - 1 WHERE id = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            // Establecer el parámetro de la consulta
            pst.setLong(1, videoId);

            // Ejecutar la consulta
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Video con id " + videoId + " eliminado correctamente.");
                return true; // Indica que la operación fue exitosa
            } else {
                System.out.println("No se encontró un video con id " + videoId + " para eliminar.");
            }
        } catch (SQLException e) {
            System.err.println("Error al intentar eliminar el video con id " + videoId);
            e.printStackTrace();
        }

        return false; // Indica que no se pudo realizar la operación
    }

    
    /* ------------------------------ NUEVOS METODOS PARA RECOMENDACIONES ------------------------------------*/
    
    public List<Video> dbConsultarVideosByGenre(String genre) {
        List<Video> videos = new ArrayList<>();
        System.out.println("---dbConsultarVideosByGenre---");

        String query = "SELECT * FROM Video WHERE genre = ? LIMIT 5";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setString(1, genre);

            try (ResultSet rset = pst.executeQuery()) {
                while (rset.next()) { // Itera por todos los resultados
                    Video video = new Video();
                    video.setId(rset.getLong(1));
                    video.setDuration(rset.getString(2));
                    video.setTitle(rset.getString(3));
                    video.setUploadDate(rset.getTimestamp(4));
                    video.setDescription(rset.getString(5));
                    video.setGenre(rset.getString(6));
                    video.setLikes(rset.getInt(7));
                    video.setAgeRestricted(rset.getBoolean(8));

                    // Convertir array de SQL a ArrayList<String> para countryRestricted
                    Array sqlArray = rset.getArray(9);
                    if (sqlArray != null) {
                        String[] countries = (String[]) sqlArray.getArray();
                        video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                    }
                    videos.add(video); // Agrega el video a la lista
                }
                if (videos.isEmpty()) {
                    System.out.println("No se encontraron videos para el género especificado.");
                } else {
                    System.out.println(videos.size() + " videos encontrados para el género: " + genre);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return videos;
    }
    
    public int dbCountLikesByVideoId(Long videoId) {
        int likes = 0;
        System.out.println("---dbCountLikesByVideoId---");

        String query = "SELECT likes FROM Video WHERE id = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setLong(1, videoId);

            try (ResultSet rset = pst.executeQuery()) {
                if (rset.next()) { // Si se encuentra el video
                    likes = rset.getInt("likes");
                    System.out.println("Likes encontrados para el video con ID " + videoId + ": " + likes);
                } else {
                    System.out.println("No se encontró un video con el ID especificado: " + videoId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return likes;
    }
    
    public Long dbCountViewsByVideoId(Long videoId) {
        Long views = 0L;
        System.out.println("---dbCountLikesByVideoId---");

        String query = "SELECT views FROM Video WHERE id = ?";

        try (PreparedStatement pst = conexion.prepareStatement(query)) {
            pst.setLong(1, videoId);

            try (ResultSet rset = pst.executeQuery()) {
                if (rset.next()) { // Si se encuentra el video
                    views = rset.getLong("views");
                    System.out.println("Views encontrados para el video con ID " + videoId + ": " + views);
                } else {
                    System.out.println("No se encontró un video con el ID especificado: " + videoId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return views;
    }
    
    public List<Video> dbGetVideosOrderedByViews() {
        List<Video> videos = new ArrayList<>();
        System.out.println("---dbGetVideosOrderedByViews---");

        String query = "SELECT * FROM Video ORDER BY views DESC LIMIT 5";

        try (PreparedStatement pst = conexion.prepareStatement(query);
             ResultSet rset = pst.executeQuery()) {

            while (rset.next()) {
                // Crear un objeto Video y asignar los valores desde la base de datos
                Video video = new Video();
                video.setId(rset.getLong("id"));
                video.setDuration(rset.getString("duration"));
                video.setTitle(rset.getString("title"));
                video.setUploadDate(rset.getTimestamp("uploaddate"));
                video.setDescription(rset.getString("description"));
                video.setGenre(rset.getString("genre"));
                video.setLikes(rset.getInt("likes"));
                video.setAgeRestricted(rset.getBoolean("agerestricted"));
                Array sqlArray = rset.getArray("countryrestricted");
                if (sqlArray != null) {
                    String[] countries = (String[]) sqlArray.getArray();
                    video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                }
                video.setUser(rset.getLong("video_user"));
                video.setViews(rset.getLong("views"));

                videos.add(video); // Añadir el video a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return videos;
    }
    
    public List<Video> dbConsultarRandomVideosByUserId(int userId) {
        PreparedStatement pst = null;
        List<Video> videos = new ArrayList<>();

        System.out.println("---dbConsultarVideosPorUsuario---");

        try {
            String query = "SELECT * FROM Video WHERE video_user = ? ORDER BY RANDOM() LIMIT 3";
            pst = conexion.prepareStatement(query);
            pst.setInt(1, userId);

            ResultSet rset = pst.executeQuery();

            System.out.println(query);
            System.out.println(" ");

            while (rset.next()) {
                Video video = new Video();
                video.setId(rset.getLong(1));
                video.setDuration(rset.getString(2));
                video.setTitle(rset.getString(3));
                video.setUploadDate(rset.getTimestamp(4));
                video.setDescription(rset.getString(5));
                video.setGenre(rset.getString(6));
                video.setLikes(rset.getInt(7));
                video.setAgeRestricted(rset.getBoolean(8));

                // Convertir array de SQL a ArrayList<String> para countryRestricted
                Array sqlArray = rset.getArray(9);
                if (sqlArray != null) {
                    String[] countries = (String[]) sqlArray.getArray();
                    video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                }

                video.setUser(rset.getLong(10));
                video.setViews(rset.getLong(11));

                videos.add(video);
                System.out.println("---------------------------------------");
            }

            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return videos;
    }
    
    public List<Video> dbConsultarVideosPorTitulo(String tituloParcial) {
        PreparedStatement pst = null;
        List<Video> videos = new ArrayList<>();

        System.out.println("---dbConsultarVideosPorTitulo---");

        try {
            String query = "SELECT * FROM Video WHERE title ILIKE ?";
            pst = conexion.prepareStatement(query);
            pst.setString(1, "%" + tituloParcial + "%");

            ResultSet rset = pst.executeQuery();

            System.out.println(query);
            System.out.println(" ");

            while (rset.next()) {
                Video video = new Video();
                video.setId(rset.getLong(1));
                video.setDuration(rset.getString(2));
                video.setTitle(rset.getString(3));
                video.setUploadDate(rset.getTimestamp(4));
                video.setDescription(rset.getString(5));
                video.setGenre(rset.getString(6));
                video.setLikes(rset.getInt(7));
                video.setAgeRestricted(rset.getBoolean(8));

                // Convertir array de SQL a ArrayList<String> para countryRestricted
                Array sqlArray = rset.getArray(9);
                if (sqlArray != null) {
                    String[] countries = (String[]) sqlArray.getArray();
                    video.setCountryRestricted(new ArrayList<>(Arrays.asList(countries)));
                }

                videos.add(video);
            }

            if (videos.isEmpty()) {
                System.out.println("No se encontraron videos con el título que contiene: " + tituloParcial);
            }

            rset.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return videos;
    }
    
    public boolean dbAddView(Long videoId) {
    	PreparedStatement ps;
        boolean isUpdated = false;

        System.out.println("---bdAddView---");

        try {

            String updateQuery = "UPDATE Video SET views = views + 1 WHERE id = ?";
            ps = conexion.prepareStatement(updateQuery);
            ps.setLong(1, videoId);

            // Ejecuta la actualización
            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    }
    
    public boolean dbDeleteVideos(Long userId) {
    	PreparedStatement ps;
        boolean isUpdated = false;

        System.out.println("---bdDeleteVideos---");

        try {

            String updateQuery = "DELETE FROM Video WHERE video_user = ?";
            ps = conexion.prepareStatement(updateQuery);
            ps.setLong(1, userId);

            // Ejecuta la actualización
            int rowsAffected = ps.executeUpdate();
            isUpdated = rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUpdated;
    	
    	
    }
}
