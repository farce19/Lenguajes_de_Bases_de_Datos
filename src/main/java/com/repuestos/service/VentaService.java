package com.repuestos.service;

import com.repuestos.model.Cliente;
import com.repuestos.model.Venta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

@Service
public class VentaService {

    private final DataSource dataSource;
    private final ClienteService clienteService;

    @Autowired
    public VentaService(DataSource dataSource, ClienteService clienteService) {
        this.dataSource = dataSource;
        this.clienteService = clienteService;
    }

    public List<Venta> obtenerTodasLasVentas() {
        List<Venta> ventas = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Ventas_Consultar(?)}")) {
            statement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(1)) {
                while (rs.next()) {
                    Venta venta = mapVentaFromResultSet(rs);
                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener todas las ventas: " + e.getMessage(), e);
        }
        return ventas;
    }

    public void registrarVenta(Venta venta) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Ventas_Registrar(?, ?, ?)}")) {
            statement.setLong(1, venta.getIdCliente());
            statement.setDate(2, new java.sql.Date(venta.getFechaVenta().getTime()));
            statement.setDouble(3, venta.getTotalVenta());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al registrar la venta: " + e.getMessage(), e);
        }
    }

    public void actualizarVenta(Venta venta) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call ActualizarVenta(?, ?, ?, ?)}")) {
            statement.setLong(1, venta.getId());
            statement.setLong(2, venta.getIdCliente());
            statement.setDate(3, new java.sql.Date(venta.getFechaVenta().getTime()));
            statement.setDouble(4, venta.getTotalVenta());
            statement.execute();
        } catch (SQLException e) {
            System.err.println("Error al actualizar la venta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void eliminarVenta(Long id) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call EliminarVenta(?)}")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar la venta: " + e.getMessage(), e);
        }
    }

    public Venta obtenerVentaPorId(Long id) {
        Venta venta = null;
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Ventas_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, id);
            statement.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    venta = mapVentaFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener la venta con ID " + id + ": " + e.getMessage(), e);
        }
        return venta;
    }

    public List<Venta> obtenerVentasPorFecha(java.sql.Date fechaInicio, java.sql.Date fechaFin) {
        List<Venta> ventas = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Ventas_ConsultarPorFecha(?, ?, ?)}")) {

            statement.setDate(1, fechaInicio);
            statement.setDate(2, fechaFin);
            statement.registerOutParameter(3, OracleTypes.CURSOR);

            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(3)) {
                while (rs.next()) {
                    Venta venta = mapVentaFromResultSet(rs);
                    ventas.add(venta);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al filtrar las ventas por fecha: " + e.getMessage(), e);
        }
        return ventas;
    }

    private Venta mapVentaFromResultSet(ResultSet rs) throws SQLException {
        Venta venta = new Venta();
        venta.setId(rs.getLong("id_venta"));
        venta.setIdCliente(rs.getLong("id_cliente"));
        venta.setFechaVenta(rs.getDate("fecha_venta"));
        venta.setTotalVenta(rs.getDouble("total_venta"));

        Cliente cliente = clienteService.obtenerClientePorId(venta.getIdCliente());
        if (cliente != null) {
            venta.setNombreCliente(cliente.getNombre() + " " + cliente.getApellido());
        } else {
            venta.setNombreCliente("Desconocido");
        }

        return venta;
    }

}
