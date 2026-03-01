package EVALUAGO.CLASES.Controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class ClaseWebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // CORREGIDO: Mapa con Set para evitar duplicados y concurrencia
    private final Map<Long, Set<UsuarioSocket>> usuariosPorClase = new ConcurrentHashMap<>();

    public static class UsuarioSocket {
        public String nombre;
        public String rol;

        public UsuarioSocket(String nombre, String rol) {
            this.nombre = nombre;
            this.rol = rol;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof UsuarioSocket)) return false;
            UsuarioSocket that = (UsuarioSocket) o;
            return Objects.equals(nombre, that.nombre);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nombre);
        }
    }

    @MessageMapping("/joinClase")
    public void joinClase(@Payload Map<String, Object> payload, Authentication authentication) {
        Long claseId = Long.valueOf(payload.get("claseId").toString());
        String nombre = authentication != null ? authentication.getName() : "Invitado";
        String rol = "ESTUDIANTE";

        if (authentication != null) {
            rol = authentication.getAuthorities().stream()
                    .map(a -> a.getAuthority().replace("ROLE_", ""))
                    .filter(r -> r.equals("DOCENTE") || r.equals("ESTUDIANTE"))
                    .findFirst().orElse("ESTUDIANTE");
        }

        // Soporte para estudiantes sin login
        if (payload.containsKey("estudianteNombre") && payload.get("estudianteNombre") != null) {
            String n = payload.get("estudianteNombre").toString().trim();
            if (!n.isBlank()) nombre = n;
        }

        Set<UsuarioSocket> usuarios = usuariosPorClase.computeIfAbsent(claseId, k -> ConcurrentHashMap.newKeySet());
        UsuarioSocket nuevo = new UsuarioSocket(nombre, rol);

        boolean agregado = usuarios.add(nuevo);
        if (agregado && rol.equals("ESTUDIANTE")) {
            enviarNotificacion(claseId, "¡Estudiante " + nombre + " se ha unido!");
        }

        enviarListaUsuarios(claseId);
    }

    @MessageMapping("/leaveClase")
    public void leaveClase(@Payload Map<String, Object> payload, Authentication authentication) {
        Long claseId = Long.valueOf(payload.get("claseId").toString());
        String nombre = authentication != null ? authentication.getName() : "Invitado";

        if (payload.containsKey("estudianteNombre") && payload.get("estudianteNombre") != null) {
            String n = payload.get("estudianteNombre").toString().trim();
            if (!n.isBlank()) nombre = n;
        }

        Set<UsuarioSocket> usuarios = usuariosPorClase.get(claseId);
        if (usuarios != null) {
            usuarios.remove(new UsuarioSocket(nombre, ""));
            if (usuarios.isEmpty()) {
                usuariosPorClase.remove(claseId);
            } else {
                enviarListaUsuarios(claseId);
            }
        }
    }

    @MessageMapping("/signal")
    public void signal(@Payload Map<String, Object> msg) {
        Long claseId = Long.valueOf(msg.get("claseId").toString());
        String type = (String) msg.get("type");
        String to = (String) msg.get("to");

        if ("NEW_VIEWER".equals(type) || to == null) {
            messagingTemplate.convertAndSend("/topic/clase/" + claseId + "/signal", msg);
        } else {
            messagingTemplate.convertAndSendToUser(to, "/queue/signal", msg);
        }
    }

    @MessageMapping("/getViewers")
    public void getViewers(@Payload Map<String, Object> payload) {
        Long claseId = Long.valueOf(payload.get("claseId").toString());
        Set<UsuarioSocket> usuarios = usuariosPorClase.getOrDefault(claseId, Collections.emptySet());

        usuarios.stream()
                .filter(u -> !u.rol.equals("DOCENTE"))
                .forEach(u -> {
                    Map<String, Object> signal = new HashMap<>();
                    signal.put("type", "NEW_VIEWER");
                    signal.put("from", u.nombre);
                    signal.put("claseId", claseId);
                    messagingTemplate.convertAndSend("/topic/clase/" + claseId + "/signal", signal);
                });
    }

    // === UTILIDADES ===
    private void enviarListaUsuarios(Long claseId) {
        Set<UsuarioSocket> usuarios = usuariosPorClase.getOrDefault(claseId, Collections.emptySet());
        List<Map<String, String>> lista = new ArrayList<>();
        usuarios.forEach(u -> {
            Map<String, String> map = new HashMap<>();
            map.put("nombre", u.nombre);
            map.put("rol", u.rol);
            lista.add(map);
        });

        Map<String, Object> msg = new HashMap<>();
        msg.put("usuarios", lista);
        messagingTemplate.convertAndSend("/topic/clase/" + claseId, msg);
    }

    private void enviarNotificacion(Long claseId, String texto) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("mensaje", texto);
        messagingTemplate.convertAndSend("/topic/clase/" + claseId, msg);
    }
}