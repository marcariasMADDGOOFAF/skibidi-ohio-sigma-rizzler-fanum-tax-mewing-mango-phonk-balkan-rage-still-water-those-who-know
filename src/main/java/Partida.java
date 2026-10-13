import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

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
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "data_inici", nullable = false)
	private Date dataInici = new Date();

	@Min(value = 1, message = "La ronda ha de ser com a mínim 1")
	@Column(name = "ronda", nullable = false)
	private int ronda = 1;

	@Min(0)
	@Max(12)
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

	public Partida() {
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