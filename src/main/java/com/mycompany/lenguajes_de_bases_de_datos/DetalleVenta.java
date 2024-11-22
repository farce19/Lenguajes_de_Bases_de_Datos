package com.mycompany.proyecto_lbd_template;

public class DetalleVenta {
    private int idDetalle;
    private int idVenta;
    private int idRepuesto;
    private int cantidad;
    private double precioUnitario;
    private double total;

    // Constructor completo
    public DetalleVenta(int idDetalle, int idVenta, int idRepuesto, int cantidad, double precioUnitario, double total) {
        this.idDetalle = idDetalle;
        this.idVenta = idVenta;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
    }

    // Constructor sin ID de detalle (para creación de nuevos registros)
    public DetalleVenta(int idVenta, int idRepuesto, int cantidad, double precioUnitario, double total) {
        this.idVenta = idVenta;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.total = total;
    }

    // Constructor básico con solo IDs y cantidad/precio
    public DetalleVenta(int idVenta, int idRepuesto, double precioUnitario) {
        this.idVenta = idVenta;
        this.idRepuesto = idRepuesto;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters
    public int getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(int idDetalle) {
        this.idDetalle = idDetalle;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(int idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "DetalleVenta{" +
               "idDetalle=" + idDetalle +
               ", idVenta=" + idVenta +
               ", idRepuesto=" + idRepuesto +
               ", cantidad=" + cantidad +
               ", precioUnitario=" + precioUnitario +
               ", total=" + total +
               '}';
    }
}
