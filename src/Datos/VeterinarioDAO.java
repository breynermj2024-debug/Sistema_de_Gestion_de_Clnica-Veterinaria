package Datos;

import Excepciones.PersistenciaException;
import Modelo.Especialidad;
import Modelo.Veterinario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarioDAO {

    // INSERT
    public int insertar(Veterinario veterinario) throws PersistenciaException {

        String sql = "INSERT INTO veterinarios (nombre, cedula, especialidad, telefono, email, activo) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, veterinario.getNombre());
            ps.setString(2, veterinario.getCedula());
            ps.setString(3, veterinario.getEspecialidad().name());
            ps.setString(4, veterinario.getTelefono());
            ps.setString(5, veterinario.getEmail());
            ps.setBoolean(6, veterinario.isActivo());
            ps.executeUpdate();

            try (ResultSet generadas = ps.getGeneratedKeys()) {
                if (generadas.next()) {
                    return generadas.getInt(1);
                }
            }

            throw new PersistenciaException("No se pudo obtener el id generado para el veterinario.");

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al guardar el veterinario en la base de datos.", ex);
        }
    }

    // SELECT: todos los veterinarios.
    public List<Veterinario> listarTodos() throws PersistenciaException {

        String sql = "SELECT * FROM veterinarios ORDER BY nombre";
        List<Veterinario> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                resultado.add(mapearFila(rs));
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar los veterinarios.", ex);
        }

        return resultado;
    }

    // UPDATE
    public void actualizar(Veterinario veterinario) throws PersistenciaException {

        String sql = "UPDATE veterinarios SET nombre = ?, cedula = ?, especialidad = ?, " +
                "telefono = ?, email = ?, activo = ? WHERE id_veterinario = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, veterinario.getNombre());
            ps.setString(2, veterinario.getCedula());
            ps.setString(3, veterinario.getEspecialidad().name());
            ps.setString(4, veterinario.getTelefono());
            ps.setString(5, veterinario.getEmail());
            ps.setBoolean(6, veterinario.isActivo());
            ps.setInt(7, veterinario.getId());
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al actualizar el veterinario.", ex);
        }
    }

    // DELETE
    public void eliminar(int idVeterinario) throws PersistenciaException {

        String sql = "DELETE FROM veterinarios WHERE id_veterinario = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idVeterinario);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException(
                    "Error al eliminar el veterinario (revise que no tenga citas asociadas).", ex);
        }
    }

    private Veterinario mapearFila(ResultSet rs) throws SQLException {
        return new Veterinario(
                rs.getInt("id_veterinario"),
                rs.getString("nombre"),
                rs.getString("cedula"),
                Especialidad.valueOf(rs.getString("especialidad")),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getBoolean("activo")
        );
    }
}
