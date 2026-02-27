package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.entity.StockAlmacen;

public interface StockAlmacenService {

    StockAlmacen obtenerOCrear(Producto producto, Almacen almacen);

    /**
     * Ajusta el stock por almacén (+/-) y devuelve el registro actualizado.
     * Si el ajuste dejaría negativo el stock, lanza IllegalArgumentException.
     */
    StockAlmacen ajustarStock(Producto producto, Almacen almacen, int delta);

    List<StockAlmacen> listarTodos();

    List<StockAlmacen> listarPorAlmacen(Almacen almacen);
}
