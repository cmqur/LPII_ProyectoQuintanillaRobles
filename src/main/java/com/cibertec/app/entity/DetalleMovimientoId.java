package com.cibertec.app.entity;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DetalleMovimientoId implements Serializable {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "idmovimiento")
    private Movimiento movimiento;

    @ManyToOne
    @JoinColumn(name = "idproducto")
    private Producto producto;

    public DetalleMovimientoId() {}

    public DetalleMovimientoId(Movimiento movimiento, Producto producto) {
        this.movimiento = movimiento;
        this.producto = producto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DetalleMovimientoId that)) return false;
        return Objects.equals(movimiento, that.movimiento) &&
                Objects.equals(producto, that.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                movimiento != null ? movimiento.getId() : null,
                producto != null ? producto.getIdProd() : null
        );
    }
}
