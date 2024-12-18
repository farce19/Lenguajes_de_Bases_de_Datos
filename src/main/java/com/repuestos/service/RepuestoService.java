package com.repuestos.service;

import com.repuestos.model.Repuesto;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

@Service
public class RepuestoService {

    private final DataSource dataSource;

    public RepuestoService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Repuesto> obtenerTodosLosRepuestos() {
        List<Repuesto> repuestos = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Repuestos_Consultar(?)}")) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(1)) {
                while (rs.next()) {
                    Repuesto repuesto = new Repuesto();
                    repuesto.setId(rs.getLong("id_repuesto"));
                    repuesto.setNombre(rs.getString("nombre"));
                    repuesto.setMarca(rs.getString("marca"));
                    repuesto.setPrecio(rs.getDouble("precio"));
                    repuesto.setStock(rs.getInt("stock"));
                    repuesto.setNombreProveedor(rs.getString("nombre_proveedor")); // Alias exacto
                    repuesto.setNombreCategoria(rs.getString("nombre_categoria")); // Alias exacto
                    repuestos.add(repuesto);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return repuestos;
    }

    public void registrarRepuesto(Repuesto repuesto) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Repuestos_Registrar(?, ?, ?, ?, ?, ?)}")) {
            statement.setString(1, repuesto.getNombre());
            statement.setString(2, repuesto.getMarca());
            statement.setDouble(3, repuesto.getPrecio());
            statement.setObject(4, repuesto.getStock());
            statement.setLong(5, repuesto.getIdProveedor());
            statement.setLong(6, repuesto.getIdCategoria());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarRepuesto(Repuesto repuesto) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Repuestos_Actualizar(?, ?, ?, ?, ?, ?, ?)}")) {
            statement.setLong(1, repuesto.getId());
            statement.setString(2, repuesto.getNombre());
            statement.setString(3, repuesto.getMarca());
            statement.setDouble(4, repuesto.getPrecio());
            statement.setObject(5, repuesto.getStock());
            statement.setLong(6, repuesto.getIdProveedor());
            statement.setLong(7, repuesto.getIdCategoria());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarRepuesto(Long id) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Repuestos_Eliminar(?)}")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Repuesto obtenerRepuestoPorId(Long id) {
        Repuesto repuesto = null;
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Repuestos_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, id);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    repuesto = new Repuesto();
                    repuesto.setId(rs.getLong("id_repuesto"));
                    repuesto.setNombre(rs.getString("nombre"));
                    repuesto.setMarca(rs.getString("marca"));
                    repuesto.setPrecio(rs.getDouble("precio"));
                    repuesto.setStock(rs.getInt("stock"));
                    repuesto.setIdProveedor(rs.getLong("id_proveedor"));
                    repuesto.setIdCategoria(rs.getLong("id_categoria"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return repuesto;
    }

}
