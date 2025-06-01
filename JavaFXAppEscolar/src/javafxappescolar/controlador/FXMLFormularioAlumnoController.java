/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package javafxappescolar.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafxappescolar.dominio.AlumnoDM;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.POJO.Alumno;
import javafxappescolar.modelo.POJO.Carrera;
import javafxappescolar.modelo.POJO.Facultad;
import javafxappescolar.modelo.POJO.ResultadoOperacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.dao.CatalogoDAO;
import javafxappescolar.utilidades.Utilidad;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author abelh
 */
public class FXMLFormularioAlumnoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApPaterno;
    @FXML
    private TextField tfApMaterno;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField tfEmail;
    @FXML
    private DatePicker datePickerFechaNacimiento;
    @FXML
    private ComboBox<Facultad> comboFacultad;
    @FXML
    private ComboBox<Carrera> comboCarrera;
    @FXML
    private ImageView imageViewFotoAlumno;
    @FXML
    private Label lbNombreError;
    @FXML
    private Label lbApPaternoError;
    @FXML
    private Label lbApMaternoError;
    @FXML
    private Label lbMatriculaError;
    @FXML
    private Label lbCorreoError;
    @FXML
    private Label lbFechaNacimientoError;
    @FXML
    private Label lbFacultadError;
    @FXML
    private Label lbCarreraError;
    @FXML
    private Label lbFotoError;
    
    private ObservableList<Facultad> facultades;
    private ObservableList<Carrera> carreras;
    private File archivoFoto;
    
    private INotificacion observador;
    private Alumno alumnoEdicion;
    private boolean esEdicion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarFacultades();
        seleccionarFacultades();
    }
    
    public void inicializarInformacion(boolean esEdicion, Alumno alumnoEdicion, INotificacion observador) {
        this.esEdicion = esEdicion;
        this.alumnoEdicion = alumnoEdicion;
        this.observador = observador;
        if (esEdicion) {
            cargarInformacionEdicion();
        }
    }
    
    private void cargarInformacionEdicion() {
        if (alumnoEdicion != null ) {
            tfNombre.setText(alumnoEdicion.getNombre());
            tfApPaterno.setText(alumnoEdicion.getApellidoPaterno());
            tfApMaterno.setText(alumnoEdicion.getApellidoMaterno());
            tfMatricula.setText(alumnoEdicion.getMatricula());
            tfMatricula.setDisable(true);
            tfEmail.setText(alumnoEdicion.getEmail());
            if(!alumnoEdicion.getFechaNacimiento().isEmpty()) {
                datePickerFechaNacimiento.setValue(LocalDate.parse(alumnoEdicion.getFechaNacimiento()));
            }
            int indiceFacultad = obtenerPosicionFacultad(alumnoEdicion.getIdFacultad());
            comboFacultad.getSelectionModel().select(indiceFacultad);
            int indiceCarrera = obtenerPosicionCarrera(alumnoEdicion.getIdCarrera());
            comboCarrera.getSelectionModel().select(indiceCarrera);
            
            //FIXME Aqui lanza un nullpointer - creo que es porque cuando editas siempre recupera la foto
            try {
                byte[] foto = AlumnoDAO.obtenerFotoAlumno(alumnoEdicion.getIdAlumno());
                if(foto!= null) {
                    alumnoEdicion.setFoto(foto);
                    ByteArrayInputStream input = new ByteArrayInputStream(foto);
                    Image image = new Image(input);
                    imageViewFotoAlumno.setImage(image);
                }
            } catch (SQLException sqle) {
                sqle.printStackTrace();
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        }
    }

    private void cargarFacultades() {
        facultades = FXCollections.observableArrayList();
        try {
            List<Facultad> facultadesDAO = CatalogoDAO.obtenerFacultades();
            facultades.addAll(facultadesDAO);
            comboFacultad.setItems(facultades);
        } catch (SQLException ex) {
            // TODO
            ex.printStackTrace();
        }
    }

    private void seleccionarFacultades() {
        comboFacultad.valueProperty().addListener(new ChangeListener<Facultad>() {
                    @Override
                    public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                       if(newValue != null){
                           cargarCarreras(newValue.getIdFacultad());
                       }
                    }
                });
    }

    private void cargarCarreras(int idFacultad) {
        try {
            carreras = FXCollections.observableArrayList();
            List<Carrera> carrerasDAO = CatalogoDAO.obtenerCarrerasPorFacultad(idFacultad);
            carreras.addAll(carrerasDAO);
            comboCarrera.setItems(carreras);
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
        }
    }

    @FXML
    private void clicBotonCancelar(ActionEvent event) {
        
    }

    @FXML
    private void clicBotonGuardar(ActionEvent event) {
        if(validarCampos()){
            try {
                if(!esEdicion) {
                    ResultadoOperacion resultado = AlumnoDM.verficarEstadoMatricula(tfMatricula.getText());
                    if(!resultado.isError()) {
                        Alumno alumno = obtenerAlumnoNuevo();
                        guardarAlumno(alumno);
                    } else {
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Verificar datos", resultado.getMensaje());
                    }
                } else {
                    Alumno alumno = obtenerAlumnoEdicion();
                    modificarAlumno(alumno);
                }
            } catch (IOException ex) {
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.ERROR,
                        "Error en foto",
                        "Lo sentimos la foto seleccionada no se pudo guardar");
            }
        }else{
        
        }
    }

    @FXML
    private void clicBotonSubirImagen(ActionEvent event) {
        mostrarDialogoSeleccionFoto();
    }
    
    private void mostrarDialogoSeleccionFoto() {
        FileChooser dialogoSeleccon = new FileChooser();
        dialogoSeleccon.setTitle("Selecciona una foto");
        FileChooser.ExtensionFilter filtroImg = 
                new FileChooser.ExtensionFilter("Archivos JPG (.jpg)", "*.jpg");
        dialogoSeleccon.getExtensionFilters().add(filtroImg);
        archivoFoto = dialogoSeleccon.showOpenDialog(
                Utilidad.getScenarioPorComponente(tfNombre));
        if(archivoFoto != null) {
            mostrarFotoPerfil();
        }
    }
    
    private void mostrarFotoPerfil() {
        try {
            BufferedImage bufferImg = ImageIO.read(archivoFoto);
            Image imagen = SwingFXUtils.toFXImage(bufferImg, null);
            imageViewFotoAlumno.setImage(imagen);
        } catch (IOException ioe) {
            //TODO
            ioe.printStackTrace();
        }        
    }
    
    private boolean validarCampos() {
        boolean valido = true;
        lbNombreError.setText("");
        lbApPaternoError.setText("");
        lbApMaternoError.setText("");
        lbMatriculaError.setText("");
        lbCorreoError.setText("");
        lbFechaNacimientoError.setText("");
        lbFacultadError.setText("");
        lbCarreraError.setText("");
        lbFotoError.setText("");
        if(tfNombre.getText().isEmpty()) {
            valido = false;
            lbNombreError.setText("*requerido");
        }
        if(tfApPaterno.getText().isEmpty()) {
            valido = false;
            lbApPaternoError.setText("*requerido");
        }
        if(tfApMaterno.getText().isEmpty()) {
            valido = false;
            lbApMaternoError.setText("*requerido");
        }
        if (tfMatricula.getText().isEmpty()) {
            valido = false;
            lbMatriculaError.setText("*requerido");
        }
        if (tfEmail.getText().isEmpty()) {
            valido = false;
            lbCorreoError.setText("*requerido");
        }
        if (datePickerFechaNacimiento.getValue() == null) {
            valido = false;
            lbFechaNacimientoError.setText("*requerido");
        }
        if (comboFacultad.getValue() == null) {
            valido = false;
            lbFacultadError.setText("*requerido");
        }
        if (comboCarrera.getValue() == null) {
            valido = false;
            lbCarreraError.setText("*requerido");
        }
        if (archivoFoto == null) {
            valido = false;
            lbFotoError.setText("*requerido");
        }
        return valido;
    }
    
    private Alumno obtenerAlumnoNuevo() throws IOException{
        Alumno alumno = new Alumno();
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApPaterno.getText());
        alumno.setApellidoMaterno(tfApMaterno.getText());
        alumno.setMatricula(tfMatricula.getText());
        alumno.setEmail(tfEmail.getText());
        alumno.setFechaNacimiento(
                datePickerFechaNacimiento.getValue().toString()); //SI ESTO NO SE VALIDA LANZARA NULLPOINTER
        alumno.setIdCarrera(
                comboCarrera.getSelectionModel().getSelectedItem().getIdCarrera()); //Lanzara nullpointer
        byte[] foto = Files.readAllBytes(archivoFoto.toPath());
        alumno.setFoto(foto);
        return alumno;
    }
    
    private Alumno obtenerAlumnoEdicion() throws IOException {
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(alumnoEdicion.getIdAlumno());
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApPaterno.getText());
        alumno.setApellidoMaterno(tfApMaterno.getText());
        alumno.setMatricula(tfMatricula.getText());
        alumno.setEmail(tfEmail.getText());
        alumno.setFechaNacimiento(
                datePickerFechaNacimiento.getValue().toString());
        alumno.setIdCarrera(
                comboCarrera.getSelectionModel().getSelectedItem().getIdCarrera());
        if (archivoFoto != null) {
            byte[] foto = Files.readAllBytes(archivoFoto.toPath());
            alumno.setFoto(foto);
        } else {
            alumno.setFoto(alumnoEdicion.getFoto());
        }
        return alumno;
    }
    
    private void guardarAlumno(Alumno nuevoAlumno) {
        try {
            ResultadoOperacion resultadoInsertar = AlumnoDAO.registrarAlumno(nuevoAlumno);
            if(!resultadoInsertar.isError()) {
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.INFORMATION,
                        "Alumno(a) registrado",
                        "El alumno(a) fue registrado exitosamente");
                Utilidad.getScenarioPorComponente(tfNombre).close();
                observador.operacionExitosa("insertar", nuevoAlumno.getNombre());
            }else{
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.ERROR,
                        "Error al registrar",
                        resultadoInsertar.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Error de conexion",
                    "Ocurrio un error al conectarse con la Base de Datos");
        }
    }
    
    private void modificarAlumno(Alumno alumno) {
        try {
            ResultadoOperacion resultadoModificar = AlumnoDAO.actualizarAlumno(alumno);
            if(!resultadoModificar.isError()) {
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.INFORMATION,
                        "Alumno(a) registrado",
                        "El alumno(a) se actualizo exitosamente");
                Utilidad.getScenarioPorComponente(tfNombre).close();
                observador.operacionExitosa("actualizar", alumno.getNombre());
            }else{
                Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.ERROR,
                        "Error al modificar",
                        resultadoModificar.getMensaje());
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Error de conexion",
                    "Ocurrio un error al conectarse con la Base de Datos");
        }
    }
    
    private int obtenerPosicionFacultad(int idFacultad) {
        for (int i = 0; i < facultades.size(); i++) {
            if (facultades.get(i).getIdFacultad() == idFacultad) {
                return i;
            }
        }
        return 0;
    }
    
    private int obtenerPosicionCarrera(int idCarrera) {
        for (int i = 0; i < carreras.size(); i++) {
            if (carreras.get(i).getIdCarrera() == idCarrera) {
                return i;
            }
        }
        return 0;
    }
}
