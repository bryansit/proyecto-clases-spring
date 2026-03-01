package EVALUAGO.CLASES.Repositorio;

import EVALUAGO.CLASES.Entidad.Clase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ClaseRepository extends JpaRepository<Clase, Long> {
    Optional<Clase> findByCodigoInscripcion(String codigoInscripcion);
}
