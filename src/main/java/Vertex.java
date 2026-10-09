import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "vertex")
public class Vertex {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @NotNull (message = "El codi és obligatori")
    @Column (name = "codi", nullable = false, unique = true)
    private int codi;

    public Vertex() {
    }

    public Vertex(int codi) {
        this.codi = codi;
    }

    public Integer getId() {
        return id;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }
}
