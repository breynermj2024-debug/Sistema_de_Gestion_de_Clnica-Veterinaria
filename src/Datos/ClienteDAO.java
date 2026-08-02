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
import Modelo.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    // INSERT
    public int insertar(Cliente cliente) throws PersistenciaException {

        String sql = "INSERT INTO clientes (nombre, telefono, correo, direccion) VALUES (?, ?, ?, ?)";

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, cliente.getNombre());
            ps.setString(2, cliente.getTelefono());
            ps.setString(3, cliente.getCorreo());
            ps.setString(4, cliente.getDireccion());
            ps.executeUpdate();

            try (ResultSet generadas = ps.getGeneratedKeys()) {
                if (generadas.next()) {
                    int idGenerado = generadas.getInt(1);
                    cliente.setId(idGenerado);
                    return idGenerado;
                }
            }

            throw new PersistenciaException("No se pudo obtener el id generado para el cliente.");

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al guardar el cliente en la base de datos.", ex);
        }
    }

    // SELECT: todos los clientes.
    public List<Cliente> listarTodos() throws PersistenciaException {

        String sql = "SELECT * FROM clientes ORDER BY nombre";
        List<Cliente> resultado = new ArrayList<>();

        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id_cliente"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo"),
                        rs.getString("direccion")
                );
                resultado.add(cliente);
            }

        } catch (SQLException ex) {
            throw new PersistenciaException("Error al consultar los clientes.", ex);
        }

        return resultado;
    }
}