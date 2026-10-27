import java.util.List;

public interface IJugadorDao extends IGenericDao<Jugador,Integer> {
    String MostrarRecursos(int idJugador);
    String ConstruirCarretera(int idJugador, int idAresta);
    String ConstruirPoblat(int idJugador, int idVertex);
    String MillorarACiutat(int idJugador, int idVertex);
    String ComerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep);
    int CalcularPunts(int idJugador);
    String PassarTorn(int idJugador);
    String MoureLladre(int idJugador, int idHexagon, Integer idJugadorVictima);
    String ComprarCarta(int idJugador);
    String JugarCarta(int idJugador, int idCarta, TipusRecurs recurs);
    String ExercitMesGran(int idPartida);
    List<Vertex> VertexsDisponibles(int idJugador);
}
