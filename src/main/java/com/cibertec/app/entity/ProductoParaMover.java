package com.cibertec.app.entity;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;


 
@Getter
@Setter
public class ProductoParaMover extends Producto {

    private static final long serialVersionUID = 1L;

    private int cantidad;

    public ProductoParaMover(Integer idProd, String codigo, String descripcion,
                             BigDecimal precioCompra, int stock, int cantidad) {
        
        super(idProd, codigo, descripcion, BigDecimal.ZERO, stock, precioCompra);
        this.cantidad = cantidad;
    }

    public void aumentarCantidad() {
        this.cantidad++;
    }
}
