package com.repuestos.service;

import com.repuestos.model.DetalleVenta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleVentaService {

    private final DataSource dataSource;

    @Autowired
    public DetalleVentaService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<DetalleVenta> obtenerDetallesPorVenta(Long idVenta) {
        List<DetalleVenta> detalles = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleVentas_ConsultarPorVenta(?, ?)}")) {
            statement.setLong(1, idVenta);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                while (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalle(rs.getLong("id_detalle"));
                    detalle.setIdVenta(rs.getLong("id_venta"));
                    detalle.setIdRepuesto(rs.getLong("id_repuesto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    detalle.setSubtotal(rs.getDouble("subtotal")); 
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles por venta: " + e.getMessage(), e);
        }
        return detalles;
    }

    public void registrarDetalle(DetalleVenta detalle) {
        if (detalle.getIdVenta() == null) {
            throw new IllegalArgumentException("El idVenta no puede ser nulo.");
        }
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleVentas_Registrar(?, ?, ?, ?)}")) {
            statement.setLong(1, detalle.getIdVenta());
            statement.setLong(2, detalle.getIdRepuesto());
            statement.setInt(3, detalle.getCantidad());
            statement.setDouble(4, detalle.getPrecioUnitario());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el detalle de venta: " + e.getMessage(), e);
        }
    }

    public void eliminarDetalle(Long idDetalle) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleVentas_Eliminar(?)}")) {
            statement.setLong(1, idDetalle);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el detalle de venta: " + e.getMessage(), e);
        }
    }
    
    public DetalleVenta obtenerDetallePorId(Long idDetalle) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call DetalleVentas_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, idDetalle);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    DetalleVenta detalle = new DetalleVenta();
                    detalle.setIdDetalle(rs.getLong("id_detalle"));
                    detalle.setIdVenta(rs.getLong("id_venta"));
                    detalle.setIdRepuesto(rs.getLong("id_repuesto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    return detalle;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalle por ID: " + e.getMessage(), e);
        }
        return null;
    }

    public void actualizarDetalle(DetalleVenta detalle) {
        try (Connection connection = dataSource.getConnection();
             CallableStatement statement = connection.prepareCall("{call DetalleVentas_Actualizar(?, ?, ?, ?, ?)}")) {
            statement.setLong(1, detalle.getIdDetalle());
            statement.setLong(2, detalle.getIdVenta());
            statement.setLong(3, detalle.getIdRepuesto());
            statement.setInt(4, detalle.getCantidad());
            statement.setDouble(5, detalle.getPrecioUnitario());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el detalle de venta: " + e.getMessage(), e);
        }
    }
}
