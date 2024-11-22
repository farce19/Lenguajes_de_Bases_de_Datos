package com.mycompany.lenguajes_de_bases_de_datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetalleVentasModule implements CRUD<DetalleVenta> {

    @Override
    public void create(DetalleVenta detalleVenta, Connection connection) {
        String sql = "INSERT INTO Detalle_Ventas (id_venta, id_repuesto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, detalleVenta.getIdVenta());
            statement.setInt(2, detalleVenta.getIdRepuesto());
            statement.setInt(3, detalleVenta.getCantidad());
            statement.setDouble(4, detalleVenta.getPrecioUnitario());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Detalle de venta registrado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el detalle de venta: " + e.getMessage());
        }
    }

    @Override
    public DetalleVenta read(int id, Connection connection) {
        String sql = "SELECT * FROM Detalle_Ventas WHERE id_detalle = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new DetalleVenta(
                    resultSet.getInt("id_detalle"),
                    resultSet.getInt("id_venta"),
                    resultSet.getInt("id_repuesto"),
                    resultSet.getInt("cantidad"),
                    resultSet.getDouble("precio_unitario"),
                    resultSet.getDouble("cantidad") * resultSet.getDouble("precio_unitario")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el detalle de venta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(DetalleVenta detalleVenta, Connection connection) {
        String sql = "UPDATE Detalle_Ventas SET id_venta = ?, id_repuesto = ?, cantidad = ?, precio_unitario = ? WHERE id_detalle = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, detalleVenta.getIdVenta());
            statement.setInt(2, detalleVenta.getIdRepuesto());
            statement.setInt(3, detalleVenta.getCantidad());
            statement.setDouble(4, detalleVenta.getPrecioUnitario());
            statement.setInt(5, detalleVenta.getIdDetalle());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Detalle de venta actualizado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el detalle de venta: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Detalle_Ventas WHERE id_detalle = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Detalle de venta eliminado correctamente.");
        } catch (SQLException e) {
            System.err.println("Error al eliminar el detalle de venta: " + e.getMessage());
        }
    }

    @Override
    public List<DetalleVenta> getAll(Connection connection) {
        List<DetalleVenta> detalles = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Ventas";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                detalles.add(new DetalleVenta(
                    resultSet.getInt("id_detalle"),
                    resultSet.getInt("id_venta"),
                    resultSet.getInt("id_repuesto"),
                    resultSet.getInt("cantidad"),
                    resultSet.getDouble("precio_unitario"),
                    resultSet.getDouble("cantidad") * resultSet.getDouble("precio_unitario")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los detalles de ventas: " + e.getMessage());
        }
        return detalles;
    }

    @Override
    public DetalleVenta crearEntidadInteractiva(Scanner scanner) {
        System.out.print("Ingrese el ID de la venta: ");
        int idVenta = scanner.nextInt();
        System.out.print("Ingrese el ID del repuesto: ");
        int idRepuesto = scanner.nextInt();
        System.out.print("Ingrese la cantidad: ");
        int cantidad = scanner.nextInt();
        System.out.print("Ingrese el precio unitario: ");
        double precioUnitario = scanner.nextDouble();

        return new DetalleVenta(idVenta, idRepuesto, cantidad, precioUnitario, cantidad * precioUnitario);
    }
}
