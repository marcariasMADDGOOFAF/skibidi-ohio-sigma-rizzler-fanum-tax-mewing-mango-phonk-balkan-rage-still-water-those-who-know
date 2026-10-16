import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "assentament")
public abstract class Assentament extends Construccio{

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vertex_id", nullable = false, unique = true)
    private Vertex vertex;


    public Assentament(){

    }

    public Vertex getVertex() {
        return vertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }

}