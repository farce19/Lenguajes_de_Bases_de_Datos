package com.mycompany.lenguajes_de_bases_de_datos;

import java.util.Date;

public class OrdenCompra {
    private int idOrden;
    private int idProveedor;
    private Date fechaOrden;
    private String estadoOrden;

    public OrdenCompra(int idOrden, int idProveedor, Date fechaOrden, String estadoOrden) {
        this.idOrden = idOrden;
        this.idProveedor = idProveedor;
        this.fechaOrden = fechaOrden;
        this.estadoOrden = estadoOrden;
    }

    public OrdenCompra(int idProveedor, Date fechaOrden, String estadoOrden) {
        this.idProveedor = idProveedor;
        this.fechaOrden = fechaOrden;
        this.estadoOrden = estadoOrden;
    }

    // Getters y Setters
    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public Date getFechaOrden() {
        return fechaOrden;
    }

    public void setFechaOrden(Date fechaOrden) {
        this.fechaOrden = fechaOrden;
    }

    public String getEstadoOrden() {
        return estadoOrden;
    }

    public void setEstadoOrden(String estadoOrden) {
        this.estadoOrden = estadoOrden;
    }

    @Override
    public String toString() {
        return "OrdenCompra{" +
               "idOrden=" + idOrden +
               ", idProveedor=" + idProveedor +
               ", fechaOrden=" + fechaOrden +
               ", estadoOrden='" + estadoOrden + '\'' +
               '}';
    }
}
