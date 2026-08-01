package Modelo;

import java.time.LocalDate;

// Representa el registro clínico de una consulta veterinaria
public class Consulta {

    // Contador estático para generar IDs automáticamente
    private static int contador = 0;

    private final int id;
    private int mascotaId;
    private int veterinarioId;
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
    private String observaciones;

    public Consulta(int mascotaId, int veterinarioId, LocalDate fecha,
                    String diagnostico, String tratamiento, String observaciones) {
        this.id = ++contador;
        this.mascotaId = mascotaId;
        this.veterinarioId = veterinarioId;
        this.fecha = fecha;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.observaciones = observaciones;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMascotaId() {
        return mascotaId;
    }

    public int getVeterinarioId() {
        return veterinarioId;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public String getTratamiento() {
        return tratamiento;
    }

    public String getObservaciones() {
        return observaciones;
    }

    // Setters
    public void setMascotaId(int mascotaId) {
        this.mascotaId = mascotaId;
    }

    public void setVeterinarioId(int veterinarioId) {
        this.veterinarioId = veterinarioId;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public void setTratamiento(String tratamiento) {
        this.tratamiento = tratamiento;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    @Override
    public String toString() {
        return "Consulta #" + id + " - " + fecha + " - Diagnóstico: " + diagnostico;
    }
}
