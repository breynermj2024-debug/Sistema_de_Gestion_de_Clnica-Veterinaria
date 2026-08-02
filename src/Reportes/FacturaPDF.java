/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Reportes;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

import Modelo.Factura;
import Modelo.DetalleFactura;
import Modelo.Cliente;

public class FacturaPDF {

    // Constante final: ruta base donde se guardan los reportes
    private static final String CARPETA_SALIDA = "reportes/";

    public String generar(Factura factura, Cliente cliente) throws DocumentException, IOException {
        Document documento = new Document();
        String nombreArchivo = CARPETA_SALIDA + "factura_" + factura.getId() + ".pdf";

        PdfWriter.getInstance(documento, new FileOutputStream(nombreArchivo));
        documento.open();

        // Encabezado
        Font tituloFont = new Font(Font.HELVETICA, 16, Font.BOLD);
        documento.add(new Paragraph("Clínica Veterinaria - Factura #" + factura.getId(), tituloFont));
        documento.add(new Paragraph("Cliente: " + cliente.getNombre()));
        documento.add(new Paragraph("Teléfono: " + cliente.getTelefono()));
        documento.add(new Paragraph("Fecha: " + factura.getFecha()));
        documento.add(new Paragraph(" "));

        // Tabla de detalles (polimorfismo real: cada Servicio dentro del
        // DetalleFactura ya calculó su propio precio con calcularPrecio())
        PdfPTable tabla = new PdfPTable(4);
        tabla.addCell("Servicio");
        tabla.addCell("Fecha");
        tabla.addCell("Cantidad");
        tabla.addCell("Subtotal");

        for (DetalleFactura detalle : factura.getDetalles()) {
            tabla.addCell(detalle.getServicio().getNombre());
            tabla.addCell(String.valueOf(detalle.getServicio().getFecha()));
            tabla.addCell(String.valueOf(detalle.getCantidad()));
            tabla.addCell(String.format("₡%.2f", detalle.getSubtotal()));
        }
        documento.add(tabla);

        documento.add(new Paragraph(" "));
        Font totalFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        documento.add(new Paragraph("Total: ₡" + String.format("%.2f", factura.getTotal()), totalFont));

        documento.close();
        return nombreArchivo;
    }
}