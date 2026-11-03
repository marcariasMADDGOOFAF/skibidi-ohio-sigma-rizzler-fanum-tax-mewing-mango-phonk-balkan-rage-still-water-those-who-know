import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner entrada = new Scanner(System.in)) {
            IPartidaDao partides = new PartidaDao();
            IJugadorDao jugadors = new JugadorDao();
            System.out.println("Connexió preparada.");
            boolean continuar = true;
            while (continuar) {
                mostrarMenu();
                try {
                    switch (llegirEnter(entrada, "Opció: ")) {
                        case 0: 
                            continuar = false;
                            break;
                        case 1:
                            System.out.print("Noms separats per comes (2-4 jugadors): ");
                            List<String> noms = new ArrayList<>();
                            String[] entradaTemp = entrada.nextLine().split(",");
                            for (int i = 0; i < entradaTemp.length; i++) {
                                noms.add(entradaTemp[i].trim());
                            }
                            int id = partides.iniciarPartida(noms);
                            if (id < 0) {
                                System.out.println("No s'ha pogut crear la partida. Revisa les dades i els errors de BD.");
                            } else {
                                System.out.println("Partida creada amb ID: " + id);
                                mostrarIds(id);
                            }
                            break;
                        case 2:
                            mostrarIds(llegirEnter(entrada, "ID partida: "));
                            break;
                        case 3:
                            resultat(partides.mostrarTauler(llegirEnter(entrada, "ID partida: ")));
                            break;
                        case 4:
                            resultat(partides.mostrarPartida(llegirEnter(entrada, "ID partida: ")));
                            break;
                        case 5:
                            resultat(jugadors.MostrarRecursos(llegirEnter(entrada, "ID jugador: ")));
                            break;
                        case 6:
                            resultat(jugadors.ConstruirPoblat(llegirEnter(entrada, "ID jugador: "), llegirEnter(entrada, "ID vèrtex: ")));
                            break;
                        case 7:
                            resultat(jugadors.ConstruirCarretera(llegirEnter(entrada, "ID jugador: "), llegirEnter(entrada, "ID aresta: ")));
                            break;
                        case 8:
                            resultat(jugadors.PassarTorn(llegirEnter(entrada, "ID jugador: ")));
                            break;
                        case 9:
                            resultat(partides.tirarDaus(llegirEnter(entrada, "ID partida: ")));
                            break;
                        case 10:
                            resultat(jugadors.MillorarACiutat(llegirEnter(entrada, "ID jugador: "), llegirEnter(entrada, "ID vèrtex: ")));
                            break;
                        case 11:
                            resultat(jugadors.ComerciarBanca(llegirEnter(entrada, "ID jugador: "),llegirRecurs(entrada, "Recurs que dones"), llegirRecurs(entrada, "Recurs que reps")));
                            break;
                        case 12:
                            int punts = jugadors.CalcularPunts(llegirEnter(entrada, "ID jugador: "));
                            System.out.println(punts < 0 ? "No s'han pogut calcular els punts." : "Punts: " + punts);
                            break;
                        case 13:
                            resultat(partides.ComprovarVictoria(llegirEnter(entrada, "ID partida: ")));
                            break;
                        case 14:
                            int jugador = llegirEnter(entrada, "ID jugador: ");
                            int hexagon = llegirEnter(entrada, "ID hexàgon de destí: ");
                            int victima = llegirEnter(entrada, "ID víctima (0 si no n'hi ha): ");
                            resultat(jugadors.MoureLladre(jugador, hexagon, victima == 0 ? null : victima));
                            break;
                        case 15:
                            resultat(jugadors.ComprarCarta(llegirEnter(entrada, "ID jugador: ")));
                            break;
                        case 16:
                            jugador = llegirEnter(entrada, "ID jugador: ");
                            int carta = llegirEnter(entrada, "ID carta: ");
                            System.out.print("És MONOPOLI? (s/n): ");
                            TipusRecurs recurs = entrada.nextLine().trim().equalsIgnoreCase("s") ? llegirRecurs(entrada, "Recurs del monopoli") : null;
                            resultat(jugadors.JugarCarta(jugador, carta, recurs));
                            break;
                        case 17:
                            resultat(jugadors.ExercitMesGran(llegirEnter(entrada, "ID partida: ")));
                            break;
                        case 18:
                            List<Vertex> vertexs = jugadors.VertexsDisponibles(llegirEnter(entrada, "ID jugador: "));
                            if (vertexs == null || vertexs.isEmpty()) {
                                System.out.println("Sense resultats. Comprova si el DAO està implementat.");
                            } else {
                                vertexs.forEach(v -> System.out.println("ID=" + v.getId() + " codi=" + v.getCodi()));
                            }
                            break;
                        default:
                            System.out.println("Opció inexistent.");
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Entrada incorrecta: " + e.getMessage());
                } catch (java.util.NoSuchElementException e) {
                    continuar = false;
                } catch (RuntimeException e) {
                    System.err.println("L'operació ha fallat: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        } catch (ExceptionInInitializerError e) {
            System.err.println("No s'ha pogut iniciar la consola.");
            e.printStackTrace();
        } finally {
            SessionManager.tancar();
        }
    }

    private static void mostrarMenu() {
        System.out.println("""
            === CATAN ===
            1. Iniciar partida
            2. Consultar IDs i topologia
            3. Mostrar tauler
            4. Mostrar partida
            5. Mostrar recursos
            6. Construir poblat
            7. Construir carretera
            8. Passar torn
            9. Tirar daus
            10. Millorar a ciutat
            11. Comerciar amb la banca
            12. Calcular punts
            13. Comprovar victòria
            14. Moure lladre
            15. Comprar carta
            16. Jugar carta
            17. Exèrcit més gran
            18. Vèrtexs disponibles
            0. Sortir
            """);
    }

    private static int llegirEnter(Scanner entrada, String missatge) {
        System.out.print(missatge);
        try {
            return Integer.parseInt(entrada.nextLine().trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("cal escriure un número enter.");
        }
    }

    private static TipusRecurs llegirRecurs(Scanner entrada, String missatge) {
        TipusRecurs[] tipus = TipusRecurs.values();
        System.out.println(missatge + ":");
        for (int i = 0; i < tipus.length; i++) {
            System.out.println((i + 1) + ". " + tipus[i]);
        }
        int opcio = llegirEnter(entrada, "Recurs: ");
        if (opcio < 1 || opcio > tipus.length) {
            throw new IllegalArgumentException("recurs inexistent.");
        }
        return tipus[opcio - 1];
    }

    private static void resultat(String text) {
        if(text == null || text.isBlank()){
            System.out.println("El DAO no ha retornat cap resultat.");
        }else{
            System.out.println(text);
        }
    }

    private static void mostrarIds(int idPartida) {
        try (Session session = SessionManager.getSessionFactory().openSession()) {
            session.beginTransaction();
            try {
                Partida partida = session.get(Partida.class, idPartida);
                if (partida == null) {
                    System.out.println("No existeix la partida: " + idPartida);
                } else {
                    System.out.println("Partida=" + partida.getId() + " estat=" + partida.getEstat() + " ronda=" + partida.getRonda() + " actiu=" + (partida.getActiu() == null ? "-" : partida.getActiu().getId()));
                    files(session, idPartida, "Jugadors: ID | nom | ordre | color", "SELECT j.id, j.nom, j.ordreTorn, j.color FROM Jugador j WHERE j.partida.id = :id ORDER BY j.ordreTorn");
                    files(session, idPartida, "Hexàgons: ID | codi | terreny | número", "SELECT h.id, h.codi, h.terreny, h.numero FROM Hexagon h WHERE h.partida.id = :id ORDER BY h.id");
                    files(session, idPartida, "Vèrtexs: ID | codi", "SELECT v.id, v.codi FROM Vertex v WHERE v.partida.id = :id ORDER BY v.id");
                    files(session, idPartida, "Arestes: ID | codi | ID vèrtex A | ID vèrtex B", "SELECT a.id, a.codi, a.vertexA.id, a.vertexB.id FROM Aresta a WHERE a.partida.id = :id ORDER BY a.id");
                    files(session, idPartida, "HexagonVertex: ID hexàgon | ID vèrtex | posició", "SELECT hv.hexagon.id, hv.vertex.id, hv.posicio FROM HexagonVertex hv WHERE hv.hexagon.partida.id = :id ORDER BY hv.hexagon.id, hv.posicio");
                    files(session, idPartida, "Cartes: ID | tipus | ID titular | usada | ronda compra", "SELECT c.id, c.tipus, j.id, c.usada, c.rondaCompra FROM CartaDesenvolupament c LEFT JOIN c.jugadorTitular j WHERE c.partida.id = :id ORDER BY c.id");
                }
                session.getTransaction().commit();
            } catch (RuntimeException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
                throw e;
            }
        }
    }

    private static void files(Session session, int idPartida, String titol, String consulta) {
        List<Object[]> files = session.createQuery(consulta, Object[].class).setParameter("id", idPartida).getResultList();
        System.out.println("\n" + titol + " (" + files.size() + " files)");
        files.forEach(fila -> System.out.println(Arrays.toString(fila)));
    }
}
