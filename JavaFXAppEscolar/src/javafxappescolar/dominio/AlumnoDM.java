/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package javafxappescolar.dominio;

import java.sql.SQLException;
import javafxappescolar.modelo.POJO.ResultadoOperacion;
import javafxappescolar.modelo.dao.AlumnoDAO;

/**
 *
 * @author abelh
 */
public class AlumnoDM {
    public static ResultadoOperacion verficarEstadoMatricula(String matricula) {
        ResultadoOperacion resultado = new ResultadoOperacion();
        if(matricula.startsWith("s")) {
            try {
                boolean existe = AlumnoDAO.verificarMatriculaExiste(matricula);
                resultado.setError(existe);
                if (existe) {
                    resultado.setMensaje("La matricula ya existe");
                } 
            } catch (SQLException ex) {
                resultado.setError(true);
                resultado.setMensaje("Por el momento no se puede validar la matricula, por favor intentelo más tarde");
            }
        } else {
            resultado.setError(true);
            resultado.setMensaje("La matricula no tienen el formato correcto");
        }
        return resultado;
    }
}
