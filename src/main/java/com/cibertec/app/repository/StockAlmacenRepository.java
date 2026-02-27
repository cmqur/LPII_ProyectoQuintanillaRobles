package com.cibertec.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.entity.StockAlmacen;

public interface StockAlmacenRepository extends JpaRepository<StockAlmacen, Integer> {

    Optional<StockAlmacen> findByProductoAndAlmacen(Producto producto, Almacen almacen);

    List<StockAlmacen> findByAlmacenOrderByProductoDescripcionAsc(Almacen almacen);
}
