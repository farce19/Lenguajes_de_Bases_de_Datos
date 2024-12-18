package com.repuestos.controller;

import com.repuestos.model.Cliente;
import com.repuestos.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        model.addAttribute("cliente", new Cliente()); // Para formulario de registro/edición
        return "clientes";
    }

    @PostMapping("/guardar")
    public String guardarCliente(@ModelAttribute Cliente cliente) {
        if (cliente.getId() != null) {
            clienteService.actualizarCliente(cliente);
        } else {
            clienteService.registrarCliente(cliente);
        }
        return "redirect:/clientes";
    }

    @GetMapping("/editar/{id}")
    public String editarCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.obtenerClientePorId(id);
        model.addAttribute("cliente", cliente);
        model.addAttribute("clientes", clienteService.obtenerTodosLosClientes());
        return "clientes";
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarCliente(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID del cliente no válido.");
        }
        clienteService.eliminarCliente(id);
        return "redirect:/clientes";
    }

}
