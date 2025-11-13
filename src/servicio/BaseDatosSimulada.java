package servicio;

import modelo.Usuario;
import java.util.ArrayList;
import java.util.List;

public class BaseDatosSimulada {
    private List<Usuario> usuarios;

    public BaseDatosSimulada() {
    usuarios = new ArrayList<>();
    // Usuarios de prueba con nombres peruanos
    usuarios.add(new Usuario("admin", "admin123", "administrador"));
    usuarios.add(new Usuario("chef", "chef123", "chef"));
    usuarios.add(new Usuario("mesero", "mesero123", "mesero"));
    usuarios.add(new Usuario("cliente", "cliente123", "cliente"));
}

    public Usuario validarUsuario(String nombreUsuario, String contraseña) {
        for (Usuario usuario : usuarios) {
            if (usuario.getNombreUsuario().equals(nombreUsuario) && 
                usuario.getContraseña().equals(contraseña)) {
                return usuario;
            }
        }
        return null;
    }
}