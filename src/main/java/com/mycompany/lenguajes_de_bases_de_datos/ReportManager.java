package com.mycompany.lenguajes_de_bases_de_datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportManager {

    public ReportManager() {
    }

    public void generateReports(Connection connection) {
        int option;
        do {
            UIHelper.clearConsole();
            UIHelper.showHeader("Generación de Reportes");
            UIHelper.showOptions(new String[]{
                "Reporte de Ventas",
                "Reporte de Inventario",
                "Reporte de Clientes",
                "Reporte de Proveedores",
                "Reporte de Productos Más Vendidos",
                "Regresar al Menú Principal"
            });
            option = UIHelper.getValidatedIntInput("Seleccione una opción: ");
            handleOption(option, connection);
        } while (option != 6);
    }

    private void handleOption(int option, Connection connection) {
        switch (option) {
            case 1 -> generateVentasReport(connection);
            case 2 -> generateInventarioReport(connection);
            case 3 -> generateClientesReport(connection);
            case 4 -> generateProveedoresReport(connection);
            case 5 -> generateTopProductosReport(connection);
            case 6 -> UIHelper.showInfo("Regresando al Menú Principal...");
            default -> UIHelper.showError("Opción inválida. Por favor, intente de nuevo.");
        }
    }

    private void generateVentasReport(Connection connection) {
        UIHelper.showHeader("Reporte de Ventas");
        String sql = "SELECT id_venta, id_cliente, fecha_venta, total_venta FROM ventas ORDER BY fecha_venta DESC";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-10s %-10s %-15s %-10s%n", "ID Venta", "ID Cliente", "Fecha", "Total");
            while (resultSet.next()) {
                int idVenta = resultSet.getInt("id_venta");
                int idCliente = resultSet.getInt("id_cliente");
                String fechaVenta = resultSet.getString("fecha_venta");
                double totalVenta = resultSet.getDouble("total_venta");
                System.out.printf("%-10d %-10d %-15s %-10.2f%n", idVenta, idCliente, fechaVenta, totalVenta);
            }
        } catch (SQLException e) {
            UIHelper.showError("Error al generar el reporte de ventas: " + e.getMessage());
        }
    }

    private void generateInventarioReport(Connection connection) {
        UIHelper.showHeader("Reporte de Inventario");
        String sql = "SELECT id_repuesto, nombre, marca, precio, stock FROM repuestos ORDER BY stock ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-10s %-20s %-15s %-10s %-10s%n", "ID", "Nombre", "Marca", "Precio", "Stock");
            while (resultSet.next()) {
                int idRepuesto = resultSet.getInt("id_repuesto");
                String nombre = resultSet.getString("nombre");
                String marca = resultSet.getString("marca");
                double precio = resultSet.getDouble("precio");
                int stock = resultSet.getInt("stock");
                System.out.printf("%-10d %-20s %-15s %-10.2f %-10d%n", idRepuesto, nombre, marca, precio, stock);
            }
        } catch (SQLException e) {
            UIHelper.showError("Error al generar el reporte de inventario: " + e.getMessage());
        }
    }

    private void generateClientesReport(Connection connection) {
        UIHelper.showHeader("Reporte de Clientes");
        String sql = "SELECT id_cliente, nombre, apellido, telefono, email FROM clientes ORDER BY nombre ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-10s %-20s %-20s %-15s %-25s%n", "ID", "Nombre", "Apellido", "Teléfono", "Email");
            while (resultSet.next()) {
                int idCliente = resultSet.getInt("id_cliente");
                String nombre = resultSet.getString("nombre");
                String apellido = resultSet.getString("apellido");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                System.out.printf("%-10d %-20s %-20s %-15s %-25s%n", idCliente, nombre, apellido, telefono, email);
            }
        } catch (SQLException e) {
            UIHelper.showError("Error al generar el reporte de clientes: " + e.getMessage());
        }
    }

    private void generateProveedoresReport(Connection connection) {
        UIHelper.showHeader("Reporte de Proveedores");
        String sql = "SELECT id_proveedor, nombre, telefono, email, direccion FROM proveedores ORDER BY nombre ASC";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-10s %-20s %-15s %-25s %-30s%n", "ID", "Nombre", "Teléfono", "Email", "Dirección");
            while (resultSet.next()) {
                int idProveedor = resultSet.getInt("id_proveedor");
                String nombre = resultSet.getString("nombre");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String direccion = resultSet.getString("direccion");
                System.out.printf("%-10d %-20s %-15s %-25s %-30s%n", idProveedor, nombre, telefono, email, direccion);
            }
        } catch (SQLException e) {
            UIHelper.showError("Error al generar el reporte de proveedores: " + e.getMessage());
        }
    }

    private void generateTopProductosReport(Connection connection) {
        UIHelper.showHeader("Reporte de Productos Más Vendidos");
        String sql = """
                     SELECT r.nombre AS producto, SUM(dv.cantidad) AS total_vendidos
                     FROM repuestos r
                     INNER JOIN detalle_ventas dv ON r.id_repuesto = dv.id_repuesto
                     GROUP BY r.nombre
                     ORDER BY total_vendidos DESC
                     FETCH FIRST 10 ROWS ONLY
                     """;
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {
            System.out.printf("%-30s %-15s%n", "Producto", "Total Vendidos");
            while (resultSet.next()) {
                String producto = resultSet.getString("producto");
                int totalVendidos = resultSet.getInt("total_vendidos");
                System.out.printf("%-30s %-15d%n", producto, totalVendidos);
            }
        } catch (SQLException e) {
            UIHelper.showError("Error al generar el reporte de productos más vendidos: " + e.getMessage());
        }
    }
}
