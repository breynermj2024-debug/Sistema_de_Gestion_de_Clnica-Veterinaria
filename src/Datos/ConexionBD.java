/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Datos;

/**
 *
 * @author HP
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de administrar la conexion JDBC hacia MySQL.
 *
 * No se usa un patron singleton con una unica Connection compartida porque
 * con Swing y SwingWorker varios hilos podrian pedir acceso a la vez; en su
 * lugar, cada metodo de los DAO pide una conexion nueva (try-with-resources)
 * y la cierra apenas termina. Esto evita condiciones de carrera entre hilos.
 *
 * Ajustar aqui los datos de conexion segun la maquina donde se ejecute.
 */
public final class ConexionBD {

    // Datos de conexion (ajustar usuario/clave segun cada equipo)
    private static final String HOST = "localhost";
    private static final String PUERTO = "3306";
    private static final String BASE_DATOS = "clinica_veterinaria";

    private static final String URL =
            "jdbc:mysql://" + HOST + ":" + PUERTO + "/" + BASE_DATOS
                    + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";

    private static final String USUARIO = "root";
    private static final String CLAVE = "farmacia123";

    private ConexionBD() {
    }

    public static Connection obtenerConexion() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, CLAVE);
    }
}