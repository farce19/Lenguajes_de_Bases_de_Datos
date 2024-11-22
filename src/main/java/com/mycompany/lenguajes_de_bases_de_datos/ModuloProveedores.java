package com.mycompany.proyecto_lbd_template;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;

public class ModuloProveedores {

    private final Connection connection;
    private final ProveedoresModule proveedoresModule;

    public ModuloProveedores(Connection connection) {
        this.connection = connection;
        this.proveedoresModule = new ProveedoresModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Proveedores");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextArea txtProveedores = new TextArea();
        txtProveedores.setEditable(false);
        actualizarListaProveedores(txtProveedores);

        Button btnAgregar = new Button("Agregar Proveedor");
        Button btnActualizar = new Button("Actualizar Proveedor");
        Button btnEliminar = new Button("Eliminar Proveedor");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        btnAgregar.setOnAction(e -> agregarProveedor(txtProveedores));
        btnActualizar.setOnAction(e -> actualizarProveedor(txtProveedores));
        btnEliminar.setOnAction(e -> eliminarProveedor(txtProveedores));
        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(new Label("Lista de Proveedores:"), txtProveedores, btnAgregar, btnActualizar, btnEliminar, btnRegresar);

        Scene scene = new Scene(layout, 500, 400);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarListaProveedores(TextArea txtProveedores) {
        txtProveedores.clear();
        proveedoresModule.getAll(connection).forEach(proveedor -> txtProveedores.appendText(proveedor + "\n"));
    }

    private void agregarProveedor(TextArea txtProveedores) {
        Dialog<Proveedor> dialog = crearDialogoProveedor("Agregar Proveedor");
        dialog.showAndWait().ifPresent(proveedor -> {
            proveedoresModule.create(proveedor, connection);
            actualizarListaProveedores(txtProveedores);
        });
    }

    private void actualizarProveedor(TextArea txtProveedores) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Actualizar Proveedor");
        idDialog.setHeaderText("Ingrese el ID del proveedor a actualizar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Proveedor proveedorExistente = proveedoresModule.read(id, connection);
                if (proveedorExistente != null) {
                    Dialog<Proveedor> dialog = crearDialogoProveedor("Actualizar Proveedor", proveedorExistente);
                    dialog.showAndWait().ifPresent(proveedor -> {
                        proveedor.setId(id);
                        proveedoresModule.update(proveedor, connection);
                        actualizarListaProveedores(txtProveedores);
                    });
                } else {
                    mostrarAlerta("Proveedor no encontrado", "No existe un proveedor con el ID proporcionado.");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void eliminarProveedor(TextArea txtProveedores) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Eliminar Proveedor");
        idDialog.setHeaderText("Ingrese el ID del proveedor a eliminar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                proveedoresModule.delete(id, connection);
                actualizarListaProveedores(txtProveedores);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }

    private Dialog<Proveedor> crearDialogoProveedor(String titulo) {
        return crearDialogoProveedor(titulo, null);
    }

    private Dialog<Proveedor> crearDialogoProveedor(String titulo, Proveedor proveedorExistente) {
        Dialog<Proveedor> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese los datos del proveedor:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        TextField txtNombre = new TextField(proveedorExistente != null ? proveedorExistente.getNombre() : "");
        TextField txtTelefono = new TextField(proveedorExistente != null ? proveedorExistente.getTelefono() : "");
        TextField txtEmail = new TextField(proveedorExistente != null ? proveedorExistente.getEmail() : "");
        TextField txtDireccion = new TextField(proveedorExistente != null ? proveedorExistente.getDireccion() : "");

        contenido.getChildren().addAll(
                new Label("Nombre:"), txtNombre,
                new Label("Teléfono:"), txtTelefono,
                new Label("Email:"), txtEmail,
                new Label("Dirección:"), txtDireccion
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                return new Proveedor(
                        txtNombre.getText(),
                        txtTelefono.getText(),
                        txtEmail.getText(),
                        txtDireccion.getText()
                );
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
