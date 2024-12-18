package com.repuestos.controller;

import com.repuestos.model.DetalleVenta;
import com.repuestos.model.Repuesto;
import com.repuestos.service.DetalleVentaService;
import com.repuestos.service.RepuestoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/detalle-ventas")
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;
    private final RepuestoService repuestoService;

    public DetalleVentaController(DetalleVentaService detalleVentaService, RepuestoService repuestoService) {
        this.detalleVentaService = detalleVentaService;
        this.repuestoService = repuestoService;
    }

    @GetMapping
    public String listarDetallesDefault(Model model) {
        model.addAttribute("detalles", List.of()); // 
        return "detalle-ventas";
    }

    @GetMapping("/{idVenta}")
    public String listarDetallesPorVenta(@PathVariable Long idVenta, Model model) {
        List<DetalleVenta> detalles = detalleVentaService.obtenerDetallesPorVenta(idVenta);
        List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();

        model.addAttribute("detalles", detalles);
        model.addAttribute("idVenta", idVenta); 
        model.addAttribute("nuevoDetalle", new DetalleVenta());
        model.addAttribute("repuestos", repuestos);
        return "detalle-ventas";
    }

    @PostMapping("/guardar")
    public String guardarDetalle(@ModelAttribute DetalleVenta detalle) {
        if (detalle.getIdVenta() == null) {
            System.err.println("Error: El ID de la venta no puede ser nulo.");
            return "redirect:/detalle-ventas"; 
        }
        detalleVentaService.registrarDetalle(detalle);
        return "redirect:/detalle-ventas/" + detalle.getIdVenta();
    }

    @GetMapping("/eliminar/{idDetalle}")
    public String eliminarDetalle(@PathVariable Long idDetalle, @RequestParam Long idVenta) {
        detalleVentaService.eliminarDetalle(idDetalle);
        return "redirect:/detalle-ventas/" + idVenta;
    }
    
    
    @GetMapping("/editar/{idDetalle}")
    public String editarDetalle(@PathVariable Long idDetalle, @RequestParam Long idVenta, Model model) {
        List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();
        DetalleVenta detalleExistente = detalleVentaService.obtenerDetallePorId(idDetalle);

        model.addAttribute("detalle", detalleExistente);
        model.addAttribute("idVenta", idVenta);
        model.addAttribute("repuestos", repuestos);
        return "editar-detalle";
    }

    
    @PostMapping("/actualizar")
    public String actualizarDetalle(@ModelAttribute DetalleVenta detalle) {
        if (detalle.getIdVenta() == null || detalle.getIdDetalle() == null) {
            System.err.println("Error: El ID del detalle o de la venta no pueden ser nulos.");
            return "redirect:/detalle-ventas";
        }
        detalleVentaService.actualizarDetalle(detalle);
        return "redirect:/detalle-ventas/" + detalle.getIdVenta();
    }

}
