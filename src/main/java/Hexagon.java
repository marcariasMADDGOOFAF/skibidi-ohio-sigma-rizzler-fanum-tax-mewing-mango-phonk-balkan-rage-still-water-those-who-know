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

@Entity
@Table(name = "gexagon")
@DynamicInsert
public class Hexagon {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @NotNull (message = "El codi és obligatori")
    @Column (name = "codi", nullable = false, unique = true)
    private int codi;

    @NotNull (message = "El terreny és obligatori")
	@Enumerated(EnumType.STRING)
	@Column(name = "terreny", nullable = false, length = 10)
    private TipusTerreny terreny;

    @Column (name = "numero", length = 2, nullable = true)
    private Integer numero;

    @Column (name = "q", nullable = false)
    private int q;
    @Column (name = "r", nullable = false)
    private int r;


    public Hexagon() {
    }

    public Hexagon(int codi, TipusTerreny terreny, Integer numero, int q, int r) {
        this.codi = codi;
        this.terreny = terreny;
        this.numero = numero;
        this.q = q;
        this.r = r;
    }

    public Integer getId() {
        return id;
    }

    public int getCodi() {
        return codi;
    }

    public void setCodi(int codi) {
        this.codi = codi;
    }

    public TipusTerreny getTerreny() {
        return terreny;
    }

    public void setTerreny(TipusTerreny terreny) {
        this.terreny = terreny;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
