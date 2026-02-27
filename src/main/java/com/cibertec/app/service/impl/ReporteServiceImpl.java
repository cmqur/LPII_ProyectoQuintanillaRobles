package com.cibertec.app.service.impl;

import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JRParameter;

@Service
public class ReporteServiceImpl {

	@Autowired
    private DataSource dataSource; //Leerá la conexión a bd del proyecto

    public byte[] generarReporte(String jrxml) throws Exception {
    	//obtenemos la conexión a la bd donde ejecutará la sentencia sql del reporte
        try (Connection connection = dataSource.getConnection()) { 
        	//leemos la ruta de ubicación del reporte (jrxml)
            InputStream jrxmlStream =
                new ClassPathResource("reportes/" + jrxml).getInputStream(); 
            
            //compilamos el archivo jrxml y generamos el archivo .jasper
            JasperReport jasperReport = JasperCompileManager.compileReport(jrxmlStream);
            
            //mapeamos los campos del jasper hacia la vista en la bd
            Map<String, Object> params = new HashMap<>();
			// Para que los recursos (imágenes, subreportes, etc.) se resuelvan desde el classpath
			// incluso cuando el JRXML se compila desde un InputStream.
			params.put(JRParameter.REPORT_CLASS_LOADER, this.getClass().getClassLoader());
            
            //copiamos los datos desde la fuente hacia el archivo jasper
            JasperPrint jasperPrint =
                JasperFillManager.fillReport(jasperReport, params, connection);
            
            //exportamos el archivo jasper a pdf
            return JasperExportManager.exportReportToPdf(jasperPrint);
        }
    }
}