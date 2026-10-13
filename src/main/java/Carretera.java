import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table (name = "assentament")
public abstract class Carretera extends Construccio{

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)     // referència de tornada
    @JoinColumn(name = "aresta_id", nullable = false, unique = true)
    private Aresta aresta;

    public Carretera(){

    }

    public Integer getId() {
        return id;
    }
    
    public Aresta getaAresta() {
        return aresta;
    }

    public void setAresta(Aresta aresta) {
        this.aresta = aresta;
    }

    

}
