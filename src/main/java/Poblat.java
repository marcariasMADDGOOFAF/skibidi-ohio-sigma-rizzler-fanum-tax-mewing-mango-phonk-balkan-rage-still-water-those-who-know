import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
@DiscriminatorValue ("P")
public class Poblat extends Assentament{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    public Poblat(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

}
