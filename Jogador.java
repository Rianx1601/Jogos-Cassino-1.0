import java.util.ArrayList;
import java.util.List;

public class Jogador {

    private int id;
    private String nome;
    private double saldo;
    private List<Perfiljogador> historico = new ArrayList<>();
    private Perfiljogador perfil;

    public Jogador(String nome, double saldoInicial) {
        this.nome = nome;
        this.saldo = saldoInicial;
        this.perfil = new Perfiljogador();
    }

    public Jogador(int id, String nome, double saldo) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;

        this.perfil = new Perfiljogador();
        this.perfil.setId(id); // 🔥 ESSENCIAL
    }
    public void atualizarRendimento(double valor) {
        perfil.setRendimento(perfil.getRendimento() + valor);
    }
    void setId(int id) {
        this.id = id;
    }
    void setNome(String nome) {
        this.nome = nome;
    }
    public Perfiljogador getPerfil() {
        return perfil;
    }

    public int getID() {
        return id;
    }

    public void setSaldo(double novoSaldo) {
        this.saldo = novoSaldo;
    }

    public void fazerAposta(double valor) {
        if (valor > 0 && valor <= saldo) {
            perfil.setApostaAtual(valor);
            saldo -= valor;
        }
    }

    public void registrarVitoria() {
        double lucro = perfil.getApostaAtual();
        saldo += perfil.getApostaAtual() * 2;

        perfil.registrarVitoria();
        atualizarRendimento(lucro);

        perfil.apostaAtual = 0;
    }

    public void registrarDerrota() {
        double prejuizo = -perfil.getApostaAtual();

        perfil.registrarDerrota();
        atualizarRendimento(prejuizo);

        perfil.apostaAtual = 0;
    }

    public void registrarEmpate() {
        saldo += perfil.getApostaAtual();

        perfil.registrarEmpate();
        // rendimento = 0, não altera

        perfil.apostaAtual = 0;
    }

    public void registrarBlackjack() {
        double lucro = perfil.getApostaAtual() * 0.5;

        saldo += perfil.getApostaAtual() * 1.5;
        perfil.registrarBlackjack();
        atualizarRendimento(lucro);

        perfil.apostaAtual = 0;
    }

    public void adicionarSaldo(double valor) {
        saldo += valor;
    }

    public void registrarPartida(Perfiljogador perfil) {
        historico.add(perfil);
    }

    public List<Perfiljogador> getHistorico() {
        return historico;
    }

    public String getNome() {
        return nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public String getEstatisticas() {
        return String.format(
                "Vitórias: %d | Derrotas: %d | Empates: %d",
                perfil.getVitorias(),
                perfil.getDerrotas(),
                perfil.getEmpates()
        );
    }

    @Override
    public String toString() {
        return nome;
    }
    public void setPerfil(Perfiljogador perfil) {
        this.perfil = perfil;
    }
    public int getId() {
        return id;
    }
}