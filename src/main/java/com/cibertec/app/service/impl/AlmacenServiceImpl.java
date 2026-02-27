package com.cibertec.app.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.repository.AlmacenRepository;
import com.cibertec.app.service.AlmacenService;

@Service
public class AlmacenServiceImpl implements AlmacenService {

    @Autowired
    private AlmacenRepository almacenRepository;

    @Override
    public Almacen guardar(Almacen almacen) {
        return almacenRepository.save(almacen);
    }

    @Override
    public List<Almacen> listarTodos() {
        return almacenRepository.findAll();
    }

    @Override
    public Almacen buscarPorId(Integer id) {
        return almacenRepository.findById(id).orElse(null);
    }

    @Override
    public void eliminarPorId(Integer id) {
        almacenRepository.deleteById(id);
    }

    @Override
    public Almacen buscarPorCodigo(String codigo) {
        return almacenRepository.findByCodigo(codigo).orElse(null);
    }
}
