package com.cibertec.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.entity.StockAlmacen;
import com.cibertec.app.repository.StockAlmacenRepository;
import com.cibertec.app.service.StockAlmacenService;

@Service
public class StockAlmacenServiceImpl implements StockAlmacenService {

    @Autowired
    private StockAlmacenRepository stockAlmacenRepository;

    @Override
    @Transactional
    public StockAlmacen obtenerOCrear(Producto producto, Almacen almacen) {
        return stockAlmacenRepository.findByProductoAndAlmacen(producto, almacen)
                .orElseGet(() -> stockAlmacenRepository.save(new StockAlmacen(producto, almacen, 0)));
    }

    @Override
    @Transactional
    public StockAlmacen ajustarStock(Producto producto, Almacen almacen, int delta) {
        StockAlmacen sa = obtenerOCrear(producto, almacen);
        int nuevo = sa.getCantidad() + delta;
        if (nuevo < 0) {
            throw new IllegalArgumentException("Stock insuficiente en el almacén " + almacen.getCodigo()
                    + " para el producto " + producto.getCodigo());
        }
        sa.setCantidad(nuevo);
        return stockAlmacenRepository.save(sa);
    }

    @Override
    public List<StockAlmacen> listarTodos() {
        return stockAlmacenRepository.findAll(
                Sort.by("almacen.nombre").ascending()
                        .and(Sort.by("producto.descripcion").ascending())
        );
    }

    @Override
    public List<StockAlmacen> listarPorAlmacen(Almacen almacen) {
        return stockAlmacenRepository.findByAlmacenOrderByProductoDescripcionAsc(almacen);
    }
}
