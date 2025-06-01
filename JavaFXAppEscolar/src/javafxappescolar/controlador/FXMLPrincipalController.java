/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.POJO.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author abelh
 */
public class FXMLPrincipalController implements Initializable {
    private Usuario usuarioSesion;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUsuario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    public void cargarInformacionUsuario(){
        if(usuarioSesion != null){
            lbNombre.setText(usuarioSesion.toString());
            lbUsuario.setText(usuarioSesion.getUsername());
        }
    }


    @FXML
    private void clicBtnAdminAlumnos(ActionEvent event) {
        try {
            Stage escenarioAdmin = new Stage();
            Parent vista = FXMLLoader.load(
                    JavaFXAppEscolar.class.getResource("vista/FXMLAdminAlumno.fxml"));
            Scene escena = new Scene(vista);
            escenarioAdmin.setScene(escena);
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.setTitle("Administración de Alumnos");
            escenarioAdmin.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void clicBtnCerrarSesion(ActionEvent event) {
        try {
            Stage escenarioLogin = (Stage) Utilidad.getScenarioPorComponente(lbNombre);
            FXMLLoader cargador =
                    new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLInicioSesion.fxml"));
            Parent vista = cargador.load();
            Scene escenaPrincipal = new Scene(vista);
            escenarioLogin.setScene(escenaPrincipal);
            escenarioLogin.setTitle("Inicio");
            escenarioLogin.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
