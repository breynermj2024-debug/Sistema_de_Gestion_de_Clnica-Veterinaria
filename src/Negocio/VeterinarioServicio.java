package Negocio;

import Excepciones.DatoInvalidoException;
import Modelo.Especialidad;
import Modelo.Veterinario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reglas de negocio para veterinarios.
 *
 * Colecciones usadas:
 * - List<Veterinario>: almacena todos los veterinarios.
 * - Map<Especialidad, List<Veterinario>>: organiza los veterinarios por
 *   especialidad para facilitar las búsquedas.
 */
public class VeterinarioServicio {

    private final List<Veterinario> cache = new ArrayList<>();
    private final Map<Especialidad, List<Veterinario>> porEspecialidad = new HashMap<>();

    public VeterinarioServicio() {
    }

    public Veterinario registrar(String nombre, String cedula, Especialidad especialidad,
                                 String telefono, String email)
            throws DatoInvalidoException {

        validarDatos(nombre, cedula, especialidad);

        Veterinario nuevo = new Veterinario(nombre, cedula, especialidad, telefono, email);

        agregarACache(nuevo);

        return nuevo;
    }

    public void actualizar(Veterinario veterinario)
            throws DatoInvalidoException {

        validarDatos(veterinario.getNombre(),
                veterinario.getCedula(),
                veterinario.getEspecialidad());

        // No hace nada más porque aún no existe la base de datos.
        // Como el objeto ya fue modificado, los cambios quedan en memoria.
    }

    public void eliminar(int idVeterinario) {

        Veterinario veterinarioEliminar = null;

        for (Veterinario v : cache) {
            if (v.getId() == idVeterinario) {
                veterinarioEliminar = v;
                break;
            }
        }

        if (veterinarioEliminar != null) {
            cache.remove(veterinarioEliminar);

            List<Veterinario> lista =
                    porEspecialidad.get(veterinarioEliminar.getEspecialidad());

            if (lista != null) {
                lista.remove(veterinarioEliminar);
            }
        }
    }

    public List<Veterinario> listarTodos() {
        return new ArrayList<>(cache);
    }

    public List<Veterinario> listarPorEspecialidad(Especialidad especialidad) {
        return new ArrayList<>(
                porEspecialidad.getOrDefault(especialidad, new ArrayList<>())
        );
    }

    private void agregarACache(Veterinario veterinario) {

        cache.add(veterinario);

        porEspecialidad
                .computeIfAbsent(veterinario.getEspecialidad(),
                        k -> new ArrayList<>())
                .add(veterinario);
    }

    private void validarDatos(String nombre,
                              String cedula,
                              Especialidad especialidad)
            throws DatoInvalidoException {

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new DatoInvalidoException(
                    "El nombre del veterinario es obligatorio.");
        }

        if (cedula == null || cedula.trim().isEmpty()) {
            throw new DatoInvalidoException(
                    "La cédula del veterinario es obligatoria.");
        }

        if (especialidad == null) {
            throw new DatoInvalidoException(
                    "Debe seleccionar una especialidad.");
        }
    }
}