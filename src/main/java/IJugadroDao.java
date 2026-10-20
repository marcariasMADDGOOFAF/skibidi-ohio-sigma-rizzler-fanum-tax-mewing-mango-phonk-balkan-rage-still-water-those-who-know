import java.util.List;

public interface IJugadroDao extends IGenericDao<Jugador,Integer> {
    void MostrarRecursos(int idJugador);
    void ConstruirCarretera(int idJugador, int idAresta);
    void ConstruirPoblat(int idJugador, int idVertex);
    void MillorarACiutat(int idJugador, int idVertex);
    void ComerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep);
    int CalcularPunts(int idJugador);
    void PassarTorn(int idJugador);
    void ComprovarVictoria(int idPartida);
    void MoureLladre(int idJugador, int idHexagon, Integer idJugadorVictima);
    void ComprarCarta(int idJugador);
    void JugarCarta(int idJugador, int idCarta, TipusRecurs recurs);
    void ExercitMesGran(int idPartida);
    List<Vertex> VertexsDisponibles(int idJugador);
}
