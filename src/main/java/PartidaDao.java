import org.hibernate.Session;

import java.util.List;

public class PartidaDao extends GenericDao<Partida,Integer> implements IPartidaDao{
    @Override
    public int iniciarPartida(List<String> nomsJugadors) {
        return 0;
    }

    @Override
    public String mostrarTaula(int idPartida) {
        return "";
    }

    @Override
    public String mostrarPartida(int idPartida) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();

        }catch (RuntimeException e){
            handleException(session, e);
        }
        return "";
    }
    private String mostrarPartida(Session session, int id){
        Partida p = buscarPerId(session,Partida.class, id);
        if(p== null){
            return Missatges.PARTIDA_NO_EXISTEIX + ": "+id;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- ESTAT PRINCIPAL DE LA PARTIDA ---\n");
        sb.append("Estat: ").append(p.getEstat()).append("\n");
        sb.append("Ronda Actual: ").append(p.getRonda()).append("\n");
        sb.append("Jugador Actiu: ");
        return "";

    }

    @Override
    public String tirarDaus(int idPartida) {
        return "";
    }

    @Override
    public String ComprovarVictoria(int idPartida) {
        return "";
    }



    public <E> E buscarPerId(Session session, Class<E> classe, int id) {
        return session.createQuery("FROM " + classe.getName() + " e WHERE e.id = :id", classe)
                .setParameter("id", id)
                .uniqueResult();
    }
}
