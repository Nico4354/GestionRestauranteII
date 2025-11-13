package servicio;

import modelo.Orden;
import modelo.EstadoOrden;
import modelo.ItemOrden;
import modelo.Producto;
import java.util.ArrayList;
import java.util.List;

public class ServicioOrdenes {
    private List<Orden> ordenes;
    private int nextId;

    public ServicioOrdenes() {
        ordenes = new ArrayList<>();
        nextId = 1;
        inicializarOrdenesEjemplo();
    }

    private void inicializarOrdenesEjemplo() {
        ServicioProductos servicioProductos = new ServicioProductos();
        List<Producto> productos = servicioProductos.obtenerTodosProductos();

        // Orden 1
        Orden orden1 = new Orden(nextId++, 1, "Javier Huamán");
        orden1.agregarItem(new ItemOrden(productos.get(0), 2, "Poco picante"));
        orden1.agregarItem(new ItemOrden(productos.get(5), 1, ""));
        ordenes.add(orden1);

        // Orden 2
        Orden orden2 = new Orden(nextId++, 3, "Javier Huamán");
        orden2.agregarItem(new ItemOrden(productos.get(1), 1, "Bien cocido"));
        orden2.agregarItem(new ItemOrden(productos.get(2), 1, ""));
        orden2.agregarItem(new ItemOrden(productos.get(10), 2, ""));
        orden2.setEstado(EstadoOrden.EN_PREPARACION);
        ordenes.add(orden2);

        // Orden 3
        Orden orden3 = new Orden(nextId++, 2, "Ana Torres");
        orden3.agregarItem(new ItemOrden(productos.get(3), 1, ""));
        orden3.agregarItem(new ItemOrden(productos.get(7), 2, "Sin gluten"));
        orden3.setEstado(EstadoOrden.LISTA);
        ordenes.add(orden3);
    }

    public List<Orden> obtenerTodasLasOrdenes() {
        return new ArrayList<>(ordenes);
    }

    public Orden buscarOrdenPorId(int id) {
        for (Orden orden : ordenes) {
            if (orden.getId() == id) {
                return orden;
            }
        }
        return null;
    }

    public boolean actualizarOrden(Orden ordenActualizada) {
        for (int i = 0; i < ordenes.size(); i++) {
            Orden orden = ordenes.get(i);
            if (orden.getId() == ordenActualizada.getId()) {
                ordenes.set(i, ordenActualizada);
                return true;
            }
        }
        return false;
    }
}