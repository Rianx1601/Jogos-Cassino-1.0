import java.util.ArrayList;
import java.util.List;

public class Cassino{

    private String nome;
    private double saldo;
    private double apostaMaxima;
    private double apostaMinima;
    private List<Perfilcassino> historico = new ArrayList<>();

    public Cassino(String nome, double saldoInicial, double apostaMinima, double apostaMaxima) {
        super();
        this.nome = nome;
        this.saldo = saldoInicial;
        this.apostaMinima = apostaMinima;
        this.apostaMaxima = apostaMaxima;
    }

    boolean aceitarAposta(double valor, Jogador jogador) {

        return valor >= apostaMinima && valor <= apostaMaxima;
    }
    void setSaldo(double saldo) {
        this.saldo = saldo;
    }
    void processarVitoria(Jogador jogador) {
        saldo -= jogador.getPerfil().getApostaAtual();
    }

    void processarDerrota(Jogador jogador) {
        saldo += jogador.getPerfil().getApostaAtual();
    }

    void processarEmpate(Jogador jogador) {
        // cassino não ganha nem perde
    }

    void processarBlackjack(Jogador jogador) {
        saldo -= jogador.getPerfil().getApostaAtual() * 1.5;
    }
    public void registrarPartida(Perfilcassino perfil) {

        historico.add(perfil);
    }

    public List<Perfilcassino> getHistorico() {

        return historico;
    }
    public String getNome() {
        return nome;
    }
    public double getSaldo() {
        return saldo;
    }
    public double getApostaMaxima() {
        return apostaMaxima;
    }
    public double getApostaMinima() {
        return apostaMinima;
    }
    @Override
    public String toString() {
        return nome;
    }

}
