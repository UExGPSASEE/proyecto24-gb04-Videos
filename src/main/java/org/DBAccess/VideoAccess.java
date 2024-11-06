package org.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
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
        String database = "ASEE";
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
	
    public void dbConsultarVideo() {
        Statement st;
        
        System.out.println("---dbConsultarVideo---"); 
        
        try {
            st = conexion.createStatement();
            // Obtener todos los videos
            String result = "SELECT * FROM Video";
            
            ResultSet rset = st.executeQuery(result);
            
            System.out.println(result);
            System.out.println(" ");
            
            while (rset.next()) {
                 System.out.println("ID: "+rset.getInt(1));
                 System.out.println("Duracion: "+rset.getString(2));
                 System.out.println("Titulo: "+rset.getString(3));
                 System.out.println("uploadDate: "+rset.getTimestamp(4));
                 System.out.println("Descripcion: "+rset.getString(5));
                 System.out.println("Genero: "+rset.getString(6));
                 System.out.println("Likes: "+rset.getInt(7));
                 System.out.println("RestriccionEdad: "+rset.getBoolean(8));
                 System.out.println("RestricciónPaises: "+rset.getArray(9));
                 
                 System.out.println("---------------------------------------");
            }
            rset.close();
        }catch (SQLException e) {
            e.printStackTrace();
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
    
}
