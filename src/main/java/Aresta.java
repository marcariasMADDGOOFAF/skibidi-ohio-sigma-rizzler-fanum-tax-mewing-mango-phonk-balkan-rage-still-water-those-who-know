import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "aresta", uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "codi"}))
public class Aresta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @Column (name = "codi", nullable = false)
    private String codi;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vertex_a_id", nullable = false)
    private Vertex vertexA;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vertex_b_id", nullable = false)
    private Vertex vertexB;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @OneToOne(mappedBy = "aresta", fetch = FetchType.LAZY, optional = true)
    private Carretera carretera;


    public Aresta() {
    }

    public Aresta(String codi) {
        this.codi = codi;
    }

    public Aresta(String codi, Vertex vertexA, Vertex vertexB, Partida partida) {
        this.codi = codi;
        this.vertexA = vertexA;
        this.vertexB = vertexB;
        this.partida = partida;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodi() {
        return codi;
    }

    public void setCodi(String codi) {
        this.codi = codi;
    }
}
