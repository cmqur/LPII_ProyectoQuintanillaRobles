package com.cibertec.app.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "producto")
public class Producto implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "idproducto")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idProd;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	
	@Column(name = "precio_compra")
	private BigDecimal precioCompra;
	
	@Column(name = "precio_venta")
	private BigDecimal precioVenta;
	
	@Column(name = "stock")
	private int stock;

	@Column(name = "stock_minimo")
	private int stockMinimo = 5;

	@ManyToOne
	@JoinColumn(name= "idcate")
	private Categoria categoria;
	
	
	public Producto() {
		
	}
	
	  public Producto(Integer idProd,String codigo,String descripcion, BigDecimal precioVenta, int stock, BigDecimal precioCompra) {
		    this.idProd = idProd;
		    this.codigo = codigo;
	        this.descripcion = descripcion;
	        this.precioVenta = precioVenta;
	        this.stock = stock;
	        this.precioCompra=precioCompra;
	    }


	    public Producto(String codigo) {
	        this.codigo = codigo;
	    }
	    
	    public void restarExistencia(int stock) {
	        this.stock -= stock;
	    }
	    
	    public boolean sinExistencia() {
	        return this.stock <= 0;
	    }
}