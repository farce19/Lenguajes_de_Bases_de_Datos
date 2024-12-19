package com.repuestos.service;

import com.repuestos.model.OrdenCompra;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrdenCompraService {

    private final DataSource dataSource;

    public OrdenCompraService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<OrdenCompra> obtenerTodasLasOrdenes() {
        List<OrdenCompra> ordenes = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call OrdenesCompra_Consultar(?)}")) {
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(1)) {
                while (rs.next()) {
                    OrdenCompra orden = mapOrdenCompraFromResultSet(rs);
                    ordenes.add(orden);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las órdenes de compra: " + e.getMessage(), e);
        }
        return ordenes;
    }

    public OrdenCompra obtenerOrdenPorId(Long id) {
        OrdenCompra orden = null;
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call OrdenesCompra_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, id);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    orden = mapOrdenCompraFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la orden con ID " + id + ": " + e.getMessage(), e);
        }
        return orden;
    }

    public void registrarOrden(OrdenCompra ordenCompra) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call OrdenesCompra_Registrar(?, ?, ?)}")) {
            statement.setLong(1, ordenCompra.getIdProveedor());
            statement.setDate(2, new java.sql.Date(ordenCompra.getFechaOrden().getTime()));
            statement.setString(3, ordenCompra.getEstadoOrden());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la orden de compra: " + e.getMessage(), e);
        }
    }

    public void actualizarOrden(OrdenCompra ordenCompra) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call OrdenCompra_Actualizar(?, ?, ?, ?)}")) {
            statement.setLong(1, ordenCompra.getId());
            statement.setLong(2, ordenCompra.getIdProveedor());
            statement.setDate(3, new java.sql.Date(ordenCompra.getFechaOrden().getTime()));
            statement.setString(4, ordenCompra.getEstadoOrden());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la orden de compra: " + e.getMessage(), e);
        }
    }

    public void eliminarOrden(Long id) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call OrdenCompra_Eliminar(?)}")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la orden de compra: " + e.getMessage(), e);
        }
    }

    private OrdenCompra mapOrdenCompraFromResultSet(ResultSet rs) throws SQLException {
        OrdenCompra orden = new OrdenCompra();
        orden.setId(rs.getLong("id_orden"));
        orden.setIdProveedor(rs.getLong("id_proveedor"));
        orden.setFechaOrden(rs.getDate("fecha_orden"));
        orden.setEstadoOrden(rs.getString("estado_orden"));
        return orden;
    }
}
