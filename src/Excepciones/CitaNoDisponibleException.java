package Excepciones;

public class CitaNoDisponibleException extends Exception {

    public CitaNoDisponibleException(String mensaje) {
        super(mensaje);
    }

    public CitaNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
