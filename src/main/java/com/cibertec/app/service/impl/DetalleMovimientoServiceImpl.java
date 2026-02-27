package com.cibertec.app.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.app.entity.DetalleMovimiento;
import com.cibertec.app.repository.DetalleMovimientoRepository;
import com.cibertec.app.service.DetalleMovimientoService;

@Service
public class DetalleMovimientoServiceImpl implements DetalleMovimientoService {

    @Autowired
    private DetalleMovimientoRepository repo;

    @Override
    public DetalleMovimiento guardar(DetalleMovimiento det) {
        return repo.save(det);
    }

	@Override
	public java.util.List<DetalleMovimiento> listarPorMovimiento(com.cibertec.app.entity.Movimiento movimiento) {
		return repo.findById_Movimiento(movimiento);
	}
}
