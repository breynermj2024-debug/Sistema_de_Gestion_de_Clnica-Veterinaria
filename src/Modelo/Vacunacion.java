package Modelo;

import java.time.LocalDate;
import java.util.HashMap;
import  java.util.Map;

// Representa la aplicación de una vacuna a una mascota
public class Vacunacion extends Servicio {

    // Tabla de tarifas según el tipo de vacuna (compartida por todas las instancias)

    private static final Map<String, Double> TARIFA = new HashMap<>();

    // Bloque estático se ejecuta una sola vez, al cargar la clase

    static {
        TARIFA.put("Antirrabica", 8000.0);
        TARIFA.put("Polivalente", 12000.0);
        TARIFA.put("Desparasitante", 6000.0);
    }

    // Tarifa pro defecto si el tipo de vacuna no esta en la tabla

    private static final double TARIFA_DEFECTO = 7000.0;

    // Tipo particular de esta vacuna

    public String tipoVacuna;

    // Contructor usa super() para inicializar las atributow heredados

    public Vacunacion(int id, LocalDate fecha, int mascotaId, String tipoVacuna) {
        super(id, "Vacunacion", fecha, mascotaId);
        this.tipoVacuna = tipoVacuna;
    }

    // Sobreescritura del metodo abstracto de servicios

    @Override
    public double calcularPrecio() {
        return TARIFA.getOrDefault(tipoVacuna, TARIFA_DEFECTO);
    }

    public String getTipoVacuna() {
        return tipoVacuna;
    }

    public void setTipoVacuna(String tipoVacuna) {
        this.tipoVacuna = tipoVacuna;
    }

}
