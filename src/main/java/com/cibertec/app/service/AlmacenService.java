package com.cibertec.app.service;

import java.util.List;

import com.cibertec.app.entity.Almacen;

public interface AlmacenService {
    Almacen guardar(Almacen almacen);
    List<Almacen> listarTodos();
    Almacen buscarPorId(Integer id);
    void eliminarPorId(Integer id);
    Almacen buscarPorCodigo(String codigo);
}
