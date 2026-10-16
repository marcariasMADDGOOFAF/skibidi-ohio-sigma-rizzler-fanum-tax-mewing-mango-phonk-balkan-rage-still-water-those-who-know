import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "assentament")
public abstract class Assentament extends Construccio{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @OneToOne (mappedBy = "assentament", fetch = FetchType.LAZY, optional = false)
    private Vertex vertex;

    public Assentament(){

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

}