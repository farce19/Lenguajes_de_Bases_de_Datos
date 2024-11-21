package com.mycompany.lenguajes_de_bases_de_datos;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RepuestosModule implements CRUD<Repuesto> {

    @Override
    public void create(Repuesto repuesto, Connection connection) {
        String sql = "INSERT INTO Repuestos (nombre, marca, precio, stock, id_proveedor, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, repuesto.getNombre());
            statement.setString(2, repuesto.getMarca());
            statement.setDouble(3, repuesto.getPrecio());
            statement.setInt(4, repuesto.getStock());
            statement.setInt(5, repuesto.getIdProveedor());
            statement.setInt(6, repuesto.getIdCategoria());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Repuesto registrado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el repuesto: " + e.getMessage());
        }
    }

    @Override
    public Repuesto read(int id, Connection connection) {
        String sql = "SELECT * FROM Repuestos WHERE id_repuesto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Repuesto(
                    resultSet.getInt("id_repuesto"),
                    resultSet.getString("nombre"),
                    resultSet.getString("marca"),
                    resultSet.getDouble("precio"),
                    resultSet.getInt("stock"),
                    resultSet.getInt("id_proveedor"),
                    resultSet.getInt("id_categoria")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el repuesto: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Repuesto repuesto, Connection connection) {
        String sql = "UPDATE Repuestos SET nombre = ?, marca = ?, precio = ?, stock = ?, id_proveedor = ?, id_categoria = ? WHERE id_repuesto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, repuesto.getNombre());
            statement.setString(2, repuesto.getMarca());
            statement.setDouble(3, repuesto.getPrecio());
            statement.setInt(4, repuesto.getStock());
            statement.setInt(5, repuesto.getIdProveedor());
            statement.setInt(6, repuesto.getIdCategoria());
            statement.setInt(7, repuesto.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Repuesto actualizado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el repuesto: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Repuestos WHERE id_repuesto = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Repuesto eliminado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el repuesto: " + e.getMessage());
        }
    }

    @Override
    public List<Repuesto> getAll(Connection connection) {
        List<Repuesto> repuestos = new ArrayList<>();
        String sql = "SELECT * FROM Repuestos";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                repuestos.add(new Repuesto(
                    resultSet.getInt("id_repuesto"),
                    resultSet.getString("nombre"),
                    resultSet.getString("marca"),
                    resultSet.getDouble("precio"),
                    resultSet.getInt("stock"),
                    resultSet.getInt("id_proveedor"),
                    resultSet.getInt("id_categoria")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los repuestos: " + e.getMessage());
        }
        return repuestos;
    }
    
    @Override
public Repuesto crearEntidadInteractiva(Scanner scanner) {
    scanner.nextLine(); 
    System.out.print("Ingrese el nombre del repuesto: ");
    String nombre = scanner.nextLine();
    System.out.print("Ingrese la marca del repuesto: ");
    String marca = scanner.nextLine();
    System.out.print("Ingrese el precio del repuesto: ");
    double precio = scanner.nextDouble();
    System.out.print("Ingrese el stock disponible: ");
    int stock = scanner.nextInt();
    System.out.print("Ingrese el ID del proveedor: ");
    int idProveedor = scanner.nextInt();
    System.out.print("Ingrese el ID de la categoría: ");
    int idCategoria = scanner.nextInt();

    return new Repuesto(0, nombre, marca, precio, stock, idProveedor, idCategoria);
}

}

