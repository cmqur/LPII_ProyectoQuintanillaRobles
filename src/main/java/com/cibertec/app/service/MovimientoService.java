package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.DetalleMovimiento;
import com.cibertec.app.entity.Movimiento;

public interface MovimientoService {

    Movimiento guardar(Movimiento movimiento, List<DetalleMovimiento> detalles);

    List<Movimiento> listarTodos();

    Movimiento buscarPorId(Integer id);
}
