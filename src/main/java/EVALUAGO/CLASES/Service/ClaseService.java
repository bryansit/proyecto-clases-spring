package EVALUAGO.CLASES.Service;

import EVALUAGO.CLASES.Entidad.Clase;
import EVALUAGO.CLASES.Repositorio.ClaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ClaseService {
    @Autowired private ClaseRepository claseRepository;
    public Clase crearClase(Clase clase) { return claseRepository.save(clase); }
    public Optional<Clase> findByCodigoInscripcion(String codigo) { return claseRepository.findByCodigoInscripcion(codigo); }
    public Optional<Clase> findById(Long id) { return claseRepository.findById(id); }
    public List<Clase> findAll() { return claseRepository.findAll(); }
}
