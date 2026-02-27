package com.cibertec.app.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cibertec.app.service.impl.ReporteServiceImpl;

/*
  @RestController maneja métodos que retornan datos (PDF en bytes).
*/
@RestController
@RequestMapping("/reportes")
public class ReporteController {

    @Autowired
    private ReporteServiceImpl reporteService;

    // Reportes disponibles (Jasper jrxml en src/main/resources/reportes)
    private static final Map<String, String> REPORTES = Map.of(
            "inventario", "Reporte_Inventario.jrxml",
            "kardex", "Kardex.jrxml",
            "stockminimo", "Stock_minimo.jrxml",
            "stockalmacen", "Resumen_Movimiento.jrxml"
    );

    @GetMapping("/{tipo}")
    public ResponseEntity<byte[]> generarReporte(
            @PathVariable String tipo,
            @RequestParam(defaultValue = "ver") String modo
    ) throws Exception {

        String jrxml = REPORTES.get(tipo.toLowerCase());
        if (jrxml == null) {
            return ResponseEntity.badRequest().body("Reporte no válido".getBytes());
        }

        byte[] pdf = reporteService.generarReporte(jrxml);

        String disposition = modo.equals("descargar") ? "attachment" : "inline";
        String nombrePdf = jrxml.replace(".jrxml", ".pdf");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition + "; filename=" + nombrePdf)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
