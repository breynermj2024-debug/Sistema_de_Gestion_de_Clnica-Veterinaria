/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

/**
 *
 * @author HP
 */
import Modelo.Cliente;
import Modelo.ConsultaGeneral;
import Modelo.DetalleFactura;
import Modelo.Factura;
import java.time.LocalDate;

public class PruebaFacturaPDF {
    public static void main(String[] args) throws Exception {
        // Cliente de prueba
        Cliente cliente = new Cliente(1, "Juan Pérez", "8888-8888", "juan@correo.com", "San José");

        // Servicio de prueba (subclase concreta, con id > 0 simulando que ya se guardó en BD)
        ConsultaGeneral consulta = new ConsultaGeneral(1, LocalDate.now(), 1, "Chequeo de rutina");

        // Factura de prueba con un detalle
        Factura factura = new Factura(cliente.getId(), LocalDate.now());
        factura.setId(999);
        factura.agregarDetalle(new DetalleFactura(consulta, 1));

        // Generar el PDF
        FacturaPDF generador = new FacturaPDF();
        String archivo = generador.generar(factura, cliente);
        System.out.println("PDF generado en: " + archivo);
    }
}