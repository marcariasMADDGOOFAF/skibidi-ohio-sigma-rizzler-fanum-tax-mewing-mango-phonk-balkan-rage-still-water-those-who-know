
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
@Table(name = "carta_desenvolupament")
public class CartaDesenvolupament {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "tipus", nullable = false, length = 15)
	private TipusCarta tipus;

	@Column(name = "usada", nullable = false)
	private boolean usada;

	@Min(value = 1, message = "La ronda de compra ha de ser positiva")
	@Column(name = "ronda_compra")
	private Integer rondaCompra;

	public CartaDesenvolupament() {
	}

	public CartaDesenvolupament(TipusCarta tipus) {
		this.tipus = tipus;
	}

	public Integer getId() {
		return id;
	}

	public TipusCarta getTipus() {
		return tipus;
	}

	public void setTipus(TipusCarta tipus) {
		this.tipus = tipus;
	}

	public boolean isUsada() {
		return usada;
	}

	public void setUsada(boolean usada) {
		this.usada = usada;
	}

	public Integer getRondaCompra() {
		return rondaCompra;
	}

	public void setRondaCompra(Integer rondaCompra) {
		this.rondaCompra = rondaCompra;
	}
}
