/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.POJO.Alumno;
import javafxappescolar.modelo.POJO.ResultadoOperacion;

/**
 *
 * @author abelh
 */
public class AlumnoDAO {
    public static ArrayList<Alumno> obtenerAlumnos() throws SQLException{
        ArrayList<Alumno> alumnos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT " +
                            "idAlumno, a.nombre, apellidoPaterno, apellidoMaterno, matricula, email, fechaNacimiento, " +
                            "a.idCarrera, c.nombre AS 'carrera', f.idFacultad, f.nombre AS 'facultad' " +
                            "FROM alumno a " +
                            "INNER JOIN carrera c ON c.idCarrera = a.idCarrera " +
                            "INNER JOIN facultad f ON f.idFacultad = c.idFacultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                alumnos.add(convertirRegistroAlumno(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion en la BD");
        }
        return alumnos;
    }
    
    private static Alumno convertirRegistroAlumno(ResultSet resultado) throws SQLException{
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(resultado.getInt("idAlumno"));
        alumno.setNombre((resultado.getString("nombre")));
        alumno.setApellidoPaterno(resultado.getString("apellidoPaterno"));
        alumno.setApellidoMaterno(
                (resultado.getString("apellidoMaterno")) != null ?
                        resultado.getString("apellidoMaterno") : "");
        alumno.setMatricula(resultado.getString("matricula"));
        alumno.setEmail(resultado.getString("email"));
        alumno.setFechaNacimiento(resultado.getString("fechaNacimiento"));
        alumno.setIdCarrera(resultado.getInt("idCarrera"));
        alumno.setCarrera(resultado.getString("carrera"));
        alumno.setIdFacultad(resultado.getInt("idFacultad"));
        alumno.setFacultad(resultado.getString("facultad"));
        return alumno;
    }
    
    public static ResultadoOperacion registrarAlumno(Alumno alumno) throws SQLException{
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null) {
            String consulta =
                    "INSERT INTO alumno(nombre, apellidoPaterno, apellidoMaterno, matricula, email, fechaNacimiento, idCarrera, foto)"
                    +" VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getMatricula());
            sentencia.setString(5, alumno.getEmail());
            sentencia.setString(6, alumno.getFechaNacimiento());
            sentencia.setInt(7, alumno.getIdCarrera());
            sentencia.setBytes(8, alumno.getFoto());
            int filasAfectadas = sentencia.executeUpdate();
            if(filasAfectadas == 1){
                resultado.setError(false);
                resultado.setMensaje("Alumno(a) registrado exitosamente.");
            }else{
                resultado.setError(true);
                resultado.setMensaje("Por el momento no se pudo registrar el alumno, por favor intentelo mas tarde");
            }
            sentencia.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion en la BD");
        }
        return resultado;
    }
    
    public static ResultadoOperacion actualizarAlumno(Alumno alumno) throws SQLException {
        //Restriccion: No se puede editar la matricula
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "UPDATE alumno "+
                    "SET nombre = ?, "+
                    "apellidoPaterno = ?, "+
                    "apellidoMaterno = ?, "+
                    "email = ?, "+
                    "idCarrera = ?, "+
                    "fechaNacimiento = ?, "+
                    "foto = ? "+
                    "WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getEmail());
            sentencia.setInt(5, alumno.getIdCarrera());
            sentencia.setString(6, alumno.getFechaNacimiento());
            sentencia.setBytes(7, alumno.getFoto());
            sentencia.setInt(8, alumno.getIdAlumno());
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("El Alumno(a) se actualizo exitosameente");
            } else {
                resultado.setError(true);
                resultado.setMensaje("Por el momento no se pudo actualizar el alumno, por favor intentelo más tarde");
            }
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion a la BD");
        }
        return resultado;
    }
    
    public static ResultadoOperacion eliminarAlumno(int idAlumno) throws SQLException {
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "DELETE FROM Alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas == 1) {
                resultado.setError(false);
                resultado.setMensaje("El Alumno(a) fue eliminado exitosameente");
            } else {
                resultado.setError(true);
                resultado.setMensaje("Por el momento no se pudo eliminar el alumno, por favor intentelo más tarde");
            }
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion a la BD");
        }
        return resultado;
    }
    
    public static byte[] obtenerFotoAlumno(int idAlumno) throws SQLException {
        byte[] foto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT foto FROM alumno WHERE idAlumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                foto = resultado.getBytes("foto");
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        }
        return foto;
    }
    
    //Si devuelve null, no hay alumno con esa matricula
    public static Alumno buscarAlumnoPorMatricula(String matricula) throws SQLException {
        Alumno alumno = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT idAlumno, nombre, apellidoPaterno, apellidoMaterno, matricula, email, fechaNacimiento, idCarrera "+
                    "FROM alumno "+
                    "WHERE matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            if (resultado.next()) {
                alumno = convertirRegistroAlumno(resultado);
            }
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion a la BD");
        }
        return alumno;
    }
    
    public static boolean verificarMatriculaExiste(String matricula) throws SQLException {
        boolean matriculaExiste = true;
        Connection conexionBD = ConexionBD.abrirConexion();
        if (conexionBD != null) {
            String consulta = "SELECT matricula FROM alumno WHERE matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            
            matriculaExiste = resultado.next(); //Si no hay registros, false, sino true
            
            resultado.close();
            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Sin conexion a la BD");
        }
        return matriculaExiste;
    }
}