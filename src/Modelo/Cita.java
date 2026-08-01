package Modelo;

import java.time.LocalDate;
import java.time.LocalTime;

public class Cita {

    private static int contador = 0;

    private final int id;
    private Mascota mascota;
    private Veterinario veterinario;
    private LocalDate fecha;
    private LocalTime hora;
    private String motivo;
    private  EstadoCita estado;

    public Cita(Mascota mascota, Veterinario veterinario,  LocalDate fecha, LocalTime hora, String motivo) {
        this.id = ++contador;
        this.mascota = mascota;
        this.veterinario = veterinario;
        this.fecha = fecha;
        this.hora = hora;
        this.motivo = motivo;
        this.estado = EstadoCita.PROGRAMADA;
    }

     // Más Getters
    public int getId() {
        return id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public Veterinario getVeterinario() {
        return veterinario;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public String getMotivo() {
        return motivo;
    }

    public EstadoCita getEstado() {
        return estado;
    }

    // Más Setters


    public void setEstado(EstadoCita estado) {
        this.estado = estado;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public String toString() {
        return "Cita #" + id + " - " + mascota.getNombre() + " con " + veterinario.getNombre();
    }


}
