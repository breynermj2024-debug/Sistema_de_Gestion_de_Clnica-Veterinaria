package Modelo;
import java.util.ArrayList;
import java.util.List;

//Clase Cliente
public class Cliente {

    //Atributos
    private int id;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;

    // Relación de composición
    private List<Mascota> mascotas;

    //Constructor
    public Cliente(int id, String nombre, String telefono, String correo, String direccion) {

        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.mascotas = new ArrayList<>();

    }

    // Métodos para administrar mascotas
    public void agregarMascota(Mascota mascota) {
        mascotas.add(mascota);
    }
    public void eliminarMascota(Mascota mascota) {
        mascotas.remove(mascota);
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    //Getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public List<Mascota> getMascotas() {
        return mascotas;
    }

    @Override
    public String toString() {
        return nombre;
    }

}
