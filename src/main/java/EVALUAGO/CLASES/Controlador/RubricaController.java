package EVALUAGO.CLASES.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/docente/rubricas")
public class RubricaController {

    @GetMapping("/crear")
    public String mostrarFormularioCreacion() {
        return "rubricas-creacion";
    }

    @PostMapping("/guardar")
    public String guardarRubrica(
            @RequestParam("nombreRubrica") String nombreRubrica,
            @RequestParam("tipoEvaluacion") String tipoEvaluacion,
            @RequestParam("criterios") String criterios,
            @RequestParam("puntajeMaximo") int puntajeMaximo,
            Model model) {

        // Validación básica
        if (nombreRubrica == null || nombreRubrica.trim().isEmpty()) {
            model.addAttribute("error", "El nombre de la rúbrica es obligatorio.");
            return "rubricas-creacion";
        }
        if (puntajeMaximo < 1 || puntajeMaximo > 100) {
            model.addAttribute("error", "El puntaje máximo debe estar entre 1 y 100.");
            return "rubricas-creacion";
        }

        // Simulación: Guardar en BD (reemplaza con repositorio real)
        // Rubrica rubrica = new Rubrica(nombreRubrica, tipoEvaluacion, criterios.split("\n"), puntajeMaximo);
        // rubricaRepository.save(rubrica);

        model.addAttribute("success", "Rúbrica '" + nombreRubrica + "' creada exitosamente.");
        return "rubricas-creacion";
    }
}