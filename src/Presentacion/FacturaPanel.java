/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Presentacion;

/**
 *
 * @author HP
 */
import Datos.ClienteDAO;
import Datos.ServicioDAO;
import Excepciones.DatoInvalidoException;
import Excepciones.PersistenciaException;
import Modelo.Cliente;
import Modelo.Factura;
import Modelo.Servicio;
import Negocio.FacturaServicio;
import Negocio.ServicioServicio;
import Reportes.FacturaPDF;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FacturaPanel extends JPanel {

    private final ClienteDAO clienteDAO = new ClienteDAO();
    private final ServicioDAO servicioDAO = new ServicioDAO();
    private final FacturaServicio facturaServicio = new FacturaServicio();
    private final ServicioServicio servicioServicio; // solo para calcularTotal()
    private final FacturaPDF facturaPDF = new FacturaPDF();

    private JComboBox<Cliente> comboClientes;
    private JList<Servicio> listaServicios;
    private JLabel labelTotal;
    private JButton btnCrearFactura;
    private JButton btnGenerarPDF;

    private Factura ultimaFacturaCreada;

    public FacturaPanel(ServicioServicio servicioServicio) {
        this.servicioServicio = servicioServicio;
        construirInterfaz();
        actualizarDatos();
    }

    private void construirInterfaz() {
        setLayout(new BorderLayout(10, 10));

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.add(new JLabel("Cliente:"));
        comboClientes = new JComboBox<>();
        comboClientes.setPreferredSize(new Dimension(200, 25));
        panelSuperior.add(comboClientes);
        add(panelSuperior, BorderLayout.NORTH);

        listaServicios = new JList<>();
        listaServicios.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(new JScrollPane(listaServicios), BorderLayout.CENTER);

        JPanel panelInferior = new JPanel(new BorderLayout());
        labelTotal = new JLabel("Total: ₡0.00");
        panelInferior.add(labelTotal, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnCrearFactura = new JButton("Crear Factura");
        btnGenerarPDF = new JButton("Generar PDF");
        btnGenerarPDF.setEnabled(false);

        btnCrearFactura.addActionListener(e -> crearFactura());
        btnGenerarPDF.addActionListener(e -> generarPDF());

        panelBotones.add(btnCrearFactura);
        panelBotones.add(btnGenerarPDF);
        panelInferior.add(panelBotones, BorderLayout.SOUTH);

        add(panelInferior, BorderLayout.SOUTH);

        listaServicios.addListSelectionListener(e -> actualizarTotal());
    }

    public void actualizarDatos() {
        try {
            comboClientes.removeAllItems();
            for (Cliente c : clienteDAO.listarTodos()) {
                comboClientes.addItem(c);
            }

            List<Servicio> servicios = servicioDAO.listarTodos();
            listaServicios.setListData(servicios.toArray(new Servicio[0]));

            actualizarTotal();

        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al cargar datos desde la base de datos: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void actualizarTotal() {
        List<Servicio> seleccionados = listaServicios.getSelectedValuesList();
        double total = servicioServicio.calcularTotal(seleccionados);
        labelTotal.setText(String.format("Total: ₡%.2f", total));
    }

    private void crearFactura() {
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        List<Servicio> seleccionados = listaServicios.getSelectedValuesList();

        if (cliente == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un cliente.",
                    "Dato faltante", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            ultimaFacturaCreada = facturaServicio.crearFactura(cliente.getId(), seleccionados);
            JOptionPane.showMessageDialog(this,
                    "Factura #" + ultimaFacturaCreada.getId() + " creada. Total: ₡"
                            + String.format("%.2f", ultimaFacturaCreada.getTotal()));
            btnGenerarPDF.setEnabled(true);
        } catch (DatoInvalidoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(),
                    "Datos inválidos", JOptionPane.ERROR_MESSAGE);
        } catch (PersistenciaException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la factura: " + ex.getMessage(),
                    "Error de base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void generarPDF() {
        if (ultimaFacturaCreada == null) {
            JOptionPane.showMessageDialog(this, "Primero debe crear una factura.",
                    "Sin factura", JOptionPane.WARNING_MESSAGE);
            return;
        }
        Cliente cliente = (Cliente) comboClientes.getSelectedItem();
        try {
            String archivo = facturaPDF.generar(ultimaFacturaCreada, cliente);
            JOptionPane.showMessageDialog(this, "PDF generado en: " + archivo);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al generar PDF: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}