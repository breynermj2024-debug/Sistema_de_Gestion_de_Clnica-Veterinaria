package Presentacion;

import Negocio.CitaServicio;
import Negocio.ClienteServicio;
import Negocio.VeterinarioServicio;

import javax.swing.*;

public class Principal extends JFrame {

    public Principal() {

        setTitle("Sistema de Clínica Veterinaria");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        // Servicios compartidos
        ClienteServicio clienteServicio = new ClienteServicio();
        VeterinarioServicio veterinarioServicio = new VeterinarioServicio();
        CitaServicio citaServicio = new CitaServicio();


        // Paneles
        ClientePanel clientePanel =
                new ClientePanel(clienteServicio);

        MascotaPanel mascotaPanel =
                new MascotaPanel(clienteServicio);

        VeterinarioPanel veterinarioPanel =
                new VeterinarioPanel(veterinarioServicio);

        CitaPanel citaPanel =
                new CitaPanel(
                        citaServicio,
                        clienteServicio,
                        veterinarioServicio
                );


        // Pestañas
        JTabbedPane pestañas = new JTabbedPane();

        pestañas.addTab("Clientes", clientePanel);
        pestañas.addTab("Mascotas", mascotaPanel);
        pestañas.addTab("Veterinarios", veterinarioPanel);
        pestañas.addTab("Citas", citaPanel);



        pestañas.addChangeListener(e -> {

            int seleccion = pestañas.getSelectedIndex();


            if (seleccion == 1) {

                // Actualiza clientes en Mascotas
                mascotaPanel.actualizarClientes();

            }


            if (seleccion == 3) {

                // Actualiza mascotas y veterinarios en Citas
                citaPanel.actualizarDatos();

            }

        });


        add(pestañas);

    }


    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            Principal ventana = new Principal();

            ventana.setVisible(true);

        });

    }
}