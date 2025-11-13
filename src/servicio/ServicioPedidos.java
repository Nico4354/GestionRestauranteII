package servicio;

import modelo.Pedido;
import modelo.ItemPedido;
import modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ServicioPedidos {
    private List<Pedido> pedidos;
    private int nextId;

    public ServicioPedidos() {
        pedidos = new ArrayList<>();
        nextId = 1;
        // Crear algunos pedidos de ejemplo
        inicializarPedidosEjemplo();
    }

    private void inicializarPedidosEjemplo() {
        ServicioProductos servicioProductos = new ServicioProductos();
        List<Producto> productos = servicioProductos.obtenerTodosProductos();

        // Pedido 1
        Pedido pedido1 = crearPedido(1, "Javier Huamán");
        pedido1.agregarItem(new ItemPedido(productos.get(0), 2, "Poco picante"));
        pedido1.agregarItem(new ItemPedido(productos.get(5), 1, ""));
        pedido1.setEstado("En preparación");

        // Pedido 2
        Pedido pedido2 = crearPedido(3, "Ana Torres");
        pedido2.agregarItem(new ItemPedido(productos.get(1), 1, "Bien cocido"));
        pedido2.agregarItem(new ItemPedido(productos.get(10), 2, ""));
        pedido2.setEstado("Listo");
    }

    public List<Pedido> obtenerTodosLosPedidos() {
        return new ArrayList<>(pedidos);
    }

    public Pedido buscarPedidoPorId(int id) {
        for (Pedido pedido : pedidos) {
            if (pedido.getId() == id) {
                return pedido;
            }
        }
        return null;
    }

    public List<Pedido> obtenerPedidosPorMesa(int numeroMesa) {
        List<Pedido> pedidosMesa = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getNumeroMesa() == numeroMesa) {
                pedidosMesa.add(pedido);
            }
        }
        return pedidosMesa;
    }

    public List<Pedido> obtenerPedidosPorEstado(String estado) {
        List<Pedido> pedidosEstado = new ArrayList<>();
        for (Pedido pedido : pedidos) {
            if (pedido.getEstado().equals(estado)) {
                pedidosEstado.add(pedido);
            }
        }
        return pedidosEstado;
    }

    public Pedido crearPedido(int numeroMesa, String mesero) {
        Pedido nuevoPedido = new Pedido(nextId++, numeroMesa, mesero);
        pedidos.add(nuevoPedido);
        return nuevoPedido;
    }

    public boolean actualizarPedido(Pedido pedidoActualizado) {
        for (int i = 0; i < pedidos.size(); i++) {
            Pedido pedido = pedidos.get(i);
            if (pedido.getId() == pedidoActualizado.getId()) {
                pedidos.set(i, pedidoActualizado);
                return true;
            }
        }
        return false;
    }

    public boolean cancelarPedido(int id) {
        Pedido pedido = buscarPedidoPorId(id);
        if (pedido != null) {
            pedido.setEstado("Cancelado");
            return true;
        }
        return false;
    }
}