import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table (name = "hexagon_vertex")
public class HexagonVertex {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @NotNull (message = "La posició es obligatoria")
    @Min(value = 0, message = "La posició ha d'estar entre 0 i 5")
	@Max(value = 5, message = "La posició ha d'estar entre 0 i 5")
    @Column (name = "posicio", nullable = false)
    private Integer posicio;

    public HexagonVertex() {
    }

    public HexagonVertex(Integer posicio) {
        this.posicio = posicio;
    }

    public Integer getId() {
        return id;
    }

    public Integer getPosicio() {
        return posicio;
    }

    public void setPosicio(Integer posicio) {
        this.posicio = posicio;
    }
}
