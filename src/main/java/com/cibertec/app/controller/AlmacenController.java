package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.service.AlmacenService;

@Controller
public class AlmacenController {

    @Autowired
    private AlmacenService almacenService;

    @GetMapping("/almacen")
    public String list(Model model) {
        model.addAttribute("almacenes", almacenService.listarTodos());
        return "almacen/index";
    }

    @GetMapping("/almacen/new")
    public String createForm(Model model) {
        model.addAttribute("almacen", new Almacen());
        return "almacen/create";
    }

    @PostMapping("/almacen")
    public String save(@ModelAttribute("almacen") Almacen almacen) {
        almacenService.guardar(almacen);
        return "redirect:/almacen";
    }

    @GetMapping("/almacen/edit/{id}")
    public String editForm(@PathVariable Integer id, Model model) {
        model.addAttribute("almacen", almacenService.buscarPorId(id));
        return "almacen/edit";
    }

    @PostMapping("/almacen/{id}")
    public String update(@PathVariable Integer id, @ModelAttribute("almacen") Almacen almacen) {
        Almacen existent = almacenService.buscarPorId(id);
        if (existent != null) {
            existent.setCodigo(almacen.getCodigo());
            existent.setNombre(almacen.getNombre());
            existent.setDireccion(almacen.getDireccion());
            existent.setActivo(almacen.isActivo());
            almacenService.guardar(existent);
        }
        return "redirect:/almacen";
    }

    @GetMapping("/almacen/{id}")
    public String delete(@PathVariable Integer id) {
        almacenService.eliminarPorId(id);
        return "redirect:/almacen";
    }
}
