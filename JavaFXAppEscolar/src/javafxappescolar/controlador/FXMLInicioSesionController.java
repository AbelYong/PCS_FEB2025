package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.POJO.Usuario;
import javafxappescolar.modelo.dao.InicioSesionDAO;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author abelh
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Connection conexionBD = ConexionBD.abrirConexion();
    }    

    @FXML
    private void btnIniciarSesion(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfPassword.getText();
        if(validarCampos(username, password)){
            Usuario usuarioSesion = validarCredenciales(username, password);
            if(usuarioSesion != null){
                irPantallaPrincipal(usuarioSesion);
            }
        }
    }
    
    private boolean validarCampos(String username, String password){
        lbErrorPassword.setText("");
        lbErrorUsuario.setText("");
        
        boolean camposValidos = true;
        if(username.isEmpty()){
            lbErrorUsuario.setText("Usuario requerido");
            camposValidos = false;
        }
        if(password.isEmpty()){
            lbErrorPassword.setText("Contraseña requerida");
            camposValidos = false;
        }
        return camposValidos;
    }
    
    
    private Usuario validarCredenciales(String username, String password){
        try{
            Usuario usuarioSesion =
                    InicioSesionDAO.verificarCredenciales(username, password);
            if(usuarioSesion != null){
                //TODO Flujo Normal
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.INFORMATION, 
                        "Credenciales correctas", 
                        String.format("%s%S", "Bienvenido(a) ", usuarioSesion.toString()));
                return usuarioSesion;
            }else{
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.WARNING,
                        "Credenciales incorrectas",
                        "El usuario y/o contraseño no coinciden, por favor intentelo de nuevo");
                return null;
            }
        }catch(SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,
                    "Problema de conexión",
                    ex.getMessage()); //Esto se deja a fines de depuracion/aprendizaje, el usuario deberia ver un mensaje en lenguaje natural
            return null;
        }
    }
    
    private void irPantallaPrincipal(Usuario usuarioSesion){
        try {
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            FXMLLoader cargador =
                    new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalController controlador = cargador.getController();
            controlador.inicializarInformacion(usuarioSesion);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inicio");
            escenarioBase.show();
        } catch (IOException ex) {
            ex.printStackTrace(); //Depuracion
        }
    }   
    
    
}
