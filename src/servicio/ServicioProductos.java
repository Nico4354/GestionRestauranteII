package servicio;

import modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ServicioProductos {
    private List<Producto> productos;
    private int nextId;

    public ServicioProductos() {
        productos = new ArrayList<>();
        nextId = 1;
        // Productos de ejemplo
        inicializarProductosEjemplo();
    }

    private void inicializarProductosEjemplo() {
    // Platos principales peruanos
    agregarProducto(new Producto(0, "Ceviche Mixto", "Pescado y mariscos marinados en limón, cebolla, ají y cilantro", 25.99, "Plato Principal", true));
    agregarProducto(new Producto(0, "Lomo Saltado", "Salteado de lomo de res con cebolla, tomate, ají y papas fritas", 18.50, "Plato Principal", true));
    agregarProducto(new Producto(0, "Aji de Gallina", "Pollo desmenuzado en salsa de ají amarillo, nueces y queso", 16.99, "Plato Principal", true));
    agregarProducto(new Producto(0, "Pachamanca", "Carnes y verduras cocidas bajo tierra al estilo andino", 32.99, "Plato Principal", true));
    agregarProducto(new Producto(0, "Arroz con Mariscos", "Arroz verde con mix de mariscos frescos", 22.50, "Plato Principal", true));
    
    // Entradas peruanas
    agregarProducto(new Producto(0, "Causa Limeña", "Pasta de papa amarilla con ají, rellena de pollo o atún", 12.50, "Entrada", true));
    agregarProducto(new Producto(0, "Papa a la Huancaína", "Papas cocidas en salsa de queso y ají amarillo", 10.99, "Entrada", true));
    agregarProducto(new Producto(0, "Anticuchos", "Brochetas de corazón de res con papa y choclo", 14.99, "Entrada", true));
    
    // Postres peruanos
    agregarProducto(new Producto(0, "Suspiro a la Limeña", "Dulce de manjar blanco y merengue de vino oporto", 8.50, "Postre", true));
    agregarProducto(new Producto(0, "Mazamorra Morada", "Postre de maíz morado con frutas secas", 7.99, "Postre", true));
    agregarProducto(new Producto(0, "Picarones", "Anillos fritos de camote y zapato con miel de chancaca", 9.50, "Postre", true));
    
    // Bebidas peruanas
    agregarProducto(new Producto(0, "Pisco Sour", "Cóctel nacional con pisco, limón, clara de huevo y jarabe", 12.99, "Bebida", true));
    agregarProducto(new Producto(0, "Chicha Morada", "Bebida de maíz morado con frutas", 5.50, "Bebida", true));
    agregarProducto(new Producto(0, "Inca Kola", "Refresco peruano sabor a hierba luisa", 4.50, "Bebida", true));
    agregarProducto(new Producto(0, "Emoliente", "Bebida caliente de hierbas andinas", 6.50, "Bebida", true));
}

    public List<Producto> obtenerTodosProductos() {
        return new ArrayList<>(productos);
    }

    public List<Producto> obtenerProductosPorCategoria(String categoria) {
        List<Producto> resultado = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getCategoria().equals(categoria) && producto.isDisponible()) {
                resultado.add(producto);
            }
        }
        return resultado;
    }

    public List<String> obtenerCategorias() {
        List<String> categorias = new ArrayList<>();
        for (Producto producto : productos) {
            if (!categorias.contains(producto.getCategoria())) {
                categorias.add(producto.getCategoria());
            }
        }
        return categorias;
    }

    public boolean agregarProducto(Producto producto) {
        producto.setId(nextId++);
        productos.add(producto);
        return true;
    }

    public boolean eliminarProducto(int id) {
        return productos.removeIf(prod -> prod.getId() == id);
    }

    public boolean actualizarProducto(Producto productoActualizado) {
        for (int i = 0; i < productos.size(); i++) {
            Producto prod = productos.get(i);
            if (prod.getId() == productoActualizado.getId()) {
                productos.set(i, productoActualizado);
                return true;
            }
        }
        return false;
    }

    public Producto buscarProductoPorId(int id) {
        for (Producto prod : productos) {
            if (prod.getId() == id) {
                return prod;
            }
        }
        return null;
    }
}