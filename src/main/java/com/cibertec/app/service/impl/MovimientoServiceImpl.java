package com.cibertec.app.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.entity.DetalleMovimiento;
import com.cibertec.app.entity.DetalleMovimientoId;
import com.cibertec.app.entity.Movimiento;
import com.cibertec.app.entity.MovimientoTipo;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.repository.MovimientoRepository;
import com.cibertec.app.service.MovimientoService;
import com.cibertec.app.service.DetalleMovimientoService;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.StockAlmacenService;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private StockAlmacenService stockAlmacenService;

    @Autowired
    private DetalleMovimientoService detalleMovimientoService;

    @Override
    @Transactional
    public Movimiento guardar(Movimiento movimiento, List<DetalleMovimiento> detalles) {

        if (movimiento.getFecha() == null) {
            movimiento.setFecha(new Date());
        }

        // Validaciones mínimas por tipo
        if (movimiento.getTipo() == null) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de movimiento");
        }
        if (movimiento.getTipo() == MovimientoTipo.INGRESO) {
            if (movimiento.getAlmacenDestino() == null) {
                throw new IllegalArgumentException("En un ingreso debe seleccionar el almacén destino");
            }
        }
        if (movimiento.getTipo() == MovimientoTipo.TRANSFERENCIA) {
            if (movimiento.getAlmacenOrigen() == null || movimiento.getAlmacenDestino() == null) {
                throw new IllegalArgumentException("En una transferencia debe seleccionar almacén origen y destino");
            }
            if (movimiento.getAlmacenOrigen().getId().equals(movimiento.getAlmacenDestino().getId())) {
                throw new IllegalArgumentException("Origen y destino no pueden ser el mismo almacén");
            }
        }

        movimiento.setTotalItems(detalles != null ? detalles.size() : 0);

        Movimiento movGuardado = movimientoRepository.save(movimiento);

        if (detalles == null) return movGuardado;

        for (DetalleMovimiento det : detalles) {
            Producto p = productoService.buscarProductoById(det.getId().getProducto().getIdProd());
            if (p == null) continue;

            int qty = det.getCantidad();

            // Ajuste de stock según tipo
            if (movimiento.getTipo() == MovimientoTipo.INGRESO) {
                Almacen destino = movimiento.getAlmacenDestino();
                stockAlmacenService.ajustarStock(p, destino, qty);
                // mantenemos compatibilidad con el stock global
                p.setStock(p.getStock() + qty);
                productoService.guardarProducto(p);
            } else if (movimiento.getTipo() == MovimientoTipo.TRANSFERENCIA) {
                Almacen origen = movimiento.getAlmacenOrigen();
                Almacen destino = movimiento.getAlmacenDestino();
                stockAlmacenService.ajustarStock(p, origen, -qty);
                stockAlmacenService.ajustarStock(p, destino, qty);
                // stock global no cambia
            } else if (movimiento.getTipo() == MovimientoTipo.SALIDA) {
                // Para SALIDA, el ejemplo ya lo hace con Venta. Si alguien lo usa aquí, restamos global y del almacén origen (si existe).
                if (movimiento.getAlmacenOrigen() != null) {
                    stockAlmacenService.ajustarStock(p, movimiento.getAlmacenOrigen(), -qty);
                }
                p.restarExistencia(qty);
                productoService.guardarProducto(p);
            }

            // Guardamos detalle con PK compuesta correcta (movGuardado + producto)
            DetalleMovimientoId id = new DetalleMovimientoId(movGuardado, p);
            det.setId(id);
            detalleMovimientoService.guardar(det);
        }

        return movGuardado;
    }

    @Override
    public List<Movimiento> listarTodos() {
        return movimientoRepository.findAll();
    }

    @Override
    public Movimiento buscarPorId(Integer id) {
        return movimientoRepository.findById(id).orElse(null);
    }
}
