package Modelo;

// Enum que representa los estados de una cita
public enum EstadoCita {

    // Estados disponibles de una cita
    PROGRAMADA,
    CONFIRMADA,
    ATENDIENDO,
    COMPLETADA,
    CANCELADA;


    // Valida si una cita puede cambiar de estado
    public boolean puedeCambiarA(EstadoCita destino) {

        // Las citas finalizadas o canceladas no cambian
        if (this == COMPLETADA || this == CANCELADA) {
            return false;
        }

        // Evita cambiar al mismo estado
        return this != destino;
    }
}