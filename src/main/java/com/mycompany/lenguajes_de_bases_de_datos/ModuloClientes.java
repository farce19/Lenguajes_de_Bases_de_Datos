package com.mycompany.lenguajes_de_bases_de_datos;


import java.sql.Connection;
import java.util.List;
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

public class ModuloClientes {

    private final Connection connection;
    private final ClientesModule clientesModule;

    public ModuloClientes(Connection connection) {
        this.connection = connection;
        this.clientesModule = new ClientesModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Clientes");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextArea txtClientes = new TextArea();
        txtClientes.setEditable(false);
        txtClientes.setPrefHeight(200);
        txtClientes.setText(obtenerListaClientes());

        Button btnAgregar = new Button("Agregar Cliente");
        Button btnActualizar = new Button("Actualizar Cliente");
        Button btnEliminar = new Button("Eliminar Cliente");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        // Acción para agregar un cliente
        btnAgregar.setOnAction(e -> {
            Dialog<Cliente> dialog = crearDialogoCliente("Agregar Cliente");
            dialog.showAndWait().ifPresent(cliente -> {
                clientesModule.create(cliente, connection);
                txtClientes.setText(obtenerListaClientes());
            });
        });

        // Acción para actualizar un cliente
        btnActualizar.setOnAction(e -> {
            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setHeaderText("Actualizar Cliente");
            idDialog.setContentText("Ingrese el ID del cliente a actualizar:");
            idDialog.showAndWait().ifPresent(idStr -> {
                try {
                    int id = Integer.parseInt(idStr);
                    Cliente clienteExistente = clientesModule.read(id, connection);
                    if (clienteExistente != null) {
                        Dialog<Cliente> dialog = crearDialogoCliente("Actualizar Cliente", clienteExistente);
                        dialog.showAndWait().ifPresent(clienteActualizado -> {
                            clienteActualizado.setId(id);
                            clientesModule.update(clienteActualizado, connection);
                            txtClientes.setText(obtenerListaClientes());
                        });
                    } else {
                        mostrarAlerta("Cliente no encontrado", "No existe un cliente con el ID proporcionado.");
                    }
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El ID ingresado no es válido.");
                }
            });
        });

        // Acción para eliminar un cliente
        btnEliminar.setOnAction(e -> {
            TextInputDialog idDialog = new TextInputDialog();
            idDialog.setHeaderText("Eliminar Cliente");
            idDialog.setContentText("Ingrese el ID del cliente a eliminar:");
            idDialog.showAndWait().ifPresent(idStr -> {
                try {
                    int id = Integer.parseInt(idStr);
                    clientesModule.delete(id, connection);
                    txtClientes.setText(obtenerListaClientes());
                } catch (NumberFormatException ex) {
                    mostrarAlerta("Error", "El ID ingresado no es válido.");
                }
            });
        });

        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(txtClientes, btnAgregar, btnActualizar, btnEliminar, btnRegresar);

        Scene scene = new Scene(layout, 400, 300);
        stage.setScene(scene);
    }

    private String obtenerListaClientes() {
        StringBuilder lista = new StringBuilder();
        List<Cliente> clientes = clientesModule.getAll(connection);
        if (clientes.isEmpty()) {
            lista.append("No hay clientes registrados.");
        } else {
            for (Cliente cliente : clientes) {
                lista.append(cliente).append("\n");
            }
        }
        return lista.toString();
    }

    private Dialog<Cliente> crearDialogoCliente(String titulo) {
        return crearDialogoCliente(titulo, null);
    }

    private Dialog<Cliente> crearDialogoCliente(String titulo, Cliente clienteExistente) {
        Dialog<Cliente> dialog = new Dialog<>();
        dialog.setTitle(titulo);

        Label lblNombre = new Label("Nombre:");
        TextField txtNombre = new TextField(clienteExistente != null ? clienteExistente.getNombre() : "");

        Label lblApellido = new Label("Apellido:");
        TextField txtApellido = new TextField(clienteExistente != null ? clienteExistente.getApellido() : "");

        Label lblTelefono = new Label("Teléfono:");
        TextField txtTelefono = new TextField(clienteExistente != null ? clienteExistente.getTelefono() : "");

        Label lblEmail = new Label("Email:");
        TextField txtEmail = new TextField(clienteExistente != null ? clienteExistente.getEmail() : "");

        Label lblDireccion = new Label("Dirección:");
        TextField txtDireccion = new TextField(clienteExistente != null ? clienteExistente.getDireccion() : "");

        VBox content = new VBox(10, lblNombre, txtNombre, lblApellido, txtApellido, lblTelefono, txtTelefono, lblEmail, txtEmail, lblDireccion, txtDireccion);
        dialog.getDialogPane().setContent(content);

        ButtonType btnOk = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnOk, ButtonType.CANCEL);

        dialog.setResultConverter(button -> {
            if (button == btnOk) {
                return new Cliente(txtNombre.getText(), txtApellido.getText(), txtTelefono.getText(), txtEmail.getText(), txtDireccion.getText());
            }
            return null;
        });

        return dialog;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }
}

