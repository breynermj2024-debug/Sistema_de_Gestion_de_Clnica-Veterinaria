package Datos;

import Excepciones.PersistenciaException;
import Modelo.Cita;
import Modelo.EstadoCita;
import Modelo.Mascota;
import Modelo.Veterinario;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // INSERT
    public int insertar(Cita cita) throws PersistenciaException {

        String sql = "INSERT INTO citas (id_mascota, id_veterinario, fecha, hora, motivo, estado) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cita.getMascota().getId());
            ps.setInt(2, cita.getVeterinario().getId());
            ps.setDate(3, Date.valueOf(cita.getFecha()));
            ps.setTime(4, Time.valueOf(cita.getHora()));
            ps.setString(5, cita.getMotivo());
            ps.setString(6, cita.getEstado().name());
            ps.executeUpdate();

            try (ResultSet generadas = ps.getGeneratedKeys()) {
                if (generadas.next()) {
                    return generadas.getInt(1);
                }
            }

            throw new PersistenciaException("No se pudo obtener el id generado para la cita.");

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al guardar la cita en la base de datos.", ex);
        }
    }

    // SELECT: todas las citas, reconstruidas con las mascotas y veterinarios ya cargados.
    public List<Cita> listarTodas(List<Mascota> mascotas, List<Veterinario> veterinarios) throws PersistenciaException {

        String sql = "SELECT * FROM citas ORDER BY fecha, hora";
        List<Cita> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Mascota mascota = buscarMascota(mascotas, rs.getInt("id_mascota"));
                Veterinario veterinario = buscarVeterinario(veterinarios, rs.getInt("id_veterinario"));

                if (mascota == null || veterinario == null) {
                    continue; // si no encontramos la mascota o el veterinario, saltamos esa fila
                }

                Cita cita = new Cita(
                        mascota,
                        veterinario,
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("motivo")
                );
                cita.setEstado(EstadoCita.valueOf(rs.getString("estado")));
                resultado.add(cita);
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar las citas.", ex);
        }

        return resultado;
    }

    // UPDATE: cambia el estado de una cita ya guardada.
    public void actualizarEstado(int idMascota, int idVeterinario, LocalDate fecha, LocalTime hora,
                                  EstadoCita nuevoEstado) throws PersistenciaException {

        String sql = "UPDATE citas SET estado = ? WHERE id_mascota = ? AND id_veterinario = ? " +
                "AND fecha = ? AND hora = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, nuevoEstado.name());
            ps.setInt(2, idMascota);
            ps.setInt(3, idVeterinario);
            ps.setDate(4, Date.valueOf(fecha));
            ps.setTime(5, Time.valueOf(hora));
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al actualizar el estado de la cita.", ex);
        }
    }

    private Mascota buscarMascota(List<Mascota> mascotas, int id) {
        for (Mascota m : mascotas) {
            if (m.getId() == id) return m;
        }
        return null;
    }

    private Veterinario buscarVeterinario(List<Veterinario> veterinarios, int id) {
        for (Veterinario v : veterinarios) {
            if (v.getId() == id) return v;
        }
        return null;
    }
}
