package com.repuestos.controller;

import com.repuestos.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reporte")
    public String generarReporte(@RequestParam(value = "datos", required = false) List<String> datos, Model model) {
        
        if (datos == null) {
            datos = List.of();
        }

        model.addAttribute("datosSeleccionados", datos);

        if (datos.contains("totalVentasCliente")) {
            model.addAttribute("totalVentasCliente", reportService.totalVentasPorCliente(1)); 
        }
        if (datos.contains("promedioVentasCliente")) {
            model.addAttribute("promedioVentasCliente", reportService.promedioVentasCliente(1));
        }
        if (datos.contains("cantidadVentasMes")) {
            model.addAttribute("cantidadVentasMes", reportService.cantidadVentasPorMes());
        }
        if (datos.contains("totalGananciasMes")) {
            model.addAttribute("totalGananciasMes", reportService.totalGananciasPorMes());
        }
        if (datos.contains("repuestosMasVendidos")) {
            model.addAttribute("repuestosMasVendidos", reportService.repuestosMasVendidos());
        }
        if (datos.contains("repuestosSinVentas")) {
            model.addAttribute("repuestosSinVentas", reportService.repuestosSinVentas());
        }
        if (datos.contains("clientesMayorConsumo")) {
            model.addAttribute("clientesMayorConsumo", reportService.clientesMayorConsumo());
        }
        if (datos.contains("repuestosStockCritico")) {
            model.addAttribute("repuestosStockCritico", reportService.repuestosStockCritico(10));
        }
        if (datos.contains("productosPendientesProveedor")) {
            model.addAttribute("productosPendientesProveedor", reportService.productosPendientesProveedor(3));
        }
        if (datos.contains("ordenesPendientesProveedor")) {
            model.addAttribute("ordenesPendientesProveedor", reportService.ordenesPendientesProveedor(3));
        }
        if (datos.contains("proveedoresActivos")) {
            model.addAttribute("proveedoresActivos", reportService.proveedoresActivos(LocalDate.now().minusMonths(1).toString()));
        }
        if (datos.contains("ventasPorCategoria")) {
            model.addAttribute("ventasPorCategoria", reportService.ventasPorCategoria(LocalDate.now().minusMonths(1), LocalDate.now()));
        }
        if (datos.contains("historialComprasCliente")) {
            model.addAttribute("historialComprasCliente", reportService.historialComprasCliente(1)); 
        }

        return "reporte";
    }
}
