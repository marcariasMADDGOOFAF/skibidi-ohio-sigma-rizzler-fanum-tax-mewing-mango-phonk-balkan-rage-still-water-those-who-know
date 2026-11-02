import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

/**
 * Comprovació d'arrencada del model: connecta a MySQL, crea/actualitza l'esquema
 * i executa una consulta senzilla per confirmar que la connexió funciona.
 */
public class Main {

    private static SessionFactory createSessionFactory() {
        return new Configuration()
                .configure("herHibernate.cfg.xml")
                .buildSessionFactory();
    }

    public static void main(String[] args) {
        System.out.println("USER=" + System.getenv("MYSQL_USER")
                + " URL=" + System.getenv("MYSQL_URL")
                + " PASSLEN=" + (System.getenv("MYSQL_PASSWORD") == null ? "null" : System.getenv("MYSQL_PASSWORD").length()));
        try (SessionFactory factory = createSessionFactory(); Session session = factory.openSession()) {
            Object result = session.createNativeQuery("SELECT 1", Object.class).getSingleResult();
            System.out.println("OK: connexió a MySQL establerta i esquema Hibernate preparat.");
            System.out.println("Resultat de la comprovació: " + result);
            PartidaDao partida = new PartidaDao();

            partida.iniciarPartida(List.of("Marc"));
            int id = partida.iniciarPartida(List.of("Marc","Oscar","Ferran"));

            System.out.println(partida.mostrarPartida(id));

            Partida p = partida.get(id);
            partida.carregarDades();
            System.out.println(p.getTauler());


        } catch (Exception e) {
            System.err.println("ERROR: no s'ha pogut inicialitzar o comprovar la base de dades.");
        System.err.println("Revisa que MySQL estigui actiu i MYSQL_URL, MYSQL_USER i MYSQL_PASSWORD siguin correctes.");
            e.printStackTrace();
            System.exit(1);
        }



    }
}
