package EVALUAGO.CLASES.Service;

import EVALUAGO.CLASES.Entidad.Rubrica;
import EVALUAGO.CLASES.Repositorio.RubricaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RubricaService {
    @Autowired private RubricaRepository rubricaRepository;
    public Rubrica crear(Rubrica rubrica) { return rubricaRepository.save(rubrica); }
    // Otros CRUD si necesitas
}
