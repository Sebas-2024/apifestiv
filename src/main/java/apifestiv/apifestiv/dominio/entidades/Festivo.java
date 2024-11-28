package apifestiv.apifestiv.dominio.entidades;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "festivo")
public class Festivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private int dia;
    private int mes;
    private int diaspascua;
    private int idtipo;


    public Festivo() {
    }


    public Festivo(int id, String nombre, int dia, int mes, int diaspascua, int idtipo) {
        this.id = id;
        this.nombre = nombre;
        this.dia = dia;
        this.mes = mes;
        this.diaspascua = diaspascua;
        this.idtipo = idtipo;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public int getDia() {
        return dia;
    }
    public void setDia(int dia) {
        this.dia = dia;
    }
    public int getMes() {
        return mes;
    }
    public void setMes(int mes) {
        this.mes = mes;
    }
    public int getDiaspascua() {
        return diaspascua;
    }
    public void setDiaspascua(int diaspascua) {
        this.diaspascua = diaspascua;
    }
    public int getIdtipo() {
        return idtipo;
    }
    public void setIdtipo(int idtipo) {
        this.idtipo = idtipo;
    }


    //public int getAnio(int añoConsulta) {
        // Dependiendo de la lógica que manejes, puedes asumir que el festivo es para el año de la consulta
        // O si tienes una regla de negocios, puedes modificar esta lógica
      //  LocalDate fecha = LocalDate.of(añoConsulta, mes, dia);
      //  return fecha.getYear();

}


