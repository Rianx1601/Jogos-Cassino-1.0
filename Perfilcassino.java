import java.time.LocalDateTime;

public class Perfilcassino {

    private int id;
    private String paraquemperdeu;
    private String dequemganhou;
    protected int vitorias;
    protected int derrotas;
    protected int empates;
    protected float quantoperdeuouganhou;
    protected LocalDateTime dataHora;

    public Perfilcassino(int id,
                         String paraquemperdeu,
                         String dequemganhou,
                         int vitorias,
                         int derrotas,
                         int empates,
                         float quantoperdeuouganhou,
                         LocalDateTime dataHora) {

        this.id = id;
        this.paraquemperdeu = paraquemperdeu;
        this.dequemganhou = dequemganhou;
        this.vitorias = vitorias;
        this.derrotas = derrotas;
        this.empates = empates;
        this.quantoperdeuouganhou = quantoperdeuouganhou;
        this.dataHora = dataHora;
    }
    public int getId() {return id;}
    public String getParaquemperdeu() { return paraquemperdeu; }
    public String getDequemganhou() { return dequemganhou; }
    public int getVitorias() { return vitorias; }
    public int getDerrotas() { return derrotas; }
    public int getEmpates() { return empates; }
    public float getQuantoperdeuouganhou() { return quantoperdeuouganhou; }
    public LocalDateTime getDataHora() { return dataHora; }
}