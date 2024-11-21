package com.mycompany.lenguajes_de_bases_de_datos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VentasModule implements CRUD<Venta> {

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void create(Venta venta, Connection connection) {
        String sql = "INSERT INTO Ventas (id_cliente, fecha_venta, total_venta) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, venta.getIdCliente());
            statement.setString(2, venta.getFechaVenta());
            statement.setDouble(3, venta.getTotalVenta());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al registrar la venta: " + e.getMessage());
        }
    }

    @Override
    public Venta read(int id, Connection connection) {
        String sql = "SELECT * FROM Ventas WHERE id_venta = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Venta(
                    resultSet.getInt("id_venta"),
                    resultSet.getInt("id_cliente"),
                    sdf.format(resultSet.getDate("fecha_venta")),
                    resultSet.getDouble("total_venta")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer la venta: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Venta venta, Connection connection) {
        String sql = "UPDATE Ventas SET id_cliente = ?, fecha_venta = ?, total_venta = ? WHERE id_venta = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, venta.getIdCliente());
            statement.setString(2, venta.getFechaVenta());
            statement.setDouble(3, venta.getTotalVenta());
            statement.setInt(4, venta.getIdVenta());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al actualizar la venta: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Ventas WHERE id_venta = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error al eliminar la venta: " + e.getMessage());
        }
    }

    @Override
    public List<Venta> getAll(Connection connection) {
        List<Venta> ventas = new ArrayList<>();
        String sql = "SELECT * FROM Ventas";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ventas.add(new Venta(
                    resultSet.getInt("id_venta"),
                    resultSet.getInt("id_cliente"),
                    sdf.format(resultSet.getDate("fecha_venta")),
                    resultSet.getDouble("total_venta")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las ventas: " + e.getMessage());
        }
        return ventas;
    }

    @Override
    public Venta crearEntidadInteractiva(Scanner scanner) {
        System.out.println("Ingrese los datos de la venta:");
        System.out.print("ID del Cliente: ");
        int idCliente = scanner.nextInt();
        scanner.nextLine();
        System.out.print("Fecha de Venta (YYYY-MM-DD): ");
        String fechaVenta = scanner.nextLine();
        System.out.print("Total de Venta: ");
        double totalVenta = scanner.nextDouble();

        return new Venta(idCliente, fechaVenta, totalVenta);
    }
}

