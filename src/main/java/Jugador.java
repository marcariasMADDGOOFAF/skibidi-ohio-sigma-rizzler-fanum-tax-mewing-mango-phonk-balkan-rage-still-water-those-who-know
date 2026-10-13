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
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "Jugador")
public class Jugador {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotBlank(message = "El nom es obligatori!")
	@Size(max = 100)
	@Column(name = "nom", nullable = false, length = 100)
	private String nom;

	@NotNull(message = "El jugador ha de tenir un color!")
	@Enumerated(EnumType.STRING)
	@Column(name = "color_jugador", nullable = false, length = 15)
	private ColorJugador color;

	@Min(1)
	@Max(4)
	@Column(name = "ordre_torn", nullable = false)
	private int ordreTorn;

	@Min(value = 0, message = "Els cavallers jugats no poden ser negatius!")
	@Column(name = "cavallers_jugats", nullable = false)
	private int cavallersJugats;

	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "partida_id", nullable = false)
	private Partida partida;
	
	public Jugador() {
	}

	public Jugador(String nom, ColorJugador color, int ordreTorn) {
		this.nom = nom;
		this.color = color;
		this.ordreTorn = ordreTorn;
	}

	public Integer getId() {
		return id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public ColorJugador getColor() {
		return color;
	}

	public void setColor(ColorJugador color) {
		this.color = color;
	}

	public int getOrdreTorn() {
		return ordreTorn;
	}

	public void setOrdreTorn(int ordreTorn) {
		this.ordreTorn = ordreTorn;
	}

	public int getCavallersJugats() {
		return cavallersJugats;
	}

	public void setCavallersJugats(int cavallersJugats) {
		this.cavallersJugats = cavallersJugats;
	}

}
