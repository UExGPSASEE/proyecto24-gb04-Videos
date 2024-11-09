package org.DBAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import org.openapitools.model.Comments;

public class CommentAccess {
	
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
    
    public void dbAddComment(Comments comment) {
        PreparedStatement ps;
        
        System.out.println("---dbAddComment---");
        
        try {
            String insertQuery = "INSERT INTO Comments (uploadDate, comment_user, video, text) VALUES (?,?,?,?)";
            ps = conexion.prepareStatement(insertQuery);
            
            // Asumiendo que el objeto Comment tiene métodos getter para obtener sus atributos
            ps.setTimestamp(1, Timestamp.from(comment.getUploadDate().toInstant())); // Convertir OffsetDateTime a Timestamp
            ps.setInt(2, comment.getUser()); // ID del usuario que escribe el comentario
            ps.setInt(3, comment.getVideo()); // ID del video al que pertenece el comentario
            ps.setString(4, comment.getText()); // El texto del comentario
            
            int rowsInserted = ps.executeUpdate();
            
            if (rowsInserted > 0) {
                System.out.println("¡El comentario fue insertado exitosamente!");
            } else {
                System.out.println("ERROR: No se ha insertado el comentario");
            }
            
            ps.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Comments> dbGetAllCommentsFromVideo(int id) {
        PreparedStatement ps;
        List<Comments> commentsList = new ArrayList<>();
        
        System.out.println("---dbGetAllComments---");
        
        try {
            String selectQuery = "SELECT * FROM Comments WHERE video = ?";
            ps = conexion.prepareStatement(selectQuery);
            ps.clearParameters();
            
            ps.setInt(1, id);
            
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                Comments comment = new Comments();
                comment.setId(rs.getLong("id"));
                comment.setUploadDate(rs.getObject("uploadDate", OffsetDateTime.class));
                comment.setUser(rs.getInt("comment_user"));
                comment.setVideo(rs.getInt("video"));
                comment.setText(rs.getString("text"));
                
                commentsList.add(comment);
            }
            
            rs.close();
            ps.close();
            
            System.out.println("Comentarios obtenidos exitosamente.");
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return commentsList;
    }

    

}
