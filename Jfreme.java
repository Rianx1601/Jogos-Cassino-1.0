import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;

public class Jfreme {
    private List<Cassino> cassinos;
    private List<Jogador> jogadores;
    private Cassino cassino;
    private Jogador jogador;

    int boardWidth = 800;
    int boardHeight = 150;

    JFrame frame = new JFrame("Menu");
    JPanel menuPanel = new JPanel();
    JPanel buttonPanel = new JPanel();

    JButton mudarJogadorButton = new JButton("Mudar Jogador");
    JButton mudarCassinoButton = new JButton("Mudar Cassino");
    JButton cadastraJogadorButton = new JButton("Cadastrar Jogador");
    JButton cadastraCassinoButton = new JButton("Cadastrar Cassino");
    JButton jogarButton = new JButton("Jogar");


    public Jfreme(List<Cassino> cassinos, List<Jogador> jogadores) {
        CassinoDAO cassinoDAO = new CassinoDAO();
        JogadorDAO jogadorDAO = new JogadorDAO();

        this.cassinos = cassinoDAO.listar();
        this.jogadores = jogadorDAO.listar();

        iniciarInterface();
        configurarEventos();



    }
    private void iniciarInterface() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        menuPanel.setLayout(new BorderLayout());
        menuPanel.setBackground(new Color(9, 72, 199));

        menuPanel.add(buttonPanel, BorderLayout.SOUTH);
        buttonPanel.add(mudarJogadorButton);
        buttonPanel.add(mudarCassinoButton);
        buttonPanel.add(cadastraJogadorButton);
        buttonPanel.add(cadastraCassinoButton);
        buttonPanel.add(jogarButton);
        frame.setContentPane(menuPanel);

        frame.setVisible(true);
    }
    private void configurarEventos() {
        mudarJogadorButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (jogadores.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nenhum jogador cadastrado!");
                    return;
                }
                Jogador escolhido = (Jogador) JOptionPane.showInputDialog(
                        frame,
                        "Selecione um jogador:",
                        "Jogadores",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        jogadores.toArray(),
                        jogadores.get(0)
                );
                if (escolhido != null) {
                    jogador = escolhido;
                    JOptionPane.showMessageDialog(frame,
                            "Jogador selecionado: " + jogador.getNome());
                }
            }
        });
        mudarCassinoButton.addActionListener(new  ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (cassinos.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Nenhum cassino cadastrado!");
                    return;
                }

                Cassino escolhido = (Cassino) JOptionPane.showInputDialog(
                        frame,
                        "Selecione um cassino:",
                        "Cassinos",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        cassinos.toArray(),
                        cassinos.get(0)
                );

                if (escolhido != null) {
                    cassino = escolhido;
                    JOptionPane.showMessageDialog(frame,
                            "Cassino selecionado: " + cassino.getNome());
                }
            }
        });
        cadastraJogadorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = JOptionPane.showInputDialog(frame, "Nome do jogador:");
                if (nome == null || nome.isBlank()) return;

                String saldoTexto = JOptionPane.showInputDialog(frame, "Saldo inicial:");
                if (saldoTexto == null) return;

                try {
                    double saldo = Double.parseDouble(saldoTexto);

                    // 1️⃣ cria e salva jogador
                    Jogador jogador = new Jogador(nome, saldo);
                    JogadorDAO jogadorDAO = new JogadorDAO();
                    jogadorDAO.salvar(jogador); // ID GERADO AQUI

                    // 2️⃣ cria perfil vinculado ao jogador
                    Perfiljogador perfil = new Perfiljogador();
                    perfil.setJogadorId(jogador.getId());

                    PerfioJogadorDAO perfilDAO = new PerfioJogadorDAO();
                    perfilDAO.salvar(perfil);

                    jogadores.add(jogador);

                    JOptionPane.showMessageDialog(
                            frame,
                            "Jogador criado com sucesso!\n" +
                                    "Nome: " + jogador.getNome() +
                                    "\nSaldo: R$ " + jogador.getSaldo()
                    );

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Saldo inválido!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        cadastraCassinoButton.addActionListener(new  ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String nome = JOptionPane.showInputDialog(
                        frame,
                        "Nome do cassino:");
                if (nome == null || nome.isBlank()) {
                    return;
                }
                String saldotexto = JOptionPane.showInputDialog(
                        frame,
                        "Saldo inicial:");
                if (saldotexto == null) {
                    return;
                }
                String mintexto = JOptionPane.showInputDialog(
                        frame,
                        "Apposta minima:");
                if (mintexto == null) {
                    return;
                }
                String maxtexto = JOptionPane.showInputDialog(
                        frame,
                        "Aposta maxima:");
                if (maxtexto == null) {
                    return;
                }

                try {
                    double saldo = Double.parseDouble(saldotexto);
                    double apostaMinima = Double.parseDouble(mintexto);
                    double apostaMaxima = Double.parseDouble(maxtexto);


                    cassino = new Cassino(nome,saldo,apostaMinima,apostaMaxima);
                    CassinoDAO dao = new CassinoDAO();
                    dao.salvar(cassino);
                    cassinos.add(cassino);
                    JOptionPane.showMessageDialog(
                            frame,"Cassino criado com sucesso!\n" +
                                    "Nome: " + cassino.getNome() +
                                    "\nSaldo: R$ " + cassino.getSaldo() +
                                    "\n Aposta Minima" + cassino.getApostaMinima() +
                                    "\n Aposta Maxima" + cassino.getApostaMaxima()
                    );

                }catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Saldo inválido!",
                            "Erro",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        jogarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (cassino == null || jogador == null) {
                    JOptionPane.showMessageDialog(
                            frame,
                            "Selecione um cassino e um jogador antes de jogar!",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                new BlackJack(cassino,jogador,cassinos,jogadores);

                frame.setVisible(false); // ou frame.dispose();
            }
        });

    }
}
