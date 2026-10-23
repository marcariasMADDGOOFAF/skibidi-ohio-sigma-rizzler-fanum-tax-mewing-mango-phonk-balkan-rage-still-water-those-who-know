import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vertex", uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "codi"}))
public class Vertex {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @OneToMany(mappedBy = "vertex", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HexagonVertex> hexagonVertices = new ArrayList<>();

    @OneToOne(mappedBy = "vertex", fetch = FetchType.LAZY, optional = true)
    private Assentament assentament;

    @NotNull (message = "El codi és obligatori")
    @Column (name = "codi", nullable = false)
    private String codi;

    @Column(name = "x", nullable = false)
    private double x;

    @Column(name = "y", nullable = false)
    private double y;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    public Vertex() {
    }

    public Vertex(String codi) {
        this.codi = codi;
    }

    public Vertex(String codi, double x, double y, Partida partida) {
        this.codi = codi;
        this.x = x;
        this.y = y;
        this.partida = partida;
    }

    public void afegirHexagon(HexagonVertex hexagonVertex) {
        hexagonVertices.add(hexagonVertex);
    }

    public int getId() {
        return id;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }
}
