import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne (fetch = FetchType.EAGER, optional = false)
    @JoinColumn (name = "hexagon_id", nullable = false)
    private Hexagon hexagon;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn (name = "vertex_id", nullable = false)
    private Vertex vertex;


    @NotNull (message = "La posició es obligatoria")
    @Min(value = 0, message = "La posició ha d'estar entre 0 i 5")
	@Max(value = 5, message = "La posició ha d'estar entre 0 i 5")
    @Column (name = "posicio", nullable = false)
    private Integer posicio;

    public HexagonVertex() {
    }

    public HexagonVertex(Hexagon hexagon, Vertex vertex, Integer posicio) {
        this.hexagon = hexagon;
        this.vertex = vertex;
        this.posicio = posicio;
    }

    public Integer getId() {
        return id;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

    public Hexagon getHexagon() {
        return hexagon;
    }

    public void setHexagon(Hexagon hexagon) {
        this.hexagon = hexagon;
    }

    public Integer getPosicio() {
        return posicio;
    }

    public void setPosicio(Integer posicio) {
        this.posicio = posicio;
    }
}
