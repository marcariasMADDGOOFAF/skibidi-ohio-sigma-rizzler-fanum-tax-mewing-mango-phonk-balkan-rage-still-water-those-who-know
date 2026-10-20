import java.util.List;

public class PartidaDao extends GenericDao<Partida,Integer> implements IPartidaDao{
    @Override
    public int iniciarPartida(List<String> nomsJugadors) {
        return 0;
    }

    @Override
    public void mostrarTaula(int idPartida) {

    }

    @Override
    public void mostrarPartida(int idPartida) {

    }

    @Override
    public String tirarDaus(int idPartida) {
        return "";
    }

    @Override
    public void ComprovarVictoria(int idPartida) {

    }

}
