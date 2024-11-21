package com.mycompany.lenguajes_de_bases_de_datos;



import java.sql.Connection;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ModuloRepuestos {

    private final Connection connection;
    private final RepuestosModule repuestosModule;

    public ModuloRepuestos(Connection connection) {
        this.connection = connection;
        this.repuestosModule = new RepuestosModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Repuestos");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // TextArea para mostrar la lista de repuestos
        TextArea txtRepuestos = new TextArea();
        txtRepuestos.setEditable(false);
        actualizarListaRepuestos(txtRepuestos);

        // Botones para gestionar repuestos
        Button btnAgregar = new Button("Agregar Repuesto");
        Button btnActualizar = new Button("Actualizar Repuesto");
        Button btnEliminar = new Button("Eliminar Repuesto");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        // Acciones de botones
        btnAgregar.setOnAction(e -> agregarRepuesto(txtRepuestos));
        btnActualizar.setOnAction(e -> actualizarRepuesto(txtRepuestos));
        btnEliminar.setOnAction(e -> eliminarRepuesto(txtRepuestos));
        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(new Label("Lista de Repuestos:"), txtRepuestos, btnAgregar, btnActualizar, btnEliminar, btnRegresar);

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarListaRepuestos(TextArea txtRepuestos) {
        txtRepuestos.clear();
        repuestosModule.getAll(connection).forEach(repuesto -> txtRepuestos.appendText(repuesto + "\n"));
    }

    private void agregarRepuesto(TextArea txtRepuestos) {
        Dialog<Repuesto> dialog = crearDialogoRepuesto("Agregar Repuesto");
        dialog.showAndWait().ifPresent(repuesto -> {
            repuestosModule.create(repuesto, connection);
            actualizarListaRepuestos(txtRepuestos);
        });
    }

    private void actualizarRepuesto(TextArea txtRepuestos) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Actualizar Repuesto");
        idDialog.setHeaderText("Ingrese el ID del repuesto a actualizar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Repuesto repuestoExistente = repuestosModule.read(id, connection);
                if (repuestoExistente != null) {
                    Dialog<Repuesto> dialog = crearDialogoRepuesto("Actualizar Repuesto", repuestoExistente);
                    dialog.showAndWait().ifPresent(repuesto -> {
                        repuesto.setId(id);
                        repuestosModule.update(repuesto, connection);
                        actualizarListaRepuestos(txtRepuestos);
                    });
                } else {
                    mostrarAlerta("Repuesto no encontrado", "No existe un repuesto con el ID proporcionado.");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void eliminarRepuesto(TextArea txtRepuestos) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Eliminar Repuesto");
        idDialog.setHeaderText("Ingrese el ID del repuesto a eliminar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                repuestosModule.delete(id, connection);
                actualizarListaRepuestos(txtRepuestos);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }

    private Dialog<Repuesto> crearDialogoRepuesto(String titulo) {
        return crearDialogoRepuesto(titulo, null);
    }

    private Dialog<Repuesto> crearDialogoRepuesto(String titulo, Repuesto repuestoExistente) {
        Dialog<Repuesto> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese los datos del repuesto:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        TextField txtNombre = new TextField(repuestoExistente != null ? repuestoExistente.getNombre() : "");
        TextField txtMarca = new TextField(repuestoExistente != null ? repuestoExistente.getMarca() : "");
        TextField txtPrecio = new TextField(repuestoExistente != null ? String.valueOf(repuestoExistente.getPrecio()) : "");
        TextField txtStock = new TextField(repuestoExistente != null ? String.valueOf(repuestoExistente.getStock()) : "");
        TextField txtProveedor = new TextField(repuestoExistente != null ? String.valueOf(repuestoExistente.getIdProveedor()) : "");
        TextField txtCategoria = new TextField(repuestoExistente != null ? String.valueOf(repuestoExistente.getIdCategoria()) : "");

        contenido.getChildren().addAll(
                new Label("Nombre:"), txtNombre,
                new Label("Marca:"), txtMarca,
                new Label("Precio:"), txtPrecio,
                new Label("Stock:"), txtStock,
                new Label("ID Proveedor:"), txtProveedor,
                new Label("ID Categoría:"), txtCategoria
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    return new Repuesto(
                            txtNombre.getText(),
                            txtMarca.getText(),
                            Double.parseDouble(txtPrecio.getText()),
                            Integer.parseInt(txtStock.getText()),
                            Integer.parseInt(txtProveedor.getText()),
                            Integer.parseInt(txtCategoria.getText())
                    );
                } catch (NumberFormatException e) {
                    mostrarAlerta("Error", "Los campos de precio, stock, proveedor e ID de categoría deben ser números.");
                }
            }
            return null;
        });

        return dialog;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
