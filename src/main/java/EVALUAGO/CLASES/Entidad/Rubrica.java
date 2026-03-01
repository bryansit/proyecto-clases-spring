package EVALUAGO.CLASES.Entidad;

import jakarta.persistence.*;

@Entity
public class Rubrica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String tipoEvaluacion;
    private String criterios;
    private int puntajeMaximo;
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
	public String getTipoEvaluacion() {
		return tipoEvaluacion;
	}
	public void setTipoEvaluacion(String tipoEvaluacion) {
		this.tipoEvaluacion = tipoEvaluacion;
	}
	public String getCriterios() {
		return criterios;
	}
	public void setCriterios(String criterios) {
		this.criterios = criterios;
	}
	public int getPuntajeMaximo() {
		return puntajeMaximo;
	}
	public void setPuntajeMaximo(int puntajeMaximo) {
		this.puntajeMaximo = puntajeMaximo;
	}

    // Constructor vacío y getters/setters
    // ...
    
    
}
