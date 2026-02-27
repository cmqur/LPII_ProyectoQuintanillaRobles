package com.cibertec.app.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "stock_almacen",
    uniqueConstraints = @UniqueConstraint(columnNames = {"idproducto", "idalmacen"})
)
public class StockAlmacen implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "idstock")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "idproducto", nullable = false)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name = "idalmacen", nullable = false)
    private Almacen almacen;

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    public StockAlmacen() {}

    public StockAlmacen(Producto producto, Almacen almacen, int cantidad) {
        this.producto = producto;
        this.almacen = almacen;
        this.cantidad = cantidad;
    }
}
