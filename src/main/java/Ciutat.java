import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
@DiscriminatorValue ("C")
public class Ciutat extends Assentament{
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

    public Ciutat(){

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    

}
