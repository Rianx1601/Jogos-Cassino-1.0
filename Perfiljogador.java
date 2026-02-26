public class Perfiljogador {

    private int id;
    private int jogadorId;
    protected double apostaAtual;
    protected int vitorias;
    protected int derrotas;
    protected int empates;
    protected double rendimento;
    protected int vBlackjack;

    public Perfiljogador() {
        this.id = 0;
        this.rendimento = 0;
        this.apostaAtual = 0;
        this.vitorias = 0;
        this.derrotas = 0;
        this.empates = 0;
        this.vBlackjack = 0;
    }
    public int getJogadorId() {
        return jogadorId;
    }

    public void setJogadorId(int jogadorId) {
        this.jogadorId = jogadorId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public  int getId() {return id;}
    public double getApostaAtual() { return apostaAtual; }
    public int getVitorias() { return vitorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
    public double getRendimento() { return rendimento; }
    public int getvBlackjack() { return vBlackjack; }
    public void registrarVitoria() { vitorias++; }
    public void registrarDerrota() { derrotas++; }
    public void registrarEmpate() { empates++; }

     void setRendimento(double r) {
        this.rendimento = r;
    }

    public void registrarBlackjack(){
        registrarVitoria();
        vBlackjack++;
    }
    public void setApostaAtual(double valor) {
        this.apostaAtual = valor;
    }

}