package com.mycompany.lenguajes_de_bases_de_datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class OrdenCompraModule implements CRUD<OrdenCompra> {

    @Override
    public void create(OrdenCompra ordenCompra, Connection connection) {
        String sql = "INSERT INTO Orden_Compra (id_proveedor, fecha_orden, estado_orden) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ordenCompra.getIdProveedor());
            statement.setDate(2, new java.sql.Date(ordenCompra.getFechaOrden().getTime()));
            statement.setString(3, ordenCompra.getEstadoOrden());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Orden de compra registrada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar la orden de compra: " + e.getMessage());
        }
    }

    @Override
    public OrdenCompra read(int id, Connection connection) {
        String sql = "SELECT * FROM Orden_Compra WHERE id_orden = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new OrdenCompra(
                    resultSet.getInt("id_orden"),
                    resultSet.getInt("id_proveedor"),
                    resultSet.getDate("fecha_orden"),
                    resultSet.getString("estado_orden")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer la orden de compra: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(OrdenCompra ordenCompra, Connection connection) {
        String sql = "UPDATE Orden_Compra SET id_proveedor = ?, fecha_orden = ?, estado_orden = ? WHERE id_orden = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, ordenCompra.getIdProveedor());
            statement.setDate(2, new java.sql.Date(ordenCompra.getFechaOrden().getTime()));
            statement.setString(3, ordenCompra.getEstadoOrden());
            statement.setInt(4, ordenCompra.getIdOrden());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Orden de compra actualizada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la orden de compra: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Orden_Compra WHERE id_orden = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Orden de compra eliminada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar la orden de compra: " + e.getMessage());
        }
    }

    @Override
    public List<OrdenCompra> getAll(Connection connection) {
        List<OrdenCompra> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM Orden_Compra";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ordenes.add(new OrdenCompra(
                    resultSet.getInt("id_orden"),
                    resultSet.getInt("id_proveedor"),
                    resultSet.getDate("fecha_orden"),
                    resultSet.getString("estado_orden")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las órdenes de compra: " + e.getMessage());
        }
        return ordenes;
    }
    
    @Override
public OrdenCompra crearEntidadInteractiva(Scanner scanner) {
    scanner.nextLine(); 
    System.out.print("Ingrese el ID del proveedor: ");
    int idProveedor = scanner.nextInt();

    scanner.nextLine();
    System.out.print("Ingrese la fecha de la orden (YYYY-MM-DD): ");
    String fechaOrdenStr = scanner.nextLine();

    System.out.print("Ingrese el estado de la orden (Pendiente, Completada, Cancelada): ");
    String estadoOrden = scanner.nextLine();

    // Conversión de la fecha
    Date fechaOrden = null;
    try {
        fechaOrden = java.sql.Date.valueOf(fechaOrdenStr); 
    } catch (IllegalArgumentException e) {
        System.err.println("Formato de fecha inválido. Use el formato YYYY-MM-DD.");
    }

    return new OrdenCompra(0, idProveedor, fechaOrden, estadoOrden);
}


}
