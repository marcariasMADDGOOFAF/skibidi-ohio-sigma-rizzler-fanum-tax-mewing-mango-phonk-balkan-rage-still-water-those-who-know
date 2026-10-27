import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Comparator;


public class PartidaDao extends GenericDao<Partida,Integer> implements IPartidaDao{

    private static final String CARPETA_DADES = "/data/";
    private static final String ARXIU_ARESTES = "arestes.csv";
    private static final String ARXIU_HEXAGON_VERTEX = "hexagon_vertex.csv";
    private static final String ARXIU_HEXAGONS = "hexagons.csv";
    private static final String ARXIU_VERTEXS = "vertexs.csv";

    @Override
    public int carregarDades() {
        Session session = sessionFactory.getCurrentSession();
		try {
			session.beginTransaction();
			int total = carregarDades(session);
			session.getTransaction().commit();
			return total;
		} catch (RuntimeException e) {
			handleException(session, e);
			return -1;
		}
    }

    private int carregarDades(Session session) {
        Partida partida = new Partida();
        session.persist(partida);
        carregarTopologia(session, partida);
        return 0;
    }

    private void carregarTopologia(Session session, Partida partida) {
        Map<String, Hexagon> hexagons = carregarHexagons(session, partida);
        Map<String, Vertex> vertexs = carregarVertexs(session, partida);
        carregarHexagonVertexs(session, hexagons, vertexs);
        carregarArestes(session, vertexs, partida);
    }

    private void assignarTerrenysINumeros(Map<String, Hexagon> hexagons) {
        List<TipusTerreny> terrenys = new ArrayList<>();
        afegirTerreny(terrenys, TipusTerreny.BOSC, 4);
        afegirTerreny(terrenys, TipusTerreny.TURONS, 3);
        afegirTerreny(terrenys, TipusTerreny.PASTURES, 4);
        afegirTerreny(terrenys, TipusTerreny.CAMPS, 4);
        afegirTerreny(terrenys, TipusTerreny.MUNTANYES, 3);
        terrenys.add(TipusTerreny.DESERT);

        List<Hexagon> ordreHexagons = new ArrayList<>(hexagons.values());
        if (ordreHexagons.size() != terrenys.size()) {
            throw new IllegalStateException(Missatges.COUNT_HEXAGONS);
        }
        Collections.shuffle(terrenys);
        for (int i = 0; i < ordreHexagons.size() - 1; i++) {
            for (int j = i + 1; j < ordreHexagons.size(); j++) {
                int codiActual = Integer.parseInt(ordreHexagons.get(i).getCodi());
                int codiSeguent = Integer.parseInt(ordreHexagons.get(j).getCodi());
                if (codiActual > codiSeguent) {
                    Hexagon temporal = ordreHexagons.get(i);
                    ordreHexagons.set(i, ordreHexagons.get(j));
                    ordreHexagons.set(j, temporal);
                }
            }
        }
        List<Integer> numeros = new ArrayList<>(List.of(2, 3, 3, 4, 4, 5, 5, 6, 6, 8, 8, 9, 9, 10, 10, 11, 11, 12));
        Collections.shuffle(numeros);

        int indexNumero = 0;
        for (int i = 0; i < ordreHexagons.size(); i++) {
            Hexagon hexagon = ordreHexagons.get(i);
            TipusTerreny terreny = terrenys.get(i);
            hexagon.setTerreny(terreny);
            if (terreny == TipusTerreny.DESERT) {
                hexagon.setNumero(null);
            } else {
                hexagon.setNumero(numeros.get(indexNumero++));
            }
        }
    }

    private void afegirTerreny(List<TipusTerreny> terrenys, TipusTerreny terreny, int quantitat) {
        for (int i = 0; i < quantitat; i++) terrenys.add(terreny);
    }

    private Map<String, Aresta> carregarArestes(Session session, Map<String, Vertex> vertexs, Partida partida) {
        Map<String, Aresta> arestes = new HashMap<>();
        for (String[] c : llegirCsv(ARXIU_ARESTES)) {
			Vertex vertexA = obtenir(vertexs, c[1], ARXIU_ARESTES);
			Vertex vertexB = obtenir(vertexs, c[2], ARXIU_ARESTES);
			Aresta e = new Aresta(c[0], vertexA, vertexB, partida);
			session.persist(e);
			partida.afegirAresta(e);
			arestes.put(c[0], e);
		}
		return arestes;
    }

    private void carregarHexagonVertexs(Session session, Map<String, Hexagon> hexagons, Map<String, Vertex> vertexs) {
        for (String[] c : llegirCsv(ARXIU_HEXAGON_VERTEX)) {
            Hexagon hexagon = obtenir(hexagons, c[0], ARXIU_HEXAGON_VERTEX);
            Vertex vertex = obtenir(vertexs, c[1], ARXIU_HEXAGON_VERTEX);
            int posicio = Integer.parseInt(c[2]);

            HexagonVertex hexagonVertex = new HexagonVertex(hexagon, vertex, posicio);
            session.persist(hexagonVertex);
            hexagon.afegirVertex(hexagonVertex);
            vertex.afegirHexagon(hexagonVertex);
        }
    }

    private Map<String, Hexagon> carregarHexagons(Session session, Partida partida) {
        Map<String, Hexagon> hexagons = new HashMap<>();
        for (String[] c : llegirCsv(ARXIU_HEXAGONS)) {
			Hexagon e = new Hexagon(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), partida);
			partida.afegirHexagon(e);
			hexagons.put(c[0], e);
		}

        assignarTerrenysINumeros(hexagons);
        for (Hexagon hexagon : hexagons.values()) {
            session.persist(hexagon);
        }
		return hexagons;
    }

    private Map<String, Vertex> carregarVertexs(Session session, Partida partida) {
        Map<String, Vertex> vertexs = new HashMap<>();
        for (String[] c : llegirCsv(ARXIU_VERTEXS)) {
			Vertex e = new Vertex(c[0], Double.parseDouble(c[1]), Double.parseDouble(c[2]), partida);
			session.persist(e);
			partida.afegirVertex(e);
			vertexs.put(c[0], e);
		}
		return vertexs;
    }

    @Override
    public int iniciarPartida(List<String> nomsJugadors) {
        if(nomsJugadors == null || nomsJugadors.size() < 2 || nomsJugadors.size() > 4){
            return -1;
        }
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        List<String> jugadorsMixed = new ArrayList<>(nomsJugadors);
        java.util.Collections.shuffle(jugadorsMixed);
        int ordre = 1;
        ColorJugador[] colors = ColorJugador.values();
        try {
            session.beginTransaction();
            Partida partida = new Partida();
            session.persist(partida);
            carregarTopologia(session, partida);
            for(String nom: jugadorsMixed){
                Jugador jugador = new Jugador(nom, colors[ordre - 1], ordre, partida);
                for (TipusRecurs tipus : TipusRecurs.values()) {
                    jugador.getInventari().add(new RecursJugador(tipus, 0, jugador));
                }
                session.persist(jugador);
                partida.afegirJugador(jugador);
                if (ordre == 1) {
                    partida.setActiu(jugador);
                }
                ordre++;
            }
            Hexagon desert = partida.getTauler().stream()
                    .filter(h -> h.getTerreny() == TipusTerreny.DESERT)
                    .findFirst().orElseThrow(() -> new IllegalStateException("No s'ha generat el desert"));
            partida.setLladre(desert);
            session.getTransaction().commit();
            return partida.getId();
        } catch (RuntimeException e) {
            handleException(session, e);
            return -1;
        }
    }

    @Override
    public String mostrarTauler(int idPartida) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();
            String text = mostrarTauler(session,idPartida);
            session.getTransaction().commit();
            return text;
        }catch (RuntimeException e){
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    private String mostrarTauler(Session session, int id) {
        Partida p = get(id);
        if (p == null) {
            return Missatges.PARTIDA_NO_EXISTEIX + ": " + id;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- ESTAT DEL TAULER DE LA PARTIDA ---\n");
        List<Hexagon> hexagons = session.createNamedQuery("Hexagon.taulerPerPartida", Hexagon.class)
                .setParameter("idPartida", id).getResultList();
        for (Hexagon h : hexagons) {
            sb.append("Hexagon: ").append(h.getCodi()).append(", Terreny: ").append(h.getTerreny())
                    .append(", Numero: ").append(h.getNumero());
            if (p.getLladre() != null && p.getLladre().getId().equals(h.getId())) {
                sb.append(", Lladre: SI");
            }
            sb.append("\n");
            List<HexagonVertex> vertexs = new ArrayList<>(h.getHexagonVertices());
            vertexs.sort(Comparator.comparingInt(HexagonVertex::getPosicio));
            for (HexagonVertex hv : vertexs) {
                Assentament assentament = hv.getVertex().getAssentament();
                if (assentament != null) {
                    sb.append("  Vertex: ").append(hv.getVertex().getCodi())
                            .append(", Posicio: ").append(hv.getPosicio())
                            .append(", Construccio: ").append(assentament instanceof Ciutat ? "Ciutat" : "Poblat")
                            .append(", Propietari: ").append(assentament.getPropietari().getNom()).append("\n");
                }
            }
        }
        return sb.toString();
    }

    @Override
    public String mostrarPartida(int idPartida) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try{
            session.beginTransaction();
            String text = mostrarPartida(session,idPartida);
            session.getTransaction().commit();
            return text;
        }catch (RuntimeException e){
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }
    
    private String mostrarPartida(Session session, int id) {
        Partida p = get(id);
        if (p == null) {
            return Missatges.PARTIDA_NO_EXISTEIX + ": " + id;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("--- ESTAT PRINCIPAL DE LA PARTIDA ---\n");
        sb.append("Estat: ").append(p.getEstat()).append("\n");
        sb.append("Ronda Actual: ").append(p.getRonda()).append("\n");
        sb.append("Jugador Actiu: ");
        if(p.getActiu() != null){
            sb.append(p.getActiu().getNom());
        }else{
            sb.append("-");
        }
        sb.append("\n");

        sb.append("Ultima Tirada: ").append(p.getUltimaTirada() == 0 ? "-" : String.valueOf(p.getUltimaTirada())).append("\n");
        sb.append("Daus tirats: ").append(p.isDausTirats()).append("\n");
        sb.append("Lladre pendent: ").append(p.isLladrePendent()).append("\n");

        // Resum Jugadors
        sb.append("--- ESTAT DE TOTS ELS JUGADORS ---\n");
        Jugador exercitGran = exercitMesGran(p);
        for (Jugador j : p.getJugadors()) {
            int punts = JugadorDao.calcularPunts(session, j, exercitGran);
            int poblats = 0, ciutats = 0, carreteres = 0;
            for (Construccio c : j.getConstruccions()) {
                if (c instanceof Poblat) {
                    poblats++;
                } else if (c instanceof Ciutat) {
                    ciutats++;
                } else if (c instanceof Carretera) {
                    carreteres++;
                }
            }

            int totalRecursos = 0;
            for (RecursJugador r : j.getInventari()) {
                totalRecursos += r.getQuantitat();
            }

            sb.append("Jugador: ").append(j.getNom()).append("\n");
            sb.append("Poblats: ").append(poblats).append("\n");
            sb.append("Ciutats: ").append(ciutats).append("\n");
            sb.append("Carreteres: ").append(carreteres).append("\n");
            sb.append("Punts de victoria totals: ").append(punts).append("\n");
            sb.append("Nombre de recursos: ").append(totalRecursos).append("\n\n");
        }

        return sb.toString();
    }

     @Override
    public String tirarDaus(int idPartida) {
        Session session = SessionManager.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            String daus = tirarDaus(session, idPartida);
            session.getTransaction().commit();
            return daus;
        } catch (Exception e) {
            // TODO: handle exception
        }
        return "";
    }

    private String tirarDaus(Session session, int idPartida){

        Partida p = get(idPartida);

        boolean tirats = session.createQuery("SELECT DISTINCT p.daus_tirats FROM Partida p WHERE p.id = :id", Boolean.class).setParameter("id", idPartida).uniqueResult();
        if (!tirats) {
            int dau1 = (int) Math.floor(Math.random()*6);
            int dau2 = (int) Math.floor(Math.random()*6);
            int numero = dau1 + dau2;

            p.setUltimaTirada(numero);
            p.setDausTirats(true);

            saveOrUpdate(p);
            

            List<Hexagon> hexs = session.createQuery("SELECT DISTINCT h FROM Hexagons h WHERE numero = :numero AND numero != :lladre", Hexagon.class).setParameter("numero", numero).setParameter("lladre", p.getLladre()).getResultList();

            return Missatges.DAUS_TIRATS;
        }
        return Missatges.DAUS_NO_TIRATS;
    }

    @Override
    public String ComprovarVictoria(int idPartida) {
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            String text = ComprovarVictoria(session, idPartida);
            session.getTransaction().commit();
            return text;
        } catch (RuntimeException e) {
            handleException(session, e);
            return Missatges.ERROR_BD;
        }
    }

    public static Jugador exercitMesGran(Partida p) {
        Jugador exercitMesGran = null;
        int maxCavallers = 2;
        for (Jugador j1 : p.getJugadors()) {
            if (j1.getCavallersJugats() > maxCavallers) {
                maxCavallers = j1.getCavallersJugats();
                exercitMesGran = j1;
            } else if (j1.getCavallersJugats() == maxCavallers) {
                exercitMesGran = null;
            }
        }
        return exercitMesGran;
    }

    private String ComprovarVictoria(Session session, int idPartida) {
        Partida p = get(idPartida);
        if (p == null) {
            return Missatges.PARTIDA_NO_EXISTEIX + ": " + idPartida;
        }
        if (p.getEstat() == EstatPartida.FINALITZADA) {
            return Missatges.PARTIDA_FINALITZADA;
        }
        if (p.getEstat() != EstatPartida.EN_CURS) {
            return Missatges.PARTIDA_NO_EN_CURS;
        }
        return verificarVictoria(session, p);
    }
    public static String verificarVictoria(Session session, Partida p) {
        Jugador exercitMesGran = exercitMesGran(p);

        Jugador guanyador = null;
        int milloresPunts = -1;
        for (Jugador j : p.getJugadors()) {
            int punts = JugadorDao.calcularPunts(session, j, exercitMesGran);
            if (punts < p.getPuntsPerGuanyar()) {
                continue;
            }
            boolean actiu = p.getActiu() != null && p.getActiu().getId().equals(j.getId());
            boolean guanyadorActiu = guanyador != null && p.getActiu() != null
                    && p.getActiu().getId().equals(guanyador.getId());
            boolean millor;
            if (guanyador == null) {
                millor = true;
            } else if (actiu != guanyadorActiu) {
                millor = actiu;
            } else if (punts != milloresPunts) {
                millor = punts > milloresPunts;
            } else {
                millor = j.getOrdreTorn() < guanyador.getOrdreTorn();
            }
            if (millor) {
                guanyador = j;
                milloresPunts = punts;
            }
        }

        if (guanyador == null) {
            return Missatges.VICTORIA_PENDENT;
        }
        p.setGuanyador(guanyador);
        p.setEstat(EstatPartida.FINALITZADA);
        return Missatges.VICTORIA + guanyador.getNom();
    }

    


    private List<String[]> llegirCsv(String fitxer) {
		InputStream is = PartidaDao.class.getResourceAsStream(CARPETA_DADES + fitxer);
		if (is == null) {
			throw new IllegalStateException("No es troba el fitxer " + CARPETA_DADES + fitxer);
		}
		List<String[]> files = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
			br.readLine();
			String linia;
			while ((linia = br.readLine()) != null) {
				if (linia.isBlank()) {
					continue;
				}
				String[] camps = linia.split(",", -1);
				for (int i = 0; i < camps.length; i++) {
					camps[i] = camps[i].trim();
				}
				files.add(camps);
			}
		} catch (IOException e) {
			throw new UncheckedIOException("Error llegint " + fitxer, e);
		}
		return files;
	}

	private <E> E obtenir(Map<String, E> mapa, String clau, String fitxer) {
		E e = mapa.get(clau);
		if (e == null) {
			throw new IllegalStateException(fitxer + ": no existeix '" + clau + "'");
		}
		return e;
	}
}
