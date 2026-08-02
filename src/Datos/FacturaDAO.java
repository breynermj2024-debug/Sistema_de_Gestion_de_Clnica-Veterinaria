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
import Modelo.DetalleFactura;
import Modelo.Factura;
import Modelo.Servicio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para "facturas" y "detalle_factura".
 *
 * insertar() usa una transaccion: si falla el guardado de algun detalle,
 * se revierte tambien la factura (no queda una factura a medias en la BD).
 */
public class FacturaDAO {

    private final ServicioDAO servicioDAO = new ServicioDAO();

    // INSERT (transaccional): factura + todos sus detalles.
    public int insertar(Factura factura) throws PersistenciaException {

        String sqlFactura = "INSERT INTO facturas (id_cliente, fecha, total) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_factura (id_factura, id_servicio, cantidad, subtotal) VALUES (?, ?, ?, ?)";

        Connection conexion = null;

        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            int idFacturaGenerado;

            try (PreparedStatement psFactura =
                         conexion.prepareStatement(sqlFactura, Statement.RETURN_GENERATED_KEYS)) {

                psFactura.setInt(1, factura.getIdCliente());
                psFactura.setDate(2, Date.valueOf(factura.getFecha()));
                psFactura.setDouble(3, factura.getTotal());
                psFactura.executeUpdate();

                try (ResultSet generadas = psFactura.getGeneratedKeys()) {
                    if (!generadas.next()) {
                        throw new PersistenciaException("No se pudo obtener el id de la factura generada.");
                    }
                    idFacturaGenerado = generadas.getInt(1);
                }
            }

            try (PreparedStatement psDetalle = conexion.prepareStatement(sqlDetalle)) {
                for (DetalleFactura detalle : factura.getDetalles()) {
                    psDetalle.setInt(1, idFacturaGenerado);
                    psDetalle.setInt(2, detalle.getServicio().getId());
                    psDetalle.setInt(3, detalle.getCantidad());
                    psDetalle.setDouble(4, detalle.getSubtotal());
                    psDetalle.addBatch();
                }
                psDetalle.executeBatch();
            }

            conexion.commit();
            factura.setId(idFacturaGenerado);
            return idFacturaGenerado;

        } catch (SQLException | PersistenciaException ex) {

            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException exRollback) {
                    throw new PersistenciaException(
                            "Error al guardar la factura y no se pudo revertir la transaccion.", exRollback);
                }
            }

            throw new PersistenciaException("Error al guardar la factura, se revirtieron los cambios.", ex);

        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException ignorada) {
                    // Cierre de conexion: no hay mas que hacer si falla aqui.
                }
            }
        }
    }

    // SELECT: todas las facturas, con sus detalles y los servicios reconstruidos.
    public List<Factura> listarTodas() throws PersistenciaException {

        String sqlFacturas = "SELECT * FROM facturas ORDER BY fecha DESC";
        List<Factura> facturas = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sqlFacturas);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                int idFactura = rs.getInt("id_factura");
                int idCliente = rs.getInt("id_cliente");
                LocalDate fecha = rs.getDate("fecha").toLocalDate();
                double total = rs.getDouble("total");

                Factura factura = new Factura(idFactura, idCliente, fecha, total);
                cargarDetalles(conexion, factura);
                facturas.add(factura);
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar las facturas.", ex);
        }

        return facturas;
    }

    // SELECT: facturas de un cliente puntual.
    public List<Factura> listarPorCliente(int idCliente) throws PersistenciaException {

        String sql = "SELECT * FROM facturas WHERE id_cliente = ? ORDER BY fecha DESC";
        List<Factura> facturas = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idCliente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int idFactura = rs.getInt("id_factura");
                    LocalDate fecha = rs.getDate("fecha").toLocalDate();
                    double total = rs.getDouble("total");

                    Factura factura = new Factura(idFactura, idCliente, fecha, total);
                    cargarDetalles(conexion, factura);
                    facturas.add(factura);
                }
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar las facturas del cliente.", ex);
        }

        return facturas;
    }

    // UPDATE: recalcula y guarda el total (por ejemplo tras corregir un detalle).
    public void actualizarTotal(int idFactura, double nuevoTotal) throws PersistenciaException {

        String sql = "UPDATE facturas SET total = ? WHERE id_factura = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setDouble(1, nuevoTotal);
            ps.setInt(2, idFactura);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al actualizar el total de la factura.", ex);
        }
    }

    // DELETE: la base de datos elimina en cascada el detalle_factura asociado
    // (ver ON DELETE CASCADE en database.sql).
    public void eliminar(int idFactura) throws PersistenciaException {

        String sql = "DELETE FROM facturas WHERE id_factura = ?";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, idFactura);
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al eliminar la factura.", ex);
        }
    }

    // Carga los detalle_factura de una factura y reconstruye cada Servicio
    // (referencia polimorfica) usando ServicioDAO.
    private void cargarDetalles(Connection conexion, Factura factura) throws SQLException, PersistenciaException {

        String sql = "SELECT * FROM detalle_factura WHERE id_factura = ?";

        try (PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, factura.getId());

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {

                    int idDetalle = rs.getInt("id_detalle");
                    int idServicio = rs.getInt("id_servicio");
                    int cantidad = rs.getInt("cantidad");
                    double subtotal = rs.getDouble("subtotal");

                    Servicio servicio = servicioDAO.buscarPorId(idServicio);

                    if (servicio != null) {
                        factura.agregarDetalleExistente(
                                new DetalleFactura(idDetalle, servicio, cantidad, subtotal));
                    }
                }
            }
        }
    }
}
