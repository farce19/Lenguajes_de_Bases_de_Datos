package com.repuestos.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "Detalle_Ventas")
public class DetalleVenta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Long idDetalle;

    @Column(name = "id_venta", nullable = false)
    private Long idVenta;

    @Column(name = "id_repuesto", nullable = false)
    private Long idRepuesto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Transient 
    private Double subtotal;

    
    public Long getIdDetalle() {
        return idDetalle;
    }

    public void setIdDetalle(Long idDetalle) {
        this.idDetalle = idDetalle;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Long getIdRepuesto() {
        return idRepuesto;
    }

    public void setIdRepuesto(Long idRepuesto) {
        this.idRepuesto = idRepuesto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public Double getSubtotal() {
        // Si el subtotal es nulo, lo calculamos localmente (como fallback)
        if (subtotal == null && cantidad != null && precioUnitario != null) {
            subtotal = cantidad * precioUnitario;
        }
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

   
    @Override
    public String toString() {
        return "DetalleVenta{" +
                "idDetalle=" + idDetalle +
                ", idVenta=" + idVenta +
                ", idRepuesto=" + idRepuesto +
                ", cantidad=" + cantidad +
                ", precioUnitario=" + precioUnitario +
                ", subtotal=" + getSubtotal() +
                '}';
    }
}
