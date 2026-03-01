package EVALUAGO.CLASES.Controlador;

import EVALUAGO.CLASES.Entidad.Mensaje;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Endpoint para mensajes de chat (real-time)
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload Mensaje mensaje, Authentication authentication) {
        // Log de depuración
        System.out.println("[CHAT][Recibido] contenido: " + mensaje.getContenido() +
                           ", claseId: " + mensaje.getClaseId() +
                           ", remitente: " + authentication.getName());

        // Completa los campos controlados por backend
        mensaje.setRemitente(authentication.getName());
        mensaje.setTimestamp(java.time.LocalDateTime.now().toString());

        boolean isDocente = authentication.getAuthorities().stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_DOCENTE"));
        mensaje.setRol(isDocente ? "DOCENTE" : "ESTUDIANTE");

        // Log de envío
        System.out.println("[CHAT][Enviando a canal] /topic/clase/" + mensaje.getClaseId() + " - " + mensaje.getRol());

        if (mensaje.getClaseId() != null) {
            messagingTemplate.convertAndSend("/topic/clase/" + mensaje.getClaseId(), mensaje);
        } else {
            System.out.println("[CHAT][WARN] Mensaje recibido sin claseId, no enviado.");
        }
    }
}
