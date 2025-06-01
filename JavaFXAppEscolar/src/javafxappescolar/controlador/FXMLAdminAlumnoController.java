/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.POJO.Alumno;
import javafxappescolar.modelo.POJO.ResultadoOperacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author abelh
 */
public class FXMLAdminAlumnoController implements Initializable, INotificacion {

    @FXML
    private TextField tfBuscar;
    @FXML
    private TableView<Alumno> tvAlumnos;
    @FXML
    private TableColumn colMatricula;
    @FXML
    private TableColumn colApPaterno;
    @FXML
    private TableColumn colApMaterno;
    @FXML
    private TableColumn colNombre;
    @FXML
    private TableColumn colFacultad;
    @FXML
    private TableColumn colCarrera;
    
    private ObservableList<Alumno> alumnos;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarInformacion();
    }
    
    private void configurarTabla(){
        colMatricula.setCellValueFactory(new PropertyValueFactory("matricula"));
        colApPaterno.setCellValueFactory(new PropertyValueFactory("apellidoPaterno"));
        colApMaterno.setCellValueFactory(new PropertyValueFactory("apellidoMaterno"));
        colNombre.setCellValueFactory(new PropertyValueFactory("nombre"));
        colFacultad.setCellValueFactory(new PropertyValueFactory("facultad"));
        colCarrera.setCellValueFactory(new PropertyValueFactory("carrera"));
    }
    
    private void cargarInformacion() {
        try {
            alumnos = FXCollections.observableArrayList();
            ArrayList<Alumno> alumnosDAO = AlumnoDAO.obtenerAlumnos();
            alumnos.addAll(alumnosDAO);
            tvAlumnos.setItems(alumnos);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Error de carga",
                    "Lo sentimos, no se pudo recuperar la informacion de los alumnos"
            );
            cerrarVentana();
        }
    }
    
    private void cerrarVentana() {
        ((Stage) tfBuscar.getScene().getWindow()).close();
    }

    @FXML
    private void clicBtnAgregar(ActionEvent event) {
        irAFormularioAlumno(false, null);
    }

    @FXML
    private void clicBtnModificar(ActionEvent event) {
        Alumno alumno = tvAlumnos.getSelectionModel().getSelectedItem();
        if (alumno != null) {
            irAFormularioAlumno(true, alumno);
        } else {
            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.INFORMATION,
                    "Seleccione un Alumno",
                    "Por favor seleccione el alumno a modificar con la tabla");
        }
    }

    @FXML
    private void clicBtnEliminar(ActionEvent event) {
        //Lo ideal seria hacerlo con getSelectedItem
        int pos = tvAlumnos.getSelectionModel().getSelectedIndex();
        if (pos >= 0) {
            Alumno alumno = alumnos.get(pos);
            String mensaje = String.format(
                    "¿Esta seguro de eliminar al alumno(a) %s?\nEsta accion no se puede deshacer", alumno.toString());
            if (Utilidad.mostrarAlertaConfirmacion("Eliminar alumno", mensaje)) {    
                eliminarAlumno(alumno.getIdAlumno());
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error", "Antes debe seleccionar un alumno");
        }
    }
    
    private void irAFormularioAlumno(boolean esEdicion, Alumno alumnoEdicion) {
        try {
            Stage escenarioFormulario = new Stage();
            FXMLLoader loader = 
                    new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLFormularioAlumno.fxml"));
            Parent vista = loader.load();
            
            FXMLFormularioAlumnoController controlador = loader.getController();
            controlador.inicializarInformacion(esEdicion, alumnoEdicion, this);
            
            Scene escena = new Scene(vista);
            escenarioFormulario.setScene(escena);
            escenarioFormulario.setTitle("Formulario del Alumno");
            escenarioFormulario.initModality(Modality.APPLICATION_MODAL);
            escenarioFormulario.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    @Override
    public void operacionExitosa(String tipo, String nombreAlumno) {
        System.out.println("Operación: " + tipo + ", con el alumno(a): " + nombreAlumno);
        cargarInformacion();
    }
    
    private void eliminarAlumno(int idAlumno) {
        try {
            ResultadoOperacion resultado = AlumnoDAO.eliminarAlumno(idAlumno);
            if(!resultado.isError()) {
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.INFORMATION,
                        "Alumno(a) eliminado",
                        "El registro del alumno fue eliminado correctamente");
                cargarInformacion();
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Problemas al eliminar",
                    "Lo sentimos por el momento no se pudo realizar la operacion seleccionada");
        }
    }
}