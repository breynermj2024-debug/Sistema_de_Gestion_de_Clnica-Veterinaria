/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author HP
 */
import Excepciones.PersistenciaException;
import Modelo.ConsultaGeneral;
import Modelo.NivelComplejidad;
import Modelo.Procedimiento;
import Modelo.Servicio;
import Modelo.Vacunacion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    // INSERT: guarda cualquier subclase de Servicio en la tabla comun.
    public int insertar(Servicio servicio) throws PersistenciaException {

        String sql = "INSERT INTO servicios "
                + "(tipo, nombre, fecha, id_mascota, motivo, tipo_vacuna, descripcion_procedimiento, complejidad) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, obtenerTipo(servicio));
            ps.setString(2, servicio.getNombre());
            ps.setDate(3, Date.valueOf(servicio.getFecha()));
            ps.setInt(4, servicio.getMascotaId());

            if (servicio instanceof ConsultaGeneral consultaGeneral) {
                ps.setString(5, consultaGeneral.getMotivo());
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);

            } else if (servicio instanceof Vacunacion vacunacion) {
                ps.setNull(5, Types.VARCHAR);
                ps.setString(6, vacunacion.getTipoVacuna());
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);

            } else if (servicio instanceof Procedimiento procedimiento) {
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setString(7, procedimiento.getDescripcionProcedimiento());
                ps.setString(8, procedimiento.getComplejidad().name());

            } else {
                throw new PersistenciaException(
                        "Tipo de servicio no reconocido: " + servicio.getClass().getSimpleName());
            }

            ps.executeUpdate();

            try (ResultSet generadas = ps.getGeneratedKeys()) {
                if (generadas.next()) {
                    int idGenerado = generadas.getInt(1);
                    servicio.setId(idGenerado);
                    return idGenerado;
                }
            }

            throw new PersistenciaException("No se pudo obtener el id generado para el servicio.");

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al guardar el servicio en la base de datos.", ex);
        }
    }

    // SELECT: trae todos los servicios de una mascota (para elegirlos al facturar).
    public List<Servicio> listarPorMascota(int idMascota) throws PersistenciaException {

        String sql = "SELECT * FROM servicios WHERE id_mascota = ? ORDER BY fecha DESC";
        List<Servicio> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idMascota);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    resultado.add(mapearFila(rs));
                }
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar los servicios de la mascota.", ex);
        }

        return resultado;
    }

    // SELECT: trae todos los servicios (para elegirlos al facturar sin filtrar por mascota).
    public List<Servicio> listarTodos() throws PersistenciaException {

        String sql = "SELECT * FROM servicios ORDER BY fecha DESC";
        List<Servicio> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapearFila(rs));
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar todos los servicios.", ex);
        }

        return resultado;
    }

    // SELECT: trae un servicio puntual por id (usado por FacturaDAO al reconstruir detalles).
    public Servicio buscarPorId(int idServicio) throws PersistenciaException {

        String sql = "SELECT * FROM servicios WHERE id_servicio = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idServicio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearFila(rs);
                }
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al buscar el servicio " + idServicio + ".", ex);
        }

        return null;
    }

    // UPDATE: permite corregir el detalle/motivo/complejidad de un servicio ya guardado.
    public void actualizar(Servicio servicio) throws PersistenciaException {

        String sql = "UPDATE servicios SET motivo = ?, tipo_vacuna = ?, "
                + "descripcion_procedimiento = ?, complejidad = ? WHERE id_servicio = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            if (servicio instanceof ConsultaGeneral consultaGeneral) {
                ps.setString(1, consultaGeneral.getMotivo());
                ps.setNull(2, Types.VARCHAR);
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);

            } else if (servicio instanceof Vacunacion vacunacion) {
                ps.setNull(1, Types.VARCHAR);
                ps.setString(2, vacunacion.getTipoVacuna());
                ps.setNull(3, Types.VARCHAR);
                ps.setNull(4, Types.VARCHAR);

            } else if (servicio instanceof Procedimiento procedimiento) {
                ps.setNull(1, Types.VARCHAR);
                ps.setNull(2, Types.VARCHAR);
                ps.setString(3, procedimiento.getDescripcionProcedimiento());
                ps.setString(4, procedimiento.getComplejidad().name());

            } else {
                throw new PersistenciaException(
                        "Tipo de servicio no reconocido: " + servicio.getClass().getSimpleName());
            }

            ps.setInt(5, servicio.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al actualizar el servicio.", ex);
        }
    }

    // DELETE
    public void eliminar(int idServicio) throws PersistenciaException {

        String sql = "DELETE FROM servicios WHERE id_servicio = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idServicio);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException(
                    "Error al eliminar el servicio (revise que no tenga facturas asociadas).", ex);
        }
    }

    // --- Metodos de apoyo ---

    private String obtenerTipo(Servicio servicio) {
        if (servicio instanceof ConsultaGeneral) return "CONSULTA_GENERAL";
        if (servicio instanceof Vacunacion) return "VACUNACION";
        if (servicio instanceof Procedimiento) return "PROCEDIMIENTO";
        return "DESCONOCIDO";
    }

    private Servicio mapearFila(ResultSet rs) throws SQLException {

        int id = rs.getInt("id_servicio");
        String tipo = rs.getString("tipo");
        LocalDate fecha = rs.getDate("fecha").toLocalDate();
        int idMascota = rs.getInt("id_mascota");

        switch (tipo) {

            case "CONSULTA_GENERAL":
                return new ConsultaGeneral(id, fecha, idMascota, rs.getString("motivo"));

            case "VACUNACION":
                return new Vacunacion(id, fecha, idMascota, rs.getString("tipo_vacuna"));

            case "PROCEDIMIENTO":
                NivelComplejidad complejidad = NivelComplejidad.valueOf(rs.getString("complejidad"));
                return new Procedimiento(id, fecha, idMascota, rs.getString("descripcion_procedimiento"), complejidad);

            default:
                throw new SQLException("Tipo de servicio desconocido en la base de datos: " + tipo);
        }
    }
}