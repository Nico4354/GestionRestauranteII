package servicio;

import modelo.Empleado;
import java.util.ArrayList;
import java.util.List;

public class ServicioEmpleados {
    private List<Empleado> empleados;

   public ServicioEmpleados() {
    empleados = new ArrayList<>();
    // Empleados con nombres peruanos
    empleados.add(new Empleado("Carlos Rodríguez", "admin", "admin123", "administrador", "555-0001", "admin@saboresdelinca.com"));
    empleados.add(new Empleado("María Quispe", "chef", "chef123", "chef", "555-0002", "chef@saboresdelinca.com"));
    empleados.add(new Empleado("Javier Huamán", "mesero", "mesero123", "mesero", "555-0003", "mesero@saboresdelinca.com"));
}

    public List<Empleado> obtenerTodosEmpleados() {
        return new ArrayList<>(empleados);
    }

    public boolean agregarEmpleado(Empleado empleado) {
        // Verificar si el usuario ya existe
        for (Empleado emp : empleados) {
            if (emp.getUsuario().equals(empleado.getUsuario())) {
                return false;
            }
        }
        empleados.add(empleado);
        return true;
    }

    public boolean eliminarEmpleado(String usuario) {
        return empleados.removeIf(emp -> emp.getUsuario().equals(usuario));
    }

    public boolean actualizarEmpleado(Empleado empleadoActualizado) {
        for (int i = 0; i < empleados.size(); i++) {
            Empleado emp = empleados.get(i);
            if (emp.getUsuario().equals(empleadoActualizado.getUsuario())) {
                empleados.set(i, empleadoActualizado);
                return true;
            }
        }
        return false;
    }

    public Empleado buscarEmpleadoPorUsuario(String usuario) {
        for (Empleado emp : empleados) {
            if (emp.getUsuario().equals(usuario)) {
                return emp;
            }
        }
        return null;
    }
}
