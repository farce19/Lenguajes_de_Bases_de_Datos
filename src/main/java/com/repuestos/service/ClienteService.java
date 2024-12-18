package com.repuestos.service;

import com.repuestos.model.Cliente;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private DataSource dataSource;

   
    public List<Cliente> obtenerTodosLosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call ConsultarClientes(?)}")) {
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            ResultSet rs = (ResultSet) statement.getObject(1);
            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getLong("id_cliente"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setTelefono(rs.getString("telefono"));
                cliente.setEmail(rs.getString("email"));
                cliente.setDireccion(rs.getString("direccion"));
                clientes.add(cliente);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clientes;
    }

   
    public void registrarCliente(Cliente cliente) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Clientes_Registrar(?,?,?,?,?)}")) {
            statement.setString(1, cliente.getNombre());
            statement.setString(2, cliente.getApellido());
            statement.setString(3, cliente.getTelefono());
            statement.setString(4, cliente.getEmail());
            statement.setString(5, cliente.getDireccion());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
    public void actualizarCliente(Cliente cliente) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call ActualizarCliente(?,?,?,?,?,?)}")) {
            statement.setLong(1, cliente.getId());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getApellido());
            statement.setString(4, cliente.getTelefono());
            statement.setString(5, cliente.getEmail());
            statement.setString(6, cliente.getDireccion());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  
    public void eliminarCliente(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID del cliente no es válido para la eliminación.");
        }
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Clientes_Eliminar(?)}")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el cliente con ID: " + id + " - " + e.getMessage(), e);
        }
    }

    
    public Cliente obtenerClientePorId(Long id) {
        Cliente cliente = null;
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call ConsultarClientePorId(?, ?)}")) {
            statement.setLong(1, id);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    cliente = new Cliente();
                    cliente.setId(rs.getLong("id_cliente"));
                    cliente.setNombre(rs.getString("nombre"));
                    cliente.setApellido(rs.getString("apellido"));
                    cliente.setTelefono(rs.getString("telefono"));
                    cliente.setEmail(rs.getString("email"));
                    cliente.setDireccion(rs.getString("direccion"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cliente;
    }
}
