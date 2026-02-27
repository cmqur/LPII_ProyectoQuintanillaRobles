package com.cibertec.app.controller;

import java.util.ArrayList;
import java.util.Date;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.cibertec.app.entity.DetalleMovimiento;
import com.cibertec.app.entity.DetalleMovimientoId;
import com.cibertec.app.entity.Movimiento;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.entity.ProductoParaMover;
import com.cibertec.app.entity.Usuario;
import com.cibertec.app.service.AlmacenService;
import com.cibertec.app.service.DetalleMovimientoService;
import com.cibertec.app.service.MovimientoService;
import com.cibertec.app.service.ProductoService;
import com.cibertec.app.service.UsuarioService;

@Controller
public class MovimientoController {

    @Autowired private ProductoService productoService;
    @Autowired private MovimientoService movimientoService;
    @Autowired private AlmacenService almacenService;
    @Autowired private UsuarioService usuarioService;
    @Autowired private DetalleMovimientoService detalleMovimientoService;

    @GetMapping("/movimiento")
    public String list(Model model) {
        model.addAttribute("movimientos", movimientoService.listarTodos());
        return "movimiento/index";
    }


    @GetMapping("/movimiento/ver/{id}")
    public String ver(@PathVariable Integer id, Model model) {
        Movimiento mov = movimientoService.buscarPorId(id);
        if (mov == null) return "redirect:/movimiento";
        model.addAttribute("movimiento", mov);
        model.addAttribute("detalles", detalleMovimientoService.listarPorMovimiento(mov));
        return "movimiento/view";
    }
    private ArrayList<ProductoParaMover> obtenerCarrito(HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        ArrayList<ProductoParaMover> carrito = (ArrayList<ProductoParaMover>) request.getSession().getAttribute("carritoMovimiento");
        if (carrito == null) carrito = new ArrayList<>();
        return carrito;
    }

    private void guardarCarrito(ArrayList<ProductoParaMover> carrito, HttpServletRequest request) {
        request.getSession().setAttribute("carritoMovimiento", carrito);
    }

    private void limpiarCarrito(HttpServletRequest request) {
        guardarCarrito(new ArrayList<>(), request);
    }

    @GetMapping("/movimiento/limpiar")
    public String limpiar(HttpServletRequest request, RedirectAttributes redirectAttrs) {
        limpiarCarrito(request);
        redirectAttrs.addFlashAttribute("mensaje", "Carrito de movimiento limpiado")
                .addFlashAttribute("clase", "info");
        return "redirect:/movimiento/new";
    }

    @PostMapping("/movimiento/quitar/{indice}")
    public String quitar(@PathVariable int indice, HttpServletRequest request) {
        ArrayList<ProductoParaMover> carrito = obtenerCarrito(request);
        if (carrito != null && carrito.size() > 0 && indice >= 0 && indice < carrito.size()) {
            carrito.remove(indice);
            guardarCarrito(carrito, request);
        }
        return "redirect:/movimiento/new";
    }

    @GetMapping("/movimiento/new")
    public String create(Model model, HttpServletRequest request) {
        model.addAttribute("movimiento", new Movimiento());
        model.addAttribute("producto", new Producto());
        model.addAttribute("almacenList", almacenService.listarTodos());
        model.addAttribute("usuarioList", usuarioService.listarTodosUsuario());

        ArrayList<ProductoParaMover> carrito = obtenerCarrito(request);
        model.addAttribute("totalItems", carrito.size());
        return "movimiento/create";
    }

    @PostMapping("/movimiento/agregar")
    public String agregar(@ModelAttribute Producto producto,
                          @RequestParam(defaultValue = "1") int cantidad,
                          HttpServletRequest request,
                          RedirectAttributes redirectAttrs) {

        if (cantidad <= 0) cantidad = 1;

        Producto p = productoService.buscarProductoByCodigo(producto.getCodigo());
        if (p == null) {
            redirectAttrs.addFlashAttribute("mensaje", "El producto con el código " + producto.getCodigo() + " no existe")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/movimiento/new";
        }

        ArrayList<ProductoParaMover> carrito = obtenerCarrito(request);

        boolean encontrado = false;
        for (ProductoParaMover item : carrito) {
            if (item.getCodigo().equalsIgnoreCase(p.getCodigo())) {
                item.setCantidad(item.getCantidad() + cantidad);
                encontrado = true;
                break;
            }
        }
        if (!encontrado) {
            carrito.add(new ProductoParaMover(
                    p.getIdProd(),
                    p.getCodigo(),
                    p.getDescripcion(),
                    p.getPrecioCompra(),
                    p.getStock(),
                    cantidad
            ));
        }
        guardarCarrito(carrito, request);
        return "redirect:/movimiento/new";
    }

    @PostMapping("/movimiento/terminar")
    public String terminar(HttpServletRequest request,
                           RedirectAttributes redirectAttrs,
                           @ModelAttribute("movimiento") Movimiento movimiento,
                           @RequestParam(required = false) Long idUsuario) {

        ArrayList<ProductoParaMover> carrito = obtenerCarrito(request);
        if (carrito == null || carrito.isEmpty()) {
            redirectAttrs.addFlashAttribute("mensaje", "No hay productos agregados al movimiento")
                    .addFlashAttribute("clase", "warning");
            return "redirect:/movimiento/new";
        }

        movimiento.setFecha(new Date());

        if (idUsuario != null) {
            Usuario u = usuarioService.buscarUsuarioById(idUsuario);
            movimiento.setUsuario(u);
        }

        // Convertimos el carrito a detalles
        ArrayList<DetalleMovimiento> detalles = new ArrayList<>();
        for (ProductoParaMover item : carrito) {
            Producto p = productoService.buscarProductoById(item.getIdProd());
            if (p == null) continue;
            DetalleMovimientoId id = new DetalleMovimientoId();
            id.setMovimiento(movimiento); // temporal, se reemplaza en el service al guardar
            id.setProducto(p);
            DetalleMovimiento det = new DetalleMovimiento(id, item.getCantidad());
            detalles.add(det);
        }

        try {
            movimientoService.guardar(movimiento, detalles);
        } catch (Exception ex) {
            redirectAttrs.addFlashAttribute("mensaje", ex.getMessage())
                    .addFlashAttribute("clase", "danger");
            return "redirect:/movimiento/new";
        }

        limpiarCarrito(request);
        redirectAttrs.addFlashAttribute("mensaje", "Movimiento registrado correctamente")
                .addFlashAttribute("clase", "success");
        return "redirect:/movimiento";
    }
}
