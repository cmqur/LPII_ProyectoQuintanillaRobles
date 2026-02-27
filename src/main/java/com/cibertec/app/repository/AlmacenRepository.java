package com.cibertec.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.app.entity.Almacen;

public interface AlmacenRepository extends JpaRepository<Almacen, Integer> {

    Optional<Almacen> findByCodigo(String codigo);
}
