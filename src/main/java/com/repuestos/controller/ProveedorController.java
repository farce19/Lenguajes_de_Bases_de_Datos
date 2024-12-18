package com.repuestos.controller;

import com.repuestos.model.Proveedor;
import com.repuestos.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    
    @GetMapping
    public String listarProveedores(Model model) {
        model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        model.addAttribute("proveedor", new Proveedor()); 
        return "proveedores";
    }

    
    @PostMapping("/guardar")
    public String guardarProveedor(@ModelAttribute Proveedor proveedor) {
        if (proveedor.getId() != null) {
            
            proveedorService.actualizarProveedor(proveedor);
        } else {
            
            proveedorService.registrarProveedor(proveedor);
        }
        return "redirect:/proveedores";
    }

    
    @GetMapping("/editar/{id}")
    public String editarProveedor(@PathVariable Long id, Model model) {
        Proveedor proveedor = proveedorService.obtenerProveedorPorId(id);
        if (proveedor != null) {
            model.addAttribute("proveedor", proveedor);
            model.addAttribute("proveedores", proveedorService.obtenerTodosLosProveedores());
        } else {
            
            return "redirect:/proveedores";
        }
        return "proveedores"; 
    }

   
    @GetMapping("/eliminar/{id}")
    public String eliminarProveedor(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
        return "redirect:/proveedores";
    }
}
