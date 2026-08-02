/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author HP
 */
/**
 * Representa una linea dentro de una factura: un servicio veterinario
 * concreto (referencia polimorfica a Servicio) mas la cantidad y el
 * subtotal calculado.
 *
 * Relacion de composicion: un DetalleFactura no tiene sentido fuera de una
 * Factura (ver Factura.agregarDetalle).
 */
public class DetalleFactura {

    private int id;
    private final Servicio servicio;
    private int cantidad;
    private double subtotal;

    public DetalleFactura(Servicio servicio, int cantidad) {

        if (servicio == null) {
            throw new IllegalArgumentException("El servicio del detalle no puede ser nulo.");
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }

        this.servicio = servicio;
        this.cantidad = cantidad;

        // El subtotal se calcula usando calcularPrecio() de forma
        // polimorfica: no importa si "servicio" es en realidad una
        // ConsultaGeneral, una Vacunacion o un Procedimiento.
        this.subtotal = servicio.calcularPrecio() * cantidad;
    }

    // Constructor usado por FacturaDAO para reconstruir un detalle que ya
    // existe en la base de datos (conserva el id y el subtotal guardados).
    public DetalleFactura(int id, Servicio servicio, int cantidad, double subtotal) {
        this.id = id;
        this.servicio = servicio;
        this.cantidad = cantidad;
        this.subtotal = subtotal;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicio getServicio() {
        return servicio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public double getSubtotal() {
        return subtotal;
    }

    @Override
    public String toString() {
        return servicio.getNombre() + " x" + cantidad + " - ₡" + subtotal;
    }
}
