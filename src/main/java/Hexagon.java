import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.Check;

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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;

@Entity
@Table(name = "hexagom", uniqueConstraints = @UniqueConstraint(columnNames = {"partida_id", "codi"}))
@Check(constraints = "numero IS NULL OR (numero BETWEEN 2 AND 12 AND numero <> 7)")
@NamedQuery(name = "Hexagon.taulerPerPartida", query =
    "SELECT DISTINCT h FROM Hexagon h LEFT JOIN FETCH h.hexagonVertices hv " +
    "LEFT JOIN FETCH hv.vertex v LEFT JOIN FETCH v.assentament a " +
    "LEFT JOIN FETCH a.propietari WHERE h.partida.id = :idPartida ORDER BY h.id")
@DynamicInsert
public class Hexagon {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
    private Integer id;

    @NotNull (message = "El codi és obligatori")
    @Column (name = "codi", nullable = false, length = 10)
    private String codi;

    @NotNull (message = "El terreny és obligatori")
	@Enumerated(EnumType.STRING)
	@Column(name = "terreny", nullable = false, length = 10)
    private TipusTerreny terreny;

    @Min(value = 2, message = "El número ha de ser com a mínim 2")
    @Max(value = 12, message = "El número ha de ser com a màxim 12")
    @Column (name = "numero", nullable = true)
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

    public Partida getPartida() {
        return partida;
    }

    public List<HexagonVertex> getHexagonVertices() {
        return hexagonVertices;
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
