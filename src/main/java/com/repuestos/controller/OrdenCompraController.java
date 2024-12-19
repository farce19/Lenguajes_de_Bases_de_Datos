package com.repuestos.controller;

import com.repuestos.model.OrdenCompra;
import com.repuestos.service.OrdenCompraService;
import com.repuestos.service.ProveedorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/ordenes-compra")
public class OrdenCompraController {

    private final OrdenCompraService ordenCompraService;
    private final ProveedorService proveedorService;

    public OrdenCompraController(OrdenCompraService ordenCompraService, ProveedorService proveedorService) {
        this.ordenCompraService = ordenCompraService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listarOrdenes(Model model) {
        List<OrdenCompra> ordenes = ordenCompraService.obtenerTodasLasOrdenes();
        model.addAttribute("ordenes", ordenes);
        model.addAttribute("nuevaOrden", new OrdenCompra());
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        return "ordenes-compra";
    }

    @PostMapping("/guardar")
    public String guardarOrden(@ModelAttribute OrdenCompra ordenCompra) {
        if (ordenCompra.getId() == null) {
            ordenCompraService.registrarOrden(ordenCompra);
        } else {
            ordenCompraService.actualizarOrden(ordenCompra);
        }
        return "redirect:/ordenes-compra";
    }

    @GetMapping("/editar/{id}")
    public String editarOrden(@PathVariable Long id, Model model) {
        OrdenCompra orden = ordenCompraService.obtenerOrdenPorId(id);
        model.addAttribute("nuevaOrden", orden);
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        return "ordenes-compra";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarOrden(@PathVariable Long id) {
        ordenCompraService.eliminarOrden(id);
        return "redirect:/ordenes-compra";
    }
}
