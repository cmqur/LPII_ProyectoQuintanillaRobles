package com.cibertec.app.entity;


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
@Table(name = "usuario")
public class Usuario {
	
	@Id
	@Column(name = "idusuario")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nombres")
	private String nombres;
	
	@Column(name = "apellidos")
	private String apellidos;
	
	@Column(name = "usuario")
	private String username;
	
	@Column(name = "clave")
	private String clave;
	
	@ManyToOne
	@JoinColumn(name= "idrol")
	private Rol rol;
	

	public Usuario() {
		
	}

	public Usuario(String nombres, String apellidos,String username ,String clave, Rol rol) {
		this.nombres=nombres;
		this.apellidos=apellidos;
		this.username=username;
		this.clave=clave;
		this.rol=rol;
	
	}
}
