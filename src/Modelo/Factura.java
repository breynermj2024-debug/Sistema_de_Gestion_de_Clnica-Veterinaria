/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author HP
 */
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Representa una factura emitida a un cliente.
 *
 * Usa composicion sobre List<DetalleFactura>: los detalles no existen sin
 * la factura que los contiene. El total se calcula sumando los subtotales
 * de cada detalle, los cuales a su vez fueron calculados de forma
 * polimorfica a partir de cada Servicio (ver DetalleFactura).
 */
public class Factura {

    private int id;
    private final int idCliente;
    private final LocalDate fecha;

    // Composicion: la lista de detalles vive y muere con la factura.
    private final List<DetalleFactura> detalles = new ArrayList<>();

    private double total;

    public Factura(int idCliente, LocalDate fecha) {
        this.idCliente = idCliente;
        this.fecha = fecha;
    }

    // Constructor usado por FacturaDAO al reconstruir una factura ya
    // guardada en la base de datos.
    public Factura(int id, int idCliente, LocalDate fecha, double total) {
        this.id = id;
        this.idCliente = idCliente;
        this.fecha = fecha;
        this.total = total;
    }

    public void agregarDetalle(DetalleFactura detalle) {
        detalles.add(detalle);
        recalcularTotal();
    }

    // Usado por FacturaDAO al reconstruir una factura ya guardada: agrega
    // el detalle sin recalcular el total, porque el total ya viene leido
    // de la base de datos y debe respetarse tal cual quedo guardado.
    public void agregarDetalleExistente(DetalleFactura detalle) {
        detalles.add(detalle);
    }

    // Demuestra el polimorfismo real que pide el proyecto: no importa que
    // subclase concreta de Servicio tenga cada detalle, aqui solo se suman
    // los subtotales ya calculados por calcularPrecio() de cada una.
    public void recalcularTotal() {
        double suma = 0.0;
        for (DetalleFactura detalle : detalles) {
            suma += detalle.getSubtotal();
        }
        this.total = suma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public List<DetalleFactura> getDetalles() {
        return new ArrayList<>(detalles);
    }

    public double getTotal() {
        return total;
    }

    @Override
    public String toString() {
        return "Factura #" + id + " - Cliente " + idCliente + " - " + fecha + " - ₡" + total;
    }
}