package Negocio;

import Excepciones.DatoInvalidoException;
import Modelo.Servicio;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Reglas de negocio para los servicios veterinarios
 *
 * Colecciones usadas:
 * - List<Servicio>: guarda todos los servicios registrados, como referencias
 *   polimórficas (pueden ser ConsultaGeneral, Vacunacion o Procedimiento).
 * - Set<String>: lleva el conjunto de nombres de tipos de servicio ya
 *   aplicados, sin duplicados, útil para reportes rápidos.
 */
public class ServicioServicio {

    private final List<Servicio> servicios = new ArrayList<>();
    private final Set<String> tiposAplicados = new HashSet<>();

    public void registrar(Servicio servicio) throws DatoInvalidoException {

        if (servicio == null) {
            throw new DatoInvalidoException("El servicio no puede ser nulo.");
        }

        if (servicio.getMascotaId() <= 0) {
            throw new DatoInvalidoException("El servicio debe estar asociado a una mascota válida.");
        }

        servicios.add(servicio);
        tiposAplicados.add(servicio.getNombre());
    }

    public List<Servicio> listarTodos() {
        return new ArrayList<>(servicios);
    }

    public Set<String> listarTiposAplicados() {
        return new HashSet<>(tiposAplicados);
    }

    // Demuestra el polimorfismo: suma precios sin importar la subclase real
    public double calcularTotal(List<Servicio> lista) {

        double total = 0.0;

        for (Servicio s : lista) {
            total += s.calcularPrecio();
        }

        return total;
    }
}
