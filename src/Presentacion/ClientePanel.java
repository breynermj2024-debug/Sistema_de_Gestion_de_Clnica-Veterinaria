package Presentacion;

import Modelo.Cliente;
import Negocio.ClienteServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ClientePanel extends JPanel {

    private final ClienteServicio clienteServicio;

    private final JTextField txtId;
    private final JTextField txtNombre;
    private final JTextField txtTelefono;
    private final JTextField txtCorreo;
    private final JTextField txtDireccion;

    private final JButton btnRegistrar;
    private final JButton btnLimpiar;
    private final JButton btnEliminar;

    private final JTable tablaClientes;
    private final DefaultTableModel modeloTabla;

    public ClientePanel(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;

        setLayout(new BorderLayout(10, 10));

        JPanel panelFormulario = new JPanel(new GridLayout(6, 2, 8, 8));

        txtId = new JTextField();
        txtNombre = new JTextField();
        txtTelefono = new JTextField();
        txtCorreo = new JTextField();
        txtDireccion = new JTextField();

        btnRegistrar = new JButton("Registrar");
        btnLimpiar = new JButton("Limpiar");
        btnEliminar = new JButton("Eliminar seleccionado");

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder("Registro de clientes")
        );

        panelFormulario.add(new JLabel("ID:"));
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Teléfono:"));
        panelFormulario.add(txtTelefono);

        panelFormulario.add(new JLabel("Correo:"));
        panelFormulario.add(txtCorreo);

        panelFormulario.add(new JLabel("Dirección:"));
        panelFormulario.add(txtDireccion);

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(
                new Object[]{"ID", "Nombre", "Teléfono", "Correo", "Dirección", "Mascotas"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaClientes = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        scrollTabla.setBorder(
                BorderFactory.createTitledBorder("Clientes registrados")
        );

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.add(btnEliminar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelInferior, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarCliente());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnEliminar.addActionListener(e -> eliminarClienteSeleccionado());

        cargarTabla();
    }

    private void registrarCliente() {
        try {
            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            String telefono = txtTelefono.getText().trim();
            String correo = txtCorreo.getText().trim();
            String direccion = txtDireccion.getText().trim();

            validarCampos(id, nombre, telefono, correo, direccion);

            Cliente cliente = new Cliente(
                    id,
                    nombre,
                    telefono,
                    correo,
                    direccion
            );

            clienteServicio.agregarCliente(cliente);

            JOptionPane.showMessageDialog(
                    this,
                    "Cliente registrado correctamente."
            );

            cargarTabla();
            limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "El ID debe ser un número entero.",
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Dato inválido",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void validarCampos(
            int id,
            String nombre,
            String telefono,
            String correo,
            String direccion
    ) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID debe ser mayor que cero."
            );
        }

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio."
            );
        }

        if (telefono.isEmpty()) {
            throw new IllegalArgumentException(
                    "El teléfono es obligatorio."
            );
        }

        if (correo.isEmpty()) {
            throw new IllegalArgumentException(
                    "El correo es obligatorio."
            );
        }

        if (!correo.contains("@")) {
            throw new IllegalArgumentException(
                    "El correo electrónico no es válido."
            );
        }

        if (direccion.isEmpty()) {
            throw new IllegalArgumentException(
                    "La dirección es obligatoria."
            );
        }
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);

        for (Cliente cliente : clienteServicio.listarClientes()) {
            modeloTabla.addRow(new Object[]{
                    cliente.getId(),
                    cliente.getNombre(),
                    cliente.getTelefono(),
                    cliente.getCorreo(),
                    cliente.getDireccion(),
                    cliente.obtenerCantidadMascotas()
            });
        }
    }

    private void eliminarClienteSeleccionado() {
        int filaSeleccionada = tablaClientes.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente de la tabla."
            );
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar el cliente seleccionado?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            int idCliente = (int) modeloTabla.getValueAt(
                    filaSeleccionada,
                    0
            );

            boolean eliminado =
                    clienteServicio.eliminarCliente(idCliente);

            if (eliminado) {
                JOptionPane.showMessageDialog(
                        this,
                        "Cliente eliminado correctamente."
                );

                cargarTabla();
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");

        txtId.requestFocus();
    }
}