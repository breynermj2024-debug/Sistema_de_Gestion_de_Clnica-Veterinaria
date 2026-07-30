package Modelo;

// Enum que contiene las especialidades disponibles del veterinario
public enum Especialidad {

    // Tipos de atención veterinaria
    MEDICINA_GENERAL("Medicina General"),
    CIRUGIA("Cirugia Veterinaria"),
    DERMATOLOGIA("Dermatologia"),
    CARDIOLOGIA("Cardiologia"),
    ODONTOLOGIA("Odontologia"),
    TRAUMATOLOGIA("Traumatologia");


    // Guarda el nombre visible de la especialidad
    private final String descripcion;


    // Constructor del enum
    Especialidad(String descripcion) {
        this.descripcion = descripcion;
    }


    // Devuelve la descripción de la especialidad
    public String getDescripcion() {
        return descripcion;
    }


    // Permite mostrar la descripción en pantalla
    @Override
    public String toString() {
        return descripcion;
    }
}