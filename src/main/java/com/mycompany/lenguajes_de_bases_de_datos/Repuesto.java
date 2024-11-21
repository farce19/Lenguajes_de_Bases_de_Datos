package com.mycompany.lenguajes_de_bases_de_datos;

public class Repuesto implements Identificable {
    private int id;
    private String nombre;
    private String marca;
    private double precio;
    private int stock;
    private int idProveedor;
    private int idCategoria;

    
    public Repuesto(int id, String nombre, String marca, double precio, int stock, int idProveedor, int idCategoria) {
        this.id = id;
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
        this.idProveedor = idProveedor;
        this.idCategoria = idCategoria;
    }

    // Constructor sin ID
    public Repuesto(String nombre, String marca, double precio, int stock, int idProveedor, int idCategoria) {
        this.nombre = nombre;
        this.marca = marca;
        this.precio = precio;
        this.stock = stock;
        this.idProveedor = idProveedor;
        this.idCategoria = idCategoria;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public void setIdProveedor(int idProveedor) {
        this.idProveedor = idProveedor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "Repuesto{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               ", marca='" + marca + '\'' +
               ", precio=" + precio +
               ", stock=" + stock +
               ", idProveedor=" + idProveedor +
               ", idCategoria=" + idCategoria +
               '}';
    }
}

