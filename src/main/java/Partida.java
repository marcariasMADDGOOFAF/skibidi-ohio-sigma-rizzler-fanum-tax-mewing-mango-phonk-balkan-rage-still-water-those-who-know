import org.hibernate.annotations.DynamicInsert;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Date;



@Entity 
@Table(name="Partida")
@DynamicInsert
public class Partida {

    @Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;


    @NotNull(message = "L'estat no pot ser null!")
	@Enumerated(EnumType.STRING)
	@Column(name = "estat", nullable = false, length = 15)
	private EstatPartida estat;


    @NotNull(message = "La data d'inici no pot ser nula!")
    @Column(name = "data_inici", nullable = false)
    private Date dataInici;

    @NotNull(message = "La data d'inici no pot ser nula!")
    @Column(name = "ronda", nullable = false)
    private int ronda;

    
    @Column(name = "ultima_tirada")
    private int ultimaTirada;

    @NotNull(message = "La data d'inici no pot ser nula!")
    @Column(name = "punts_per_guanyar", nullable = false)
    private int puntsPerGuanyar;

    @NotNull(message = "El camp daus_tirats no pot ser null")
    @Column(name = "daus_tirats", nullable = false)
    private boolean dausTirats;
    
    @NotNull(message = "El camp lladre_pendent no pot ser null")
    @Column(name = "lladre_pendent", nullable = false)
    private boolean lladrePendent;

}