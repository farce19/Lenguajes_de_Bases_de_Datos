package com.repuestos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import oracle.jdbc.OracleTypes;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Map<String, Object>> repuestosMasVendidos() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call RepuestosMasVendidos(TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD')) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            LocalDate fechaInicio = LocalDate.now().minusMonths(1).withDayOfMonth(1);
            LocalDate fechaFin = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());

            callableStatement.setString(2, fechaInicio.toString());
            callableStatement.setString(3, fechaFin.toString());

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("repuesto", resultSet.getString("repuesto"));
                    row.put("total_vendido", resultSet.getInt("total_vendido"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultList;
    }

    public List<Map<String, Object>> repuestosSinVentas() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call RepuestosSinVentas(TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD')) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            LocalDate fechaInicio = LocalDate.now().minusMonths(1).withDayOfMonth(1);
            LocalDate fechaFin = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());

            callableStatement.setString(2, fechaInicio.toString());
            callableStatement.setString(3, fechaFin.toString());

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("nombre", resultSet.getString("nombre"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función RepuestosSinVentas", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> clientesMayorConsumo() {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call ClientesMayorConsumo(TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD')) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            LocalDate fechaInicio = LocalDate.now().minusMonths(1).withDayOfMonth(1);
            LocalDate fechaFin = LocalDate.now().minusMonths(1).withDayOfMonth(LocalDate.now().minusMonths(1).lengthOfMonth());

            callableStatement.setString(2, fechaInicio.toString());
            callableStatement.setString(3, fechaFin.toString());

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("nombre", resultSet.getString("nombre"));
                    row.put("apellido", resultSet.getString("apellido"));
                    row.put("total_gastado", resultSet.getDouble("total_gastado"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función ClientesMayorConsumo", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> ordenesPendientesProveedor(int idProveedor) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call OrdenesPendientesProveedor(?) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            callableStatement.setInt(2, idProveedor);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("id_orden", resultSet.getInt("id_orden"));
                    row.put("fecha_orden", resultSet.getDate("fecha_orden"));
                    row.put("estado_orden", resultSet.getString("estado_orden"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función OrdenesPendientesProveedor", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> proveedoresActivos(String fechaLimite) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call ProveedoresActivos(TO_DATE(?, 'YYYY-MM-DD')) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            callableStatement.setString(2, fechaLimite);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("nombre", resultSet.getString("nombre"));
                    row.put("total_ordenes", resultSet.getInt("total_ordenes"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función ProveedoresActivos", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> ventasPorCategoria(String fechaInicio, String fechaFin) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call VentasPorCategoria(TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD')) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            callableStatement.setString(2, fechaInicio);
            callableStatement.setString(3, fechaFin);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("categoria", resultSet.getString("Categoria"));
                    row.put("total_vendidos", resultSet.getInt("TotalVendidos"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función VentasPorCategoria", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> ventasPorCategoria(LocalDate fechaInicio, LocalDate fechaFin) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call VentasPorCategoria(TO_DATE(?, 'YYYY-MM-DD'), TO_DATE(?, 'YYYY-MM-DD')) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setString(2, fechaInicio.toString());
            callableStatement.setString(3, fechaFin.toString());

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("categoria", resultSet.getString("categoria"));
                    row.put("total_vendidos", resultSet.getInt("total_vendidos"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función VentasPorCategoria", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> productosPendientesProveedor(int idProveedor) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call ProductosPendientesProveedor(?) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            callableStatement.setInt(2, idProveedor);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("repuesto_nombre", resultSet.getString("repuesto_nombre"));
                    row.put("cantidad_pendiente", resultSet.getInt("cantidad_pendiente"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función ProductosPendientesProveedor", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> historialComprasCliente(int idCliente) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call HistorialComprasCliente(?) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);

            callableStatement.setInt(2, idCliente);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("fecha_venta", resultSet.getDate("fecha_venta"));
                    row.put("total_venta", resultSet.getDouble("total_venta"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función HistorialComprasCliente", e);
        }

        return resultList;
    }

    public List<Map<String, Object>> repuestosStockCritico(int nivelCritico) {
        List<Map<String, Object>> resultList = new ArrayList<>();
        String sql = "{ ? = call RepuestosStockCritico(?) }";

        try (Connection connection = jdbcTemplate.getDataSource().getConnection(); CallableStatement callableStatement = connection.prepareCall(sql)) {

            callableStatement.registerOutParameter(1, OracleTypes.CURSOR);
            callableStatement.setInt(2, nivelCritico);

            callableStatement.execute();

            try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                while (resultSet.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("nombre", resultSet.getString("nombre"));
                    row.put("stock", resultSet.getInt("stock"));
                    resultList.add(row);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función RepuestosStockCritico", e);
        }

        return resultList;
    }

    public Double stockPromedioCategoria(int idCategoria) {
        String sql = "SELECT StockPromedioCategoria(?) FROM DUAL";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{idCategoria}, Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función StockPromedioCategoria", e);
        }
    }

    public Double promedioVentasCliente(int idCliente) {
        String sql = "SELECT PromedioVentasCliente(?) FROM DUAL";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{idCliente}, Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función PromedioVentasCliente", e);
        }
    }

    public Double totalVentasPorCliente(int idCliente) {
        String sql = "SELECT TotalVentasPorCliente(?) FROM DUAL";

        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{idCliente}, Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función TotalVentasPorCliente", e);
        }
    }

    public Integer cantidadVentasPorMes() {
        String sql = "SELECT CantidadVentasPorMes FROM DUAL";

        try {
            return jdbcTemplate.queryForObject(sql, Integer.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función CantidadVentasPorMes", e);
        }
    }

    public Double totalGananciasPorMes() {
        String sql = "SELECT TotalGananciasPorMes FROM DUAL";

        try {
            return jdbcTemplate.queryForObject(sql, Double.class);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al ejecutar la función TotalGananciasPorMes", e);
        }
    }

}
