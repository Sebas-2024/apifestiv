package apifestiv.apifestiv.aplicaciones.controladores;

//import java.time.LocalDate;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;

//import apifestiv.apifestiv.core.interfaces.servicios.FestivoService;



//@RestController
//@RequestMapping("/festivos")
//public class FestivoController {

   // @Autowired
   // private FestivoService festivoService;

   // @GetMapping("/verificar/{anio}/{mes}/{dia}")
   // public String verificarFestivo(@PathVariable("anio") int anio, @PathVariable("mes") int mes, @PathVariable("dia") int dia) {
     //   LocalDate fecha = LocalDate.of(anio, mes, dia);
   //     return festivoService.procesarFestivo(fecha);
  //  }
//}












//@RestController
//@RequestMapping("/festivos")
//public class FestivoController {

  //  @Autowired
    //private FestivoService festivoService;

    //@GetMapping("/verificar/{anio}/{mes}/{dia}")
    //public String verificarFestivo(@PathVariable int anio, @PathVariable int mes, @PathVariable int dia) {
      //  LocalDate fecha = LocalDate.of(anio, mes, dia);
        //return festivoService.procesarFestivo(fecha);
    //}
//}


//Cualquier cosa este es el correcto


import java.time.DateTimeException;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import apifestiv.apifestiv.core.interfaces.servicios.FestivoService;

@RestController
@RequestMapping("/festivos")
public class FestivoController {

    @Autowired
    private FestivoService festivoService;

    @GetMapping("/verificar/{anio}/{mes}/{dia}")
    public String verificarFestivo(@PathVariable("anio") int anio, @PathVariable("mes") int mes, @PathVariable("dia") int dia) {
        try {
            LocalDate fecha = LocalDate.of(anio, mes, dia);
            return festivoService.procesarFestivo(fecha);
        } catch (DateTimeException e) {
            return "La fecha ingresada no es válida. Asegúrese de usar valores correctos para año, mes y día.";
    }
}

}


