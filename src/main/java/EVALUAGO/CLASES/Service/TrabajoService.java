package EVALUAGO.CLASES.Service;

import EVALUAGO.CLASES.Entidad.Trabajo;
import EVALUAGO.CLASES.Repositorio.TrabajoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TrabajoService {
    @Autowired private TrabajoRepository trabajoRepository;
    public Trabajo crear(Trabajo trabajo) { return trabajoRepository.save(trabajo); }
    // Otros CRUD si necesitas
}
