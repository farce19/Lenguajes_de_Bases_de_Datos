package com.mycompany.lenguajes_de_bases_de_datos;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ClientesModule implements CRUD<Cliente> {

    @Override
    public void create(Cliente cliente, Connection connection) {
        String sql = "INSERT INTO Clientes (nombre, apellido, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getApellido());
            statement.setString(3, cliente.getTelefono());
            statement.setString(4, cliente.getEmail());
            statement.setString(5, cliente.getDireccion());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Cliente registrado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el cliente: " + e.getMessage());
        }
    }

    @Override
    public Cliente read(int id, Connection connection) {
        String sql = "SELECT * FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Cliente(
                    resultSet.getInt("id_cliente"),
                    resultSet.getString("nombre"),
                    resultSet.getString("apellido"),
                    resultSet.getString("telefono"),
                    resultSet.getString("email"),
                    resultSet.getString("direccion")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el cliente: " + e.getMessage());
        }
        return null;
    }

    @Override
public void update(Cliente cliente, Connection connection) {
    String sql = "UPDATE Clientes SET nombre = ?, apellido = ?, telefono = ?, email = ?, direccion = ? WHERE id_cliente = ?";
    try (PreparedStatement statement = connection.prepareStatement(sql)) {
        statement.setString(1, cliente.getNombre());
        statement.setString(2, cliente.getApellido());
        statement.setString(3, cliente.getTelefono());
        statement.setString(4, cliente.getEmail());
        statement.setString(5, cliente.getDireccion());
        statement.setInt(6, cliente.getId()); // Enlace del ID

        int rowsUpdated = statement.executeUpdate();
        if (rowsUpdated > 0) {
            System.out.println("Cliente actualizado con éxito.");
        } else {
            System.out.println("No se encontró el cliente con el ID proporcionado.");
        }
    } catch (SQLException e) {
        System.err.println("Error al actualizar el cliente: " + e.getMessage());
    }
}



    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Clientes WHERE id_cliente = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Cliente eliminado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el cliente: " + e.getMessage());
        }
    }

    @Override
    public List<Cliente> getAll(Connection connection) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM Clientes";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                clientes.add(new Cliente(
                    resultSet.getInt("id_cliente"),
                    resultSet.getString("nombre"),
                    resultSet.getString("apellido"),
                    resultSet.getString("telefono"),
                    resultSet.getString("email"),
                    resultSet.getString("direccion")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los clientes: " + e.getMessage());
        }
        return clientes;
    }

   
    public void createCliente(Connection connection) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el nombre del cliente: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese el apellido del cliente: ");
        String apellido = scanner.nextLine();
        System.out.print("Ingrese el teléfono del cliente: ");
        String telefono = scanner.nextLine();
        System.out.print("Ingrese el email del cliente: ");
        String email = scanner.nextLine();
        System.out.print("Ingrese la dirección del cliente: ");
        String direccion = scanner.nextLine();

        String sql = "INSERT INTO Clientes (nombre, apellido, telefono, email, direccion) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, nombre);
            statement.setString(2, apellido);
            statement.setString(3, telefono);
            statement.setString(4, email);
            statement.setString(5, direccion);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Cliente registrado exitosamente.");
            } else {
                System.out.println("No se pudo registrar el cliente.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el cliente: " + e.getMessage());
        }
    }
    
    @Override
public Cliente crearEntidadInteractiva(Scanner scanner) {
    scanner.nextLine(); 
    System.out.print("Ingrese el nombre del cliente: ");
    String nombre = scanner.nextLine();
    System.out.print("Ingrese el apellido del cliente: ");
    String apellido = scanner.nextLine();
    System.out.print("Ingrese el teléfono del cliente: ");
    String telefono = scanner.nextLine();
    System.out.print("Ingrese el email del cliente: ");
    String email = scanner.nextLine();
    System.out.print("Ingrese la dirección del cliente: ");
    String direccion = scanner.nextLine();

    return new Cliente(0, nombre, apellido, telefono, email, direccion);
}

}
