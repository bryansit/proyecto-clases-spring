package EVALUAGO.CLASES.Entidad;

import jakarta.persistence.*;

@Entity
public class Trabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipo;
    private String rubricaNombre;
    private String estado;
    private int nota;
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
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getRubricaNombre() {
		return rubricaNombre;
	}
	public void setRubricaNombre(String rubricaNombre) {
		this.rubricaNombre = rubricaNombre;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public int getNota() {
		return nota;
	}
	public void setNota(int nota) {
		this.nota = nota;
	}

    // Constructor y getters/setters
    // ...
    
    
}
