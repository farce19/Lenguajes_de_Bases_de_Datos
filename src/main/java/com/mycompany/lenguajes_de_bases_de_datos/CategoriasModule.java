package com.mycompany.lenguajes_de_bases_de_datos;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CategoriasModule implements CRUD<Categoria> {

    @Override
    public void create(Categoria categoria, Connection connection) {
        String sql = "INSERT INTO Categorias (nombre) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria.getNombre());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Categoría registrada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar la categoría: " + e.getMessage());
        }
    }

    @Override
    public Categoria read(int id, Connection connection) {
        String sql = "SELECT * FROM Categorias WHERE id_categoria = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Categoria(
                    resultSet.getInt("id_categoria"),
                    resultSet.getString("nombre")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer la categoría: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(Categoria categoria, Connection connection) {
        String sql = "UPDATE Categorias SET nombre = ? WHERE id_categoria = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, categoria.getNombre());
            statement.setInt(2, categoria.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Categoría actualizada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar la categoría: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Categorias WHERE id_categoria = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Categoría eliminada con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar la categoría: " + e.getMessage());
        }
    }

    @Override
    public List<Categoria> getAll(Connection connection) {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT * FROM Categorias";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                categorias.add(new Categoria(
                    resultSet.getInt("id_categoria"),
                    resultSet.getString("nombre")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener las categorías: " + e.getMessage());
        }
        return categorias;
    }
    
    @Override
public Categoria crearEntidadInteractiva(Scanner scanner) {
    scanner.nextLine(); // Consumir la línea pendiente en el buffer
    System.out.print("Ingrese el nombre de la categoría: ");
    String nombre = scanner.nextLine();

    return new Categoria(0, nombre);
}

}
