Catan amb Hibernate

Fet per: Ferran Fernandez, Marc Arias i Oscar Arnabat.

Què és aquest projecte?

És una pràctica de Java sobre el joc Catan. El programa es fa servir des de la consola i guarda les dades a MySQL amb Hibernate. Podem crear partides, fer construccions i jugar els torns des del menú.

Què necessitem?

- JDK 21.
- Maven per carregar les dependències del projecte.
- MySQL instal·lat i en funcionament.
- Un IDE com Eclipse o IntelliJ IDEA.

Com es prepara?

1. Obrim el projecte a l'IDE com a projecte Maven.
2. Posem el JDK 21 a la configuració del projecte.
3. Esperem que Maven carregui les dependències del fitxer pom.xml.
4. Obrim src/main/resources/herHibernate.cfg.xml.
5. Canviem l'usuari i la contrasenya pels del nostre MySQL.
6. Revisem la connexió. Per defecte, utilitza localhost, el port 3306 i la base de dades catan.
7. Executem la classe Main.java.

La connexió està preparada per crear la base de dades si no existeix, sempre que l'usuari tingui permisos. Hibernate utilitza update per crear les taules que falten i actualitzar l'estructura. No cal crear les taules a mà.

Com es fa servir?

Quan executem el programa apareix un menú amb les opcions.

1. Triem l'opció 1 per crear una partida amb entre 2 i 4 jugadors. Escrivim els noms separats per comes.
2. El programa mostra l'ID de la partida i els ID dels elements del tauler. També els podem consultar amb l'opció 2.
3. Mirem quin jugador té el torn amb l'opció 4.
4. A la preparació, el jugador actiu col·loca un poblat amb l'opció 6 i una carretera amb l'opció 7. Després passa el torn amb l'opció 8.
5. La primera volta va en ordre i la segona va al revés. Cada jugador acaba amb dos poblats i dues carreteres.
6. Quan acaba la preparació, comencen els torns normals. El jugador tira els daus amb l'opció 9, fa les accions que pot pagar i passa el torn.
7. Si el lladre queda pendent, l'hem de moure amb l'opció 14 abans de continuar.
8. Per tancar el programa, triem l'opció 0.

Cal posar els ID que mostra el programa. El codi d'un vèrtex o d'una aresta no és el mateix que el seu ID.

Quines opcions té?

- Crear partides i consultar el tauler i els jugadors.
- Consultar els recursos i els punts de cada jugador.
- Construir poblats i carreteres i millorar poblats a ciutats.
- Tirar els daus i repartir recursos.
- Passar el torn.
- Canviar quatre recursos d'un tipus per un d'un altre amb la banca.
- Comprar i jugar cartes de desenvolupament.
- Moure el lladre i robar un recurs quan correspon.
- Consultar l'exèrcit més gran i comprovar si hi ha un guanyador.
- Consultar els vèrtexs on el jugador pot construir un poblat en aquell moment.

Com estan ordenats els fitxers?

- src/main/java: classes del joc, DAO i menú principal.
- PartidaDao.java: accions de les partides i càrrega del tauler.
- JugadorDao.java: accions dels jugadors.
- GenericDao.java: operacions bàsiques per guardar, consultar i eliminar dades.
- SessionManager.java: connexió de Hibernate.
- src/main/resources/herHibernate.cfg.xml: configuració de MySQL i Hibernate.
- src/main/resources/data: fitxers CSV amb els hexàgons, els vèrtexs, les arestes i les seves relacions.
- pom.xml: dependències i versió de Java.

Si no arrenca

Comprovem que MySQL està en funcionament, que l'usuari i la contrasenya són correctes, que el projecte utilitza el JDK 21 i que Maven ha carregat les dependències.
