import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "hexagom")
@DynamicInsert
public class Hexagon {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @NotNull (message = "El codi és obligatori")
    @Column (name = "codi", nullable = false)
    private String codi;

    @NotNull (message = "El terreny és obligatori")
	@Enumerated(EnumType.STRING)
	@Column(name = "terreny", nullable = false, length = 10)
    private TipusTerreny terreny;

    @Column (name = "numero", length = 2, nullable = true)
    private Integer numero;

    @NotNull (message = "La coordenada q és obligatoria")
    @Column (name = "q", nullable = false)
    private int q;
    @NotNull (message = "La coordenada r és obligatoria")
    @Column (name = "r", nullable = false)
    private int r;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "partida_id", nullable = false)
    private Partida partida;

    @OneToMany(mappedBy = "hexagon", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HexagonVertex> hexagonVertices = new ArrayList<>();


    public Hexagon() {
    }

    public Hexagon(String codi) {
        this.codi = codi;
    }

    public Hexagon(
            String codi,
            int q,
            int r,
            Partida partida) {
        this.codi = codi;
        this.q = q;
        this.r = r;
        this.partida = partida;
    }

    public void afegirVertex(HexagonVertex hexagonVertex) {
        hexagonVertices.add(hexagonVertex);
    }

    public String getCodi() { return codi; }

    public Integer getId() {
        return id;
    }


    public void setCodi(String codi) {
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
