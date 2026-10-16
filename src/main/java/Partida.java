import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Partida")
@DynamicInsert
public class Partida {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id;

	@NotNull(message = "L'estat no pot ser null!")
	@Enumerated(EnumType.STRING)
	@Column(name = "estat", nullable = false, length = 15)
	private EstatPartida estat = EstatPartida.PREPARACIO;

	@NotNull(message = "La data d'inici no pot ser nula!")
	@Column(name = "data_inici", nullable = false)
	private Date dataInici = new Date();

	@Min(value = 1, message = "La ronda ha de ser com a minim 1")
	@Column(name = "ronda", nullable = false)
	private int ronda = 1;

	@Min(value = 2, message = "La tirada ha de ser minim 2")
	@Max(value =12, message = "La tirada ha de ser Maxim 12")
	@Column(name = "ultima_tirada", nullable = false)
	private int ultimaTirada;

	@Min(value = 1, message = "Els punts per guanyar han de ser positius")
	@Column(name = "punts_per_guanyar", nullable = false)
	private int puntsPerGuanyar = 10;

	@NotNull(message = "El camp daus_tirats no pot ser null")
	@Column(name = "daus_tirats", nullable = false)
	private boolean dausTirats;

	@NotNull(message = "El camp lladre_pendent no pot ser null")
	@Column(name = "lladre_pendent", nullable = false)
	private boolean lladrePendent;

	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<Jugador> jugadors = new ArrayList<>();
	
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "jugador_actiu_id")
	private Jugador actiu;
	
	
	@OneToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "jugador_guanyador_id")
	private Jugador guanyador;
	
	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<CartaDesenvolupament> pila = new ArrayList<>();

	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Hexagon> tauler = new ArrayList<>();

	@OneToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "hexagon_lladre_id")
	private Hexagon lladre;

	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Aresta> arestes = new ArrayList<>();

	@OneToMany(mappedBy = "partida", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vertex> vertexs = new ArrayList<>();
	
	
	public Partida() {
	}

	public List<CartaDesenvolupament> getPila() {
		return this.pila;
	}
	
	public Partida(int puntsPerGuanyar) {
		this.puntsPerGuanyar = puntsPerGuanyar;
	}

	public Integer getId() {
		return id;
	}

	public EstatPartida getEstat() {
		return estat;
	}

	public void setEstat(EstatPartida estat) {
		this.estat = estat;
	}

	public Date getDataInici() {
		return dataInici;
	}

	public void setDataInici(Date dataInici) {
		this.dataInici = dataInici;
	}

	public int getRonda() {
		return ronda;
	}

	public void setRonda(int ronda) {
		this.ronda = ronda;
	}

	public int getUltimaTirada() {
		return ultimaTirada;
	}

	public void setUltimaTirada(int ultimaTirada) {
		this.ultimaTirada = ultimaTirada;
	}

	public int getPuntsPerGuanyar() {
		return puntsPerGuanyar;
	}

	public void setPuntsPerGuanyar(int puntsPerGuanyar) {
		this.puntsPerGuanyar = puntsPerGuanyar;
	}

	public boolean isDausTirats() {
		return dausTirats;
	}

	public void setDausTirats(boolean dausTirats) {
		this.dausTirats = dausTirats;
	}

	public boolean isLladrePendent() {
		return lladrePendent;
	}

	public void setLladrePendent(boolean lladrePendent) {
		this.lladrePendent = lladrePendent;
	}

}