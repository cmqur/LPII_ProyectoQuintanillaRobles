package com.cibertec.app.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "detalle_movimiento")
public class DetalleMovimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private DetalleMovimientoId id;

    @Column(nullable = false)
    private int cantidad;
}
