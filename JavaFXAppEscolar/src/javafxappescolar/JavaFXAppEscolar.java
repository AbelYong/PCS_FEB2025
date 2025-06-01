package javafxappescolar;

import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author abelh
 */
public class JavaFXAppEscolar extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{
            //getClass se refiere a esta misma clase
            //memo: Para java, y para netbeans, un recurso es cualquier archivo NO .java
            Parent vista = FXMLLoader.load(getClass().getResource("vista/FXMLInicioSesion.fxml"));
            Scene escenaInicioSesion = new Scene(vista);
            
            primaryStage.setScene(escenaInicioSesion);
            //Si no cambiamos el titulo del escenario, el titulo de la ventana sera el nombre del archivo .fxml
            primaryStage.setTitle("Inicio de Sesión");
            primaryStage.show();
            
        } catch(IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Error: "+ioe.getMessage());
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
