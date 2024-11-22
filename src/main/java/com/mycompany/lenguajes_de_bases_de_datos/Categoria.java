package com.mycompany.lenguajes_de_bases_de_datos;

public class Categoria implements Identificable {
    private int id;
    private String nombre;

    // Constructor completo
    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    // Constructor sin ID
    public Categoria(String nombre) {
        this.nombre = nombre;
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

    @Override
    public String toString() {
        return "Categoria{" +
               "id=" + id +
               ", nombre='" + nombre + '\'' +
               '}';
    }
}
