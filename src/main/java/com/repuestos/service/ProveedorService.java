package com.repuestos.service;

import com.repuestos.model.Proveedor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

@Service
public class ProveedorService {

    @Autowired
    private DataSource dataSource;

    
    public List<Proveedor> obtenerTodosLosProveedores() {
        List<Proveedor> proveedores = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Proveedores_Consultar(?)}")) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(1)) {
                while (rs.next()) {
                    Proveedor proveedor = new Proveedor();
                    proveedor.setId(rs.getLong("id_proveedor"));
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setTelefono(rs.getString("telefono"));
                    proveedor.setEmail(rs.getString("email"));
                    proveedor.setDireccion(rs.getString("direccion"));
                    proveedores.add(proveedor);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return proveedores;
    }

    
    public void registrarProveedor(Proveedor proveedor) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Proveedores_Registrar(?,?,?,?)}")) {
            statement.setString(1, proveedor.getNombre());
            statement.setString(2, proveedor.getTelefono());
            statement.setString(3, proveedor.getEmail());
            statement.setString(4, proveedor.getDireccion());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void actualizarProveedor(Proveedor proveedor) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Proveedores_Actualizar(?,?,?,?,?)}")) {
            statement.setLong(1, proveedor.getId());
            statement.setString(2, proveedor.getNombre());
            statement.setString(3, proveedor.getTelefono());
            statement.setString(4, proveedor.getEmail());
            statement.setString(5, proveedor.getDireccion());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void eliminarProveedor(Long id) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Proveedores_Eliminar(?)}")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public Proveedor obtenerProveedorPorId(Long id) {
        Proveedor proveedor = null;
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Proveedores_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, id);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    proveedor = new Proveedor();
                    proveedor.setId(rs.getLong("id_proveedor"));
                    proveedor.setNombre(rs.getString("nombre"));
                    proveedor.setTelefono(rs.getString("telefono"));
                    proveedor.setEmail(rs.getString("email"));
                    proveedor.setDireccion(rs.getString("direccion"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener el proveedor por ID: " + e.getMessage(), e);
        }
        return proveedor;
    }

}
