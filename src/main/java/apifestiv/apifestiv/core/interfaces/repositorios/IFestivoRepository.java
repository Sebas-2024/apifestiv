package apifestiv.apifestiv.core.interfaces.repositorios;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import apifestiv.apifestiv.dominio.entidades.Festivo;

public interface IFestivoRepository extends JpaRepository<Festivo, Integer> {

    // Método para buscar los festivos por mes y día
    List<Festivo> findByDiaAndMes(int mes, int dia);

}

