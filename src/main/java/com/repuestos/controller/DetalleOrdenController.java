package com.repuestos.controller;

import com.repuestos.model.DetalleOrden;
import com.repuestos.model.Repuesto;
import com.repuestos.service.DetalleOrdenService;
import com.repuestos.service.RepuestoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/detalle-ordenes")
public class DetalleOrdenController {

    private final DetalleOrdenService detalleOrdenService;
    private final RepuestoService repuestoService;

    public DetalleOrdenController(DetalleOrdenService detalleOrdenService, RepuestoService repuestoService) {
        this.detalleOrdenService = detalleOrdenService;
        this.repuestoService = repuestoService;
    }

    @GetMapping
    public String listarDetallesDefault(Model model) {
        model.addAttribute("detalles", List.of()); 
        return "detalle-ordenes";
    }

    @GetMapping("/{idOrden}")
    public String listarDetallesPorOrden(@PathVariable Long idOrden, Model model) {
        List<DetalleOrden> detalles = detalleOrdenService.obtenerDetallesPorOrden(idOrden);
        List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();

        model.addAttribute("detalles", detalles);
        model.addAttribute("idOrden", idOrden);
        model.addAttribute("nuevoDetalle", new DetalleOrden());
        model.addAttribute("repuestos", repuestos);
        return "detalle-ordenes";
    }

    @PostMapping("/guardar")
    public String guardarDetalle(@ModelAttribute DetalleOrden detalle) {
        if (detalle.getIdOrden() == null) {
            System.err.println("Error: El ID de la orden no puede ser nulo.");
            return "redirect:/detalle-ordenes";
        }
        detalleOrdenService.registrarDetalle(detalle);
        return "redirect:/detalle-ordenes/" + detalle.getIdOrden();
    }

    @GetMapping("/eliminar/{idDetalleOrden}")
    public String eliminarDetalle(@PathVariable Long idDetalleOrden, @RequestParam Long idOrden) {
        detalleOrdenService.eliminarDetalle(idDetalleOrden);
        return "redirect:/detalle-ordenes/" + idOrden;
    }

    @GetMapping("/editar/{idDetalleOrden}")
    public String editarDetalle(@PathVariable Long idDetalleOrden, @RequestParam Long idOrden, Model model) {
        List<Repuesto> repuestos = repuestoService.obtenerTodosLosRepuestos();
        DetalleOrden detalleExistente = detalleOrdenService.obtenerDetallePorId(idDetalleOrden);

        model.addAttribute("detalle", detalleExistente);
        model.addAttribute("idOrden", idOrden);
        model.addAttribute("repuestos", repuestos);
        return "editar-detalle-orden";
    }

    @PostMapping("/actualizar")
    public String actualizarDetalle(@ModelAttribute DetalleOrden detalle) {
        if (detalle.getIdOrden() == null || detalle.getIdDetalleOrden() == null) {
            System.err.println("Error: El ID del detalle o de la orden no pueden ser nulos.");
            return "redirect:/detalle-ordenes";
        }
        detalleOrdenService.actualizarDetalle(detalle);
        return "redirect:/detalle-ordenes/" + detalle.getIdOrden();
    }
}
