package EVALUAGO.CLASES.Entidad;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Clase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String nivel;
    private String codigoInscripcion;

    @ManyToOne
    @JoinColumn(name = "docente_id")
    private Usuario docente;

    @ManyToMany
    @JoinTable(
        name = "clase_estudiante",
        joinColumns = @JoinColumn(name = "clase_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> estudiantes;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNivel() {
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getCodigoInscripcion() {
		return codigoInscripcion;
	}

	public void setCodigoInscripcion(String codigoInscripcion) {
		this.codigoInscripcion = codigoInscripcion;
	}

	public Usuario getDocente() {
		return docente;
	}

	public void setDocente(Usuario docente) {
		this.docente = docente;
	}

	public List<Usuario> getEstudiantes() {
		return estudiantes;
	}

	public void setEstudiantes(List<Usuario> estudiantes) {
		this.estudiantes = estudiantes;
	}

    // Getters y setters
    // ...
    
    
}

