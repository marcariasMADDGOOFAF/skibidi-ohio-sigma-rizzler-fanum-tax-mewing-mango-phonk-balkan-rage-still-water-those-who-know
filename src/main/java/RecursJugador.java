
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "recurs_jugador", uniqueConstraints = @UniqueConstraint(columnNames = {"jugador_id", "tipus"}))
@NamedQuery(name = "RecursJugador.perJugador",
    query = "FROM RecursJugador r WHERE r.jugador.id = :idJugador ORDER BY r.tipus")
@Check(constraints = "quantitat >= 0")
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    public RecursJugador() {
    	
    }

    public RecursJugador(TipusRecurs tipus, int quantitat) {
        this.tipus = tipus;
        this.quantitat = quantitat;
    }

    public RecursJugador(TipusRecurs tipus, int quantitat, Jugador jugador) {
        this(tipus, quantitat);
        this.jugador = jugador;
    }

    public Jugador getJugador() {
        return jugador;
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
