import org.hibernate.Session;

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


public class PartidaDao extends GenericDao<Partida,Integer> implements IPartidaDao{

    private static final String CARPETA_DADES = "/resources/data/";

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
        partida.setUltimaTirada(7);
        session.persist(partida);

        Map<String, Hexagon> hexagons = carregarHexagons(session, partida);
        Map<String, Vertex> vertexs = carregarVertexs(session, partida);
        carregarHexagonVertexs(session, hexagons, vertexs);
        carregarArestes(session, vertexs, partida);
        return 0;
    }

    private Map<String, Aresta> carregarArestes(Session session, Map<String, Vertex> vertexs, Partida partida) {
        Map<String, Aresta> arestes = new HashMap<>();
        for (String[] c : llegirCsv("arestes.csv")) {
			Vertex vertexA = obtenir(vertexs, c[1], "arestes.csv");
			Vertex vertexB = obtenir(vertexs, c[2], "arestes.csv");
			Aresta e = new Aresta(c[0], vertexA, vertexB, partida);
			session.persist(e);
			arestes.put(c[0], e);
		}
		return arestes;
    }

    private void carregarHexagonVertexs(Session session, Map<String, Hexagon> hexagons, Map<String, Vertex> vertexs) {
        for (String[] c : llegirCsv("hexagon_vertex.csv")) {
            Hexagon hexagon = obtenir(hexagons, c[0], "hexagon_vertex.csv");
            Vertex vertex = obtenir(vertexs, c[1], "hexagon_vertex.csv");
            int posicio = Integer.parseInt(c[2]);

            HexagonVertex hexagonVertex = new HexagonVertex(hexagon, vertex, posicio);
            session.persist(hexagonVertex);
        }
    }

    private Map<String, Hexagon> carregarHexagons(Session session, Partida partida) {
        Map<String, Hexagon> hexagons = new HashMap<>();
        for (String[] c : llegirCsv("hexagons.csv")) {
			Hexagon e = new Hexagon(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), partida);
			session.persist(e);
			hexagons.put(c[0], e);
		}
		return hexagons;
    }

    private Map<String, Vertex> carregarVertexs(Session session, Partida partida) {
        Map<String, Vertex> vertexs = new HashMap<>();
        for (String[] c : llegirCsv("vertexs.csv")) {
			Vertex e = new Vertex(c[0], Double.parseDouble(c[1]), Double.parseDouble(c[2]), partida);
			session.persist(e);
			vertexs.put(c[0], e);
		}
		return vertexs;
    }

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
