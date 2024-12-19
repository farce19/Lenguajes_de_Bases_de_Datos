package com.repuestos.service;

import com.repuestos.model.DetalleOrden;
import com.repuestos.repository.DetalleOrdenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DetalleOrdenService {

    private final DataSource dataSource;
    private final DetalleOrdenRepository detalleOrdenRepository;

    @Autowired
    public DetalleOrdenService(DataSource dataSource, DetalleOrdenRepository detalleOrdenRepository) {
        this.dataSource = dataSource;
        this.detalleOrdenRepository = detalleOrdenRepository;
    }

    public List<DetalleOrden> obtenerDetallesPorOrden(Long idOrden) {
        List<DetalleOrden> detalles = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleOrdenes_ConsultarPorOrden(?, ?)}")) {
            statement.setLong(1, idOrden);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                while (rs.next()) {
                    DetalleOrden detalle = new DetalleOrden();
                    detalle.setIdDetalleOrden(rs.getLong("id_detalle_orden"));
                    detalle.setIdOrden(rs.getLong("id_orden"));
                    detalle.setIdRepuesto(rs.getLong("id_repuesto"));
                    detalle.setCantidad(rs.getInt("cantidad"));
                    detalle.setPrecioUnitario(rs.getDouble("precio_unitario"));
                    detalle.setSubtotal(rs.getDouble("subtotal"));
                    detalles.add(detalle);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener detalles por orden: " + e.getMessage(), e);
        }
        return detalles;
    }

    public void registrarDetalle(DetalleOrden detalle) {
        if (detalle.getIdOrden() == null) {
            throw new IllegalArgumentException("El idOrden no puede ser nulo.");
        }
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleOrdenes_Registrar(?, ?, ?, ?)}")) {
            statement.setLong(1, detalle.getIdOrden());
            statement.setLong(2, detalle.getIdRepuesto());
            statement.setInt(3, detalle.getCantidad());
            statement.setDouble(4, detalle.getPrecioUnitario());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar el detalle de orden: " + e.getMessage(), e);
        }
    }

    public void eliminarDetalle(Long idDetalleOrden) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleOrdenes_Eliminar(?)}")) {
            statement.setLong(1, idDetalleOrden);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el detalle de orden: " + e.getMessage(), e);
        }
    }

    public DetalleOrden obtenerDetallePorId(Long idDetalleOrden) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleOrdenes_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, idDetalleOrden);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    DetalleOrden detalle = new DetalleOrden();
                    detalle.setIdDetalleOrden(rs.getLong("id_detalle_orden"));
                    detalle.setIdOrden(rs.getLong("id_orden"));
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

    public void actualizarDetalle(DetalleOrden detalle) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call DetalleOrdenes_Actualizar(?, ?, ?, ?, ?)}")) {
            statement.setLong(1, detalle.getIdDetalleOrden());
            statement.setLong(2, detalle.getIdOrden());
            statement.setLong(3, detalle.getIdRepuesto());
            statement.setInt(4, detalle.getCantidad());
            statement.setDouble(5, detalle.getPrecioUnitario());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el detalle de orden: " + e.getMessage(), e);
        }
    }
}
