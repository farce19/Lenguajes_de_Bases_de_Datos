package com.repuestos.service;

import com.repuestos.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

@Service
public class CategoriaService {

    @Autowired
    private DataSource dataSource;

    public List<Categoria> obtenerTodasLasCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Categorias_Consultar(?)}")) {
            statement.registerOutParameter(1, OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(1)) {
                while (rs.next()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(rs.getLong("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                    categorias.add(categoria);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }

    public void registrarCategoria(Categoria categoria) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Categorias_Registrar(?)}")) {
            statement.setString(1, categoria.getNombre());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void actualizarCategoria(Categoria categoria) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Categorias_Actualizar(?, ?)}")) {
            statement.setLong(1, categoria.getId());
            statement.setString(2, categoria.getNombre());
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void eliminarCategoria(Long id) {
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Categorias_Eliminar(?)}")) {
            statement.setLong(1, id);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Categoria obtenerCategoriaPorId(Long idCategoria) {
        Categoria categoria = null;
        try (Connection connection = dataSource.getConnection(); CallableStatement statement = connection.prepareCall("{call Categorias_ConsultarPorId(?, ?)}")) {
            statement.setLong(1, idCategoria);
            statement.registerOutParameter(2, OracleTypes.CURSOR);
            statement.execute();

            try (ResultSet rs = (ResultSet) statement.getObject(2)) {
                if (rs.next()) {
                    categoria = new Categoria();
                    categoria.setId(rs.getLong("id_categoria"));
                    categoria.setNombre(rs.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoria;
    }

}
