package Presentacion;

import Excepciones.CitaNoDisponibleException;
import Modelo.*;
import Negocio.CitaServicio;
import Negocio.VeterinarioServicio;
import Negocio.ClienteServicio;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CitaPanel extends JPanel {

    private final CitaServicio citaServicio;
    private final ClienteServicio clienteServicio;
    private final VeterinarioServicio veterinarioServicio;

    private final JComboBox<Mascota> cboMascota = new JComboBox<>();
    private final JComboBox<Veterinario> cboVeterinario = new JComboBox<>();

    private final JTextField txtFecha = new JTextField("aaaa-mm-dd", 10);
    private final JTextField txtHora = new JTextField("HH:mm", 6);
    private final JTextArea txtMotivo = new JTextArea(2, 20);

    private final JComboBox<EstadoCita> cboEstado =
            new JComboBox<>(EstadoCita.values());

    private final DefaultTableModel modeloTabla = new DefaultTableModel(
            new Object[]{"ID", "Mascota", "Veterinario", "Fecha", "Hora", "Motivo", "Estado"}, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable tabla = new JTable(modeloTabla);
    private final JLabel lblEstadoCarga = new JLabel(" ");


    public CitaPanel(
            CitaServicio citaServicio,
            ClienteServicio clienteServicio,
            VeterinarioServicio veterinarioServicio
    ) {

        this.citaServicio = citaServicio;
        this.clienteServicio = clienteServicio;
        this.veterinarioServicio = veterinarioServicio;

        setLayout(new BorderLayout(10,10));

        add(construirFormulario(), BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
        add(lblEstadoCarga, BorderLayout.SOUTH);

        actualizarDatos();
        cargarCitasEnSegundoPlano();
    }


    private JPanel construirFormulario() {

        JPanel panel = new JPanel(new GridLayout(3,4,5,5));

        panel.add(new JLabel("Mascota:"));
        panel.add(cboMascota);

        panel.add(new JLabel("Veterinario:"));
        panel.add(cboVeterinario);

        panel.add(new JLabel("Fecha:"));
        panel.add(txtFecha);

        panel.add(new JLabel("Hora:"));
        panel.add(txtHora);

        panel.add(new JLabel("Motivo:"));
        panel.add(new JScrollPane(txtMotivo));

        panel.add(new JLabel("Estado:"));
        panel.add(cboEstado);


        JButton btnAgendar = new JButton("Agendar cita");
        btnAgendar.addActionListener(e -> agendarCita());


        JButton btnRecargar = new JButton("Recargar");
        btnRecargar.addActionListener(e -> actualizarDatos());


        JPanel botones = new JPanel();

        botones.add(btnAgendar);
        botones.add(btnRecargar);


        JPanel contenedor = new JPanel(new BorderLayout());

        contenedor.add(panel,BorderLayout.CENTER);
        contenedor.add(botones,BorderLayout.SOUTH);


        return contenedor;
    }



    private void agendarCita(){

        try {

            Mascota mascota =
                    (Mascota)cboMascota.getSelectedItem();

            Veterinario veterinario =
                    (Veterinario)cboVeterinario.getSelectedItem();


            if(mascota == null || veterinario == null){

                JOptionPane.showMessageDialog(
                        this,
                        "Debe seleccionar mascota y veterinario."
                );

                return;
            }


            LocalDate fecha =
                    LocalDate.parse(txtFecha.getText().trim());

            LocalTime hora =
                    LocalTime.parse(txtHora.getText().trim());


            citaServicio.agendar(
                    mascota,
                    veterinario,
                    fecha,
                    hora,
                    txtMotivo.getText()
            );


            JOptionPane.showMessageDialog(
                    this,
                    "Cita registrada correctamente."
            );


            cargarCitasEnSegundoPlano();


        }catch(CitaNoDisponibleException ex){

            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage()
            );


        }catch(Exception ex){

            JOptionPane.showMessageDialog(
                    this,
                    "Error: " + ex.getMessage()
            );
        }

    }



    public void actualizarDatos(){

        cboMascota.removeAllItems();
        cboVeterinario.removeAllItems();


        for(Veterinario veterinario :
                veterinarioServicio.listarTodos()){

            cboVeterinario.addItem(veterinario);

        }


        for(Cliente cliente :
                clienteServicio.listarClientes()){


            for(Mascota mascota :
                    cliente.getMascotas()){

                cboMascota.addItem(mascota);

            }

        }

    }



    private void cargarCitasEnSegundoPlano(){

        lblEstadoCarga.setText("Cargando citas...");


        SwingWorker<List<Cita>,Void> worker =
                new SwingWorker<>(){


                    @Override
                    protected List<Cita> doInBackground(){

                        return citaServicio.listarTodas();

                    }



                    @Override
                    protected void done(){

                        try{

                            List<Cita> citas=get();

                            modeloTabla.setRowCount(0);


                            for(Cita c:citas){

                                modeloTabla.addRow(new Object[]{
                                        c.getId(),
                                        c.getMascota().getNombre(),
                                        c.getVeterinario().getNombre(),
                                        c.getFecha(),
                                        c.getHora(),
                                        c.getMotivo(),
                                        c.getEstado()
                                });

                            }


                            lblEstadoCarga.setText(
                                    "Citas actualizadas."
                            );


                        }catch(Exception ex){

                            JOptionPane.showMessageDialog(
                                    CitaPanel.this,
                                    ex.getMessage()
                            );

                        }

                    }

                };


        worker.execute();

    }

}