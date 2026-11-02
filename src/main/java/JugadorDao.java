
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class JugadorDao extends GenericDao<Jugador,Integer> implements IJugadorDao {


    @Override
    public String MostrarRecursos(int idJugador) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();
            String text = MostrarRecursos(session,idJugador);
            session.getTransaction().commit();
            return text;
        }catch (RuntimeException e){
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String MostrarRecursos(Session session, int id) {
        Jugador p = get(id);
        if (p == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + id;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- ESTAT DE L'INVENTARI DE ").append(p.getNom()).append(" ---\n");
        for (RecursJugador r : session.createNamedQuery("RecursJugador.perJugador", RecursJugador.class)
                .setParameter("idJugador", id).getResultList()) {
            sb.append("Recurs: ").append(r.getTipus()).append(", Quantitat: ").append(r.getQuantitat()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String ConstruirCarretera(int idJugador, int idAresta) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String resultat = ConstruirCarretera(session, idJugador, idAresta);
            session.getTransaction().commit();
            return resultat;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    public String ConstruirCarretera(Session session, int idJugador, int idAresta) {

        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }

        Partida p = j.getPartida();

        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.PREPARACIO && p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }

        Aresta ar = session.get(Aresta.class, idAresta);
        if (ar == null) {
            return Missatges.ARESTA_NO_EXISTEIX + ": " + idAresta;
        }
        if (!ar.getPartida().getId().equals(p.getId())) {
            return Missatges.ARESTA_ALTRA_PARTIDA;
        }
        if (ar.getCarretera() != null) {
            return Missatges.ARESTA_OCUPADA;
        }

        boolean preparacio = p.getEstat() == EstatPartida.PREPARACIO;

        if (!preparacio) {
            if (!p.isDausTirats()) {
                return Missatges.DAUS_NO_TIRATS;
            }
            if (p.isLladrePendent()) {
                return Missatges.LLADRE_PENDENT;
            }
        }

        int carreteres = 0;
        int poblats = 0;
        for (Construccio c : j.getConstruccions()) {
            if (c instanceof Carretera) {
                carreteres++;
            } else if (c instanceof Poblat) {
                poblats++;
            }
        }
        if (carreteres >= 15) {
            return Missatges.LIMIT_CARRETERES;
        }

        if (preparacio) {
            if (poblats == 0) {
                return Missatges.POBLAT_PREVI_PENDENT;
            }
            // ha de tocar un poblat propi que encara no tingui cap carretera propia adjacent
            boolean toca = false;
            for (Construccio c : j.getConstruccions()) {
                if (c instanceof Poblat poblat) {
                    int idVertex = poblat.getVertex().getId();
                    if (ar.getVertexA().getId() == idVertex || ar.getVertexB().getId() == idVertex) {
                        boolean teCarretera = false;
                        for (Construccio c2 : j.getConstruccions()) {
                            if (c2 instanceof Carretera car) {
                                if (car.getAresta().getVertexA().getId() == idVertex
                                        || car.getAresta().getVertexB().getId() == idVertex) {
                                    teCarretera = true;
                                }
                            }
                        }
                        if (!teCarretera) {
                            toca = true;
                        }
                    }
                }
            }
            if (!toca) {
                return Missatges.CARRETERA_NO_CONNECTADA;
            }
        } else {
            // ha de tocar una construccio o una carretera propia
            int idVertexA = ar.getVertexA().getId();
            int idVertexB = ar.getVertexB().getId();
            boolean connectada = false;
            for (Construccio c : j.getConstruccions()) {
                if (c instanceof Assentament a) {
                    int idVertex = a.getVertex().getId();
                    if (idVertex == idVertexA || idVertex == idVertexB) {
                        connectada = true;
                    }
                }
                if (c instanceof Carretera car) {
                    int idA = car.getAresta().getVertexA().getId();
                    int idB = car.getAresta().getVertexB().getId();
                    if (idA == idVertexA || idA == idVertexB || idB == idVertexA || idB == idVertexB) {
                        connectada = true;
                    }
                }
            }
            if (!connectada) {
                return Missatges.CARRETERA_NO_CONNECTADA;
            }

            RecursJugador fusta = null;
            RecursJugador mao = null;
            for (RecursJugador r : j.getInventari()) {
                if (r.getTipus() == TipusRecurs.FUSTA) {
                    fusta = r;
                } else if (r.getTipus() == TipusRecurs.MAO) {
                    mao = r;
                }
            }
            if (fusta == null || mao == null || fusta.getQuantitat() < 1 || mao.getQuantitat() < 1) {
                return Missatges.RECURSOS_INSUFICIENTS;
            }
            fusta.setQuantitat(fusta.getQuantitat() - 1);
            mao.setQuantitat(mao.getQuantitat() - 1);
        }

        Carretera carretera = new Carretera();
        carretera.setAresta(ar);
        carretera.setPropietari(j);
        session.persist(carretera);
        j.getConstruccions().add(carretera);

        return Missatges.CARRETERA_CONSTRUIDA;
    }

    @Override
    public String ConstruirPoblat(int idJugador, int idVertex) {

        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String resultat = ConstruirPoblat(idJugador, idVertex, session);
            session.getTransaction().commit();
            return resultat;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String ConstruirPoblat(int idJugador, int idVertex, Session session) {
        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }

        Partida p = j.getPartida();

        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.PREPARACIO && p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }

        Vertex v = session.get(Vertex.class, idVertex);
        if (v == null) {
            return Missatges.VERTEX_NO_EXISTEIX + ": " + idVertex;
        }
        if (!v.getPartida().getId().equals(p.getId())) {
            return Missatges.VERTEX_ALTRA_PARTIDA;
        }
        if (v.getAssentament() != null) {
            return Missatges.VERTEX_OCUPAT;
        }
        if (!compleixDistancia(v)) {
            return Missatges.DISTANCIA_NO_COMPLERTA;
        }

        boolean preparacio = p.getEstat() == EstatPartida.PREPARACIO;

        if (preparacio) {
            if (comptarPoblats(j) >= p.getRonda()) {
                return Missatges.POBLAT_JA_COLOCAT;
            }
        } else {
            if (!p.isDausTirats()) {
                return Missatges.DAUS_NO_TIRATS;
            }
            if (p.isLladrePendent()) {
                return Missatges.LLADRE_PENDENT;
            }
            if (!teCarreteraAdjacent(j, v)) {
                return Missatges.POBLAT_NO_CONNECTAT;
            }
        }

        if (comptarPoblats(j) >= 5) {
            return Missatges.LIMIT_POBLATS;
        }

        if (!preparacio) {
            if (!potPagarPoblat(j)) {
                return Missatges.RECURSOS_INSUFICIENTS_POBLAT;
            }
            pagarPoblat(j);
        }

        boolean segonPoblat = preparacio && comptarPoblats(j) == 1;

        Poblat poblat = new Poblat();
        poblat.setVertex(v);
        poblat.setPropietari(j);
        session.persist(poblat);
        j.getConstruccions().add(poblat);
        v.setAssentament(poblat);

        StringBuilder sb = new StringBuilder(Missatges.POBLAT_CONSTRUIT);
        if (segonPoblat) {
            sb.append("\n").append(repartirRecursosInicials(session, j, v));
        }
        return sb.toString();
    }

    @Override
    public String MillorarACiutat(int idJugador, int idVertex) {
        return "";
    }

    @Override
    public String ComerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            String text = executarComerciarBanca(idJugador, dona, rep);
            session.getTransaction().commit();
            return text;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String executarComerciarBanca(int idJugador, TipusRecurs dona, TipusRecurs rep) {
        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }
        Partida p = j.getPartida();
        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }
        if (!p.isDausTirats()) {
            return Missatges.DAUS_NO_TIRATS;
        }
        if (p.isLladrePendent()) {
            return Missatges.COMERCIAR_LLADRE_PENDENT;
        }
        if (dona == rep) {
            return Missatges.COMERCIAR_MATEIX_TYPE;
        }

        RecursJugador recursDonat = null;
        RecursJugador recursRebut = null;
        for (RecursJugador r : j.getInventari()) {
            if (r.getTipus() == dona) {
                recursDonat = r;
            } else if (r.getTipus() == rep) {
                recursRebut = r;
            }
        }
        if (recursDonat == null || recursDonat.getQuantitat() < 4) {
            return Missatges.COMERCIAR_NO_SUFICIENT_DONA;
        }
        if (recursRebut == null) {
            return Missatges.ERROR_BD;
        }

        recursDonat.setQuantitat(recursDonat.getQuantitat() - 4);
        recursRebut.setQuantitat(recursRebut.getQuantitat() + 1);
        return Missatges.COMERCIAR_FET + "4 " + dona + " = 1 " + rep;
    }

    @Override
    public int CalcularPunts(int idJugador) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            int res = CalcularPunts(session, idJugador);
            session.getTransaction().commit();
            return res;
        } catch (RuntimeException e) {
            handleException(session, e);
            System.out.println(Missatges.ERROR_BD);
            return -1;
        }
    }

    private int CalcularPunts(Session session, int idJugador) {
        Jugador j = get(idJugador);
        if (j == null) {
            System.out.println(Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador);
            return -1;
        }
        return calcularPunts(session, j, PartidaDao.exercitMesGran(j.getPartida()));
    }

    /** Punts de victòria: construccions + cartes de punt + exèrcit més gran. Única font de veritat. */
    public static int calcularPunts(Session session, Jugador j, Jugador exercitMesGran) {
        int punts = puntsConstruccions(session, j.getId());
        for (CartaDesenvolupament c : j.getMa()) {
            if (c.getTipus() == TipusCarta.PUNT_VICTORIA) {
                punts++;
            }
        }
        if (j.getId().equals(exercitMesGran == null ? null : exercitMesGran.getId())) {
            punts += 2;
        }
        return punts;
    }

    public static int puntsConstruccions(Session session, int idJugador) {
        long poblats = session
                .createQuery("select count(c) from Poblat c where c.propietari.id = :id", Long.class)
                .setParameter("id", idJugador)
                .uniqueResult();
        long ciutats = session
                .createQuery("select count(c) from Ciutat c where c.propietari.id = :id", Long.class)
                .setParameter("id", idJugador)
                .uniqueResult();
        return (int) (poblats + ciutats * 2);
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
            String victoria = PartidaDao.verificarVictoria(session, p);
            if (p.getEstat() == EstatPartida.FINALITZADA) {
                return victoria;
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
    public String MoureLladre(int idJugador, int idHexagon, Integer idJugadorVictima) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            String text = MoureLladre(session, idJugador, idHexagon, idJugadorVictima);
            session.getTransaction().commit();
            return text;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String MoureLladre(Session session, int idJugador, int idHexagon, Integer idJugadorVictima) {
        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }
        Partida p = j.getPartida();
        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }
        if (!p.isLladrePendent()) {
            return Missatges.LLADRE_NO_PENDENT;
        }

        Hexagon h = session.get(Hexagon.class, idHexagon);
        if (h == null) {
            return Missatges.HEXAGON_NO_EXISTEIX + ": " + idHexagon;
        }
        if (!h.getPartida().getId().equals(p.getId())) {
            return Missatges.HEXAGON_ALTRA_PARTIDA;
        }
        if (p.getLladre() != null && p.getLladre().getId().equals(h.getId())) {
            return Missatges.LLADRE_MATEIX_HEXAGON;
        }

        List<Jugador> candidats = session.createQuery(
                "select distinct a.propietari from Assentament a join a.vertex v join v.hexagonVertices hv " +
                "where hv.hexagon.id = :idHexagon and a.propietari.id <> :idJugador", Jugador.class)
                .setParameter("idHexagon", idHexagon)
                .setParameter("idJugador", idJugador)
                .getResultList();

        Jugador victima = null;
        if (idJugadorVictima != null) {
            for (Jugador candidat : candidats) {
                if (candidat.getId().equals(idJugadorVictima)) {
                    victima = candidat;
                }
            }
            if (victima == null) {
                return Missatges.VICTIMA_INVALIDA;
            }
        } else {
            for (Jugador candidat : candidats) {
                for (RecursJugador r : candidat.getInventari()) {
                    if (r.getQuantitat() > 0) {
                        return Missatges.VICTIMA_OBLIGATORIA;
                    }
                }
            }
        }

        String robatA = null;
        if (victima != null) {
            List<RecursJugador> unitats = new ArrayList<>();
            for (RecursJugador r : victima.getInventari()) {
                for (int i = 0; i < r.getQuantitat(); i++) {
                    unitats.add(r);
                }
            }
            if (!unitats.isEmpty()) {
                RecursJugador robat = unitats.get(new Random().nextInt(unitats.size()));
                robat.setQuantitat(robat.getQuantitat() - 1);
                RecursJugador meu = null;
                for (RecursJugador r : j.getInventari()) {
                    if (r.getTipus() == robat.getTipus()) {
                        meu = r;
                    }
                }
                meu.setQuantitat(meu.getQuantitat() + 1);
                robatA = victima.getNom();
            }
        }

        p.setLladre(h);
        p.setLladrePendent(false);
        return Missatges.LLADRE_MOGUT + h.getCodi()
                + (robatA == null ? Missatges.LLADRE_NO_ROBA : Missatges.LLADRE_ROBA + robatA);
    }

    @Override
    public String ComprarCarta(int idJugador) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            String text = ComprarCarta(session, idJugador);
            session.getTransaction().commit();
            return text;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String ComprarCarta(Session session, int idJugador) {
        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }
        Partida p = j.getPartida();
        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }
        if (!p.isDausTirats()) {
            return Missatges.DAUS_NO_TIRATS;
        }
        if (p.isLladrePendent()) {
            return Missatges.LLADRE_PENDENT_ACCIO;
        }

        RecursJugador blat = null;
        RecursJugador llana = null;
        RecursJugador mineral = null;
        for (RecursJugador r : j.getInventari()) {
            if (r.getTipus() == TipusRecurs.BLAT) {
                blat = r;
            } else if (r.getTipus() == TipusRecurs.LLANA) {
                llana = r;
            } else if (r.getTipus() == TipusRecurs.MINERAL) {
                mineral = r;
            }
        }
        if (blat.getQuantitat() < 1 || llana.getQuantitat() < 1 || mineral.getQuantitat() < 1) {
            return Missatges.CARTA_RECURSOS_INSUFICIENTS;
        }

        List<CartaDesenvolupament> pila = session.createQuery(
                "from CartaDesenvolupament c where c.partida.id = :idPartida and c.jugadorTitular is null",
                CartaDesenvolupament.class)
                .setParameter("idPartida", p.getId())
                .getResultList();
        if (pila.isEmpty()) {
            return Missatges.PILA_BUIDA;
        }

        blat.setQuantitat(blat.getQuantitat() - 1);
        llana.setQuantitat(llana.getQuantitat() - 1);
        mineral.setQuantitat(mineral.getQuantitat() - 1);

        CartaDesenvolupament carta = pila.get(new Random().nextInt(pila.size()));
        carta.setJugadorTitular(j);
        carta.setRondaCompra(p.getRonda());
        j.getMa().add(carta);

        String resultat = Missatges.CARTA_COMPRADA + carta.getTipus();
        String victoria = PartidaDao.verificarVictoria(session, p);
        return p.getEstat() == EstatPartida.FINALITZADA ? resultat + ". " + victoria : resultat;
    }

    @Override
    public String JugarCarta(int idJugador, int idCarta, TipusRecurs recurs) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            String text = JugarCarta(session, idJugador, idCarta, recurs);
            session.getTransaction().commit();
            return text;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String JugarCarta(Session session, int idJugador, int idCarta, TipusRecurs recurs) {
        Jugador j = get(idJugador);
        if (j == null) {
            return Missatges.JUGADOR_NO_EXISTEIX + ": " + idJugador;
        }
        Partida p = j.getPartida();
        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        if (p.getActiu() == null || !p.getActiu().getId().equals(j.getId())) {
            return Missatges.JUGADOR_NO_ACTIU;
        }
        if (!p.isDausTirats()) {
            return Missatges.DAUS_NO_TIRATS;
        }
        if (p.isLladrePendent()) {
            return Missatges.LLADRE_PENDENT_ACCIO;
        }

        CartaDesenvolupament carta = session.get(CartaDesenvolupament.class, idCarta);
        if (carta == null) {
            return Missatges.CARTA_NO_EXISTEIX + ": " + idCarta;
        }
        if (carta.getJugadorTitular() == null || !carta.getJugadorTitular().getId().equals(j.getId())) {
            return Missatges.CARTA_NO_ES_TEVA;
        }
        if (carta.isUsada()) {
            return Missatges.CARTA_USADA;
        }
        if (carta.getTipus() == TipusCarta.PUNT_VICTORIA) {
            return Missatges.CARTA_NO_JUGABLE;
        }
        if (carta.getRondaCompra() >= p.getRonda()) {
            return Missatges.CARTA_MATEIXA_RONDA;
        }

        String resultat;
        if (carta.getTipus() == TipusCarta.CAVALLER) {
            j.setCavallersJugats(j.getCavallersJugats() + 1);
            p.setLladrePendent(true);
            resultat = Missatges.CAVALLER_JUGAT;
        } else {
            if (recurs == null) {
                return Missatges.MONOPOLI_SENSE_RECURS;
            }
            int total = 0;
            for (Jugador altre : p.getJugadors()) {
                if (!altre.getId().equals(j.getId())) {
                    for (RecursJugador r : altre.getInventari()) {
                        if (r.getTipus() == recurs) {
                            total += r.getQuantitat();
                            r.setQuantitat(0);
                        }
                    }
                }
            }
            for (RecursJugador r : j.getInventari()) {
                if (r.getTipus() == recurs) {
                    r.setQuantitat(r.getQuantitat() + total);
                }
            }
            resultat = Missatges.MONOPOLI_FET + total + " " + recurs;
        }
        carta.setUsada(true);

        String victoria = PartidaDao.verificarVictoria(session, p);
        return p.getEstat() == EstatPartida.FINALITZADA ? resultat + ". " + victoria : resultat;
    }

    @Override
    public List<Vertex> VertexsDisponibles(int idJugador) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            List<Vertex> vertexs = VertexsDisponibles(session, idJugador);
            session.getTransaction().commit();
            return vertexs;
        } catch (RuntimeException e) {
            handleException(session, e);
            return List.of();
        }
    }

    private List<Vertex> VertexsDisponibles(Session session, int idJugador) {
        Jugador j = get(idJugador);
        if (j == null) {
            return List.of();
        }
        boolean preparacio = j.getPartida().getEstat() == EstatPartida.PREPARACIO;
        String jpql = "select v from Vertex v where v.partida.id = :idPartida and v.assentament is null "
                + "and not exists (select a from Aresta a where "
                + "(a.vertexA = v and a.vertexB.assentament is not null) or (a.vertexB = v and a.vertexA.assentament is not null)) "
                + (preparacio ? "" : "and exists (select c from Carretera c where c.propietari.id = :idJugador "
                + "and (c.aresta.vertexA = v or c.aresta.vertexB = v)) ")
                + "order by v.id";
        var query = session.createQuery(jpql, Vertex.class)
                .setParameter("idPartida", j.getPartida().getId());
        if (!preparacio) {
            query.setParameter("idJugador", idJugador);
        }
        return query.getResultList();
    }

    private long comptarPoblats(Jugador j) {
        return j.getConstruccions().stream()
                .filter(c -> c instanceof Poblat)
                .count();
    }

    private boolean teCarreteraAdjacent(Jugador j, Vertex v) {
        for (Construccio c : j.getConstruccions()) {
            if (c instanceof Carretera car) {
                Aresta a = car.getAresta();
                if (a.getVertexA().getId() == v.getId() || a.getVertexB().getId() == v.getId()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compleixDistancia(Vertex v) {
        for (Aresta a : v.getArestesA()) {
            if (a.getVertexB().getAssentament() != null) {
                return false;
            }
        }
        for (Aresta a : v.getArestesB()) {
            if (a.getVertexA().getAssentament() != null) {
                return false;
            }
        }
        return true;
    }

    private boolean potPagarPoblat(Jugador j) {
        return quantitatRecurs(j, TipusRecurs.FUSTA) >= 1
                && quantitatRecurs(j, TipusRecurs.MAO) >= 1
                && quantitatRecurs(j, TipusRecurs.LLANA) >= 1
                && quantitatRecurs(j, TipusRecurs.BLAT) >= 1;
    }

    private void pagarPoblat(Jugador j) {
        for (RecursJugador r : j.getInventari()) {
            if (r.getTipus() == TipusRecurs.FUSTA
                    || r.getTipus() == TipusRecurs.MAO
                    || r.getTipus() == TipusRecurs.LLANA
                    || r.getTipus() == TipusRecurs.BLAT) {
                r.setQuantitat(r.getQuantitat() - 1);
            }
        }
    }

    private TipusRecurs recursDeTerreny(TipusTerreny terreny) {
        return switch (terreny) {
            case BOSC -> TipusRecurs.FUSTA;
            case TURONS -> TipusRecurs.MAO;
            case PASTURES -> TipusRecurs.LLANA;
            case CAMPS -> TipusRecurs.BLAT;
            case MUNTANYES -> TipusRecurs.MINERAL;
            case DESERT -> null;
        };
    }

    private void afegirRecurs(Session session, Jugador j, TipusRecurs tipus, int quantitat) {
        for (RecursJugador r : j.getInventari()) {
            if (r.getTipus() == tipus) {
                r.setQuantitat(r.getQuantitat() + quantitat);
                return;
            }
        }
        RecursJugador nou = new RecursJugador(tipus, quantitat, j);
        j.getInventari().add(nou);
        session.persist(nou);
    }

    private String repartirRecursosInicials(Session session, Jugador j, Vertex v) {
        StringBuilder sb = new StringBuilder("Recursos inicials:");
        boolean cap = true;
        for (HexagonVertex hv : v.getHexagonVertices()) {
            TipusRecurs recurs = recursDeTerreny(hv.getHexagon().getTerreny());
            if (recurs == null) {
                continue;
            }
            afegirRecurs(session, j, recurs, 1);
            sb.append(" +1 ").append(recurs);
            cap = false;
        }
        if (cap) {
            sb.append(" cap");
        }
        return sb.toString();
    }

    private int quantitatRecurs(Jugador j, TipusRecurs tipus) {
        for (RecursJugador r : j.getInventari()) {
            if (r.getTipus() == tipus) {
                return r.getQuantitat();
            }
        }
        return 0;
    }

}
