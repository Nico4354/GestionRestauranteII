package modelo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Orden {
    private int id;
    private int numeroMesa;
    private String mesero;
    private Date fechaHora;
    private EstadoOrden estado;
    private List<ItemOrden> items;
    private String notas;

    public Orden(int id, int numeroMesa, String mesero) {
        this.id = id;
        this.numeroMesa = numeroMesa;
        this.mesero = mesero;
        this.fechaHora = new Date();
        this.estado = EstadoOrden.PENDIENTE;
        this.items = new ArrayList<>();
        this.notas = "";
    }

    // Getters
    public int getId() { return id; }
    public int getNumeroMesa() { return numeroMesa; }
    public String getMesero() { return mesero; }
    public Date getFechaHora() { return fechaHora; }
    public EstadoOrden getEstado() { return estado; }
    public List<ItemOrden> getItems() { return items; }
    public String getNotas() { return notas; }

    // Setters
    public void setEstado(EstadoOrden estado) { this.estado = estado; }
    public void setNotas(String notas) { this.notas = notas; }

    public void agregarItem(ItemOrden item) {
        items.add(item);
    }

    public double getTotal() {
        double total = 0;
        for (ItemOrden item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String getEstadoString() {
        switch (estado) {
            case PENDIENTE: return "Pendiente";
            case EN_PREPARACION: return "En Preparación";
            case LISTA: return "Lista";
            case ENTREGADA: return "Entregada";
            case CANCELADA: return "Cancelada";
            default: return "Desconocido";
        }
    }
}