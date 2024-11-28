package apifestiv.apifestiv.core.interfaces.servicios;

import apifestiv.apifestiv.core.interfaces.repositorios.IFestivoRepository;
import apifestiv.apifestiv.dominio.entidades.Festivo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

@Service
public class FestivoServiceImpl implements FestivoService {

    @Autowired
    private IFestivoRepository festivoRepository;

    private final Map<LocalDate, LocalDate> festivosTrasladados = new HashMap<>();

    @Override
    public boolean esFechaValida(LocalDate fecha) {
        return fecha != null;
    }

    @Override
    public Optional<Festivo> obtenerFestivoPorFecha(LocalDate fecha) {
        List<Festivo> festivos = festivoRepository.findByDiaAndMes(fecha.getDayOfMonth(), fecha.getMonthValue());
        return festivos.isEmpty() ? Optional.empty() : Optional.of(festivos.get(0));
    }

    @Override
    public String procesarFestivo(LocalDate fecha) {
        try {
            if (fecha == null) {
                return "La fecha ingresada no es válida.";
            }

            if (festivosTrasladados.containsKey(fecha)) {
                LocalDate fechaBase = festivosTrasladados.get(fecha);
                return "La Fecha " + fecha + " es festivo, trasladado desde: " + fechaBase;
            }

            Optional<Festivo> festivoOpt = obtenerFestivoPorFecha(fecha);
            if (festivoOpt.isEmpty()) {
                return procesarFestivoDinamico(fecha);
            }

            Festivo festivo = festivoOpt.get();
            switch (festivo.getIdtipo()) {
                case 1:
                    return "La Fecha " + fecha + " es Festivo (Fijo)";
                case 2:
                    return manejarFestivoTipo2(fecha);
                case 3:
                    return procesarFestivoTipo3(fecha);
                case 4:
                    return procesarFestivoTipo4(fecha);
                default:
                    return "Tipo de festivo no reconocido.";
            }
        } catch (Exception e) {
            return "Ocurrió un error al procesar la solicitud: " + e.getMessage();
        }
    }

    private String manejarFestivoTipo2(LocalDate fecha) {
        LocalDate fechaTrasladada = trasladarALunesMasCercano(fecha);
        if (!fecha.equals(fechaTrasladada)) {
            festivosTrasladados.put(fechaTrasladada, fecha);
            return "La Fecha " + fecha + " no es festivo. Fue trasladada al lunes: " + fechaTrasladada;
        }
        return "La Fecha " + fecha + " es Festivo (Ley de Puente Festivo)";
    }

    private String procesarFestivoTipo3(LocalDate fecha) {
        Map<String, LocalDate> festivosDinamicos = calcularFestivosDinamicos(fecha.getYear());
        for (Map.Entry<String, LocalDate> entry : festivosDinamicos.entrySet()) {
            if (fecha.equals(entry.getValue())) {
                return "La Fecha " + fecha + " es Festivo dinámico fijo (" + entry.getKey() + ")";
            }
        }
        return "La Fecha " + fecha + " no es festivo dinámico.";
    }

    private String procesarFestivoDinamico(LocalDate fecha) {
        Map<String, LocalDate> festivosDinamicos = calcularFestivosDinamicos(fecha.getYear());
        for (Map.Entry<String, LocalDate> entry : festivosDinamicos.entrySet()) {
            LocalDate fechaDinamica = entry.getValue();
            if (fecha.equals(fechaDinamica)) {
                if (!entry.getKey().equalsIgnoreCase("Jueves Santo") &&
                    !entry.getKey().equalsIgnoreCase("Viernes Santo") &&
                    !entry.getKey().equalsIgnoreCase("Domingo de Pascua")) {
                    
                    LocalDate fechaTrasladada = trasladarALunesMasCercano(fechaDinamica);
                    if (!fechaDinamica.equals(fechaTrasladada)) {
                        festivosTrasladados.put(fechaTrasladada, fechaDinamica);
                        return "La Fecha " + fecha + " no es festivo. Fue trasladada al lunes: " + fechaTrasladada;
                    }
                }
                return "La Fecha " + fecha + " es Festivo dinámico (" + entry.getKey() + ")";
            }
        }
        return "La Fecha " + fecha + " no es festivo.";
    }

    private String procesarFestivoTipo4(LocalDate fecha) {
        Map<String, LocalDate> festivosPascua = calcularFestivosDinamicos(fecha.getYear());
        for (Map.Entry<String, LocalDate> entry : festivosPascua.entrySet()) {
            LocalDate fechaDinamica = entry.getValue();
            if (fecha.equals(fechaDinamica)) {
                LocalDate fechaTrasladada = trasladarALunesMasCercano(fechaDinamica);
                if (!fechaDinamica.equals(fechaTrasladada)) {
                    festivosTrasladados.put(fechaTrasladada, fechaDinamica);
                    return "La Fecha " + fecha + " no es festivo. Fue trasladada al lunes: " + fechaTrasladada;
                }
                return "La Fecha " + fecha + " es Festivo (Basado en Pascua + Puente)";
            }
        }
        return "La Fecha " + fecha + " no es festivo Pascual.";
    }

    private LocalDate trasladarALunesMasCercano(LocalDate fecha) {
        if (fecha.getDayOfWeek() != DayOfWeek.MONDAY) {
            return fecha.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        }
        return fecha;
    }

    public Map<String, LocalDate> calcularFestivosDinamicos(int anio) {
        LocalDate domingoPascua = calcularDomingoDePascua(anio);
        Map<String, LocalDate> festivosDinamicos = new HashMap<>();
        festivosDinamicos.put("Jueves Santo", domingoPascua.minusDays(3));
        festivosDinamicos.put("Viernes Santo", domingoPascua.minusDays(2));
        festivosDinamicos.put("Domingo de Pascua", domingoPascua);
        festivosDinamicos.put("Ascensión del Señor", domingoPascua.plusDays(40));
        festivosDinamicos.put("Corpus Christi", domingoPascua.plusDays(61));
        festivosDinamicos.put("Sagrado Corazón de Jesús", domingoPascua.plusDays(68));
        return festivosDinamicos;
    }

    private LocalDate calcularDomingoDePascua(int anio) {
        int a = anio % 19;
        int b = anio / 100;
        int c = anio % 100;
        int d = b / 4;
        int e = b % 4;
        int f = (b + 8) / 25;
        int g = (b - f + 1) / 3;
        int h = (19 * a + b - d - g + 15) % 30;
        int i = c / 4;
        int k = c % 4;
        int l = (32 + 2 * e + 2 * i - h - k) % 7;
        int m = (a + 11 * h + 22 * l) / 451;
        int mes = (h + l - 7 * m + 114) / 31;
        int dia = ((h + l - 7 * m + 114) % 31) + 1;
        return LocalDate.of(anio, mes, dia);
    }
}