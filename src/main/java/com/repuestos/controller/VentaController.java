package com.repuestos.controller;

import com.repuestos.model.Venta;
import com.repuestos.service.ClienteService;
import com.repuestos.service.VentaService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.sql.Date;

import java.util.List;
import org.springframework.validation.BindingResult;

@Controller
@RequestMapping("/ventas")
public class VentaController {

    private final VentaService ventaService;
    private final ClienteService clienteService;

    public VentaController(VentaService ventaService, ClienteService clienteService) {
        this.ventaService = ventaService;
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listarVentas(Model model) {
        model.addAttribute("ventas", ventaService.obtenerTodasLasVentas());
        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "ventas";
    }

    @PostMapping("/guardar")
    public String guardarVenta(@ModelAttribute @Valid Venta venta, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            return "ventas"; // Retorna a la misma vista con mensajes de error
        }
        if (venta.getId() != null) {
            ventaService.actualizarVenta(venta);
        } else {
            ventaService.registrarVenta(venta);
        }
        return "redirect:/ventas";
    }

    @GetMapping("/editar/{id}")
    public String editarVenta(@PathVariable Long id, Model model) {
        Venta venta = ventaService.obtenerVentaPorId(id);
        model.addAttribute("venta", venta);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "ventas";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarVenta(@PathVariable Long id) {
        ventaService.eliminarVenta(id);
        return "redirect:/ventas";
    }

    @GetMapping("/filtrar")
    public String filtrarVentasPorFecha(
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date fechaFin,
            Model model) {

// Validar si las fechas son nulas
        if (fechaInicio == null || fechaFin == null) {
            model.addAttribute("error", "Las fechas de inicio y fin son obligatorias.");
            model.addAttribute("ventas", ventaService.obtenerTodasLasVentas());
            model.addAttribute("venta", new Venta());
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            return "ventas";
        }

// Validar si la fecha de inicio es posterior a la fecha de fin
        if (fechaInicio.after(fechaFin)) {
            model.addAttribute("error", "La fecha de inicio no puede ser posterior a la fecha de fin.");
            model.addAttribute("ventas", ventaService.obtenerTodasLasVentas());
            model.addAttribute("venta", new Venta());
            model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
            return "ventas";
        }

// Conversión a java.sql.Date para enviar al servicio
        java.sql.Date sqlFechaInicio = new java.sql.Date(fechaInicio.getTime());
        java.sql.Date sqlFechaFin = new java.sql.Date(fechaFin.getTime());

        try {
            List<Venta> ventas = ventaService.obtenerVentasPorFecha(sqlFechaInicio, sqlFechaFin);
            model.addAttribute("ventas", ventas);
        } catch (RuntimeException e) {
            model.addAttribute("error", "Ocurrió un error al filtrar las ventas: " + e.getMessage());
        }

        model.addAttribute("venta", new Venta());
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "ventas";
    }

}
