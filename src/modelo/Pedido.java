package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Pedido {
    private int id;
    private int numeroMesa;
    private String mesero;
    private Date fechaHora;
    private String estado; // "Pendiente", "En preparación", "Listo", "Entregado", "Cancelado"
    private List<ItemPedido> items;
    private double total;
    private String notas;

    public Pedido(int id, int numeroMesa, String mesero) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.mesero = mesero;
        this.fechaHora = new Date();
        this.estado = "Pendiente";
        this.items = new ArrayList<>();
        this.total = 0.0;
        this.notas = "";
    }

    // Getters
    public int getId() { return id; }
    public int getNumeroMesa() { return numeroMesa; }
    public String getMesero() { return mesero; }
    public Date getFechaHora() { return fechaHora; }
    public String getEstado() { return estado; }
    public List<ItemPedido> getItems() { return items; }
    public double getTotal() { return total; }
    public String getNotas() { return notas; }

    // Setters
    public void setEstado(String estado) { this.estado = estado; }
    public void setNotas(String notas) { this.notas = notas; }

    // Métodos de negocio
    public void agregarItem(ItemPedido item) {
        items.add(item);
        calcularTotal();
    }

    public void eliminarItem(ItemPedido item) {
        items.remove(item);
        calcularTotal();
    }

    private void calcularTotal() {
        total = 0;
        for (ItemPedido item : items) {
            total += item.getSubtotal();
        }
    }

    @Override
    public String toString() {
        return "Pedido #" + id + " - Mesa " + numeroMesa + " - " + estado + " - S/" + total;
    }
}
