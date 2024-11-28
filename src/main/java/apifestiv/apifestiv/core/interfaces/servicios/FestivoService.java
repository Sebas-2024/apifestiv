package apifestiv.apifestiv.core.interfaces.servicios;

import apifestiv.apifestiv.dominio.entidades.Festivo;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface FestivoService {

    boolean esFechaValida(LocalDate fecha);

    Optional<Festivo> obtenerFestivoPorFecha(LocalDate fecha);

    String procesarFestivo(LocalDate fecha);

    Map<String, LocalDate> calcularFestivosDinamicos(int anio);
}

