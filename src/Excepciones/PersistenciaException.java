/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepciones;

/**
 *
 * @author HP
 */
/**
 * Excepcion personalizada para errores al guardar, leer, actualizar o
 * eliminar datos en la base de datos.
 *
 * Se usa en los DAO para envolver SQLException y no obligar a que las
 * capas de negocio y presentacion conozcan detalles de JDBC: reciben un
 * mensaje claro de negocio en vez de un codigo SQL crudo.
 */
public class PersistenciaException extends Exception {

    public PersistenciaException(String mensaje) {
        super(mensaje);
    }

    public PersistenciaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}