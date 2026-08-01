package Modelo;

import java.time.LocalDate;

// Representa un procedimiento veterinario
public class Procedimiento extends Servicio {

    // Tarifa base según el nivel de complejidad
    private static final double TARIFA_BAJA = 20000.0;
    private static final double TARIFA_MEDIA = 45000.0;
    private static final double TARIFA_ALTA = 90000.0;

    private String descripcionProcedimiento;
    private NivelComplejidad complejidad;

    public Procedimiento(int id, LocalDate fecha, int mascotaId,
                         String descripcionProcedimiento, NivelComplejidad complejidad) {
        super(id, "Procedimiento", fecha, mascotaId);
        this.descripcionProcedimiento = descripcionProcedimiento;
        this.complejidad = complejidad;
    }

    //SobreEscritura del método abstracto de servicio
    @Override
    public double calcularPrecio() {
        switch (complejidad) {
            case BAJA:
                return TARIFA_BAJA;
            case MEDIA:
                return TARIFA_MEDIA;
            case ALTA:
                return TARIFA_ALTA;
            default:
                return TARIFA_BAJA;
        }
    }

    public String getDescripcionProcedimiento() {
        return descripcionProcedimiento;
    }

    public void setDescripcionProcedimiento(String descripcionProcedimiento) {
        this.descripcionProcedimiento = descripcionProcedimiento;
    }

    public NivelComplejidad getComplejidad() {
        return complejidad;
    }

    public void setComplejidad(NivelComplejidad complejidad) {
        this.complejidad = complejidad;
    }
}
