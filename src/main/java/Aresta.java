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
import jakarta.persistence.Table;

@Entity
@Table (name = "aresta")
public class Aresta {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @Column (name = "codi", nullable = false, unique = true)
    private int codi;

    @OneToMany (mappedBy = "vertex1", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Vertex vertex1;

    @OneToMany (mappedBy = "vertex2", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Vertex vertex2;

    @ManyToOne (optional = false, fetch = FetchType.EAGER)
    @JoinColumn (name = "partida_id", nullable = false)
    private Partida partida;

    public Aresta() {
    }

    public Aresta(int codi) {
        this.codi = codi;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }
}
