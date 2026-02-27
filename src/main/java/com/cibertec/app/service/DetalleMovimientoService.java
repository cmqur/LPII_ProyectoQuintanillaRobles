package com.cibertec.app.service;

import com.cibertec.app.entity.DetalleMovimiento;

public interface DetalleMovimientoService {
    DetalleMovimiento guardar(DetalleMovimiento det);

	java.util.List<DetalleMovimiento> listarPorMovimiento(com.cibertec.app.entity.Movimiento movimiento);
}
