package com.cibertec.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.app.entity.Movimiento;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
}
