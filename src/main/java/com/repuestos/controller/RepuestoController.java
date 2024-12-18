package com.repuestos.controller;

import com.repuestos.model.Repuesto;
import com.repuestos.service.CategoriaService;
import com.repuestos.service.ProveedorService;
import com.repuestos.service.RepuestoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/repuestos")
public class RepuestoController {

    private final RepuestoService repuestoService;
    private final CategoriaService categoriaService;
    private final ProveedorService proveedorService;

    public RepuestoController(RepuestoService repuestoService, CategoriaService categoriaService, ProveedorService proveedorService) {
        this.repuestoService = repuestoService;
        this.categoriaService = categoriaService;
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public String listarRepuestos(Model model) {
        model.addAttribute("repuestos", repuestoService.obtenerTodosLosRepuestos());
        model.addAttribute("repuesto", new Repuesto());
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        return "repuestos";
    }

    @GetMapping("/editar/{id}")
    public String editarRepuesto(@PathVariable Long id, Model model) {
        
        Repuesto repuesto = repuestoService.obtenerRepuestoPorId(id);
        model.addAttribute("repuesto", repuesto);
        model.addAttribute("categorias", categoriaService.obtenerTodasLasCategorias());
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        return "repuestos"; 
    }

    @PostMapping("/guardar")
    public String guardarRepuesto(@ModelAttribute Repuesto repuesto) {
        if (repuesto.getId() != null) {
            repuestoService.actualizarRepuesto(repuesto);
        } else {
            repuestoService.registrarRepuesto(repuesto);
        }
        return "redirect:/repuestos";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarRepuesto(@PathVariable Long id) {
        repuestoService.eliminarRepuesto(id);
        return "redirect:/repuestos";
    }
}
