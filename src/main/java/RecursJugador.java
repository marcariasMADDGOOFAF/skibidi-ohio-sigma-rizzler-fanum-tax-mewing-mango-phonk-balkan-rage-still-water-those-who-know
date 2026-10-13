
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "recurs_jugador")
public class RecursJugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipus", nullable = false, length = 10)
    private TipusRecurs tipus;

    @Min(value = 0, message = "La quantitat no pot ser negativa")
    @Column(name = "quantitat", nullable = false)
    private int quantitat;

    public RecursJugador() {
    	
    }

    public RecursJugador(TipusRecurs tipus, int quantitat) {
        this.tipus = tipus;
        this.quantitat = quantitat;
    }

    public Integer getId() { 
    	return id;
    	}
    public TipusRecurs getTipus() {
    	return tipus;
    	}
    public void setTipus(TipusRecurs tipus) { 
    	this.tipus = tipus; 
    	}
    public int getQuantitat() {
    	return quantitat; 
    	}
    public void setQuantitat(int quantitat) { 
    	this.quantitat = quantitat; 
    	}
}
