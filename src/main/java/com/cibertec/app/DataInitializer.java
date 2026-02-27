package com.cibertec.app;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.cibertec.app.entity.Almacen;
import com.cibertec.app.entity.Categoria;
import com.cibertec.app.entity.Producto;
import com.cibertec.app.entity.Rol;
import com.cibertec.app.entity.StockAlmacen;
import com.cibertec.app.entity.Usuario;
import com.cibertec.app.repository.AlmacenRepository;
import com.cibertec.app.repository.CategoriaRepository;
import com.cibertec.app.repository.ProductoRepository;
import com.cibertec.app.repository.RolRepository;
import com.cibertec.app.repository.StockAlmacenRepository;
import com.cibertec.app.repository.UsuarioRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AlmacenRepository almacenRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;
    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final StockAlmacenRepository stockAlmacenRepository;

    public DataInitializer(
            AlmacenRepository almacenRepository,
            RolRepository rolRepository,
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            ProductoRepository productoRepository,
            StockAlmacenRepository stockAlmacenRepository
    ) {
        this.almacenRepository = almacenRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.stockAlmacenRepository = stockAlmacenRepository;
    }

    @Override
    public void run(String... args) {

        // Roles base (importante para /register)
        if (rolRepository.count() == 0) {
            Rol admin = new Rol();
            admin.setDescripcion("ADMIN");
            rolRepository.save(admin);

            Rol user = new Rol();
            user.setDescripcion("USER");
            rolRepository.save(user);
        }

        // Usuario demo (para poder entrar rápido)
        if (usuarioRepository.count() == 0) {
            Rol rolUser = rolRepository.findAll().stream()
                    .filter(r -> "USER".equalsIgnoreCase(r.getDescripcion()))
                    .findFirst()
                    .orElse(null);

            Usuario u = new Usuario();
            u.setNombres("Usuario");
            u.setApellidos("Demo");
            u.setUsername("demo");
            u.setClave("demo");
            u.setRol(rolUser);
            usuarioRepository.save(u);
        }

        // Almacenes base
        if (almacenRepository.count() == 0) {
            almacenRepository.save(new Almacen("ALM-PRIN", "Almacén Principal", "Sede Central"));
            almacenRepository.save(new Almacen("ALM-SEC", "Almacén Secundario", "Sede Alterna"));
        }

        // Categorías base
        if (categoriaRepository.count() == 0) {
            Categoria c1 = new Categoria();
            c1.setId("C01");
            c1.setDescripcion("General");
            categoriaRepository.save(c1);

            Categoria c2 = new Categoria();
            c2.setId("C02");
            c2.setDescripcion("Ferretería");
            categoriaRepository.save(c2);
        }

        // Productos demo
        if (productoRepository.count() == 0) {
            Categoria cat = categoriaRepository.findAll().get(0);

            Producto p1 = new Producto();
            p1.setCodigo("P-0001");
            p1.setDescripcion("Caja plástica apilable");
            p1.setPrecioCompra(new BigDecimal("12.50"));
            p1.setPrecioVenta(new BigDecimal("0"));
            p1.setStock(0);
            p1.setStockMinimo(5);
            p1.setCategoria(cat);
            productoRepository.save(p1);

            Producto p2 = new Producto();
            p2.setCodigo("P-0002");
            p2.setDescripcion("Cinta de embalaje 48mm");
            p2.setPrecioCompra(new BigDecimal("3.40"));
            p2.setPrecioVenta(new BigDecimal("0"));
            p2.setStock(0);
            p2.setStockMinimo(10);
            p2.setCategoria(cat);
            productoRepository.save(p2);
        }

        // Stock inicial por almacén (opcional)
        if (stockAlmacenRepository.count() == 0) {
            var almacenes = almacenRepository.findAll();
            var productos = productoRepository.findAll();
            if (!almacenes.isEmpty() && !productos.isEmpty()) {
                StockAlmacen s1 = new StockAlmacen(productos.get(0), almacenes.get(0), 8);
                stockAlmacenRepository.save(s1);
                // reflejamos stock global
                Producto prod = productos.get(0);
                prod.setStock(8);
                productoRepository.save(prod);
            }
        }
    }
}
