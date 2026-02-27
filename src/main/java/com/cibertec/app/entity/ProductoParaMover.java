package com.cibertec.app.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO simple para armar la lista (carrito) de productos en un movimiento.
 * Reutiliza atributos de Producto y agrega la cantidad.
 */
@Getter
@Setter
public class ProductoParaMover extends Producto {

    private static final long serialVersionUID = 1L;

    private int cantidad;

    public ProductoParaMover(Integer idProd, String codigo, String descripcion,
                             BigDecimal precioCompra, int stock, int cantidad) {
        //Usamos precioCompra como referencia de costo (no es obligatorio para los movimientos)
        super(idProd, codigo, descripcion, BigDecimal.ZERO, stock, precioCompra);
        this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }
}
