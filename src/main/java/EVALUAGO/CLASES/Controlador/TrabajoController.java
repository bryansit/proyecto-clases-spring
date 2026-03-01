package EVALUAGO.CLASES.Controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/docente/trabajos")
public class TrabajoController {

    @GetMapping
    public String mostrarTrabajos(Model model) {
        // Simulación de datos (reemplazar con consulta a BD)
        List<Trabajo> trabajos = new ArrayList<>();
        trabajos.add(new Trabajo(1L, "Trabajo 1", "INDIVIDUAL", "Rúbrica A", "Entregado", 85));
        trabajos.add(new Trabajo(2L, "Proyecto Grupal", "GRUPAL", "Rúbrica B", "Entregado", 90));
        model.addAttribute("trabajos", trabajos);
        return "trabajos-gestion";
    }

    @PostMapping("/guardarNota")
    @ResponseBody
    public Resultado guardarNota(@RequestBody NotaRequest notaRequest) {
        // Simulación: Guardar nota en BD
        System.out.println("Guardando nota: Trabajo ID=" + notaRequest.getTrabajoId() + ", Nota=" + notaRequest.getNota());
        // En producción: actualizar en BD
        return new Resultado(true, "Nota guardada para Trabajo ID " + notaRequest.getTrabajoId());
    }

    // Clases auxiliares
    public static class Trabajo {
        private Long id;
        private String nombre;
        private String tipo;
        private String rubricaNombre;
        private String estado;
        private int nota;

        public Trabajo(Long id, String nombre, String tipo, String rubricaNombre, String estado, int nota) {
            this.id = id;
            this.nombre = nombre;
            this.tipo = tipo;
            this.rubricaNombre = rubricaNombre;
            this.estado = estado;
            this.nota = nota;
        }

        // Getters
        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public String getTipo() { return tipo; }
        public String getRubricaNombre() { return rubricaNombre; }
        public String getEstado() { return estado; }
        public int getNota() { return nota; }
        public void setNota(int nota) { this.nota = nota; }
    }

    public static class NotaRequest {
        private Long trabajoId;
        private int nota;

        // Getters y setters
        public Long getTrabajoId() { return trabajoId; }
        public void setTrabajoId(Long trabajoId) { this.trabajoId = trabajoId; }
        public int getNota() { return nota; }
        public void setNota(int nota) { this.nota = nota; }
    }

    public static class Resultado {
        private boolean success;
        private String message;

        public Resultado(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
}