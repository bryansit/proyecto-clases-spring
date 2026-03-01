package EVALUAGO.CLASES.Controlador;

import EVALUAGO.CLASES.Entidad.Clase;
import EVALUAGO.CLASES.Entidad.Usuario;
import EVALUAGO.CLASES.Repositorio.ClaseRepository;
import EVALUAGO.CLASES.Repositorio.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.regex.Pattern;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private ClaseRepository claseRepository;

    private static final Pattern CODIGO_PATTERN = Pattern.compile("^[A-Z0-9]{6}$");

    @GetMapping("/")
    public String home() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String redirectUserToDashboard() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String rolWithPrefix = auth.getAuthorities().iterator().next().getAuthority();
        String rol = rolWithPrefix.replace("ROLE_", "");
        return redirigirPorRol(rol);
    }

    @GetMapping("/oauth2/redirect")
    public String oauth2Redirect(OAuth2AuthenticationToken token, Model model) {
        String email = token.getPrincipal().getAttribute("email");
        if (email.equals("admin@example.com") || email.equals("docente@example.com")) {
            return "redirect:/?error";
        }
        usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario nuevo = new Usuario();
                    nuevo.setEmail(email);
                    nuevo.setPassword("");
                    nuevo.setDni(null);
                    nuevo.setRol("ESTUDIANTE");
                    return usuarioRepository.save(nuevo);
                });
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(newAuth);
        } catch (Exception e) {
            return "redirect:/?error";
        }
        return "redirect:/estudiante";
    }

    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@ModelAttribute Usuario usuario, Model model) {
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            model.addAttribute("error", "El correo electrónico ya está registrado. ⚠️");
            model.addAttribute("usuario", usuario);
            return "registro";
        }
        // NO encriptar: se guarda tal cual
        usuarioRepository.save(usuario);
        return "redirect:/?registroExitoso";
    }


    @GetMapping("/admin")
    public String adminDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario admin = usuarioRepository.findByEmail(email).orElse(null);
        model.addAttribute("admin", admin);
        return "admin";
    }

    @GetMapping("/docente")
    public String docenteDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario docente = usuarioRepository.findByEmail(email).orElse(null);
        model.addAttribute("docente", docente);
        return "docente";
    }

    @GetMapping("/estudiante")
    public String estudianteDashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        Usuario estudiante = usuarioRepository.findByEmail(email).orElse(null);
        model.addAttribute("estudiante", estudiante);
        return "estudiante";
    }

    @GetMapping("/estudiante/unirse")
    public String mostrarFormularioUnirse() {
        return "estudiante-unirse";
    }

    @PostMapping("/estudiante/unirse")
    public String procesarUnirseAClase(
            @RequestParam("codigo") String codigoClase,
            Authentication authentication,
            Model model) {

        String codigoUpper = codigoClase.toUpperCase().trim();
        String nombreEstudiante = authentication.getName();

        Clase clase = claseRepository.findByCodigoInscripcion(codigoUpper)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Long claseUnidaId = clase.getId();
        String mensajeExitoEstudiante = "¡Unido! Entrando a la clase en vivo del docente...";
        String encodedMensajeEstudiante = URLEncoder.encode(mensajeExitoEstudiante, StandardCharsets.UTF_8);

        String redirectUrl = String.format(
                "redirect:/estudiante/clase/%d?unidoExitosoEstudiante=true&mensaje=%s",
                claseUnidaId,
                encodedMensajeEstudiante
        );
        return redirectUrl;
    }

    @GetMapping("/estudiante/clase/{claseId}")
    public String mostrarPanelClaseEstudiante(
            @PathVariable Long claseId,
            Authentication authentication,
            @RequestParam(value = "unidoExitosoEstudiante", required = false) boolean unidoExitosoEstudiante,
            @RequestParam(value = "mensaje", required = false) String mensaje,
            Model model
    ) {
        Clase clase = claseRepository.findById(claseId)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada con ID: " + claseId));
        model.addAttribute("clase", clase);

        // Puedes mostrar datos del usuario actual si lo necesitas en la vista
        String email = authentication.getName();
        Usuario estudiante = usuarioRepository.findByEmail(email).orElse(null);
        model.addAttribute("estudiante", estudiante);

        if (unidoExitosoEstudiante) {
            model.addAttribute("notificacionEstudiante", mensaje);
        }
        return "clases-sesion-estudiante";
    }

    private String redirigirPorRol(String rol) {
        switch (rol) {
            case "ADMIN":
            case "ROLE_ADMIN":
                return "redirect:/admin";
            case "DOCENTE":
            case "ROLE_DOCENTE":
                return "redirect:/docente";
            case "ESTUDIANTE":
            case "ROLE_ESTUDIANTE":
                return "redirect:/estudiante";
            default:
                return "redirect:/";
        }
    }
}
