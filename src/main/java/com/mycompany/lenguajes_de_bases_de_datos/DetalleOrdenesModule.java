package com.mycompany.lenguajes_de_bases_de_datos;



import com.mycompany.lenguajes_de_bases_de_datos.DetalleOrden;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DetalleOrdenesModule implements CRUD<DetalleOrden> {

    @Override
    public void create(DetalleOrden detalleOrden, Connection connection) {
        String sql = "INSERT INTO Detalle_Ordenes (id_orden, id_repuesto, cantidad, precio_unitario) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, detalleOrden.getIdOrden());
            statement.setInt(2, detalleOrden.getIdRepuesto());
            statement.setInt(3, detalleOrden.getCantidad());
            statement.setDouble(4, detalleOrden.getPrecioUnitario());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Detalle de orden registrado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar el detalle de orden: " + e.getMessage());
        }
    }

    @Override
    public DetalleOrden read(int id, Connection connection) {
        String sql = "SELECT * FROM Detalle_Ordenes WHERE id_detalle_orden = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new DetalleOrden(
                    resultSet.getInt("id_detalle_orden"),
                    resultSet.getInt("id_orden"),
                    resultSet.getInt("id_repuesto"),
                    resultSet.getInt("cantidad"),
                    resultSet.getDouble("precio_unitario")
                );
            }
        } catch (SQLException e) {
            System.err.println("Error al leer el detalle de orden: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void update(DetalleOrden detalleOrden, Connection connection) {
        String sql = "UPDATE Detalle_Ordenes SET id_orden = ?, id_repuesto = ?, cantidad = ?, precio_unitario = ? WHERE id_detalle_orden = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, detalleOrden.getIdOrden());
            statement.setInt(2, detalleOrden.getIdRepuesto());
            statement.setInt(3, detalleOrden.getCantidad());
            statement.setDouble(4, detalleOrden.getPrecioUnitario());
            statement.setInt(5, detalleOrden.getIdDetalleOrden());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Detalle de orden actualizado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al actualizar el detalle de orden: " + e.getMessage());
        }
    }

    @Override
    public void delete(int id, Connection connection) {
        String sql = "DELETE FROM Detalle_Ordenes WHERE id_detalle_orden = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Detalle de orden eliminado con éxito.");
            }
        } catch (SQLException e) {
            System.err.println("Error al eliminar el detalle de orden: " + e.getMessage());
        }
    }

    @Override
    public List<DetalleOrden> getAll(Connection connection) {
        List<DetalleOrden> detalles = new ArrayList<>();
        String sql = "SELECT * FROM Detalle_Ordenes";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                detalles.add(new DetalleOrden(
                    resultSet.getInt("id_detalle_orden"),
                    resultSet.getInt("id_orden"),
                    resultSet.getInt("id_repuesto"),
                    resultSet.getInt("cantidad"),
                    resultSet.getDouble("precio_unitario")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener los detalles de órdenes: " + e.getMessage());
        }
        return detalles;
    }
    @Override
public DetalleOrden crearEntidadInteractiva(Scanner scanner) {
    scanner.nextLine();
    System.out.print("Ingrese el ID de la orden: ");
    int idOrden = scanner.nextInt();
    System.out.print("Ingrese el ID del repuesto: ");
    int idRepuesto = scanner.nextInt();
    System.out.print("Ingrese la cantidad: ");
    int cantidad = scanner.nextInt();
    System.out.print("Ingrese el precio unitario: ");
    double precioUnitario = scanner.nextDouble();

    return new DetalleOrden(0, idOrden, idRepuesto, cantidad, precioUnitario);
}

}

