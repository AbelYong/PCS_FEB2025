package javafxappescolar.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author abelh
 */
public class ConexionBD {
    private static final String IP = "127.0.0.1"; //"localhost" tambien funciaria
    private static final String PUERTO = "3306"; //¿Por que un String?, el formato de conexion pide un dato String
    private static final String NOMBRE_BD = "escolar";
    private static final String USUARIO = "escolarAdmin"; //No debe ser root
    private static final String PASSWORD = "(4dM1n)c4rD";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver"; //Es la direccion a un paquete, dependiendo la version podria cambiar
    
    public static Connection abrirConexion(){
        Connection conexionBD = null;
        String urlConexion =
                String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                        IP, PUERTO, NOMBRE_BD); 
        try{
            Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(urlConexion, USUARIO, PASSWORD);
        } catch (SQLException sqlEx){
            System.err.println("Error en la conexion: "+sqlEx.getMessage());
            sqlEx.printStackTrace();
        } catch (ClassNotFoundException cnfEx) {
            System.err.println("Error: Clase no encontrada: "+cnfEx.getMessage());
            cnfEx.printStackTrace();
        }
        return conexionBD;
    }
}