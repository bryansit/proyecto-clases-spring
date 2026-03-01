package EVALUAGO.CLASES.Controlador;

import EVALUAGO.CLASES.Entidad.Clase;
import EVALUAGO.CLASES.Repositorio.ClaseRepository;

// ***************************************************************
// CORRECCIÓN: Usar la clase Authentication de Spring Security
import org.springframework.security.core.Authentication; 
// ***************************************************************

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional; 
import java.net.URLEncoder; 
import java.nio.charset.StandardCharsets; 

@Controller
@RequestMapping("/docente")
public class ClaseController {

    @Autowired
    private ClaseRepository claseRepository;

    // Mapea a /docente/clases
    @GetMapping("/clases")
    public String gestionarClases() {
        return "clases-gestion"; 
    }

    // Mapea a /docente/clases/manual
    @GetMapping("/clases/manual")
    public String mostrarFormularioClaseManual() {
        return "clases-creacion-manual"; 
    }
    
    // Mapea a /docente/clases/qr
    @GetMapping("/clases/qr")
    public String mostrarGeneradorQR() {
        return "clases-creacion-qr"; 
    }

    /**
     * Guarda la clase creada manualmente y redirige al panel activo.
     * AÑADE LA NOTIFICACIÓN DE ÉXITO.
     * Mapea a POST /docente/clases/guardar
     */
    @PostMapping("/clases/guardar")
    public String guardarClaseManual(
        @RequestParam("nombre") String nombreClase,
        @RequestParam("nivel") String nivel,
        @RequestParam("codigo") String codigo) 
    {
        Clase nuevaClase = new Clase();
        nuevaClase.setNombre(nombreClase);
        nuevaClase.setNivel(nivel);
        
        // Generación de código si es nulo o vacío
        if (codigo == null || codigo.trim().isEmpty()) {
            // Código de inscripción simple de 6 dígitos
            codigo = String.format("%06d", (int)(Math.random() * 999999)); 
        }
        nuevaClase.setCodigoInscripcion(codigo.toUpperCase().trim());
        
        // ** (PENDIENTE: Asignar el ID del Docente logueado de la sesión) **
        
        Clase claseGuardada = claseRepository.save(nuevaClase);
        
        // *****************************************************************
        // CAMBIO CLAVE: Añadir el parámetro 'claseCreada=true' a la URL
        // *****************************************************************
        return "redirect:/docente/clase/" + claseGuardada.getId() + "?claseCreada=true";
    }
    
    /**
     * Muestra el panel activo de una clase específica.
     * MANEJA LAS NOTIFICACIONES DE CREACIÓN Y UNIÓN DE ESTUDIANTES.
     * Mapea a /docente/clase/{claseId}
     */
    @GetMapping("/clase/{claseId}")
    public String mostrarPanelClase(
        @PathVariable Long claseId, 
        Authentication authentication, // Obtenemos la información del usuario
        @RequestParam(value = "claseCreada", required = false) boolean claseCreada,
        
        // Parámetros para la UNIÓN del ESTUDIANTE (Si un estudiante es el que accede)
        @RequestParam(value = "unidoExitosoEstudiante", required = false) boolean unidoExitosoEstudiante,
        @RequestParam(value = "mensaje", required = false) String mensajeEstudiante,
        
        // Parámetros de NOTIFICACIÓN (Si un docente recibe la alerta)
        @RequestParam(value = "notificarDocenteClaseId", required = false) Long notificarDocenteClaseId,
        @RequestParam(value = "estudianteNombre", required = false) String estudianteNombre,
        Model model) 
    {
        // 1. Cargar la clase
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + claseId));
        
        model.addAttribute("clase", clase);
        
        // Determinar el rol del usuario que accede a esta vista
        // La importación de org.springframework.security.core.Authentication es crucial aquí
        String rol = authentication.getAuthorities().iterator().next().getAuthority().replace("ROLE_", "");
        model.addAttribute("rolUsuario", rol);

        String mensaje = null;

        // =================================================================
        // 2. LÓGICA DE NOTIFICACIONES (DOCENTE)
        // =================================================================
        if (rol.equals("DOCENTE")) {
            if (claseCreada) {
                mensaje = "¡Clase **'" + clase.getNombre() + "'** creada exitosamente! Código: **" + clase.getCodigoInscripcion() + "**.";
            }
            else if (notificarDocenteClaseId != null && notificarDocenteClaseId.equals(claseId) && estudianteNombre != null) {
                mensaje = "¡El estudiante **" + estudianteNombre + "** se ha unido a la clase y ahora está en la sesión en vivo!";
            }
            if (mensaje != null) {
                model.addAttribute("notificacionMensaje", mensaje);
            }
            return "clases-panel-activo"; // Vista del Docente
        }
        
        // =================================================================
        // 3. LÓGICA DEL ESTUDIANTE UNIÉNDOSE A LA SESIÓN EN VIVO
        // =================================================================
        else if (rol.equals("ESTUDIANTE")) {
            if (unidoExitosoEstudiante) {
                // Mensaje que verá el estudiante al unirse
                model.addAttribute("notificacionEstudiante", "¡Unido al aula! Estás viendo la sesión en vivo del docente.");
            }
            // Retorna una nueva vista específica para la experiencia en vivo del ESTUDIANTE
            return "clases-sesion-estudiante"; 
        }

        return "redirect:/dashboard"; // En caso de rol no manejado
    }
 
 // Nuevo endpoint para mostrar la gestión de Evaluaciones de una clase
 @GetMapping("/clase/{claseId}/evaluaciones")
 public String gestionarEvaluaciones(@PathVariable Long claseId, Model model) {
     // 1. Buscar la clase para el breadcrumb y contexto
     Clase clase = claseRepository.findById(claseId)
             .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + claseId));
     
     // 2. Aquí se buscaría la lista de Evaluaciones y Rúbricas existentes para esa clase
     // List<Evaluacion> evaluaciones = evaluacionRepository.findByClaseId(claseId);

     model.addAttribute("clase", clase);
     // model.addAttribute("evaluaciones", evaluaciones);
     
     return "clases-evaluaciones-gestion"; // Nueva vista a crear
 }

 // Endpoint para mostrar el formulario de creación de una nueva Evaluación/Rúbrica
 @GetMapping("/clase/{claseId}/evaluaciones/crear")
 public String crearEvaluacion(@PathVariable Long claseId, Model model) {
     Clase clase = claseRepository.findById(claseId)
             .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + claseId));

     model.addAttribute("clase", clase);
     // model.addAttribute("evaluacion", new Evaluacion()); // Objeto para el formulario
     
     return "clases-evaluaciones-creacion"; // Nueva vista a crear
 }
}