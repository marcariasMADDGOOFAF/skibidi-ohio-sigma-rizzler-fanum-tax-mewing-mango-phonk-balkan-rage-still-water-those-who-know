import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

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
        try (SessionFactory factory = createSessionFactory(); Session session = factory.openSession()) {
            Object result = session.createNativeQuery("SELECT 1", Object.class).getSingleResult();
            System.out.println("OK: connexió a MySQL establerta i esquema Hibernate preparat.");
            System.out.println("Resultat de la comprovació: " + result);
        } catch (Exception e) {
            System.err.println("ERROR: no s'ha pogut inicialitzar o comprovar la base de dades.");
        System.err.println("Revisa que MySQL estigui actiu i MYSQL_URL, MYSQL_USER i MYSQL_PASSWORD siguin correctes.");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
