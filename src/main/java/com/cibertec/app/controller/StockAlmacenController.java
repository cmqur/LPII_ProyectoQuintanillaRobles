package com.cibertec.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.service.AlmacenService;
import com.cibertec.app.service.StockAlmacenService;

@Controller
public class StockAlmacenController {

    @Autowired private StockAlmacenService stockAlmacenService;
    @Autowired private AlmacenService almacenService;

    @GetMapping("/stock")
    public String stock(@RequestParam(name = "almacenId", required = false) Integer almacenId,
                        Model model) {

        model.addAttribute("almacenList", almacenService.listarTodos());
        model.addAttribute("almacenId", almacenId);

        if (almacenId != null) {
            Almacen a = almacenService.buscarPorId(almacenId);
            if (a != null) {
                model.addAttribute("stockList", stockAlmacenService.listarPorAlmacen(a));
                model.addAttribute("almacenSel", a);
            } else {
                model.addAttribute("stockList", stockAlmacenService.listarTodos());
            }
        } else {
            model.addAttribute("stockList", stockAlmacenService.listarTodos());
        }
        return "stock/index";
    }
}
