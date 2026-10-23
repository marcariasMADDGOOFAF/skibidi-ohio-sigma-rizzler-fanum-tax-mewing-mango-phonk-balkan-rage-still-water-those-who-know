import org.hibernate.Session;

import java.util.List;

public class JugadorDao extends GenericDao<Jugador,Integer> implements IJugadorDao {



    @Override
    public String MostrarRecursos(int idJugador) {
        return "";
    }

    @Override
    public String ConstruirCarretera(int idJugador, int idAresta) {
        return "";
    }

    @Override
    public String ConstruirPoblat(int idJugador, int idVertex) {
        return "";
    }

    @Override
    public String MillorarACiutat(int idJugador, int idVertex) {
        return "";
    }

    @Override
    public String ComerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep) {
        return "";
    }

    @Override
    public int CalcularPunts(int idJugador) {
        return 0;
    }

    @Override
    public String PassarTorn(int idJugador) {
        return "";
    }

    private String PassarTorn(Session session, int id){
        Jugador j = buscarPerId(session,Jugador.class,id);
        if (j == null) {
            return Missatges.PARTIDA_NO_EXISTEIX + ": " + id;
        }
        return "";
    }

    @Override
    public String ComprovarVictoria(int idPartida) {
        return "";
    }

    @Override
    public String MoureLladre(int idJugador, int idHexagon, Integer idJugadorVictima) {
        return "";
    }

    @Override
    public String ComprarCarta(int idJugador) {
        return "";
    }

    @Override
    public String JugarCarta(int idJugador, int idCarta, TipusRecurs recurs) {
        return "";
    }

    @Override
    public String ExercitMesGran(int idPartida) {
        return "";
    }

    @Override
    public List<Vertex> VertexsDisponibles(int idJugador) {
        return List.of();
    }


    private <E> E buscarPerId(Session session, Class<E> classe, int id) {
        return session.createQuery("FROM " + classe.getName() + " e WHERE e.id = :id", classe)
                .setParameter("id", id)
                .uniqueResult();
    }

}
