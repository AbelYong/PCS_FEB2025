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
import javafxappescolar.modelo.POJO.Carrera;
import javafxappescolar.modelo.POJO.Facultad;

/**
 *
 * @author abelh
 */
public class CatalogoDAO {
    
    public static ArrayList<Facultad> obtenerFacultades() throws SQLException {
        ArrayList<Facultad> facultades = new ArrayList<Facultad>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null) {
            String consulta = "SELECT idFacultad, nombre FROM facultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()) {
                facultades.add(convertirRegistroFacultad(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión a la BD");
        }
        return facultades;
    }
    
    public static Facultad convertirRegistroFacultad(ResultSet resultado) throws SQLException {
        Facultad facultad = new Facultad();
        facultad.setIdFacultad(resultado.getInt("idFacultad"));
        facultad.setNombre(resultado.getString("nombre"));
        return facultad;
    }
    
    public static ArrayList<Carrera> obtenerCarrerasPorFacultad(int idFacultad) throws SQLException {
        ArrayList<Carrera> carreras = new ArrayList<Carrera>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null) {
            String consulta = "SELECT idCarrera, nombre, codigo, idFacultad FROM carrera WHERE idFacultad = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idFacultad);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                carreras.add(convertirRegistroCarrera(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion a la BD");
        }
        return carreras;
    }
    
    public static Carrera convertirRegistroCarrera(ResultSet resultado) throws SQLException {
        Carrera carrera = new Carrera();
        carrera.setIdCarrera(resultado.getInt("idCarrera"));
        carrera.setNombre(resultado.getString("nombre"));
        carrera.setCodigo(resultado.getString("codigo"));
        carrera.setIdFacultad(resultado.getInt("idFacultad"));
        return carrera;
    }
}
