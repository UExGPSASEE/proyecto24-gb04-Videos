package org.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.sql.*;

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
            String query = "UPDATE Video SET duration = ?, title = ?, uploadDate = ?, description = ?, genre = ?, likes = ?, ageRestricted = ?, countryRestricted = ? WHERE id = ?";
            pst = conexion.prepareStatement(query);
            
            // Establecer los valores en la consulta
            pst.setString(1, video.getDuration());
            pst.setString(2, video.getTitle());
            pst.setTimestamp(3, video.getUploadDate());
            pst.setString(4, video.getDescription());
            pst.setString(5, video.getGenre());
            pst.setInt(6, video.getLikes());
            pst.setBoolean(7, video.getAgeRestricted());
            
            // Convertir ArrayList<String> a Array para la base de datos
            Array countryArray = conexion.createArrayOf("VARCHAR", video.getCountryRestricted().toArray());
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
            
            ps.setInt(9, video.getUser()); // ID del usuario que sube el video
            
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

    
}
