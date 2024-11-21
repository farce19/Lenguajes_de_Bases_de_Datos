/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.lenguajes_de_bases_de_datos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import static javafx.application.Application.launch;

public class MainApp extends Application {

    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Admin";

    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            if (connection != null) {
                System.out.println("Conexión a la base de datos establecida exitosamente.");
            }
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return; 
        }

        
        primaryStage.setTitle("Sistema de Gestión de Repuestos Mecánicos");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        
        Button btnClientes = new Button("Gestionar Clientes");
        Button btnProveedores = new Button("Gestionar Proveedores");
        Button btnRepuestos = new Button("Gestionar Repuestos");
        Button btnVentas = new Button("Gestionar Ventas");
        Button btnDetalleVentas = new Button("Gestionar Detalle de Ventas");
        Button btnOrdenesCompra = new Button("Gestionar Órdenes de Compra");
        Button btnDetalleOrdenes = new Button("Gestionar Detalle de Órdenes");
        Button btnSalir = new Button("Salir");

        //Acciones de botones
        btnClientes.setOnAction(e -> abrirModuloClientes(primaryStage));
        btnProveedores.setOnAction(e -> abrirModuloProveedores(primaryStage));
        btnRepuestos.setOnAction(e -> abrirModuloRepuestos(primaryStage));
        btnVentas.setOnAction(e -> abrirModuloVentas(primaryStage));
        btnDetalleVentas.setOnAction(e -> abrirModuloDetalleVentas(primaryStage));
        btnOrdenesCompra.setOnAction(e -> abrirModuloOrdenesCompra(primaryStage));
        btnDetalleOrdenes.setOnAction(e -> abrirModuloDetalleOrdenes(primaryStage));
        btnSalir.setOnAction(e -> cerrarAplicacion());

        // Agregar botones al GridPane
        grid.add(btnClientes, 0, 0);
        grid.add(btnProveedores, 1, 0);
        grid.add(btnRepuestos, 0, 1);
        grid.add(btnVentas, 1, 1);
        grid.add(btnDetalleVentas, 0, 2);
        grid.add(btnOrdenesCompra, 1, 2);
        grid.add(btnDetalleOrdenes, 0, 3);
        grid.add(btnSalir, 1, 3);

        // Crear la escena y mostrarla
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void abrirModuloClientes(Stage stage) {
        new ModuloClientes(connection).mostrarVentana(stage);
    }

    private void abrirModuloProveedores(Stage stage) {
        new ModuloProveedores(connection).mostrarVentana(stage);
    }

    private void abrirModuloRepuestos(Stage stage) {
        new ModuloRepuestos(connection).mostrarVentana(stage);
    }

    private void abrirModuloVentas(Stage stage) {
        new ModuloVentas(connection).mostrarVentana(stage);
    }

    private void abrirModuloDetalleVentas(Stage stage) {
        new ModuloDetalleVentas(connection).mostrarVentana(stage);
    }

    private void abrirModuloOrdenesCompra(Stage stage) {
        new ModuloOrdenesCompra(connection).mostrarVentana(stage);
    }

    private void abrirModuloDetalleOrdenes(Stage stage) {
        new ModuloDetalleOrdenes(connection).mostrarVentana(stage);
    }

    private void cerrarAplicacion() {
        System.out.println("Cerrando aplicación...");
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}

