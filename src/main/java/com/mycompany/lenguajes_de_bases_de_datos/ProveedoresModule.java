package com.mycompany.lenguajes_de_bases_de_datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProveedoresModule implements CRUD<Proveedor> {

    @Override
    public void create(Proveedor proveedor, Connection connection) {
        String sql = "INSERT INTO Proveedores (nombre, telefono, email, direccion) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, proveedor.getNombre());
            statement.setString(2, proveedor.getTelefono());
            statement.setString(3, proveedor.getEmail());
            statement.setString(4, proveedor.getDireccion());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Proveedor registrado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el proveedor: " + e.getMessage());
        }
    }

    @Override
    public Proveedor read(int id, Connection connection) {
        String sql = "SELECT * FROM Proveedores WHERE id_proveedor = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Proveedor(
                    resultSet.getInt("id_proveedor"),
                    resultSet.getString("nombre"),
                    resultSet.getString("telefono"),
                    resultSet.getString("email"),
                    resultSet.getString("direccion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el proveedor: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Proveedor proveedor, Connection connection) {
        String sql = "UPDATE Proveedores SET nombre = ?, telefono = ?, email = ?, direccion = ? WHERE id_proveedor = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, proveedor.getNombre());
            statement.setString(2, proveedor.getTelefono());
            statement.setString(3, proveedor.getEmail());
            statement.setString(4, proveedor.getDireccion());
            statement.setInt(5, proveedor.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Proveedor actualizado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el proveedor: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Proveedores WHERE id_proveedor = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Proveedor eliminado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el proveedor: " + e.getMessage());
        }
    }

    @Override
    public List<Proveedor> getAll(Connection connection) {
        List<Proveedor> proveedores = new ArrayList<>();
        String sql = "SELECT * FROM Proveedores";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                proveedores.add(new Proveedor(
                    resultSet.getInt("id_proveedor"),
                    resultSet.getString("nombre"),
                    resultSet.getString("telefono"),
                    resultSet.getString("email"),
                    resultSet.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los proveedores: " + e.getMessage());
        }
        return proveedores;
    }
    
    @Override
public Proveedor crearEntidadInteractiva(Scanner scanner) {
    scanner.nextLine();
    System.out.print("Ingrese el nombre del proveedor: ");
    String nombre = scanner.nextLine();
    System.out.print("Ingrese el teléfono del proveedor: ");
    String telefono = scanner.nextLine();
    System.out.print("Ingrese el email del proveedor: ");
    String email = scanner.nextLine();
    System.out.print("Ingrese la dirección del proveedor: ");
    String direccion = scanner.nextLine();

    return new Proveedor(0, nombre, telefono, email, direccion);
}

}
