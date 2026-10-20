import java.util.List;

public class JugadorDao extends GenericDao<Jugador,Integer> implements IJugadroDao{



    @Override
    public void MostrarRecursos(int idJugador) {

    }

    @Override
    public void ConstruirCarretera(int idJugador, int idAresta) {

    }

    @Override
    public void ConstruirPoblat(int idJugador, int idVertex) {

    }

    @Override
    public void MillorarACiutat(int idJugador, int idVertex) {

    }

    @Override
    public void ComerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep) {

    }

    @Override
    public int CalcularPunts(int idJugador) {
        return 0;
    }

    @Override
    public void PassarTorn(int idJugador) {

    }

    @Override
    public void ComprovarVictoria(int idPartida) {

    }

    @Override
    public void MoureLladre(int idJugador, int idHexagon, Integer idJugadorVictima) {

    }

    @Override
    public void ComprarCarta(int idJugador) {

    }

    @Override
    public void JugarCarta(int idJugador, int idCarta, TipusRecurs recurs) {

    }

    @Override
    public void ExercitMesGran(int idPartida) {

    }

    @Override
    public List<Vertex> VertexsDisponibles(int idJugador) {
        return List.of();
    }


}
