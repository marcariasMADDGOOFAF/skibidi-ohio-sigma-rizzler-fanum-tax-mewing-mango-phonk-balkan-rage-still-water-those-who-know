import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Comparator;
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

    private String ComerciarBanca(Session session, int idJugador, TipusRecurs dona, TipusRecurs rep) {
        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }
        return "";
    }

    @Override
    public int CalcularPunts(int idJugador) {
        return 0;
    }

    @Override
    public String PassarTorn(int idJugador) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();
            String text = PassarTorn(session,idJugador);
            session.getTransaction().commit();
            return text;
        }catch (RuntimeException e){
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String PassarTorn(Session session, int id) {
        Jugador j = get(id);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + id;
        }
        Partida p = j.getPartida();

        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }
        if (p.isLladrePendent()) {
            return Missatges.LLADRE_PENDENT;
        }

        List<Jugador> jugadors = new ArrayList<>(p.getJugadors());
        jugadors.sort(Comparator.comparingInt(Jugador::getOrdreTorn));
        int n = jugadors.size();
        int pos = jugadors.indexOf(j);

        if (p.getEstat() == EstatPartida.EN_CURS) {
            if (!p.isDausTirats()) {
                return Missatges.DAUS_NO_TIRATS;
            }
            if (pos == n - 1) {
                p.setRonda(p.getRonda() + 1);
                p.setActiu(jugadors.get(0));
            } else {
                p.setActiu(jugadors.get(pos + 1));
            }
            p.setDausTirats(false);
            return Missatges.TORN_PASSAT + p.getActiu().getNom();
        }

        int ronda = p.getRonda();
        int poblats = 0, carreteres = 0;
        for (Construccio c : j.getConstruccions()) {
            if (c instanceof Poblat) {
                poblats++;
            } else if (c instanceof Carretera) {
                carreteres++;
            }
        }
        if (poblats < ronda || carreteres < ronda) {
            return Missatges.PREPARACIO_INCOMPLETA;
        }

        boolean tots = true;
        for (Jugador altre : jugadors) {
            int poblatsAltre = 0, carreteresAltre = 0;
            for (Construccio c : altre.getConstruccions()) {
                if (c instanceof Poblat) {
                    poblatsAltre++;
                } else if (c instanceof Carretera) {
                    carreteresAltre++;
                }
            }
            if (poblatsAltre < 2 || carreteresAltre < 2) {
                tots = false;
            }
        }
        if (tots) {
            p.setEstat(EstatPartida.EN_CURS);
            p.setRonda(1);
            p.setActiu(jugadors.get(0));
            p.setDausTirats(false);
            return Missatges.PREPARACIO_ACABADA + p.getActiu().getNom();
        }

        if (ronda == 1) {
            if (pos < n - 1) {
                p.setActiu(jugadors.get(pos + 1));
            } else {
                p.setRonda(2);
            }
        } else if (pos > 0) {
            p.setActiu(jugadors.get(pos - 1));
        }
        p.setDausTirats(false);
        return Missatges.TORN_PASSAT + p.getActiu().getNom();
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




}
