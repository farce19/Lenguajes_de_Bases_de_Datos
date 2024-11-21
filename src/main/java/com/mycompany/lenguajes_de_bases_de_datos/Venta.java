package com.mycompany.lenguajes_de_bases_de_datos;

public class Venta {
    private int idVenta;
    private int idCliente;
    private String fechaVenta;
    private double totalVenta;

    public Venta(int idVenta, int idCliente, String fechaVenta, double totalVenta) {
        this.idVenta = idVenta;
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
    }

    public Venta(int idCliente, String fechaVenta, double totalVenta) {
        this.idCliente = idCliente;
        this.fechaVenta = fechaVenta;
        this.totalVenta = totalVenta;
    }

    
    public int getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(int idVenta) {
        this.idVenta = idVenta;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    @Override
    public String toString() {
        return "Venta{" +
               "idVenta=" + idVenta +
               ", idCliente=" + idCliente +
               ", fechaVenta='" + fechaVenta + '\'' +
               ", totalVenta=" + totalVenta +
               '}';
    }
}

