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

public class ModuloVentas {

    private final Connection connection;
    private final VentasModule ventasModule;

    public ModuloVentas(Connection connection) {
        this.connection = connection;
        this.ventasModule = new VentasModule();
    }

    public void mostrarVentana(Stage stage) {
        stage.setTitle("Gestión de Ventas");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        TextArea txtVentas = new TextArea();
        txtVentas.setEditable(false);
        actualizarListaVentas(txtVentas);

        Button btnAgregar = new Button("Agregar Venta");
        Button btnActualizar = new Button("Actualizar Venta");
        Button btnEliminar = new Button("Eliminar Venta");
        Button btnRegresar = new Button("Regresar al Menú Principal");

        btnAgregar.setOnAction(e -> agregarVenta(txtVentas));
        btnActualizar.setOnAction(e -> actualizarVenta(txtVentas));
        btnEliminar.setOnAction(e -> eliminarVenta(txtVentas));
        btnRegresar.setOnAction(e -> regresarAlMenu(stage));

        layout.getChildren().addAll(
                new Label("Lista de Ventas:"), txtVentas,
                btnAgregar, btnActualizar, btnEliminar, btnRegresar
        );

        Scene scene = new Scene(layout, 600, 500);
        stage.setScene(scene);
        stage.show();
    }

    private void actualizarListaVentas(TextArea txtVentas) {
        txtVentas.clear();
        ventasModule.getAll(connection).forEach(venta -> txtVentas.appendText(venta + "\n"));
    }

    private void agregarVenta(TextArea txtVentas) {
        Dialog<Venta> dialog = crearDialogoVenta("Agregar Venta");
        dialog.showAndWait().ifPresent(venta -> {
            ventasModule.create(venta, connection);
            actualizarListaVentas(txtVentas);
        });
    }

    private void actualizarVenta(TextArea txtVentas) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Actualizar Venta");
        idDialog.setHeaderText("Ingrese el ID de la venta a actualizar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                Venta ventaExistente = ventasModule.read(id, connection);
                if (ventaExistente != null) {
                    Dialog<Venta> dialog = crearDialogoVenta("Actualizar Venta", ventaExistente);
                    dialog.showAndWait().ifPresent(venta -> {
                        venta.setIdVenta(id);
                        ventasModule.update(venta, connection);
                        actualizarListaVentas(txtVentas);
                    });
                } else {
                    mostrarAlerta("Venta no encontrada", "No existe una venta con el ID proporcionado.");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void eliminarVenta(TextArea txtVentas) {
        TextInputDialog idDialog = new TextInputDialog();
        idDialog.setTitle("Eliminar Venta");
        idDialog.setHeaderText("Ingrese el ID de la venta a eliminar");
        idDialog.setContentText("ID:");
        idDialog.showAndWait().ifPresent(idStr -> {
            try {
                int id = Integer.parseInt(idStr);
                ventasModule.delete(id, connection);
                actualizarListaVentas(txtVentas);
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "El ID debe ser un número.");
            }
        });
    }

    private void regresarAlMenu(Stage stage) {
        new MainApp().start(stage);
    }

    private Dialog<Venta> crearDialogoVenta(String titulo) {
        return crearDialogoVenta(titulo, null);
    }

    private Dialog<Venta> crearDialogoVenta(String titulo, Venta ventaExistente) {
        Dialog<Venta> dialog = new Dialog<>();
        dialog.setTitle(titulo);
        dialog.setHeaderText("Ingrese los datos de la venta:");

        ButtonType btnGuardar = new ButtonType("Guardar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(btnGuardar, ButtonType.CANCEL);

        VBox contenido = new VBox(10);
        contenido.setPadding(new Insets(10));

        TextField txtIdCliente = new TextField(ventaExistente != null ? String.valueOf(ventaExistente.getIdCliente()) : "");
        TextField txtFecha = new TextField(ventaExistente != null ? ventaExistente.getFechaVenta() : "");
        TextField txtTotal = new TextField(ventaExistente != null ? String.valueOf(ventaExistente.getTotalVenta()) : "");

        contenido.getChildren().addAll(
                new Label("ID Cliente:"), txtIdCliente,
                new Label("Fecha (YYYY-MM-DD):"), txtFecha,
                new Label("Total Venta:"), txtTotal
        );

        dialog.getDialogPane().setContent(contenido);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == btnGuardar) {
                try {
                    return new Venta(
                            Integer.parseInt(txtIdCliente.getText()),
                            txtFecha.getText(),
                            Double.parseDouble(txtTotal.getText())
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
