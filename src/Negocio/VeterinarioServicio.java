package Negocio;

import Datos.VeterinarioDAO;
import Excepciones.DatoInvalidoException;
import Excepciones.PersistenciaException;
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
 *   especialidad para facilitar las busquedas.
 */
public class VeterinarioServicio {

    private final List<Veterinario> cache = new ArrayList<>();
    private final Map<Especialidad, List<Veterinario>> porEspecialidad = new HashMap<>();
    private final VeterinarioDAO veterinarioDAO = new VeterinarioDAO();

    public VeterinarioServicio() {
        // Carga los veterinarios existentes desde la base de datos al iniciar.
        try {
            for (Veterinario v : veterinarioDAO.listarTodos()) {
                agregarACache(v);
            }
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    public Veterinario registrar(String nombre, String cedula, Especialidad especialidad,
                                 String telefono, String email)
            throws DatoInvalidoException {
        validarDatos(nombre, cedula, especialidad);
        Veterinario nuevo = new Veterinario(nombre, cedula, especialidad, telefono, email);

        try {
            int idGenerado = veterinarioDAO.insertar(nuevo);
            // El id del objeto en memoria lo genera el contador estatico de
            // Veterinario; si difiere del id real de la BD, lo recargamos
            // completo para mantenerlos sincronizados.
            if (idGenerado != nuevo.getId()) {
                nuevo = new Veterinario(idGenerado, nombre, cedula, especialidad, telefono, email, true);
            }
        } catch (PersistenciaException e) {
            throw new DatoInvalidoException("Error guardando el veterinario en la base de datos: " + e.getMessage());
        }

        agregarACache(nuevo);
        return nuevo;
    }

    public void actualizar(Veterinario veterinario)
            throws DatoInvalidoException {
        validarDatos(veterinario.getNombre(),
                veterinario.getCedula(),
                veterinario.getEspecialidad());

        try {
            veterinarioDAO.actualizar(veterinario);
        } catch (PersistenciaException e) {
            throw new DatoInvalidoException("Error actualizando el veterinario: " + e.getMessage());
        }
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
            try {
                veterinarioDAO.eliminar(idVeterinario);
            } catch (PersistenciaException e) {
                e.printStackTrace();
                return; // si no se pudo borrar en la BD, no lo quitamos de memoria
            }

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
                    "La cedula del veterinario es obligatoria.");
        }
        if (especialidad == null) {
            throw new DatoInvalidoException(
                    "Debe seleccionar una especialidad.");
        }
    }
}
