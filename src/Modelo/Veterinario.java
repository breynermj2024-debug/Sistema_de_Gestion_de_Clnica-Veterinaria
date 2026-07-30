package Modelo;

import java.util.Objects;

// Representa un veterinario dentro del sistema
public class Veterinario {

    // Contador para generar IDs automáticos
    private static int contadorVeterinarios = 0;

    // Datos principales del veterinario
    private final int id;
    private String nombre;
    private String cedula;
    private Especialidad especialidad;
    private String telefono;
    private String email;
    private boolean activo;


    // Constructor para crear un veterinario nuevo
    public Veterinario(String nombre, String cedula, Especialidad especialidad, String telefono, String email) {

        this.id = ++contadorVeterinarios;
        this.nombre = nombre;
        this.cedula = cedula;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.email = email;

        // El veterinario se crea activo por defecto
        this.activo = true;
    }


    // Constructor para cargar un veterinario existente
    public Veterinario(int id, String nombre, String cedula, Especialidad especialidad,
                       String telefono, String email, boolean activo) {

        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
        this.especialidad = especialidad;
        this.telefono = telefono;
        this.email = email;
        this.activo = activo;

        // Actualiza el contador para evitar IDs repetidos
        if (id > contadorVeterinarios) {
            contadorVeterinarios = id;
        }
    }


    // Métodos para acceder y modificar datos
    public int getId() { return id; }

    public String getNombre() { return nombre; }

    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCedula() { return cedula; }

    public void setCedula(String cedula) { this.cedula = cedula; }

    public Especialidad getEspecialidad() { return especialidad; }

    public void setEspecialidad(Especialidad especialidad) { this.especialidad = especialidad; }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public boolean isActivo() { return activo; }

    public void setActivo(boolean activo) { this.activo = activo; }


    // Compara veterinarios usando el ID
    @Override
    public boolean equals(Object o) {

        if (this == o) return true;

        if (!(o instanceof Veterinario)) return false;

        Veterinario that = (Veterinario) o;

        return id == that.id;
    }


    // Genera un código para colecciones
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


    // Muestra información del veterinario
    @Override
    public String toString() {
        return "Dr(a). " + nombre + " - " + especialidad.getDescripcion();
    }
}

