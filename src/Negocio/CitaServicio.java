package Negocio;

import Datos.CitaDAO;
import Excepciones.CitaNoDisponibleException;
import Excepciones.PersistenciaException;
import Modelo.Cita;
import Modelo.EstadoCita;
import Modelo.Mascota;
import Modelo.Veterinario;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reglas de negocio para citas.
 *
 * Colecciones usadas:
 * - List<Cita>: almacena todas las citas.
 * - Map<Integer, List<Cita>>: organiza las citas por veterinario para
 *   validar rapidamente la disponibilidad.
 */
public class CitaServicio {

    private final List<Cita> cache = new ArrayList<>();
    private final Map<Integer, List<Cita>> porVeterinario = new HashMap<>();
    private final CitaDAO citaDAO = new CitaDAO();

    // Una Cita necesita objetos Mascota y Veterinario ya construidos, asi
    // que este metodo se llama despues de que Principal cargue clientes
    // (con sus mascotas) y veterinarios, para traer las citas de la BD.
    public void cargarDesdeBD(List<Mascota> mascotas, List<Veterinario> veterinarios) {
        try {
            for (Cita c : citaDAO.listarTodas(mascotas, veterinarios)) {
                agregarACache(c);
            }
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    public Cita agendar(Mascota mascota, Veterinario veterinario,
                        LocalDate fecha, LocalTime hora, String motivo)
            throws CitaNoDisponibleException {

        if (mascota == null || veterinario == null || fecha == null || hora == null) {
            throw new CitaNoDisponibleException("Mascota, veterinario, fecha y hora son obligatorios.");
        }

        if (motivo == null || motivo.isBlank()) {
            throw new CitaNoDisponibleException("Debe indicar el motivo de la cita.");
        }

        if (fecha.isBefore(LocalDate.now())) {
            throw new CitaNoDisponibleException("No se puede agendar una cita en una fecha pasada.");
        }

        if (hayConflicto(veterinario.getId(), fecha, hora)) {
            throw new CitaNoDisponibleException(
                    "El veterinario " + veterinario.getNombre() +
                            " ya tiene una cita el " + fecha + " a las " + hora);
        }

        Cita nueva = new Cita(mascota, veterinario, fecha, hora, motivo);

        try {
            citaDAO.insertar(nueva);
        } catch (PersistenciaException e) {
            throw new CitaNoDisponibleException("Error guardando la cita en la base de datos: " + e.getMessage());
        }

        agregarACache(nueva);

        return nueva;
    }

    public void cambiarEstado(Cita cita, EstadoCita nuevoEstado)
            throws CitaNoDisponibleException {

        if (!cita.getEstado().puedeCambiarA(nuevoEstado)) {
            throw new CitaNoDisponibleException(
                    "No se puede cambiar de " + cita.getEstado() +
                            " a " + nuevoEstado + ".");
        }

        try {
            citaDAO.actualizarEstado(
                    cita.getMascota().getId(),
                    cita.getVeterinario().getId(),
                    cita.getFecha(),
                    cita.getHora(),
                    nuevoEstado
            );
        } catch (PersistenciaException e) {
            throw new CitaNoDisponibleException("Error actualizando el estado en la base de datos: " + e.getMessage());
        }

        cita.setEstado(nuevoEstado);
    }

    public List<Cita> listarTodas() {
        return new ArrayList<>(cache);
    }

    private boolean hayConflicto(int idVeterinario, LocalDate fecha, LocalTime hora) {

        List<Cita> citasDelVet =
                porVeterinario.getOrDefault(idVeterinario, new ArrayList<>());

        for (Cita c : citasDelVet) {

            boolean mismoMomento =
                    c.getFecha().equals(fecha) &&
                            c.getHora().equals(hora);

            boolean sigueActiva =
                    c.getEstado() != EstadoCita.CANCELADA;

            if (mismoMomento && sigueActiva) {
                return true;
            }
        }

        return false;
    }

    private void agregarACache(Cita c) {

        cache.add(c);

        porVeterinario
                .computeIfAbsent(c.getVeterinario().getId(), k -> new ArrayList<>())
                .add(c);
    }
}
