import java.util.List;

public interface IPartidaDao extends IGenericDao<Partida, Integer>{
    int iniciarPartida(List<String> nomsJugadors);
    String mostrarTaula(int idPartida);
    String mostrarPartida(int idPartida);
    String tirarDaus(int idPartida);
    String ComprovarVictoria(int idPartida);
}
