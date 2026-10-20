import java.util.List;

public interface IPartidaDao extends IGenericDao<Partida, Integer>{
    int iniciarPartida(List<String> nomsJugadors);
    void mostrarTaula(int idPartida);
    void mostrarPartida(int idPartida);
    String tirarDaus(int idPartida);
    void ComprovarVictoria(int idPartida);
}
