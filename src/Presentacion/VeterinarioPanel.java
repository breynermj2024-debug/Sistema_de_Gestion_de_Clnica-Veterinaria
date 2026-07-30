package Presentacion;

import Excepciones.DatoInvalidoException;
import Modelo.Especialidad;
import Modelo.Veterinario;
import Negocio.VeterinarioServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * Panel Swing para registrar y consultar veterinarios.
 * Se integra dentro del JFrame principal del sistema.
 */
public class VeterinarioPanel extends JPanel {

    private final VeterinarioServicio veterinarioServicio;

    private final JTextField txtNombre = new JTextField(15);
    private final JTextField txtCedula = new JTextField(12);

    private final JComboBox<Especialidad> cboEspecialidad =
            new JComboBox<>(Especialidad.values());

    private final JTextField txtTelefono = new JTextField(10);
    private final JTextField txtEmail = new JTextField(15);

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{
                    "ID",
                    "Nombre",
                    "Cédula",
                    "Especialidad",
                    "Teléfono",
                    "Email"
            }, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modeloTabla);


    public VeterinarioPanel(VeterinarioServicio veterinarioServicio) {

        this.veterinarioServicio = veterinarioServicio;

        setLayout(new BorderLayout(10, 10));

        add(construirFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);

        cargarTabla();
    }


    private JPanel construirFormulario() {

        JPanel panel = new JPanel(
                new GridLayout(2, 6, 5, 5)
        );

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Cédula:"));
        panel.add(txtCedula);

        panel.add(new JLabel("Especialidad:"));
        panel.add(cboEspecialidad);

        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);

        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);


        JButton btnAgregar = new JButton("Agregar");

        btnAgregar.addActionListener(
                e -> agregarVeterinario()
        );


        JButton btnEliminar = new JButton(
                "Eliminar seleccionado"
        );

        btnEliminar.addActionListener(
                e -> eliminarSeleccionado()
        );


        JPanel botones = new JPanel();

        botones.add(btnAgregar);
        botones.add(btnEliminar);


        JPanel contenedor = new JPanel(
                new BorderLayout()
        );

        contenedor.add(
                panel,
                BorderLayout.CENTER
        );

        contenedor.add(
                botones,
                BorderLayout.SOUTH
        );


        return contenedor;
    }


    private void agregarVeterinario() {

        try {

            veterinarioServicio.registrar(
                    txtNombre.getText(),
                    txtCedula.getText(),
                    (Especialidad) cboEspecialidad.getSelectedItem(),
                    txtTelefono.getText(),
                    txtEmail.getText()
            );


            limpiarFormulario();

            cargarTabla();


            JOptionPane.showMessageDialog(
                    this,
                    "Veterinario registrado con éxito."
            );


        } catch (DatoInvalidoException ex) {


            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Datos inválidos",
                    JOptionPane.WARNING_MESSAGE
            );

        }
    }


    private void eliminarSeleccionado() {

        int fila = tabla.getSelectedRow();


        if (fila == -1) {

            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un veterinario de la tabla."
            );

            return;
        }


        int id = (int) modeloTabla.getValueAt(
                fila,
                0
        );


        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Eliminar este veterinario?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION
        );


        if (confirmacion == JOptionPane.YES_OPTION) {


            veterinarioServicio.eliminar(id);


            cargarTabla();

        }

    }


    private void limpiarFormulario() {

        txtNombre.setText("");
        txtCedula.setText("");
        txtTelefono.setText("");
        txtEmail.setText("");

    }


    private void cargarTabla() {

        modeloTabla.setRowCount(0);


        List<Veterinario> veterinarios =
                veterinarioServicio.listarTodos();


        for (Veterinario v : veterinarios) {


            modeloTabla.addRow(
                    new Object[]{
                            v.getId(),
                            v.getNombre(),
                            v.getCedula(),
                            v.getEspecialidad().getDescripcion(),
                            v.getTelefono(),
                            v.getEmail()
                    }
            );

        }

    }


    public JComboBox<Especialidad> getComboEspecialidad() {

        return cboEspecialidad;

    }
}