package Datos;

import Excepciones.PersistenciaException;
import Modelo.Consulta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    // INSERT
    public int insertar(Consulta consulta) throws PersistenciaException {

        String sql = "INSERT INTO consultas (id_mascota, id_veterinario, fecha, diagnostico, tratamiento, observaciones) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, consulta.getMascotaId());
            ps.setInt(2, consulta.getVeterinarioId());
            ps.setDate(3, Date.valueOf(consulta.getFecha()));
            ps.setString(4, consulta.getDiagnostico());
            ps.setString(5, consulta.getTratamiento());
            ps.setString(6, consulta.getObservaciones());
            ps.executeUpdate();

            try (ResultSet generadas = ps.getGeneratedKeys()) {
                if (generadas.next()) {
                    return generadas.getInt(1);
                }
            }

            throw new PersistenciaException("No se pudo obtener el id generado para la consulta.");

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al guardar la consulta en la base de datos.", ex);
        }
    }

    // SELECT: todas las consultas.
    public List<Consulta> listarTodas() throws PersistenciaException {

        String sql = "SELECT * FROM consultas ORDER BY fecha DESC";
        List<Consulta> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(new Consulta(
                        rs.getInt("id_mascota"),
                        rs.getInt("id_veterinario"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("diagnostico"),
                        rs.getString("tratamiento"),
                        rs.getString("observaciones")
                ));
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar las consultas.", ex);
        }

        return resultado;
    }

    // SELECT: consultas de una mascota puntual (historial clinico).
    public List<Consulta> listarPorMascota(int idMascota) throws PersistenciaException {

        String sql = "SELECT * FROM consultas WHERE id_mascota = ? ORDER BY fecha DESC";
        List<Consulta> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idMascota);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(new Consulta(
                            rs.getInt("id_mascota"),
                            rs.getInt("id_veterinario"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getString("diagnostico"),
                            rs.getString("tratamiento"),
                            rs.getString("observaciones")
                    ));
                }
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar el historial de la mascota.", ex);
        }

        return resultado;
    }
}
