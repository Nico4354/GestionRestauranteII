package modelo;

public class ItemPedido {
    private Producto producto;
    private int cantidad;
    private String notas;
    private double subtotal;

    public ItemPedido(Producto producto, int cantidad, String notas) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.notas = notas;
        this.subtotal = producto.getPrecio() * cantidad;
    }

    // Getters
    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public String getNotas() { return notas; }
    public double getSubtotal() { return subtotal; }

    // Setters
    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
        this.subtotal = producto.getPrecio() * cantidad;
    }

    public void setNotas(String notas) { this.notas = notas; }

    @Override
    public String toString() {
        return cantidad + "x " + producto.getNombre() + " - S/" + subtotal + 
               (notas.isEmpty() ? "" : " (" + notas + ")");
    }
}