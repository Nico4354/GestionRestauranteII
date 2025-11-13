package servicio;

import modelo.Ingrediente;
import java.util.ArrayList;
import java.util.List;

public class ServicioInventario {
    private List<Ingrediente> ingredientes;
    private int nextId;

    public ServicioInventario() {
        ingredientes = new ArrayList<>();
        nextId = 1;
        inicializarIngredientesEjemplo();
    }

    private void inicializarIngredientesEjemplo() {
        // Ingredientes típicos de cocina peruana
        agregarIngrediente(new Ingrediente(0, "Pescado fresco", "kg", 15.5, 5.0, "Pescados y Mariscos"));
        agregarIngrediente(new Ingrediente(0, "Limón", "kg", 8.0, 3.0, "Frutas y Verduras"));
        agregarIngrediente(new Ingrediente(0, "Cebolla roja", "kg", 12.0, 4.0, "Frutas y Verduras"));
        agregarIngrediente(new Ingrediente(0, "Ají amarillo", "kg", 2.5, 1.0, "Especias y Condimentos"));
        agregarIngrediente(new Ingrediente(0, "Cilantro", "atado", 10.0, 3.0, "Hierbas"));
        agregarIngrediente(new Ingrediente(0, "Papa amarilla", "kg", 25.0, 10.0, "Tubérculos"));
        agregarIngrediente(new Ingrediente(0, "Lomo de res", "kg", 8.0, 3.0, "Carnes"));
        agregarIngrediente(new Ingrediente(0, "Pollo", "kg", 12.0, 5.0, "Carnes"));
        agregarIngrediente(new Ingrediente(0, "Arroz", "kg", 20.0, 8.0, "Granos"));
        agregarIngrediente(new Ingrediente(0, "Maíz morado", "kg", 3.0, 1.0, "Granos"));
        agregarIngrediente(new Ingrediente(0, "Leche evaporada", "l", 10.0, 4.0, "Lácteos"));
        agregarIngrediente(new Ingrediente(0, "Queso fresco", "kg", 5.0, 2.0, "Lácteos"));
    }

    public List<Ingrediente> obtenerTodosIngredientes() {
        return new ArrayList<>(ingredientes);
    }

    // MÉTODO QUE FALTA - AGREGAR ESTE
    public List<Ingrediente> obtenerIngredientesBajos() {
        List<Ingrediente> ingredientesBajos = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientes) {
            if (ingrediente.necesitaReabastecer()) {
                ingredientesBajos.add(ingrediente);
            }
        }
        return ingredientesBajos;
    }

    public boolean agregarIngrediente(Ingrediente ingrediente) {
        ingrediente.setId(nextId++);
        ingredientes.add(ingrediente);
        return true;
    }

    public boolean actualizarIngrediente(Ingrediente ingredienteActualizado) {
        for (int i = 0; i < ingredientes.size(); i++) {
            Ingrediente ing = ingredientes.get(i);
            if (ing.getId() == ingredienteActualizado.getId()) {
                ingredientes.set(i, ingredienteActualizado);
                return true;
            }
        }
        return false;
    }
}