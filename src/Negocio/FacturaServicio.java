/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

/**
 *
 * @author HP
 */
import Datos.FacturaDAO;
import Excepciones.DatoInvalidoException;
import Excepciones.PersistenciaException;
import Modelo.DetalleFactura;
import Modelo.Factura;
import Modelo.Servicio;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Reglas de negocio de facturacion.
 *
 * Colecciones usadas:
 * - Map<Integer, Factura>: cache en memoria de las facturas ya cargadas o
 *   creadas, indexadas por id, para poder recuperarlas rapido (O(1)) sin
 *   volver a consultar la base de datos cada vez que la UI las necesita.
 *
 * El calculo del total de cada factura se apoya en Servicio.calcularPrecio(),
 * que es polimorfico (cada subclase decide como calcula su propio precio).
 */
public class FacturaServicio {

    private final FacturaDAO facturaDAO = new FacturaDAO();

    // Cache local de facturas por id (segunda coleccion, distinta de la List
    // que ya se usa en ServicioServicio, con una funcion propia: busqueda rapida).
    private final Map<Integer, Factura> cache = new HashMap<>();

    /**
     * Crea una factura nueva a partir de un cliente y una lista de servicios
     * ya guardados en la base de datos (deben tener id > 0).
     */
    public Factura crearFactura(int idCliente, List<Servicio> serviciosSeleccionados)
            throws DatoInvalidoException, PersistenciaException {

        if (idCliente <= 0) {
            throw new DatoInvalidoException("Debe seleccionar un cliente valido para la factura.");
        }

        if (serviciosSeleccionados == null || serviciosSeleccionados.isEmpty()) {
            throw new DatoInvalidoException("La factura debe incluir al menos un servicio.");
        }

        Factura factura = new Factura(idCliente, LocalDate.now());

        for (Servicio servicio : serviciosSeleccionados) {

            if (servicio.getId() <= 0) {
                throw new DatoInvalidoException(
                        "El servicio \"" + servicio.getNombre() + "\" debe guardarse antes de facturarlo.");
            }

            // calcularPrecio() se ejecuta de forma polimorfica dentro de DetalleFactura.
            factura.agregarDetalle(new DetalleFactura(servicio, 1));
        }

        facturaDAO.insertar(factura);
        cache.put(factura.getId(), factura);

        return factura;
    }

    public List<Factura> listarTodas() throws PersistenciaException {

        List<Factura> facturas = facturaDAO.listarTodas();

        for (Factura factura : facturas) {
            cache.put(factura.getId(), factura);
        }

        return facturas;
    }

    public Factura buscarEnCache(int idFactura) {
        return cache.get(idFactura);
    }

    public void eliminarFactura(int idFactura) throws PersistenciaException {
        facturaDAO.eliminar(idFactura);
        cache.remove(idFactura);
    }
}
