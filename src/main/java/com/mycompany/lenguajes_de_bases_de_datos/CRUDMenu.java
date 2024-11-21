package com.mycompany.lenguajes_de_bases_de_datos;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class CRUDMenu<T> {
    private final String nombreEntidad;
    private final CRUD<T> crudModule;

    public CRUDMenu(String nombreEntidad, CRUD<T> crudModule) {
        this.nombreEntidad = nombreEntidad;
        this.crudModule = crudModule;
    }

    public void mostrarMenu(Connection connection) {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\nGestión de " + nombreEntidad);
            System.out.println("1. Crear");
            System.out.println("2. Leer");
            System.out.println("3. Actualizar");
            System.out.println("4. Eliminar");
            System.out.println("5. Listar Todos");
            System.out.println("6. Regresar al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); //Saltar línea

            switch (opcion) {
                case 1:
                    T nuevaEntidad = crudModule.crearEntidadInteractiva(scanner);
                    if (nuevaEntidad != null) {
                        crudModule.create(nuevaEntidad, connection);
                    } else {
                        System.out.println("No se pudo crear la entidad.");
                    }
                    break;
                case 2:
                    System.out.print("Ingrese el ID: ");
                    int idLeer = scanner.nextInt();
                    scanner.nextLine(); 
                    T entidadLeida = crudModule.read(idLeer, connection);
                    if (entidadLeida != null) {
                        System.out.println(entidadLeida);
                    } else {
                        System.out.println("No se encontró la entidad con el ID proporcionado.");
                    }
                    break;
                case 3:
                    System.out.print("Ingrese el ID para actualizar: ");
                    int idActualizar = scanner.nextInt();
                    scanner.nextLine();
                    T entidadActualizada = crudModule.crearEntidadInteractiva(scanner);
                    if (entidadActualizada != null) {
                        if (entidadActualizada instanceof Identificable) {
                            ((Identificable) entidadActualizada).setId(idActualizar);
                            crudModule.update(entidadActualizada, connection);
                        } else {
                            System.out.println("La entidad no soporta la operación de actualización.");
                        }
                    } else {
                        System.out.println("No se pudo actualizar la entidad.");
                    }
                    break;
                case 4:
                    System.out.print("Ingrese el ID para eliminar: ");
                    int idEliminar = scanner.nextInt();
                    scanner.nextLine();
                    crudModule.delete(idEliminar, connection);
                    break;
                case 5:
                    List<T> lista = crudModule.getAll(connection);
                    if (lista.isEmpty()) {
                        System.out.println("No hay entidades disponibles para listar.");
                    } else {
                        lista.forEach(System.out::println);
                    }
                    break;
                case 6:
                    System.out.println("Regresando al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 6);
    }
}
