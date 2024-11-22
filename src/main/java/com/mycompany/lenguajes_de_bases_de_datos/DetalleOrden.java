package com.mycompany.lenguajes_de_bases_de_datos;

public class DetalleOrden {
    private int idDetalleOrden;
    private int idOrden;
    private int idRepuesto;
    private int cantidad;
    private double precioUnitario;

    public DetalleOrden(int idDetalleOrden, int idOrden, int idRepuesto, int cantidad, double precioUnitario) {
        this.idDetalleOrden = idDetalleOrden;
        this.idOrden = idOrden;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public DetalleOrden(int idOrden, int idRepuesto, int cantidad, double precioUnitario) {
        this.idOrden = idOrden;
        this.idRepuesto = idRepuesto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    // Getters y Setters
    public int getIdDetalleOrden() {
        return idDetalleOrden;
    }

    public void setIdDetalleOrden(int idDetalleOrden) {
        this.idDetalleOrden = idDetalleOrden;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
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

    @Override
    public String toString() {
        return "DetalleOrden{" +
               "idDetalleOrden=" + idDetalleOrden +
               ", idOrden=" + idOrden +
               ", idRepuesto=" + idRepuesto +
               ", cantidad=" + cantidad +
               ", precioUnitario=" + precioUnitario +
               '}';
    }
}
