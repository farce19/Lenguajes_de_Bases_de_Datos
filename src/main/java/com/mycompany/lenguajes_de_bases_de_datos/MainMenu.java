package com.mycompany.lenguajes_de_bases_de_datos;



import java.sql.Connection;
import java.util.Scanner;

import java.sql.Connection;
import java.util.Scanner;

public class MainMenu {

    private final Connection connection;

    public MainMenu(Connection connection) {
        this.connection = connection;
    }

    public void mostrarMenuPrincipal() {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\nSistema de Gestión de Repuestos Mecánicos");
            System.out.println("1. Gestionar Clientes");
            System.out.println("2. Gestionar Proveedores");
            System.out.println("3. Gestionar Repuestos");
            System.out.println("4. Gestionar Ventas");
            System.out.println("5. Gestionar Detalle de Ventas");
            System.out.println("6. Gestionar Órdenes de Compra");
            System.out.println("7. Gestionar Detalle de Órdenes");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    gestionarClientes();
                    break;
                case 2:
                    gestionarProveedores();
                    break;
                case 3:
                    gestionarRepuestos();
                    break;
                case 4:
                    gestionarVentas();
                    break;
                case 5:
                    gestionarDetalleVentas();
                    break;
                case 6:
                    gestionarOrdenesCompra();
                    break;
                case 7:
                    gestionarDetalleOrdenes();
                    break;
                case 8:
                    System.out.println("Saliendo del sistema...");
                    break;
                default:
                    System.out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 8);
    }

    private void gestionarClientes() {
        ClientesModule clientesModule = new ClientesModule();
        CRUDMenu<Cliente> menuClientes = new CRUDMenu<>("Clientes", clientesModule);
        menuClientes.mostrarMenu(connection);
    }

    private void gestionarProveedores() {
        ProveedoresModule proveedoresModule = new ProveedoresModule();
        CRUDMenu<Proveedor> menuProveedores = new CRUDMenu<>("Proveedores", proveedoresModule);
        menuProveedores.mostrarMenu(connection);
    }

    private void gestionarRepuestos() {
        RepuestosModule repuestosModule = new RepuestosModule();
        CRUDMenu<Repuesto> menuRepuestos = new CRUDMenu<>("Repuestos", repuestosModule);
        menuRepuestos.mostrarMenu(connection);
    }

    private void gestionarVentas() {
        VentasModule ventasModule = new VentasModule();
        CRUDMenu<Venta> menuVentas = new CRUDMenu<>("Ventas", ventasModule);
        menuVentas.mostrarMenu(connection);
    }

    private void gestionarDetalleVentas() {
        DetalleVentasModule detalleVentasModule = new DetalleVentasModule();
        CRUDMenu<DetalleVenta> menuDetalleVentas = new CRUDMenu<>("Detalle Ventas", detalleVentasModule);
        menuDetalleVentas.mostrarMenu(connection);
    }

    private void gestionarOrdenesCompra() {
        OrdenCompraModule ordenCompraModule = new OrdenCompraModule();
        CRUDMenu<OrdenCompra> menuOrdenesCompra = new CRUDMenu<>("Órdenes de Compra", ordenCompraModule);
        menuOrdenesCompra.mostrarMenu(connection);
    }

    private void gestionarDetalleOrdenes() {
        DetalleOrdenesModule detalleOrdenesModule = new DetalleOrdenesModule();
        CRUDMenu<DetalleOrden> menuDetalleOrdenes = new CRUDMenu<>("Detalle Órdenes", detalleOrdenesModule);
        menuDetalleOrdenes.mostrarMenu(connection);
    }
}

