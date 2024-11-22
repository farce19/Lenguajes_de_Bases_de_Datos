package com.mycompany.lenguajes_de_bases_de_datos;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class ModuloOrdenesCompra {

    private final Connection connection;
    private final OrdenCompraModule ordenCompraModule;

    public ModuloOrdenesCompra(Connection connection) {
        this.connection = connection;
        this.ordenCompraModule = new OrdenCompraModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Órdenes de Compra");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // TextArea para mostrar la lista de órdenes de compra
        TextArea txtOrdenesCompra = new TextArea();
        txtOrdenesCompra.setEditable(false);
        actualizarListaOrdenesCompra(txtOrdenesCompra);

        // Botones para gestionar órdenes de compra
        Button btnAgregar = new Button("Agregar Orden de Compra");
        Button btnActualizar = new Button("Actualizar Orden de Compra");
        Button btnEliminar = new Button("Eliminar Orden de Compra");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        // Acciones de botones
        btnAgregar.setOnAction(e -> agregarOrdenCompra(txtOrdenesCompra));
        btnActualizar.setOnAction(e -> actualizarOrdenCompra(txtOrdenesCompra));
        btnEliminar.setOnAction(e -> eliminarOrdenCompra(txtOrdenesCompra));
        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(
                new Label("Lista de Órdenes de Compra:"), txtOrdenesCompra,
                btnAgregar, btnActualizar, btnEliminar, btnRegresar
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarListaOrdenesCompra(TextArea txtOrdenesCompra) {
        txtOrdenesCompra.clear();
        ordenCompraModule.getAll(connection).forEach(orden -> txtOrdenesCompra.appendText(orden + "\n"));
    }

    private void agregarOrdenCompra(TextArea txtOrdenesCompra) {
        Dialog<OrdenCompra> dialog = crearDialogoOrdenCompra("Agregar Orden de Compra");
        dialog.showAndWait().ifPresent(orden -> {
            ordenCompraModule.create(orden, connection);
            actualizarListaOrdenesCompra(txtOrdenesCompra);
        });
    }

    private void actualizarOrdenCompra(TextArea txtOrdenesCompra) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Actualizar Orden de Compra");
        idDialog.setHeaderText("Ingrese el ID de la orden a actualizar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                OrdenCompra ordenExistente = ordenCompraModule.read(id, connection);
                if (ordenExistente != null) {
                    Dialog<OrdenCompra> dialog = crearDialogoOrdenCompra("Actualizar Orden de Compra", ordenExistente);
                    dialog.showAndWait().ifPresent(orden -> {
                        orden.setIdOrden(id);
                        ordenCompraModule.update(orden, connection);
                        actualizarListaOrdenesCompra(txtOrdenesCompra);
                    });
                } else {
                    mostrarAlerta("Orden no encontrada", "No existe una orden con el ID proporcionado.");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void eliminarOrdenCompra(TextArea txtOrdenesCompra) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Eliminar Orden de Compra");
        idDialog.setHeaderText("Ingrese el ID de la orden a eliminar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                ordenCompraModule.delete(id, connection);
                actualizarListaOrdenesCompra(txtOrdenesCompra);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }

    private Dialog<OrdenCompra> crearDialogoOrdenCompra(String titulo) {
        return crearDialogoOrdenCompra(titulo, null);
    }

    private Dialog<OrdenCompra> crearDialogoOrdenCompra(String titulo, OrdenCompra ordenExistente) {
        Dialog<OrdenCompra> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese los datos de la orden de compra:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        TextField txtIdProveedor = new TextField(ordenExistente != null ? String.valueOf(ordenExistente.getIdProveedor()) : "");
        TextField txtFechaOrden = new TextField(ordenExistente != null ? ordenExistente.getFechaOrden().toString() : "");
        TextField txtEstadoOrden = new TextField(ordenExistente != null ? ordenExistente.getEstadoOrden() : "");

        contenido.getChildren().addAll(
                new Label("ID Proveedor:"), txtIdProveedor,
                new Label("Fecha de Orden (YYYY-MM-DD):"), txtFechaOrden,
                new Label("Estado de Orden:"), txtEstadoOrden
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    return new OrdenCompra(
                            Integer.parseInt(txtIdProveedor.getText()),
                            java.sql.Date.valueOf(txtFechaOrden.getText()),
                            txtEstadoOrden.getText()
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
