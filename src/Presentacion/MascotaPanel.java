package Presentacion;

import Modelo.Cliente;
import Modelo.Especie;
import Modelo.Mascota;
import Negocio.ClienteServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class MascotaPanel extends JPanel {

    private final ClienteServicio clienteServicio;

    private final JComboBox<Cliente> cbxCliente;
    private final JTextField txtId;
    private final JTextField txtNombre;
    private final JComboBox<Especie> cbxEspecie;
    private final JTextField txtRaza;
    private final JTextField txtEdad;
    private final JTextField txtPeso;
    private final JComboBox<String> cbxSexo;

    private final JButton btnRegistrar;
    private final JButton btnLimpiar;
    private final JButton btnActualizarClientes;
    private final JButton btnEliminar;

    private final JTable tablaMascotas;
    private final DefaultTableModel modeloTabla;

    public MascotaPanel(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;

        setLayout(new BorderLayout(10, 10));

        cbxCliente = new JComboBox<>();
        txtId = new JTextField();
        txtNombre = new JTextField();
        cbxEspecie = new JComboBox<>(Especie.values());
        txtRaza = new JTextField();
        txtEdad = new JTextField();
        txtPeso = new JTextField();

        cbxSexo = new JComboBox<>(
                new String[]{"Macho", "Hembra"}
        );

        btnRegistrar = new JButton("Registrar mascota");
        btnLimpiar = new JButton("Limpiar");
        btnActualizarClientes = new JButton("Actualizar clientes");
        btnEliminar = new JButton("Eliminar mascota");

        JPanel panelFormulario = new JPanel(
                new GridLayout(9, 2, 8, 8)
        );

        panelFormulario.setBorder(
                BorderFactory.createTitledBorder("Registro de mascotas")
        );

        panelFormulario.add(new JLabel("Cliente:"));
        panelFormulario.add(cbxCliente);

        panelFormulario.add(new JLabel("ID de la mascota:"));
        panelFormulario.add(txtId);

        panelFormulario.add(new JLabel("Nombre:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Especie:"));
        panelFormulario.add(cbxEspecie);

        panelFormulario.add(new JLabel("Raza:"));
        panelFormulario.add(txtRaza);

        panelFormulario.add(new JLabel("Edad:"));
        panelFormulario.add(txtEdad);

        panelFormulario.add(new JLabel("Peso:"));
        panelFormulario.add(txtPeso);

        panelFormulario.add(new JLabel("Sexo:"));
        panelFormulario.add(cbxSexo);

        panelFormulario.add(btnRegistrar);
        panelFormulario.add(btnLimpiar);

        modeloTabla = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Nombre",
                        "Especie",
                        "Raza",
                        "Edad",
                        "Peso",
                        "Sexo",
                        "Dueño"
                },
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaMascotas = new JTable(modeloTabla);

        JScrollPane scrollTabla = new JScrollPane(tablaMascotas);
        scrollTabla.setBorder(
                BorderFactory.createTitledBorder("Mascotas registradas")
        );

        JPanel panelBotones = new JPanel(
                new FlowLayout(FlowLayout.RIGHT)
        );

        panelBotones.add(btnActualizarClientes);
        panelBotones.add(btnEliminar);

        add(panelFormulario, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        btnRegistrar.addActionListener(e -> registrarMascota());
        btnLimpiar.addActionListener(e -> limpiarCampos());
        btnActualizarClientes.addActionListener(e -> actualizarClientes());
        btnEliminar.addActionListener(e -> eliminarMascotaSeleccionada());

        cbxCliente.addActionListener(e -> cargarTabla());

        actualizarClientes();
    }

    private void registrarMascota() {
        try {
            Cliente clienteSeleccionado =
                    (Cliente) cbxCliente.getSelectedItem();

            if (clienteSeleccionado == null) {
                throw new IllegalArgumentException(
                        "Primero debe registrar un cliente."
                );
            }

            int id = Integer.parseInt(txtId.getText().trim());
            String nombre = txtNombre.getText().trim();
            Especie especie =
                    (Especie) cbxEspecie.getSelectedItem();
            String raza = txtRaza.getText().trim();
            int edad = Integer.parseInt(txtEdad.getText().trim());
            double peso = Double.parseDouble(
                    txtPeso.getText().trim()
            );
            String sexo = (String) cbxSexo.getSelectedItem();

            validarCampos(
                    id,
                    nombre,
                    especie,
                    raza,
                    edad,
                    peso,
                    sexo
            );

            Mascota mascota = new Mascota(
                    id,
                    nombre,
                    especie,
                    raza,
                    edad,
                    peso,
                    sexo
            );

            clienteServicio.agregarMascotaACliente(
                    clienteSeleccionado.getId(),
                    mascota
            );

            JOptionPane.showMessageDialog(
                    this,
                    "Mascota registrada y asociada al cliente."
            );

            cargarTabla();
            limpiarCampos();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "La edad y el ID deben ser enteros, y el peso debe ser numérico.",
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
            Especie especie,
            String raza,
            int edad,
            double peso,
            String sexo
    ) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El ID debe ser mayor que cero."
            );
        }

        if (nombre.isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la mascota es obligatorio."
            );
        }

        if (especie == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar una especie."
            );
        }

        if (raza.isEmpty()) {
            throw new IllegalArgumentException(
                    "La raza es obligatoria."
            );
        }

        if (edad < 0) {
            throw new IllegalArgumentException(
                    "La edad no puede ser negativa."
            );
        }

        if (peso <= 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser mayor que cero."
            );
        }

        if (sexo == null) {
            throw new IllegalArgumentException(
                    "Debe seleccionar el sexo."
            );
        }
    }

    public void actualizarClientes() {
        cbxCliente.removeAllItems();

        for (Cliente cliente : clienteServicio.listarClientes()) {
            cbxCliente.addItem(cliente);
        }

        cargarTabla();
    }

    public void cargarTabla() {
        modeloTabla.setRowCount(0);

        Cliente clienteSeleccionado =
                (Cliente) cbxCliente.getSelectedItem();

        if (clienteSeleccionado == null) {
            return;
        }

        for (Mascota mascota :
                clienteServicio.listarMascotasDeCliente(
                        clienteSeleccionado.getId()
                )) {

            modeloTabla.addRow(new Object[]{
                    mascota.getId(),
                    mascota.getNombre(),
                    mascota.getEspecie(),
                    mascota.getRaza(),
                    mascota.getEdad(),
                    mascota.getPeso(),
                    mascota.getSexo(),
                    clienteSeleccionado.getNombre()
            });
        }
    }

    private void eliminarMascotaSeleccionada() {
        Cliente clienteSeleccionado =
                (Cliente) cbxCliente.getSelectedItem();

        if (clienteSeleccionado == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione un cliente."
            );
            return;
        }

        int filaSeleccionada = tablaMascotas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Seleccione una mascota de la tabla."
            );
            return;
        }

        int idMascota = (int) modeloTabla.getValueAt(
                filaSeleccionada,
                0
        );

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar la mascota seleccionada?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion == JOptionPane.YES_OPTION) {
            boolean eliminada =
                    clienteServicio.eliminarMascotaDeCliente(
                            clienteSeleccionado.getId(),
                            idMascota
                    );

            if (eliminada) {
                JOptionPane.showMessageDialog(
                        this,
                        "Mascota eliminada correctamente."
                );

                cargarTabla();
            }
        }
    }

    private void limpiarCampos() {
        txtId.setText("");
        txtNombre.setText("");
        txtRaza.setText("");
        txtEdad.setText("");
        txtPeso.setText("");

        cbxEspecie.setSelectedIndex(0);
        cbxSexo.setSelectedIndex(0);

        txtId.requestFocus();
    }
}