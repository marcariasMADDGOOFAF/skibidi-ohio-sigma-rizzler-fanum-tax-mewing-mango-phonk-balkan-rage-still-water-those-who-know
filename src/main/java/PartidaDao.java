import java.util.List;

public class PartidaDao implements IPartidaDao{
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

    @Override
    public void saveOrUpdate(Partida entity) {

    }

    @Override
    public Partida get(Integer integer) {
        return null;
    }

    @Override
    public void delete(Integer integer) {

    }

    @Override
    public void delete(Partida entity) {

    }

    @Override
    public List<Partida> list() {
        return List.of();
    }
}
