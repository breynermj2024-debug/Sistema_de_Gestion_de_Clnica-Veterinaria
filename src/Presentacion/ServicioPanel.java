package Presentacion;

import Excepciones.DatoInvalidoException;
import Modelo.*;
import Negocio.ClienteServicio;
import Negocio.ServicioServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel Swing para registrar y consultar servicios veterinarios
 * (ConsultaGeneral, Vacunacion, Procedimiento).
 * Se integra dentro del JFrame principal del sistema.
 */
public class ServicioPanel extends JPanel {

    private final ServicioServicio servicioServicio;
    private final ClienteServicio clienteServicio;

    private static int contadorId = 0;

    private final JComboBox<Mascota> cboMascota = new JComboBox<>();

    private final JComboBox<String> cboTipo =
            new JComboBox<>(new String[]{"Consulta General", "Vacunacion", "Procedimiento"});

    private final JTextField txtFecha = new JTextField("aaaa-mm-dd", 10);

    private final JTextField txtDetalle = new JTextField(15);

    private final JComboBox<NivelComplejidad> cboComplejidad =
            new JComboBox<>(NivelComplejidad.values());

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Tipo", "Mascota", "Fecha", "Detalle", "Precio"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modeloTabla);


    public ServicioPanel(ServicioServicio servicioServicio, ClienteServicio clienteServicio) {

        this.servicioServicio = servicioServicio;
        this.clienteServicio = clienteServicio;

        setLayout(new BorderLayout(10, 10));

        add(construirFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarMascotas();
        cargarTabla();
    }


    private JPanel construirFormulario() {

        JPanel panel = new JPanel(new GridLayout(3, 4, 5, 5));

        panel.add(new JLabel("Tipo de servicio:"));
        panel.add(cboTipo);

        panel.add(new JLabel("Mascota:"));
        panel.add(cboMascota);

        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);

        panel.add(new JLabel("Detalle (motivo/vacuna/desc.):"));
        panel.add(txtDetalle);

        panel.add(new JLabel("Complejidad (solo Procedimiento):"));
        panel.add(cboComplejidad);


        JButton btnAgregar = new JButton("Registrar servicio");
        btnAgregar.addActionListener(e -> registrarServicio());

        JButton btnRecargar = new JButton("Recargar");
        btnRecargar.addActionListener(e -> cargarTabla());


        JPanel botones = new JPanel();
        botones.add(btnAgregar);
        botones.add(btnRecargar);


        JPanel contenedor = new JPanel(new BorderLayout());
        contenedor.add(panel, BorderLayout.CENTER);
        contenedor.add(botones, BorderLayout.SOUTH);

        return contenedor;
    }


    private void registrarServicio() {

        try {

            Mascota mascota = (Mascota) cboMascota.getSelectedItem();

            if (mascota == null) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar una mascota.");
                return;
            }

            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());

            String tipo = (String) cboTipo.getSelectedItem();
            String detalle = txtDetalle.getText().trim();

            Servicio nuevo;

            switch (tipo) {

                case "Consulta General":
                    nuevo = new ConsultaGeneral(++contadorId, fecha, mascota.getId(), detalle);
                    break;

                case "Vacunacion":
                    nuevo = new Vacunacion(++contadorId, fecha, mascota.getId(), detalle);
                    break;

                case "Procedimiento":
                    NivelComplejidad complejidad = (NivelComplejidad) cboComplejidad.getSelectedItem();
                    nuevo = new Procedimiento(++contadorId, fecha, mascota.getId(), detalle, complejidad);
                    break;

                default:
                    JOptionPane.showMessageDialog(this, "Tipo de servicio no reconocido.");
                    return;
            }

            servicioServicio.registrar(nuevo);

            txtDetalle.setText("");

            cargarTabla();

            JOptionPane.showMessageDialog(this, "Servicio registrado correctamente.");

        } catch (DatoInvalidoException ex) {

            JOptionPane.showMessageDialog(this, ex.getMessage(), "Datos inválidos", JOptionPane.WARNING_MESSAGE);

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }


    public void cargarMascotas() {

        cboMascota.removeAllItems();

        for (Cliente cliente : clienteServicio.listarClientes()) {
            for (Mascota mascota : cliente.getMascotas()) {
                cboMascota.addItem(mascota);
            }
        }
    }

    public void actualizarMascotas() {
        cargarMascotas();
    }


    private void cargarTabla() {

        modeloTabla.setRowCount(0);

        List<Servicio> servicios = servicioServicio.listarTodos();

        for (Servicio s : servicios) {

            String detalle;

            if (s instanceof ConsultaGeneral cg) {
                detalle = cg.getMotivo();
            } else if (s instanceof Vacunacion v) {
                detalle = v.getTipoVacuna();
            } else if (s instanceof Procedimiento p) {
                detalle = p.getDescripcionProcedimiento();
            } else {
                detalle = "";
            }

            modeloTabla.addRow(new Object[]{
                    s.getId(),
                    s.getNombre(),
                    s.getMascotaId(),
                    s.getFecha(),
                    detalle,
                    s.calcularPrecio()
            });
        }
    }
}