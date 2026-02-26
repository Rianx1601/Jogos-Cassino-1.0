import java.util.ArrayList;
import java.util.List;
public class App {
    public static void main(String[] args) {

        List<Cassino> cassinos = new ArrayList<>();
        List<Jogador> jogadores = new ArrayList<>();

        new Jfreme(cassinos, jogadores);
    }
}