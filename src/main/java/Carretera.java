import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity 
@Table(name = "carretera")
public class Carretera extends Construccio {


    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "aresta_id", nullable = false, unique = true)
    private Aresta aresta;

    public Carretera(){

    }

    
    public Aresta getAresta() {
        return aresta;
    }

    public void setAresta(Aresta aresta) {
        this.aresta = aresta;
    }

    

}
