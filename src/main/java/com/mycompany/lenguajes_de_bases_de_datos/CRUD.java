package com.mycompany.lenguajes_de_bases_de_datos;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public interface CRUD<T> {
    void create(T t, Connection connection);

    T read(int id, Connection connection);

    void update(T t, Connection connection);

    void delete(int id, Connection connection);

    List<T> getAll(Connection connection);

    
    T crearEntidadInteractiva(Scanner scanner);
}
