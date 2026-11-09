package garafaela.doo_ep2_gr.controller;

import garafaela.doo_ep2_gr.dao.GsonCursoDAO;
import garafaela.doo_ep2_gr.dao.PersistenciaException;
import garafaela.doo_ep2_gr.model.Curso;
import garafaela.doo_ep2_gr.model.CursoCertificado;
import garafaela.doo_ep2_gr.model.CursoLibre;
import garafaela.doo_ep2_gr.repository.CursoRepository;
import garafaela.doo_ep2_gr.repository.Repository;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.nio.file.Path;

public class MainController {

    // TABLA
    @FXML private TableView<Curso> tabla;
    @FXML private TableColumn<Curso, String> colTipo;
    @FXML private TableColumn<Curso, String> colCodigo;
    @FXML private TableColumn<Curso, Integer> colDuracion;
    @FXML private TableColumn<Curso, Integer> colCupoMaximo;
    @FXML private TableColumn<Curso, String> colCosto;

    // REGISTRO
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtCodigo;
    @FXML private TextField txtDuracion;
    @FXML private TextField txtCupoMaximo;
    @FXML private TextField txtInscritos;
    @FXML private TextField txtCertificadora;
    @FXML private CheckBox chbEvaluacionFinalAlDia;
    @FXML private CheckBox chbDiplomaEmitido;
    @FXML private Button btnRegistrar;


    // OTROS
    @FXML private Label lblMensaje;
    @FXML private Button btnEliminar;

    // ATRIBUTOS
    private GsonCursoDAO certificadosDAO = new GsonCursoDAO<>(Path.of("certificados.json"), CursoCertificado.class);
    private GsonCursoDAO libresDAO = new GsonCursoDAO<>(Path.of("libres.json"), CursoLibre.class);
    private Repository<Curso> repo = new CursoRepository(certificadosDAO, libresDAO);
    private final ObservableList<Curso> todo = FXCollections.observableArrayList();



    // MÉTODOS AUXILIARES
    private void iniciarTabla(){
        this.tabla.setItems(todo);
        this.colTipo.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getTipo()));
        this.colCodigo.setCellValueFactory(celda -> new ReadOnlyStringWrapper(celda.getValue().getCodigo()));

        // NO PUDE CARGAR LOS DATOS A LA TABLA POR ERROR DE TIPO EN LA CLASE CURSOS, PERO EL RESTO DEL SISTEMA FUNCIONA
        /*
        this.colDuracion.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(Integer.parseInt(celda.getValue().getDuracionHoras())));
        this.colCupoMaximo.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(Integer.parseInt(celda.getValue().getCupoMaximo())));
        this.colCosto.setCellValueFactory(celda -> new ReadOnlyObjectWrapper<>(Double.parseDouble(celda.getValue().calcularCosto())));

         */

        this.cmbTipo.getItems().setAll("Certificado","Libre");
    }

    private void actualizarObservableList(){
        this.todo.setAll(repo.listar());
    }


    private void limpiarYResetearCampos(){
        cmbTipo.getSelectionModel().clearSelection();
        txtCodigo.clear();
        txtDuracion.clear();
        txtCupoMaximo.clear();
        txtInscritos.clear();
        txtCertificadora.clear();
        chbEvaluacionFinalAlDia.setSelected(false);
        chbDiplomaEmitido.setSelected(false);
        txtCodigo.setDisable(true);
        txtDuracion.setDisable(true);
        txtCupoMaximo.setDisable(true);
        txtInscritos.setDisable(true);
        txtCertificadora.setDisable(true);
        chbEvaluacionFinalAlDia.setDisable(true);
        chbDiplomaEmitido.setDisable(true);
        btnRegistrar.setDisable(true);


    }



    // MÉTODOS FXML
    @FXML void initialize() {
        iniciarTabla();
        try{
            repo.cargar();
            actualizarObservableList();
        }catch(PersistenciaException e){

        }
    }

    @FXML public void onEliminar() {
        Curso seleccion =  this.tabla.getSelectionModel().getSelectedItem();

        if (seleccion == null){
            lblMensaje.setText("Debe seleccionar un curso para eliminar.");
            return;
        }

        try{
            repo.eliminar(seleccion);
            actualizarObservableList();
            lblMensaje.setText("Curso eliminado correctamente.");
        }catch(PersistenciaException e){
            lblMensaje.setText("Error al eliminar el curso.");
        }


    }

    @FXML public void onRegistrar() {
        String tipo = cmbTipo.getValue();
        Curso newCurso = null;

        try{
            if (tipo.equals("Certificado")) {
                if (txtCodigo.getText().isEmpty() || txtDuracion.getText().isEmpty() || txtCupoMaximo.getText().isEmpty() || txtCertificadora.getText().isEmpty()) {
                    lblMensaje.setText("Debe rellenar todos los campos.");
                    return;
                }

                newCurso = new CursoCertificado(txtCodigo.getText(), Integer.parseInt(txtDuracion.getText()), Integer.parseInt(txtCupoMaximo.getText()), txtCertificadora.getText(), chbEvaluacionFinalAlDia.isSelected());

            } else if (tipo.equals("Libre")) {
                if (txtCodigo.getText().isEmpty() || txtDuracion.getText().isEmpty() || txtCupoMaximo.getText().isEmpty() || txtInscritos.getText().isEmpty()) {
                    lblMensaje.setText("Debe rellenar todos los campos.");
                    return;
                }
                newCurso = new CursoLibre(txtCodigo.getText(), Integer.parseInt(txtDuracion.getText()), Integer.parseInt(txtCupoMaximo.getText()), Integer.parseInt(txtInscritos.getText()));


            }

            if (newCurso != null) {
                repo.agregar(newCurso);
                actualizarObservableList();
                lblMensaje.setText("Curso agregado correctamente.");
                limpiarYResetearCampos();
            }
        }catch(NumberFormatException e){
            lblMensaje.setText("Debe ingresar números positivos o con '.'.");
        }catch(IllegalArgumentException e){
            lblMensaje.setText(e.getMessage());
        }catch(PersistenciaException e){
            lblMensaje.setText("Error al agregar el curso.");
        }

    }

    @FXML public void onTipo() {
        String tipo = cmbTipo.getValue();

        if (tipo == null){
            return;
        }

        if (tipo.equals("Certificado")) {
            txtCodigo.setDisable(false);
            txtDuracion.setDisable(false);
            txtCupoMaximo.setDisable(false);
            txtInscritos.setDisable(true);
            txtCertificadora.setDisable(false);
            chbEvaluacionFinalAlDia.setDisable(false);
            chbDiplomaEmitido.setDisable(false);
            btnRegistrar.setDisable(false);

        }else if (tipo.equals("Libre")) {
            txtCodigo.setDisable(false);
            txtDuracion.setDisable(false);
            txtCupoMaximo.setDisable(false);
            txtInscritos.setDisable(false);
            txtCertificadora.setDisable(true);
            chbEvaluacionFinalAlDia.setDisable(true);
            chbDiplomaEmitido.setDisable(true);
            btnRegistrar.setDisable(false);

        }


    }



}
