package Negocio;

import Modelo.Cliente;
import Modelo.Mascota;
import java.util.ArrayList;
import java.util.List;

// Clase encargada de administrar los clientes y sus mascotas
public class ClienteServicio {

    // Atributos
    private final List<Cliente> clientes;

    // Constructor
    public ClienteServicio() {
        clientes = new ArrayList<>();
    }

    // Método para agregar un cliente
    public void agregarCliente(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no puede ser nulo.");
        }

        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del cliente es obligatorio.");
        }

        if (buscarClientePorId(cliente.getId()) != null) {
            throw new IllegalArgumentException("Ya existe un cliente con ese ID.");
        }

        clientes.add(cliente);
    }

    // Método para eliminar un cliente
    public boolean eliminarCliente(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (cliente != null) {
            return clientes.remove(cliente);
        }

        return false;
    }

    // Método para buscar un cliente por su ID
    public Cliente buscarClientePorId(int idCliente) {
        for (Cliente cliente : clientes) {
            if (cliente.getId() == idCliente) {
                return cliente;
            }
        }

        return null;
    }

    // Método para listar todos los clientes
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clientes);
    }

    // Método para agregar una mascota a un cliente
    public void agregarMascotaACliente(int idCliente, Mascota mascota) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (cliente == null) {
            throw new IllegalArgumentException("El cliente no existe.");
        }

        if (mascota == null) {
            throw new IllegalArgumentException("La mascota no puede ser nula.");
        }

        if (mascota.getNombre() == null || mascota.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la mascota es obligatorio.");
        }

        if (cliente.buscarMascotaPorId(mascota.getId()) != null) {
            throw new IllegalArgumentException(
                    "El cliente ya tiene una mascota registrada con ese ID."
            );
        }

        cliente.agregarMascota(mascota);
    }

    // Método para eliminar una mascota de un cliente
    public boolean eliminarMascotaDeCliente(int idCliente, int idMascota) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (cliente == null) {
            return false;
        }

        Mascota mascota = cliente.buscarMascotaPorId(idMascota);

        if (mascota == null) {
            return false;
        }

        return cliente.eliminarMascota(mascota);
    }

    //Método para listar las mascotas de un cliente
    public List<Mascota> listarMascotasDeCliente(int idCliente) {
        Cliente cliente = buscarClientePorId(idCliente);

        if (cliente == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(cliente.getMascotas());
    }

}