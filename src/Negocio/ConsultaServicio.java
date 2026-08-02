package Negocio;

import Datos.ConsultaDAO;
import Excepciones.DatoInvalidoException;
import Excepciones.PersistenciaException;
import Modelo.Consulta;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Colecciones usadas:
 * List<Consulta>: almacena todas las consultas registradas.
 * Map<Integer, List<Consulta>>: organiza las consultas por mascota para
 * consultar rapidamente el historial clinico de cada una.
 */
public class ConsultaServicio {

    private final List<Consulta> consultas = new ArrayList<>();
    private final Map<Integer, List<Consulta>> porMascota = new HashMap<>();
    private final ConsultaDAO consultaDAO = new ConsultaDAO();

    public ConsultaServicio() {
        try {
            for (Consulta c : consultaDAO.listarTodas()) {
                agregarACache(c);
            }
        } catch (PersistenciaException e) {
            e.printStackTrace();
        }
    }

    public Consulta registar(int mascotaId, int veterinarioId, LocalDate fecha,
                             String diagnostico, String tratamiento, String observaciones)
        throws DatoInvalidoException {

        if (mascotaId <= 0 || veterinarioId <= 0) {
            throw new DatoInvalidoException("Debe indicar una mascota y un veterinario validos.");
        }

        if (fecha == null) {
            throw new DatoInvalidoException("La fecha de la consulta es obligatoria.");
        }

        if (diagnostico == null || diagnostico.isBlank()) {
            throw new DatoInvalidoException("El diagnostico es obligatorio.");
        }

        Consulta nueva = new Consulta(mascotaId, veterinarioId, fecha, diagnostico, tratamiento, observaciones);

        try {
            consultaDAO.insertar(nueva);
        } catch (PersistenciaException e) {
            throw new DatoInvalidoException("Error guardando la consulta en la base de datos: " + e.getMessage());
        }

        agregarACache(nueva);

        return nueva;
    }

    public List<Consulta> listarTodas() {
        return new ArrayList<>(consultas);
    }

    public List<Consulta> listarPorMascota(int mascotaId) {
        return new ArrayList<>(porMascota.getOrDefault(mascotaId, new ArrayList<>()));
    }

    private void agregarACache(Consulta c) {
        consultas.add(c);

        porMascota
                .computeIfAbsent(c.getMascotaId(), k -> new ArrayList<>())
                .add(c);
    }

}
