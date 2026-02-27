package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cibertec.app.service.AlmacenService;
import com.cibertec.app.service.MovimientoService;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.UsuarioService;

@Controller
public class DashboardController {

    @Autowired private ProductoService productoService;
    @Autowired private MovimientoService movimientoService;
    @Autowired private AlmacenService almacenService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var productos = productoService.listarTodosProductos();
        long alertasStock = productos.stream()
                .filter(p -> p.getStock() <= p.getStockMinimo())
                .count();

        model.addAttribute("totalProductos", productos.size());
        model.addAttribute("totalAlmacenes", almacenService.listarTodos().size());
        model.addAttribute("totalMovimientos", movimientoService.listarTodos().size());
        model.addAttribute("totalUsuarios", usuarioService.listarTodosUsuario().size());
        model.addAttribute("alertasStock", alertasStock);

        return "dashboard";
    }
}
