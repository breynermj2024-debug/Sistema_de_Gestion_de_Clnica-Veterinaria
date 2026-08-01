package Modelo;

import java.time.LocalDate;

// Clase abstracta que representa cualquier servicio veteriano
public abstract class Servicio {
    // Atributos comunes a todos los servicios
    protected int id;
    protected  String nombre;
    protected  LocalDate fecha;
    protected  int mascotaId;

    //Constructor que usarán todas las subclases con super
    public Servicio(int id, String nombre, LocalDate fecha, int mascotaId) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.mascotaId = mascotaId;

    }

    // Metodo abstracto de cada subclase decide como calcula su precio
    public abstract double calcularPrecio();

    // Metodo concreto que usa el comportamiento polimórfico de las hijas

    public String getDescripcion() {
        return nombre + " - " + fecha + " - ₡" + calcularPrecio();
    }

    //Getter
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getMascotaId() {
        return  mascotaId;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public  void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }
}
