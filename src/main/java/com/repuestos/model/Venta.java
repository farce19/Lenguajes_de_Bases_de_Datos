package com.repuestos.model;

import jakarta.persistence.*;
import java.util.Date;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(name = "Ventas")
public class Venta {

    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_venta")
    private Long id;

   
    @Column(name = "id_cliente", nullable = false)
    private Long idCliente;

    
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date fechaVenta;

    
    @Column(name = "total_venta", nullable = false)
    private Double totalVenta;

    
    @Transient
    private String nombreCliente;

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public Date getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(Date fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public Double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(Double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    
    @Override
    public String toString() {
        return "Venta{"
                + "id=" + id
                + ", idCliente=" + idCliente
                + ", fechaVenta=" + fechaVenta
                + ", totalVenta=" + totalVenta
                + ", nombreCliente='" + nombreCliente + '\''
                + '}';
    }
}
