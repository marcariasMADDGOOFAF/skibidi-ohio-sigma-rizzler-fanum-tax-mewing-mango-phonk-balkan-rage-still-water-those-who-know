import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name="Partida")
@DynamicInsert
public class Jugador {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;


    @NotNull(message = "El nom no pot ser null!")
    @Column(name = "nom",nullable = false)
    private String nom;

    @NotNull(message = "El jugador ha de tenir un color!")
    @Enumerated(EnumType.STRING)
    @Column(name = "color_jugador", nullable = false, length = 15)
    private ColorJugador color;

    @NotNull(message = "El jugador si o si ha de tenir un ordre de torn!")
    @Column(name = "ordre_torn", nullable = false)
    private int ordreTorn;

    @NotNull(message = "El jugador si o si ha de tenir un ordre de torn!")
    @Column(name = "ordre_torn", nullable = false)
    private int cavallersJugats;


}
