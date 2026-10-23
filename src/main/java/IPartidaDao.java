import java.util.List;

public interface IPartidaDao extends IGenericDao<Partida, Integer>{
    int carregarDades();
    int iniciarPartida(List<String> nomsJugadors);
    String mostrarTauler(int idPartida);
    String mostrarPartida(int idPartida);
    String tirarDaus(int idPartida);
    String ComprovarVictoria(int idPartida);
}
