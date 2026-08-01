package Modelo;

import java.time.LocalDate;
//Representa una consulta veterinaria general (chequeo, revision de rutina)

public class ConsultaGeneral extends Servicio {

    //Precio base fijo para toda consulta general (atributo constante)

    private static final double PRECIO_BASE = 15000.0;

    //Motivo particular de esta consulta

    private String motivo;

    //Contructor usa super() para inicializar los atributos heredados

    public ConsultaGeneral(int id, LocalDate fecha, int mascotaId, String motivo) {
        super(id, "Consulta General", fecha, mascotaId);
        this.motivo = motivo;
    }

    // SobreEscritura del metodo abstracto de servici

    @Override
    public double calcularPrecio() {
        return PRECIO_BASE;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
