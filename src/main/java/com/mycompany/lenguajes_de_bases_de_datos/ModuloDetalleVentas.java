package com.mycompany.proyecto_lbd_template;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class ModuloDetalleVentas {

    private final Connection connection;
    private final DetalleVentasModule detalleVentasModule;

    public ModuloDetalleVentas(Connection connection) {
        this.connection = connection;
        this.detalleVentasModule = new DetalleVentasModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Detalle de Ventas");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // TextArea para mostrar la lista de detalles de ventas
        TextArea txtDetalleVentas = new TextArea();
        txtDetalleVentas.setEditable(false);
        actualizarListaDetalleVentas(txtDetalleVentas);

        // Botones para gestionar detalles de ventas
        Button btnAgregar = new Button("Agregar Detalle de Venta");
        Button btnActualizar = new Button("Actualizar Detalle de Venta");
        Button btnEliminar = new Button("Eliminar Detalle de Venta");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        // Acciones de botones
        btnAgregar.setOnAction(e -> agregarDetalleVenta(txtDetalleVentas));
        btnActualizar.setOnAction(e -> actualizarDetalleVenta(txtDetalleVentas));
        btnEliminar.setOnAction(e -> eliminarDetalleVenta(txtDetalleVentas));
        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(
                new Label("Lista de Detalle de Ventas:"), txtDetalleVentas,
                btnAgregar, btnActualizar, btnEliminar, btnRegresar
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarListaDetalleVentas(TextArea txtDetalleVentas) {
        txtDetalleVentas.clear();
        detalleVentasModule.getAll(connection).forEach(detalle -> txtDetalleVentas.appendText(detalle + "\n"));
    }

    private void agregarDetalleVenta(TextArea txtDetalleVentas) {
        Dialog<DetalleVenta> dialog = crearDialogoDetalleVenta("Agregar Detalle de Venta");
        dialog.showAndWait().ifPresent(detalle -> {
            detalleVentasModule.create(detalle, connection);
            actualizarListaDetalleVentas(txtDetalleVentas);
        });
    }

    private void actualizarDetalleVenta(TextArea txtDetalleVentas) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Actualizar Detalle de Venta");
        idDialog.setHeaderText("Ingrese el ID del detalle de venta a actualizar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                DetalleVenta detalleExistente = detalleVentasModule.read(id, connection);
                if (detalleExistente != null) {
                    Dialog<DetalleVenta> dialog = crearDialogoDetalleVenta("Actualizar Detalle de Venta", detalleExistente);
                    dialog.showAndWait().ifPresent(detalle -> {
                        detalle.setIdDetalle(id);
                        detalleVentasModule.update(detalle, connection);
                        actualizarListaDetalleVentas(txtDetalleVentas);
                    });
                } else {
                    mostrarAlerta("Detalle de Venta no encontrado", "No existe un detalle de venta con el ID proporcionado.");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void eliminarDetalleVenta(TextArea txtDetalleVentas) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Eliminar Detalle de Venta");
        idDialog.setHeaderText("Ingrese el ID del detalle de venta a eliminar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                detalleVentasModule.delete(id, connection);
                actualizarListaDetalleVentas(txtDetalleVentas);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }

    private Dialog<DetalleVenta> crearDialogoDetalleVenta(String titulo) {
        return crearDialogoDetalleVenta(titulo, null);
    }

    private Dialog<DetalleVenta> crearDialogoDetalleVenta(String titulo, DetalleVenta detalleExistente) {
        Dialog<DetalleVenta> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese los datos del detalle de venta:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        TextField txtIdVenta = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getIdVenta()) : "");
        TextField txtIdRepuesto = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getIdRepuesto()) : "");
        TextField txtCantidad = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getCantidad()) : "");
        TextField txtPrecioUnitario = new TextField(detalleExistente != null ? String.valueOf(detalleExistente.getPrecioUnitario()) : "");

        contenido.getChildren().addAll(
                new Label("ID Venta:"), txtIdVenta,
                new Label("ID Repuesto:"), txtIdRepuesto,
                new Label("Cantidad:"), txtCantidad,
                new Label("Precio Unitario:"), txtPrecioUnitario
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
    if (dialogButton == btnGuardar) {
        try {
            return new DetalleVenta(
                Integer.parseInt(txtIdVenta.getText()),
                Integer.parseInt(txtIdRepuesto.getText()),
                Integer.parseInt(txtCantidad.getText()),
                Double.parseDouble(txtPrecioUnitario.getText()),
                Integer.parseInt(txtCantidad.getText()) * Double.parseDouble(txtPrecioUnitario.getText())
            );
        } catch (NumberFormatException e) {
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
