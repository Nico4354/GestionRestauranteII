package vista;

import javax.swing.SpinnerNumberModel;
import javax.swing.JSpinner;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import modelo.Mesa;
import modelo.Pedido;
import servicio.ServicioMesas;
import servicio.ServicioPedidos;
import java.util.List;
import modelo.Usuario;
import modelo.Empleado;
import modelo.Producto;
import modelo.Orden;
import modelo.ItemOrden;
import modelo.EstadoOrden;
import modelo.Ingrediente;
import servicio.ServicioEmpleados;
import servicio.ServicioProductos;
import servicio.ServicioOrdenes;
import servicio.ServicioInventario;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VentanaPrincipal extends JFrame {
    private Usuario usuario;
    private JPanel panelContenido;
    private CardLayout cardLayout;

    public VentanaPrincipal(Usuario usuario) {
        this.usuario = usuario;
        configurarVentana();
        inicializarComponentes();
        mostrarPanelSegunRol();
    }

    private void configurarVentana() {
        setTitle("Sistema de Gestión - Restaurante Peruano 'Sabores del Inca' - Usuario: " + usuario.getNombreUsuario());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    private void inicializarComponentes() {
        // Panel principal con BorderLayout
        setLayout(new BorderLayout());

        // Panel de cabecera
        JPanel panelCabecera = crearPanelCabecera();
        add(panelCabecera, BorderLayout.NORTH);

        // Panel de navegación lateral
        JPanel panelNavegacion = crearPanelNavegacion();
        add(panelNavegacion, BorderLayout.WEST);

        // Panel de contenido principal (CardLayout)
        cardLayout = new CardLayout();
        panelContenido = new JPanel(cardLayout);
        add(panelContenido, BorderLayout.CENTER);

        // Inicializar todos los paneles
        inicializarPaneles();
    }

    private JPanel crearPanelCabecera() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(193, 39, 45)); // Rojo peruano
        panel.setPreferredSize(new Dimension(getWidth(), 80));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Logo y título
        JLabel etiquetaTitulo = new JLabel("SABORES DEL INCA - RESTAURANTE PERUANO");
        etiquetaTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        etiquetaTitulo.setForeground(Color.WHITE);

        // Información del usuario
        JLabel etiquetaUsuario = new JLabel("Usuario: " + usuario.getNombreUsuario() + " | Rol: " + usuario.getRol());
        etiquetaUsuario.setFont(new Font("Arial", Font.PLAIN, 14));
        etiquetaUsuario.setForeground(Color.WHITE);

        panel.add(etiquetaTitulo, BorderLayout.WEST);
        panel.add(etiquetaUsuario, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelNavegacion() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(240, 240, 240));
        panel.setPreferredSize(new Dimension(200, getHeight()));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        // Botones según el rol
        String rol = usuario.getRol();
        
        switch (rol) {
            case "administrador":
                agregarBotonNavegacion(panel, "Dashboard", "dashboard");
                agregarBotonNavegacion(panel, "Gestión de Usuarios", "gestion_usuarios");
                agregarBotonNavegacion(panel, "Gestión de Menú", "gestion_menu");
                agregarBotonNavegacion(panel, "Reportes", "reportes");
                agregarBotonNavegacion(panel, "Configuración", "configuracion");
                break;
                
            case "chef":
                agregarBotonNavegacion(panel, "Órdenes Pendientes", "ordenes_pendientes");
                agregarBotonNavegacion(panel, "Historial de Órdenes", "historial_ordenes");
                agregarBotonNavegacion(panel, "Inventario Cocina", "inventario_cocina");
                break;
                
            case "mesero":
                agregarBotonNavegacion(panel, "Tomar Pedido", "tomar_pedido");
                agregarBotonNavegacion(panel, "Mesas", "gestion_mesas");
                agregarBotonNavegacion(panel, "Órdenes Activas", "ordenes_activas");
                break;
                
            case "cliente":
                agregarBotonNavegacion(panel, "Ver Menú", "ver_menu");
                agregarBotonNavegacion(panel, "Realizar Pedido", "realizar_pedido");
                agregarBotonNavegacion(panel, "Estado Pedido", "estado_pedido");
                break;
        }

        // Espacio flexible
        panel.add(Box.createVerticalGlue());
        
        // Botón de salir
        JButton botonSalir = new JButton("Cerrar Sesión");
        botonSalir.setBackground(new Color(220, 53, 69));
        botonSalir.setForeground(Color.WHITE);
        botonSalir.setMaximumSize(new Dimension(180, 40));
        botonSalir.addActionListener(e -> cerrarSesion());
        panel.add(botonSalir);

        return panel;
    }

    private void agregarBotonNavegacion(JPanel panel, String texto, String comando) {
        JButton boton = new JButton(texto);
        boton.setAlignmentX(Component.LEFT_ALIGNMENT);
        boton.setMaximumSize(new Dimension(180, 45));
        boton.setBackground(new Color(200, 200, 200));
        boton.setFocusPainted(false);
        boton.setMargin(new Insets(10, 10, 10, 10));
        
        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(panelContenido, comando);
            }
        });
        
        panel.add(boton);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
    }

    private void inicializarPaneles() {
        // Panel de bienvenida (dashboard)
        panelContenido.add(crearPanelBienvenida(), "dashboard");

        // Paneles según rol
        String rol = usuario.getRol();
        
        if (rol.equals("administrador")) {
            panelContenido.add(crearPanelGestionUsuarios(), "gestion_usuarios");
            panelContenido.add(crearPanelGestionMenu(), "gestion_menu");
            panelContenido.add(crearPanelReportes(), "reportes");
            panelContenido.add(crearPanelConfiguracion(), "configuracion");
        } else if (rol.equals("chef")) {
            panelContenido.add(crearPanelOrdenesPendientes(), "ordenes_pendientes");
            panelContenido.add(crearPanelHistorialOrdenes(), "historial_ordenes");
            panelContenido.add(crearPanelInventarioCocina(), "inventario_cocina");
        } else if (rol.equals("mesero")) {
            panelContenido.add(crearPanelTomarPedido(), "tomar_pedido");
            panelContenido.add(crearPanelGestionMesas(), "gestion_mesas");
            panelContenido.add(crearPanelOrdenesActivas(), "ordenes_activas");
        } else if (rol.equals("cliente")) {
            panelContenido.add(crearPanelVerMenu(), "ver_menu");
            panelContenido.add(crearPanelRealizarPedido(), "realizar_pedido");
            panelContenido.add(crearPanelEstadoPedido(), "estado_pedido");
        }
    }

    private void mostrarPanelSegunRol() {
        cardLayout.show(panelContenido, "dashboard");
    }

    // ==================== PANELES DE ADMINISTRADOR ====================

    private JPanel crearPanelBienvenida() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel etiqueta = new JLabel("¡Bienvenido al Sistema de Gestión!", JLabel.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 28));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        JLabel etiquetaRol = new JLabel("Rol: " + usuario.getRol(), JLabel.CENTER);
        etiquetaRol.setFont(new Font("Arial", Font.PLAIN, 18));
        etiquetaRol.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        panel.add(etiqueta, BorderLayout.NORTH);
        panel.add(etiquetaRol, BorderLayout.CENTER);
        
        return panel;
    }

    // ==================== GESTIÓN DE USUARIOS ====================

    private JPanel crearPanelGestionUsuarios() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Servicio de empleados
        ServicioEmpleados servicioEmpleados = new ServicioEmpleados();

        // Tabla de empleados
        String[] columnas = {"Nombre", "Usuario", "Rol", "Teléfono", "Email"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaEmpleados = new JTable(modeloTabla);
        actualizarTablaEmpleados(modeloTabla, servicioEmpleados);

        // Botones de gestión
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonAgregar = new JButton("Agregar Empleado");
        JButton botonEditar = new JButton("Editar Empleado");
        JButton botonEliminar = new JButton("Eliminar Empleado");

        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);

        // Agregar componentes al panel
        panelPrincipal.add(new JScrollPane(tablaEmpleados), BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Acción para agregar empleado
        botonAgregar.addActionListener(e -> {
            mostrarDialogoAgregarEmpleado(servicioEmpleados, modeloTabla);
        });

        // Acción para editar empleado
        botonEditar.addActionListener(e -> {
            int filaSeleccionada = tablaEmpleados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un empleado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String usuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            Empleado empleado = servicioEmpleados.buscarEmpleadoPorUsuario(usuario);
            if (empleado != null) {
                mostrarDialogoEditarEmpleado(empleado, servicioEmpleados, modeloTabla);
            }
        });

        // Acción para eliminar empleado
        botonEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaEmpleados.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un empleado", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String usuario = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 0);
            
            int confirmacion = JOptionPane.showConfirmDialog(
                panelPrincipal, 
                "¿Está seguro de eliminar al empleado: " + nombre + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (servicioEmpleados.eliminarEmpleado(usuario)) {
                    actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
                    JOptionPane.showMessageDialog(panelPrincipal, "Empleado eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar el empleado", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panelPrincipal;
    }

    private void actualizarTablaEmpleados(DefaultTableModel modeloTabla, ServicioEmpleados servicioEmpleados) {
        modeloTabla.setRowCount(0);
        for (Empleado empleado : servicioEmpleados.obtenerTodosEmpleados()) {
            Object[] fila = {
                empleado.getNombre(),
                empleado.getUsuario(),
                empleado.getRol(),
                empleado.getTelefono(),
                empleado.getEmail()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoAgregarEmpleado(ServicioEmpleados servicioEmpleados, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Agregar Nuevo Empleado", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);

        JTextField campoNombre = new JTextField();
        JTextField campoUsuario = new JTextField();
        JPasswordField campoContraseña = new JPasswordField();
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"administrador", "chef", "mesero"});
        JTextField campoTelefono = new JTextField();
        JTextField campoEmail = new JTextField();

        dialogo.add(new JLabel("Nombre Completo:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Usuario:"));
        dialogo.add(campoUsuario);
        dialogo.add(new JLabel("Contraseña:"));
        dialogo.add(campoContraseña);
        dialogo.add(new JLabel("Rol:"));
        dialogo.add(comboRol);
        dialogo.add(new JLabel("Teléfono:"));
        dialogo.add(campoTelefono);
        dialogo.add(new JLabel("Email:"));
        dialogo.add(campoEmail);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");

        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String usuario = campoUsuario.getText();
            String contraseña = new String(campoContraseña.getPassword());
            String rol = (String) comboRol.getSelectedItem();
            String telefono = campoTelefono.getText();
            String email = campoEmail.getText();

            if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Empleado nuevoEmpleado = new Empleado(nombre, usuario, contraseña, rol, telefono, email);
            if (servicioEmpleados.agregarEmpleado(nuevoEmpleado)) {
                actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Empleado agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "El usuario ya existe", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditarEmpleado(Empleado empleado, ServicioEmpleados servicioEmpleados, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Editar Empleado", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);

        JTextField campoNombre = new JTextField(empleado.getNombre());
        JTextField campoUsuario = new JTextField(empleado.getUsuario());
        campoUsuario.setEditable(false); // No se puede cambiar el usuario
        JPasswordField campoContraseña = new JPasswordField(empleado.getContraseña());
        JComboBox<String> comboRol = new JComboBox<>(new String[]{"administrador", "chef", "mesero"});
        comboRol.setSelectedItem(empleado.getRol());
        JTextField campoTelefono = new JTextField(empleado.getTelefono());
        JTextField campoEmail = new JTextField(empleado.getEmail());

        dialogo.add(new JLabel("Nombre Completo:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Usuario:"));
        dialogo.add(campoUsuario);
        dialogo.add(new JLabel("Contraseña:"));
        dialogo.add(campoContraseña);
        dialogo.add(new JLabel("Rol:"));
        dialogo.add(comboRol);
        dialogo.add(new JLabel("Teléfono:"));
        dialogo.add(campoTelefono);
        dialogo.add(new JLabel("Email:"));
        dialogo.add(campoEmail);

        JButton botonGuardar = new JButton("Guardar Cambios");
        JButton botonCancelar = new JButton("Cancelar");

        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String contraseña = new String(campoContraseña.getPassword());
            String rol = (String) comboRol.getSelectedItem();
            String telefono = campoTelefono.getText();
            String email = campoEmail.getText();

            if (nombre.isEmpty() || contraseña.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            empleado.setNombre(nombre);
            empleado.setContraseña(contraseña);
            empleado.setRol(rol);
            empleado.setTelefono(telefono);
            empleado.setEmail(email);

            if (servicioEmpleados.actualizarEmpleado(empleado)) {
                actualizarTablaEmpleados(modeloTabla, servicioEmpleados);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Empleado actualizado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar el empleado", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    // ==================== GESTIÓN DEL MENÚ ====================

    private JPanel crearPanelGestionMenu() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Servicio de productos
        ServicioProductos servicioProductos = new ServicioProductos();

        // Tabla de productos
        String[] columnas = {"ID", "Nombre", "Descripción", "Precio (S/)", "Categoría", "Disponible"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaProductos = new JTable(modeloTabla);
        actualizarTablaProductos(modeloTabla, servicioProductos);

        // Botones de gestión
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonAgregar = new JButton("Agregar Producto");
        JButton botonEditar = new JButton("Editar Producto");
        JButton botonEliminar = new JButton("Eliminar Producto");

        panelBotones.add(botonAgregar);
        panelBotones.add(botonEditar);
        panelBotones.add(botonEliminar);

        // Agregar componentes al panel
        panelPrincipal.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Acción para agregar producto
        botonAgregar.addActionListener(e -> {
            mostrarDialogoAgregarProducto(servicioProductos, modeloTabla);
        });

        // Acción para editar producto
        botonEditar.addActionListener(e -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Producto producto = servicioProductos.buscarProductoPorId(id);
            if (producto != null) {
                mostrarDialogoEditarProducto(producto, servicioProductos, modeloTabla);
            }
        });

        // Acción para eliminar producto
        botonEliminar.addActionListener(e -> {
            int filaSeleccionada = tablaProductos.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione un producto", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            String nombre = (String) modeloTabla.getValueAt(filaSeleccionada, 1);
            
            int confirmacion = JOptionPane.showConfirmDialog(
                panelPrincipal, 
                "¿Está seguro de eliminar el producto: " + nombre + "?", 
                "Confirmar Eliminación", 
                JOptionPane.YES_NO_OPTION
            );

            if (confirmacion == JOptionPane.YES_OPTION) {
                if (servicioProductos.eliminarProducto(id)) {
                    actualizarTablaProductos(modeloTabla, servicioProductos);
                    JOptionPane.showMessageDialog(panelPrincipal, "Producto eliminado exitosamente");
                } else {
                    JOptionPane.showMessageDialog(panelPrincipal, "Error al eliminar el producto", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panelPrincipal;
    }

    private void actualizarTablaProductos(DefaultTableModel modeloTabla, ServicioProductos servicioProductos) {
        modeloTabla.setRowCount(0);
        for (Producto producto : servicioProductos.obtenerTodosProductos()) {
            Object[] fila = {
                producto.getId(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getCategoria(),
                producto.isDisponible() ? "Sí" : "No"
            };
            modeloTabla.addRow(fila);
        }
    }

    private void mostrarDialogoAgregarProducto(ServicioProductos servicioProductos, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Agregar Nuevo Producto", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);

        JTextField campoNombre = new JTextField();
        JTextField campoDescripcion = new JTextField();
        JTextField campoPrecio = new JTextField();
        JComboBox<String> comboCategoria = new JComboBox<>(new String[]{
            "Plato Principal", "Entrada", "Postre", "Bebida", "Sopa", "Especialidad de la Casa"
        });
        JCheckBox checkDisponible = new JCheckBox("Disponible", true);

        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Descripción:"));
        dialogo.add(campoDescripcion);
        dialogo.add(new JLabel("Precio (S/):"));
        dialogo.add(campoPrecio);
        dialogo.add(new JLabel("Categoría:"));
        dialogo.add(comboCategoria);
        dialogo.add(new JLabel("Disponible:"));
        dialogo.add(checkDisponible);

        JButton botonGuardar = new JButton("Guardar");
        JButton botonCancelar = new JButton("Cancelar");

        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String descripcion = campoDescripcion.getText();
            String precioTexto = campoPrecio.getText();
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean disponible = checkDisponible.isSelected();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto nuevoProducto = new Producto(0, nombre, descripcion, precio, categoria, disponible);
            if (servicioProductos.agregarProducto(nuevoProducto)) {
                actualizarTablaProductos(modeloTabla, servicioProductos);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al agregar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    private void mostrarDialogoEditarProducto(Producto producto, ServicioProductos servicioProductos, DefaultTableModel modeloTabla) {
        JDialog dialogo = new JDialog(this, "Editar Producto", true);
        dialogo.setLayout(new GridLayout(7, 2, 10, 10));
        dialogo.setSize(400, 350);
        dialogo.setLocationRelativeTo(this);

        JTextField campoNombre = new JTextField(producto.getNombre());
        JTextField campoDescripcion = new JTextField(producto.getDescripcion());
        JTextField campoPrecio = new JTextField(String.valueOf(producto.getPrecio()));
        JComboBox<String> comboCategoria = new JComboBox<>(new String[]{
            "Plato Principal", "Entrada", "Postre", "Bebida", "Sopa", "Especialidad de la Casa"
        });
        comboCategoria.setSelectedItem(producto.getCategoria());
        JCheckBox checkDisponible = new JCheckBox("Disponible", producto.isDisponible());

        dialogo.add(new JLabel("Nombre:"));
        dialogo.add(campoNombre);
        dialogo.add(new JLabel("Descripción:"));
        dialogo.add(campoDescripcion);
        dialogo.add(new JLabel("Precio (S/):"));
        dialogo.add(campoPrecio);
        dialogo.add(new JLabel("Categoría:"));
        dialogo.add(comboCategoria);
        dialogo.add(new JLabel("Disponible:"));
        dialogo.add(checkDisponible);

        JButton botonGuardar = new JButton("Guardar Cambios");
        JButton botonCancelar = new JButton("Cancelar");

        botonGuardar.addActionListener(ev -> {
            String nombre = campoNombre.getText();
            String descripcion = campoDescripcion.getText();
            String precioTexto = campoPrecio.getText();
            String categoria = (String) comboCategoria.getSelectedItem();
            boolean disponible = checkDisponible.isSelected();

            if (nombre.isEmpty() || descripcion.isEmpty() || precioTexto.isEmpty()) {
                JOptionPane.showMessageDialog(dialogo, "Por favor complete los campos obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double precio;
            try {
                precio = Double.parseDouble(precioTexto);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialogo, "El precio debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            producto.setNombre(nombre);
            producto.setDescripcion(descripcion);
            producto.setPrecio(precio);
            producto.setCategoria(categoria);
            producto.setDisponible(disponible);

            if (servicioProductos.actualizarProducto(producto)) {
                actualizarTablaProductos(modeloTabla, servicioProductos);
                dialogo.dispose();
                JOptionPane.showMessageDialog(this, "Producto actualizado exitosamente");
            } else {
                JOptionPane.showMessageDialog(dialogo, "Error al actualizar el producto", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        botonCancelar.addActionListener(ev -> dialogo.dispose());

        dialogo.add(botonGuardar);
        dialogo.add(botonCancelar);
        dialogo.setVisible(true);
    }

    // ==================== PANELES DEL CHEF ====================

    private JPanel crearPanelOrdenesPendientes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Servicio de órdenes
        ServicioOrdenes servicioOrdenes = new ServicioOrdenes();

        // Tabla de órdenes
        String[] columnas = {"ID", "Mesa", "Mesero", "Estado", "Total", "Hora"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaOrdenes = new JTable(modeloTabla);
        actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);

        // Panel de detalles de orden
        JPanel panelDetalles = new JPanel(new BorderLayout());
        panelDetalles.setBorder(BorderFactory.createTitledBorder("Detalles de la Orden"));
        panelDetalles.setPreferredSize(new Dimension(300, 0));

        JTextArea areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setFont(new Font("Arial", Font.PLAIN, 12));
        panelDetalles.add(new JScrollPane(areaDetalles), BorderLayout.CENTER);

        // Botones de gestión
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonPreparar = new JButton("Comenzar Preparación");
        JButton botonLista = new JButton("Marcar como Lista");
        JButton botonActualizar = new JButton("Actualizar");

        botonPreparar.setBackground(new Color(255, 193, 7));
        botonLista.setBackground(new Color(40, 167, 69));
        botonLista.setForeground(Color.WHITE);

        panelBotones.add(botonPreparar);
        panelBotones.add(botonLista);
        panelBotones.add(botonActualizar);

        // Split pane para dividir la vista
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            new JScrollPane(tablaOrdenes), panelDetalles);
        splitPane.setDividerLocation(600);

        // Agregar componentes al panel
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Mostrar detalles cuando se seleccione una orden
        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            int filaSeleccionada = tablaOrdenes.getSelectedRow();
            if (filaSeleccionada != -1) {
                int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
                Orden orden = servicioOrdenes.buscarOrdenPorId(id);
                if (orden != null) {
                    mostrarDetallesOrden(orden, areaDetalles);
                }
            }
        });

        // Acción para comenzar preparación
        botonPreparar.addActionListener(e -> {
            int filaSeleccionada = tablaOrdenes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione una orden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Orden orden = servicioOrdenes.buscarOrdenPorId(id);
            if (orden != null && orden.getEstado() == EstadoOrden.PENDIENTE) {
                orden.setEstado(EstadoOrden.EN_PREPARACION);
                servicioOrdenes.actualizarOrden(orden);
                actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);
                mostrarDetallesOrden(orden, areaDetalles);
                JOptionPane.showMessageDialog(panelPrincipal, "Orden en preparación");
            }
        });

        // Acción para marcar como lista
        botonLista.addActionListener(e -> {
            int filaSeleccionada = tablaOrdenes.getSelectedRow();
            if (filaSeleccionada == -1) {
                JOptionPane.showMessageDialog(panelPrincipal, "Por favor seleccione una orden", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int id = (int) modeloTabla.getValueAt(filaSeleccionada, 0);
            Orden orden = servicioOrdenes.buscarOrdenPorId(id);
            if (orden != null && orden.getEstado() == EstadoOrden.EN_PREPARACION) {
                orden.setEstado(EstadoOrden.LISTA);
                servicioOrdenes.actualizarOrden(orden);
                actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);
                mostrarDetallesOrden(orden, areaDetalles);
                JOptionPane.showMessageDialog(panelPrincipal, "Orden marcada como lista");
            }
        });

        // Acción para actualizar
        botonActualizar.addActionListener(e -> {
            actualizarTablaOrdenesChef(modeloTabla, servicioOrdenes);
        });

        return panelPrincipal;
    }

    private void actualizarTablaOrdenesChef(DefaultTableModel modeloTabla, ServicioOrdenes servicioOrdenes) {
        modeloTabla.setRowCount(0);
        // Mostrar solo órdenes pendientes y en preparación
        for (Orden orden : servicioOrdenes.obtenerTodasLasOrdenes()) {
            if (orden.getEstado() == EstadoOrden.PENDIENTE || orden.getEstado() == EstadoOrden.EN_PREPARACION) {
                Object[] fila = {
                    orden.getId(),
                    orden.getNumeroMesa(),
                    orden.getMesero(),
                    orden.getEstadoString(),
                    "S/" + orden.getTotal(),
                    orden.getFechaHora()
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private void mostrarDetallesOrden(Orden orden, JTextArea areaDetalles) {
        StringBuilder detalles = new StringBuilder();
        detalles.append("Orden #").append(orden.getId()).append("\n");
        detalles.append("Mesa: ").append(orden.getNumeroMesa()).append("\n");
        detalles.append("Mesero: ").append(orden.getMesero()).append("\n");
        detalles.append("Estado: ").append(orden.getEstadoString()).append("\n");
        detalles.append("Hora: ").append(orden.getFechaHora()).append("\n\n");
        detalles.append("ITEMS:\n");
        
        for (ItemOrden item : orden.getItems()) {
            detalles.append("• ").append(item.getCantidad()).append("x ")
                    .append(item.getProducto().getNombre()).append(" - S/")
                    .append(item.getSubtotal()).append("\n");
            if (!item.getNotas().isEmpty()) {
                detalles.append("  Notas: ").append(item.getNotas()).append("\n");
            }
        }
        
        detalles.append("\nTOTAL: S/").append(orden.getTotal());
        
        areaDetalles.setText(detalles.toString());
    }

    private JPanel crearPanelHistorialOrdenes() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Servicio de órdenes
        ServicioOrdenes servicioOrdenes = new ServicioOrdenes();

        // Tabla de historial
        String[] columnas = {"ID", "Mesa", "Mesero", "Estado", "Total", "Hora"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaHistorial = new JTable(modeloTabla);
        actualizarTablaHistorial(modeloTabla, servicioOrdenes);

        // Filtro por estado
        JPanel panelFiltros = new JPanel(new FlowLayout());
        JComboBox<String> comboFiltro = new JComboBox<>(new String[]{"Todas", "Pendiente", "En Preparación", "Lista", "Entregada"});
        JButton botonFiltrar = new JButton("Filtrar");

        panelFiltros.add(new JLabel("Filtrar por estado:"));
        panelFiltros.add(comboFiltro);
        panelFiltros.add(botonFiltrar);

        // Agregar componentes al panel
        panelPrincipal.add(panelFiltros, BorderLayout.NORTH);
        panelPrincipal.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        // Acción para filtrar
        botonFiltrar.addActionListener(e -> {
            String filtro = (String) comboFiltro.getSelectedItem();
            actualizarTablaHistorialFiltrado(modeloTabla, servicioOrdenes, filtro);
        });

        return panelPrincipal;
    }

    private void actualizarTablaHistorial(DefaultTableModel modeloTabla, ServicioOrdenes servicioOrdenes) {
        modeloTabla.setRowCount(0);
        for (Orden orden : servicioOrdenes.obtenerTodasLasOrdenes()) {
            Object[] fila = {
                orden.getId(),
                orden.getNumeroMesa(),
                orden.getMesero(),
                orden.getEstadoString(),
                "S/" + orden.getTotal(),
                orden.getFechaHora()
            };
            modeloTabla.addRow(fila);
        }
    }

    private void actualizarTablaHistorialFiltrado(DefaultTableModel modeloTabla, ServicioOrdenes servicioOrdenes, String filtro) {
        modeloTabla.setRowCount(0);
        for (Orden orden : servicioOrdenes.obtenerTodasLasOrdenes()) {
            if (filtro.equals("Todas") || orden.getEstadoString().equals(filtro)) {
                Object[] fila = {
                    orden.getId(),
                    orden.getNumeroMesa(),
                    orden.getMesero(),
                    orden.getEstadoString(),
                    "S/" + orden.getTotal(),
                    orden.getFechaHora()
                };
                modeloTabla.addRow(fila);
            }
        }
    }

    private JPanel crearPanelInventarioCocina() {
        JPanel panelPrincipal = new JPanel(new BorderLayout());
        panelPrincipal.setBackground(Color.WHITE);

        // Servicio de inventario
        ServicioInventario servicioInventario = new ServicioInventario();

        // Tabla de inventario
        String[] columnas = {"ID", "Ingrediente", "Categoría", "Disponible", "Mínimo", "Unidad", "Estado"};
        DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable tablaInventario = new JTable(modeloTabla);
        actualizarTablaInventario(modeloTabla, servicioInventario);

        // Panel de alertas
        JPanel panelAlertas = new JPanel(new BorderLayout());
        panelAlertas.setBorder(BorderFactory.createTitledBorder("Alertas de Inventario Bajo"));
        panelAlertas.setPreferredSize(new Dimension(300, 0));

        JTextArea areaAlertas = new JTextArea();
        areaAlertas.setEditable(false);
        areaAlertas.setFont(new Font("Arial", Font.PLAIN, 12));
        areaAlertas.setForeground(Color.RED);
        actualizarAlertasInventario(areaAlertas, servicioInventario);

        panelAlertas.add(new JScrollPane(areaAlertas), BorderLayout.CENTER);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());
        JButton botonActualizar = new JButton("Actualizar Inventario");
        JButton botonReabastecer = new JButton("Solicitar Reabastecimiento");

        panelBotones.add(botonActualizar);
        panelBotones.add(botonReabastecer);

        // Split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, 
            new JScrollPane(tablaInventario), panelAlertas);
        splitPane.setDividerLocation(600);

        // Agregar componentes al panel
        panelPrincipal.add(splitPane, BorderLayout.CENTER);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        // Acción para actualizar
        botonActualizar.addActionListener(e -> {
            actualizarTablaInventario(modeloTabla, servicioInventario);
            actualizarAlertasInventario(areaAlertas, servicioInventario);
        });

        // Acción para reabastecer
        botonReabastecer.addActionListener(e -> {
            List<Ingrediente> ingredientesBajos = servicioInventario.obtenerIngredientesBajos();
            if (ingredientesBajos.isEmpty()) {
                JOptionPane.showMessageDialog(panelPrincipal, "No hay ingredientes que necesiten reabastecimiento urgente");
            } else {
                StringBuilder mensaje = new StringBuilder("Solicitud de reabastecimiento:\n\n");
                for (Ingrediente ing : ingredientesBajos) {
                    mensaje.append("• ").append(ing.getNombre()).append(" - ")
                          .append(ing.getCantidadDisponible()).append("/")
                          .append(ing.getCantidadMinima()).append(" ")
                          .append(ing.getUnidadMedida()).append("\n");
                }
                JOptionPane.showMessageDialog(panelPrincipal, mensaje.toString(), 
                    "Solicitud de Reabastecimiento", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        return panelPrincipal;
    }

    private void actualizarTablaInventario(DefaultTableModel modeloTabla, ServicioInventario servicioInventario) {
        modeloTabla.setRowCount(0);
        for (Ingrediente ingrediente : servicioInventario.obtenerTodosIngredientes()) {
            String estado = ingrediente.necesitaReabastecer() ? "BAJO" : "OK";
            
            Object[] fila = {
                ingrediente.getId(),
                ingrediente.getNombre(),
                ingrediente.getCategoria(),
                ingrediente.getCantidadDisponible(),
                ingrediente.getCantidadMinima(),
                ingrediente.getUnidadMedida(),
                estado
            };
            modeloTabla.addRow(fila);
        }
    }

    private void actualizarAlertasInventario(JTextArea areaAlertas, ServicioInventario servicioInventario) {
        List<Ingrediente> ingredientesBajos = servicioInventario.obtenerIngredientesBajos();
        
        if (ingredientesBajos.isEmpty()) {
            areaAlertas.setText("No hay alertas de inventario.\nTodo está en orden.");
            areaAlertas.setForeground(Color.GREEN);
        } else {
            StringBuilder alertas = new StringBuilder("INGREDIENTES BAJOS:\n\n");
            for (Ingrediente ing : ingredientesBajos) {
                alertas.append("⚠ ").append(ing.getNombre()).append("\n");
                alertas.append("   Disponible: ").append(ing.getCantidadDisponible())
                      .append(" ").append(ing.getUnidadMedida()).append("\n");
                alertas.append("   Mínimo: ").append(ing.getCantidadMinima())
                      .append(" ").append(ing.getUnidadMedida()).append("\n\n");
            }
            areaAlertas.setText(alertas.toString());
            areaAlertas.setForeground(Color.RED);
        }
    }

    // ==================== PANELES PLACEHOLDER ====================

    private JPanel crearPanelReportes() {
        return crearPanelPlaceholder("Reportes");
    }

    private JPanel crearPanelConfiguracion() {
        return crearPanelPlaceholder("Configuración");
    }

    private JPanel crearPanelTomarPedido() {
    JPanel panelPrincipal = new JPanel(new BorderLayout());
    panelPrincipal.setBackground(Color.WHITE);

    // Servicios
    ServicioMesas servicioMesas = new ServicioMesas();
    ServicioProductos servicioProductos = new ServicioProductos();
    ServicioPedidos servicioPedidos = new ServicioPedidos();

    // Variables para el pedido actual - CORREGIDO
    final modelo.Pedido[] pedidoActual = {null};
    DefaultTableModel modeloTablaPedido = new DefaultTableModel(new String[]{"Producto", "Cantidad", "Precio", "Subtotal"}, 0);

    // ===== PANEL SUPERIOR - SELECCIÓN DE MESA =====
    JPanel panelSuperior = new JPanel(new FlowLayout());
    panelSuperior.setBackground(Color.WHITE);
    
    JLabel lblMesa = new JLabel("Seleccionar Mesa:");
    JComboBox<Integer> comboMesas = new JComboBox<>();
    
    // Llenar combo con mesas ocupadas
    for (Mesa mesa : servicioMesas.obtenerMesasOcupadas()) {
        comboMesas.addItem(mesa.getNumero());
    }
    
    JButton btnCrearPedido = new JButton("Crear Pedido para Mesa");
    
    panelSuperior.add(lblMesa);
    panelSuperior.add(comboMesas);
    panelSuperior.add(btnCrearPedido);

    // ===== PANEL IZQUIERDO - MENÚ =====
    JPanel panelMenu = new JPanel(new BorderLayout());
    panelMenu.setPreferredSize(new Dimension(400, 0));
    panelMenu.setBorder(BorderFactory.createTitledBorder("Menú"));
    
    // Categorías
    JComboBox<String> comboCategorias = new JComboBox<>();
    for (String categoria : servicioProductos.obtenerCategorias()) {
        comboCategorias.addItem(categoria);
    }
    
    // Lista de productos
    DefaultListModel<String> modeloProductos = new DefaultListModel<>();
    JList<String> listaProductos = new JList<>(modeloProductos);
    
    // Actualizar productos cuando cambia categoría
    comboCategorias.addActionListener(e -> {
        modeloProductos.clear();
        String categoria = (String) comboCategorias.getSelectedItem();
        for (modelo.Producto producto : servicioProductos.obtenerProductosPorCategoria(categoria)) {
            modeloProductos.addElement(producto.getNombre() + " - S/" + producto.getPrecio());
        }
    });
    
    // Cargar primera categoría
    if (comboCategorias.getItemCount() > 0) {
        comboCategorias.setSelectedIndex(0);
    }
    
    panelMenu.add(comboCategorias, BorderLayout.NORTH);
    panelMenu.add(new JScrollPane(listaProductos), BorderLayout.CENTER);

    // ===== PANEL CENTRAL - AGREGAR PRODUCTO =====
    JPanel panelAgregar = new JPanel(new GridLayout(4, 2, 10, 10));
    panelAgregar.setBorder(BorderFactory.createTitledBorder("Agregar al Pedido"));
    panelAgregar.setPreferredSize(new Dimension(300, 0));
    
    JLabel lblCantidad = new JLabel("Cantidad:");
    JSpinner spinnerCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 10, 1));
    JLabel lblNotas = new JLabel("Notas:");
    JTextField txtNotas = new JTextField();
    JButton btnAgregar = new JButton("Agregar Producto");
    
    panelAgregar.add(lblCantidad);
    panelAgregar.add(spinnerCantidad);
    panelAgregar.add(lblNotas);
    panelAgregar.add(txtNotas);
    panelAgregar.add(new JLabel());
    panelAgregar.add(btnAgregar);

    // ===== PANEL DERECHO - PEDIDO ACTUAL =====
    JPanel panelPedido = new JPanel(new BorderLayout());
    panelPedido.setBorder(BorderFactory.createTitledBorder("Pedido Actual"));
    
    JTable tablaPedido = new JTable(modeloTablaPedido);
    JLabel lblTotal = new JLabel("Total: S/0.00", JLabel.CENTER);
    lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
    
    JButton btnEnviarCocina = new JButton("Enviar a Cocina");
    btnEnviarCocina.setBackground(new Color(40, 167, 69));
    btnEnviarCocina.setForeground(Color.WHITE);
    
    panelPedido.add(new JScrollPane(tablaPedido), BorderLayout.CENTER);
    panelPedido.add(lblTotal, BorderLayout.SOUTH);

    // ===== PANEL INFERIOR =====
    JPanel panelInferior = new JPanel(new FlowLayout());
    panelInferior.add(btnEnviarCocina);

    // ===== CONFIGURAR LAYOUT PRINCIPAL =====
    JPanel panelCentral = new JPanel(new BorderLayout());
    panelCentral.add(panelMenu, BorderLayout.WEST);
    panelCentral.add(panelAgregar, BorderLayout.CENTER);
    panelCentral.add(panelPedido, BorderLayout.EAST);

    panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
    panelPrincipal.add(panelCentral, BorderLayout.CENTER);
    panelPrincipal.add(panelInferior, BorderLayout.SOUTH);

    // ===== FUNCIONALIDADES - CORREGIDAS =====

    // Crear nuevo pedido
    btnCrearPedido.addActionListener(e -> {
        if (comboMesas.getItemCount() == 0) {
            JOptionPane.showMessageDialog(panelPrincipal, "No hay mesas ocupadas");
            return;
        }
        
        int numeroMesa = (Integer) comboMesas.getSelectedItem();
        pedidoActual[0] = servicioPedidos.crearPedido(numeroMesa, usuario.getNombreUsuario());
        modeloTablaPedido.setRowCount(0);
        lblTotal.setText("Total: S/0.00");
        JOptionPane.showMessageDialog(panelPrincipal, "Nuevo pedido creado para Mesa " + numeroMesa);
    });

    // Agregar producto al pedido
    btnAgregar.addActionListener(e -> {
        if (pedidoActual[0] == null) {
            JOptionPane.showMessageDialog(panelPrincipal, "Primero debe crear un pedido");
            return;
        }
        
        int selectedIndex = listaProductos.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(panelPrincipal, "Seleccione un producto del menú");
            return;
        }
        
        String categoria = (String) comboCategorias.getSelectedItem();
        java.util.List<modelo.Producto> productos = servicioProductos.obtenerProductosPorCategoria(categoria);
        modelo.Producto productoSeleccionado = productos.get(selectedIndex);
        
        int cantidad = (Integer) spinnerCantidad.getValue();
        String notas = txtNotas.getText();
        
        // Crear item y agregar al pedido - CORREGIDO
        modelo.ItemPedido item = new modelo.ItemPedido(productoSeleccionado, cantidad, notas);
        pedidoActual[0].agregarItem(item);
        
        // Agregar a la tabla
        modeloTablaPedido.addRow(new Object[]{
            productoSeleccionado.getNombre(),
            cantidad,
            "S/" + productoSeleccionado.getPrecio(),
            "S/" + item.getSubtotal()
        });
        
        // Actualizar total
        lblTotal.setText("Total: S/" + pedidoActual[0].getTotal());
        
        // Limpiar campos
        txtNotas.setText("");
        spinnerCantidad.setValue(1);
    });

    // Enviar pedido a cocina
    btnEnviarCocina.addActionListener(e -> {
        if (pedidoActual[0] == null || pedidoActual[0].getItems().isEmpty()) {
            JOptionPane.showMessageDialog(panelPrincipal, "No hay pedido para enviar");
            return;
        }
        
        pedidoActual[0].setEstado("Pendiente");
        servicioPedidos.actualizarPedido(pedidoActual[0]);
        
        JOptionPane.showMessageDialog(panelPrincipal, 
            "Pedido #" + pedidoActual[0].getId() + " enviado a cocina\n" +
            "Mesa: " + pedidoActual[0].getNumeroMesa() + "\n" +
            "Total: S/" + pedidoActual[0].getTotal());
        
        // Reiniciar interfaz
        pedidoActual[0] = null;
        modeloTablaPedido.setRowCount(0);
        lblTotal.setText("Total: S/0.00");
    });

    return panelPrincipal;
}
      private JPanel crearPanelGestionMesas() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    
    JLabel titulo = new JLabel("GESTIÓN DE MESAS", JLabel.CENTER);
    titulo.setFont(new Font("Arial", Font.BOLD, 20));
    titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    
    // Servicio de mesas
    ServicioMesas servicioMesas = new ServicioMesas();
    
    // Grid de mesas
    JPanel gridMesas = new JPanel(new GridLayout(2, 5, 10, 10));
    gridMesas.setBackground(Color.WHITE);
    gridMesas.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
    for (Mesa mesa : servicioMesas.obtenerTodasLasMesas()) {
        JButton btnMesa = new JButton("Mesa " + mesa.getNumero());
        btnMesa.setBackground(mesa.isOcupada() ? new Color(220, 53, 69) : new Color(40, 167, 69));
        btnMesa.setForeground(Color.WHITE);
        btnMesa.setFont(new Font("Arial", Font.BOLD, 14));
        
        btnMesa.addActionListener(e -> {
            if (mesa.isOcupada()) {
                int respuesta = JOptionPane.showConfirmDialog(panel, 
                    "¿Liberar Mesa " + mesa.getNumero() + "?", "Liberar Mesa", 
                    JOptionPane.YES_NO_OPTION);
                if (respuesta == JOptionPane.YES_OPTION) {
                    servicioMesas.liberarMesa(mesa.getNumero());
                    btnMesa.setBackground(new Color(40, 167, 69));
                }
            } else {
                String comensalesStr = JOptionPane.showInputDialog(panel, 
                    "Número de comensales para Mesa " + mesa.getNumero() + ":");
                try {
                    int comensales = Integer.parseInt(comensalesStr);
                    if (servicioMesas.ocuparMesa(mesa.getNumero(), comensales)) {
                        btnMesa.setBackground(new Color(220, 53, 69));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(panel, "Número inválido");
                }
            }
        });
        
        gridMesas.add(btnMesa);
    }
    
    panel.add(titulo, BorderLayout.NORTH);
    panel.add(gridMesas, BorderLayout.CENTER);
    
    return panel;
}

    private JPanel crearPanelOrdenesActivas() {
    JPanel panel = new JPanel(new BorderLayout());
    panel.setBackground(Color.WHITE);
    
    JLabel titulo = new JLabel("ÓRDENES ACTIVAS - ENTREGAR A CLIENTES", JLabel.CENTER);
    titulo.setFont(new Font("Arial", Font.BOLD, 20));
    titulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
    
    // Servicio de pedidos
    ServicioPedidos servicioPedidos = new ServicioPedidos();
    
    // Tabla de órdenes listas para entregar
    String[] columnas = {"ID", "Mesa", "Estado", "Total", "Hora"};
    DefaultTableModel modeloTabla = new DefaultTableModel(columnas, 0);
    JTable tablaOrdenes = new JTable(modeloTabla);
    
    // Actualizar tabla con órdenes listas
    for (Pedido pedido : servicioPedidos.obtenerPedidosPorEstado("Listo")) {
        Object[] fila = {
            pedido.getId(),
            pedido.getNumeroMesa(),
            pedido.getEstado(),
            "S/" + pedido.getTotal(),
            pedido.getFechaHora()
        };
        modeloTabla.addRow(fila);
    }
    
    JButton btnEntregar = new JButton("Marcar como Entregado");
    btnEntregar.setBackground(new Color(40, 167, 69));
    btnEntregar.setForeground(Color.WHITE);
    
    btnEntregar.addActionListener(e -> {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila != -1) {
            int idPedido = (int) modeloTabla.getValueAt(fila, 0);
            Pedido pedido = servicioPedidos.buscarPedidoPorId(idPedido);
            if (pedido != null) {
                pedido.setEstado("Entregado");
                servicioPedidos.actualizarPedido(pedido);
                modeloTabla.removeRow(fila);
                JOptionPane.showMessageDialog(panel, "Pedido entregado al cliente");
            }
        }
    });
    
    JPanel panelSur = new JPanel();
    panelSur.add(btnEntregar);
    
    panel.add(titulo, BorderLayout.NORTH);
    panel.add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);
    panel.add(panelSur, BorderLayout.SOUTH);
    
    return panel;
}

    private JPanel crearPanelVerMenu() {
        return crearPanelPlaceholder("Ver Menú");
    }

    private JPanel crearPanelRealizarPedido() {
        return crearPanelPlaceholder("Realizar Pedido");
    }

    private JPanel crearPanelEstadoPedido() {
        return crearPanelPlaceholder("Estado Pedido");
    }

    private JPanel crearPanelPlaceholder(String nombrePanel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        JLabel etiqueta = new JLabel(nombrePanel, JLabel.CENTER);
        etiqueta.setFont(new Font("Arial", Font.BOLD, 24));
        etiqueta.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        panel.add(etiqueta, BorderLayout.CENTER);
        return panel;
    }

    private void cerrarSesion() {
        int respuesta = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro que desea cerrar sesión?", 
            "Cerrar Sesión", 
            JOptionPane.YES_NO_OPTION
        );
        
        if (respuesta == JOptionPane.YES_OPTION) {
            new VentanaLogin().setVisible(true);
            this.dispose();
        }
    }
}