package Negocio;

import Excepciones.DatoInvalidoException;
import Modelo.Consulta;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Colecciones usadas:
 * List<Consulta>: almacena todas las consultas registradas.
 *  Map<Integer, List<Consulta>>: organiza las consultas por mascota para
 *  consultar rápidamente el historial clínico de cada una.
 */

public class ConsultaServicio {

    private final List<Consulta> consultas = new ArrayList<>();
    private final Map<Integer, List<Consulta>> porMascota = new HashMap<>();

    public Consulta registar(int mascotaId, int veterinarioId, LocalDate fecha,
                             String diagnostico, String tratamiento, String observaciones)
        throws DatoInvalidoException {

        if (mascotaId <= 0 || veterinarioId <= 0) {
            throw new DatoInvalidoException("Debe indicar una mascota y un veterinario válidos.");
        }

        if (fecha == null) {
            throw new DatoInvalidoException("La fecha de la consulta es obligatoria.");
        }

        if (diagnostico == null || diagnostico.isBlank()) {
            throw new DatoInvalidoException("El diagnóstico es obligatorio.");
        }

        Consulta nueva = new Consulta(mascotaId, veterinarioId, fecha, diagnostico, tratamiento, observaciones);

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
                .computeIfAbsent(c.getMascotaId(), k ->new ArrayList<>())
                .add(c);
    }

}
