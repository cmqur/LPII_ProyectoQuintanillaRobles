package com.cibertec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.app.entity.DetalleMovimiento;
import com.cibertec.app.entity.DetalleMovimientoId;

import java.util.List;

public interface DetalleMovimientoRepository extends JpaRepository<DetalleMovimiento, DetalleMovimientoId> {
	List<DetalleMovimiento> findById_Movimiento(com.cibertec.app.entity.Movimiento movimiento);
}
