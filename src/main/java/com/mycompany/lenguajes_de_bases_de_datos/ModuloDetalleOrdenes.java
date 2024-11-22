package com.mycompany.proyecto_lbd_template;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class ModuloDetalleOrdenes {

    private final Connection connection;
    private final DetalleOrdenesModule detalleOrdenesModule;

    public ModuloDetalleOrdenes(Connection connection) {
        this.connection = connection;
        this.detalleOrdenesModule = new DetalleOrdenesModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Detalles de Órdenes");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // TextArea para mostrar la lista de detalles de órdenes
        TextArea txtDetalleOrdenes = new TextArea();
        txtDetalleOrdenes.setEditable(false);
        actualizarListaDetalleOrdenes(txtDetalleOrdenes);

        // Botones para gestionar detalles de órdenes
        Button btnAgregar = new Button("Agregar Detalle de Orden");
        Button btnActualizar = new Button("Actualizar Detalle de Orden");
        Button btnEliminar = new Button("Eliminar Detalle de Orden");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        // Acciones de botones
        btnAgregar.setOnAction(e -> agregarDetalleOrden(txtDetalleOrdenes));
        btnActualizar.setOnAction(e -> actualizarDetalleOrden(txtDetalleOrdenes));
        btnEliminar.setOnAction(e -> eliminarDetalleOrden(txtDetalleOrdenes));
        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(
                new Label("Lista de Detalles de Órdenes:"), txtDetalleOrdenes,
                btnAgregar, btnActualizar, btnEliminar, btnRegresar
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarListaDetalleOrdenes(TextArea txtDetalleOrdenes) {
        txtDetalleOrdenes.clear();
        detalleOrdenesModule.getAll(connection).forEach(detalle -> txtDetalleOrdenes.appendText(detalle + "\n"));
    }

    private void agregarDetalleOrden(TextArea txtDetalleOrdenes) {
        Dialog<DetalleOrden> dialog = crearDialogoDetalleOrden("Agregar Detalle de Orden");
        dialog.showAndWait().ifPresent(detalle -> {
            detalleOrdenesModule.create(detalle, connection);
            actualizarListaDetalleOrdenes(txtDetalleOrdenes);
        });
    }

    private void actualizarDetalleOrden(TextArea txtDetalleOrdenes) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Actualizar Detalle de Orden");
        idDialog.setHeaderText("Ingrese el ID del detalle a actualizar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                DetalleOrden detalleExistente = detalleOrdenesModule.read(id, connection);
                if (detalleExistente != null) {
                    Dialog<DetalleOrden> dialog = crearDialogoDetalleOrden("Actualizar Detalle de Orden", detalleExistente);
                    dialog.showAndWait().ifPresent(detalle -> {
                        detalle.setIdDetalleOrden(id);
                        detalleOrdenesModule.update(detalle, connection);
                        actualizarListaDetalleOrdenes(txtDetalleOrdenes);
                    });
                } else {
                    mostrarAlerta("Detalle no encontrado", "No existe un detalle con el ID proporcionado.");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void eliminarDetalleOrden(TextArea txtDetalleOrdenes) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Eliminar Detalle de Orden");
        idDialog.setHeaderText("Ingrese el ID del detalle a eliminar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                detalleOrdenesModule.delete(id, connection);
                actualizarListaDetalleOrdenes(txtDetalleOrdenes);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }

    private Dialog<DetalleOrden> crearDialogoDetalleOrden(String titulo) {
        return crearDialogoDetalleOrden(titulo, null);
    }

    private Dialog<DetalleOrden> crearDialogoDetalleOrden(String titulo, DetalleOrden detalleExistente) {
        Dialog<DetalleOrden> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese los datos del detalle de la orden:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        TextField txtIdOrden = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getIdOrden()) : "");
        TextField txtIdRepuesto = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getIdRepuesto()) : "");
        TextField txtCantidad = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getCantidad()) : "");
        TextField txtPrecioUnitario = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getPrecioUnitario()) : "");

        contenido.getChildren().addAll(
                new Label("ID Orden:"), txtIdOrden,
                new Label("ID Repuesto:"), txtIdRepuesto,
                new Label("Cantidad:"), txtCantidad,
                new Label("Precio Unitario:"), txtPrecioUnitario
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    return new DetalleOrden(
                            Integer.parseInt(txtIdOrden.getText()),
                            Integer.parseInt(txtIdRepuesto.getText()),
                            Integer.parseInt(txtCantidad.getText()),
                            Double.parseDouble(txtPrecioUnitario.getText())
                    );
                } catch (Exception e) {
                    mostrarAlerta("Error", "Los campos deben estar correctamente formateados.");
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
