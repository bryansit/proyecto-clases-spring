package EVALUAGO.CLASES.Entidad;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String dni;
    private String email;
    private String password;
    private String rol; // "DOCENTE", "ESTUDIANTE", "ADMIN"

    @OneToMany(mappedBy = "docente")
    private List<Clase> clases; // Clases donde es docente

    @ManyToMany(mappedBy = "estudiantes")
    private List<Clase> clasesInscritas; // Clases donde es estudiante

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDni() {
		return dni;
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public List<Clase> getClases() {
		return clases;
	}

	public void setClases(List<Clase> clases) {
		this.clases = clases;
	}

	public List<Clase> getClasesInscritas() {
		return clasesInscritas;
	}

	public void setClasesInscritas(List<Clase> clasesInscritas) {
		this.clasesInscritas = clasesInscritas;
	}   
    // Getters y setters
    // ...
}
