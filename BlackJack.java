import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


public class BlackJack{
    private List<Cassino> cassinos;
    private List<Jogador> jogadores;
    private Cassino cassino;
    private Jogador jogador;
    private JogadorDAO jogadorDAO = new JogadorDAO();
    private CassinoDAO cassinoDAO = new CassinoDAO();
    private PerfioJogadorDAO perfilJogadorDAO = new PerfioJogadorDAO();
    private PerfilcassinoDAO perfilCassinoDAO = new PerfilcassinoDAO();
    private class  Card {
        String value;
        String type;
        String messsagemresutado = "";

        Card(String value, String type) {
            this.value = value;
            this.type = type;
        }

        public String toString() {
            return value + "-" + type;
        }

        public int getValue() {
            if ("AJQK".contains(value)) { //A J Q K
                if ("A".equals(value)) {
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(value); //2-10
        }

        public boolean isAce() {
            return "A".equals(value);
        }


        public String getImagePath() {
            return "./cards/" + toString() + ".png";
        }
    }

    ArrayList<Card> deck;
    Random random = new Random(); //shuffle deck

    //dealer
    Card hiddenCard;
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;

    //player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    //window
    int boardWidth = 700;
    int boardHeight = 600;

    int cardWidth = 110; //ratio should 1/1.4
    int cardHeight = 154;

    JFrame frame = new JFrame("Black Jack");
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            try {
                //draw hidden card
                Image hiddenCardImg = new ImageIcon(getClass().getResource("./cards/BACK.png")).getImage();
                if (!stayButton.isEnabled()) {
                    hiddenCardImg = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImg, 20, 20, cardWidth, cardHeight, null);

                //draw dealer's hand
                for (int i = 0; i < dealerHand.size(); i++) {
                    Card card = dealerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, cardWidth + 25 + (cardWidth + 5)*i, 20, cardWidth, cardHeight, null);
                }

                //draw player's hand
                for (int i = 0; i < playerHand.size(); i++) {
                    Card card = playerHand.get(i);
                    Image cardImg = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(cardImg, 20 + (cardWidth + 5)*i, 320, cardWidth, cardHeight, null);
                }
                g.setFont(new Font("Arial", Font.PLAIN, 30));
                g.setColor(Color.white);
                g.drawString("Cassino saldo: "+ cassino.getSaldo(), 300, 200);
                g.drawString("Soma do Player: "+ playerSum +" Saldo do Player: " + jogador.getSaldo(), 20, 500);
                if (!stayButton.isEnabled()) {
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();
                    System.out.println("STAY: ");
                    System.out.println(dealerSum);
                    System.out.println(playerSum);

                    String message = "";

                    if (playerHand.size() == 2 && playerSum == 21){
                        message = "blackjack!!";
                    }
                    else {
                        if (playerSum > 21) {
                            message = "Você perdeu!";
                        }
                        else if (dealerSum > 21) {
                            message = "Você ganhou!";
                        }
                        //both you and dealer <= 21
                        else if (playerSum == dealerSum) {
                            message = "Empate!";
                        }
                        else if (playerSum > dealerSum) {
                            message = "Você ganhou!";
                        }
                        else if (playerSum < dealerSum) {
                            message = "Você perdeu!";
                        }

                    }
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 220, 250);
                    message = "Soma do dealer: "+ dealerSum;
                    g.setFont(new Font("Arial", Font.PLAIN, 30));
                    g.setColor(Color.white);
                    g.drawString(message, 20, 200);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    JPanel buttonPanel = new JPanel();
    JOptionPane valoraposta = new JOptionPane();
    JButton apostaButton = new JButton("Aposta");
    JButton hitButton = new JButton("Mais Um");
    JButton stayButton = new JButton("Ficar");
    JButton reloadButton = new JButton("Reiniciar");
    JButton MenuButton = new JButton("Menu");


    BlackJack(Cassino cassino, Jogador jogador, List<Cassino> cassinos, List<Jogador> jogadores) {

        this.cassinos = cassinos;
        this.jogadores = jogadores;
        double max;
        double min;
        min = cassino.getApostaMinima();
        max = cassino.getApostaMaxima();
        this.cassino = cassino;
        this.jogador = jogador;


        startGame();

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53, 101, 77));
        frame.add(gamePanel);

        apostaButton.setFocusable(false);
        buttonPanel.add(apostaButton);
        hitButton.setFocusable(false);
        buttonPanel.add(hitButton);
        hitButton.setEnabled(false);
        stayButton.setFocusable(false);
        buttonPanel.add(stayButton);
        stayButton.setEnabled(false);
        reloadButton.setFocusable(false);
        buttonPanel.add(reloadButton);
        reloadButton.setEnabled(false);
        MenuButton.setFocusable(false);
        buttonPanel.add(MenuButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        apostaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String saldoTexto = JOptionPane.showInputDialog(
                        frame,
                        "aposta:"
                );

                if (saldoTexto == null) {
                    return;
                }

                try {
                    double aposta = Double.parseDouble(saldoTexto);
                    JOptionPane.showMessageDialog(
                            frame,
                            "Jogador Apostou!\n" +
                                    "R$ " + aposta
                    );
                    if (min > aposta || max < aposta) {
                        return;
                    }
                    if (aposta > 0 && aposta <= jogador.getSaldo()
                            && aposta >= min && aposta <= max) {

                        jogador.fazerAposta(aposta);

                        hitButton.setEnabled(true);
                        stayButton.setEnabled(true);
                        apostaButton.setEnabled(false);
                        reloadButton.setEnabled(false);
                        MenuButton.setEnabled(false);
                        drawdelerhend();
                        drawplayerhend();
                    } else {
                        JOptionPane.showMessageDialog(frame,
                                "Valor de aposta inválido!",
                                "Erro",
                                JOptionPane.ERROR_MESSAGE);
                    }
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

        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Card card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                if (reducePlayerAce() > 21) { //A + 2 + J --> 1 + 2 + J
                    hitButton.setEnabled(false);
                }
                gamePanel.repaint();
            }
        });

        stayButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                hitButton.setEnabled(false);
                stayButton.setEnabled(false);
                reloadButton.setEnabled(true);
                gamePanel.repaint();

                while (dealerSum < 17) {
                    Card card = deck.remove(deck.size() - 1);
                    dealerSum += card.getValue();
                    dealerAceCount += card.isAce() ? 1 : 0;
                    dealerHand.add(card);
                }
                double valorAposta = jogador.getPerfil().getApostaAtual();
                gamePanel.repaint();
                String resultado = "";

                // calcula resultado
                if (playerHand.size() == 2 && playerSum == 21) {
                    resultado = "BLACKJACK";
                    cassino.processarBlackjack(jogador);
                    jogador.getPerfil().registrarBlackjack();
                }
                else if (playerSum > 21) {
                    resultado = "DERROTA";
                    cassino.processarDerrota(jogador);
                    jogador.getPerfil().registrarDerrota();
                }
                else if (dealerSum > 21 || playerSum > dealerSum) {
                    resultado = "VITORIA";
                    cassino.processarVitoria(jogador);
                    jogador.getPerfil().registrarVitoria();
                }
                else if (playerSum == dealerSum) {
                    resultado = "EMPATE";
                    cassino.processarEmpate(jogador);
                    jogador.getPerfil().registrarEmpate();
                }
                else {
                    resultado = "DERROTA";
                    cassino.processarDerrota(jogador);
                    jogador.getPerfil().registrarDerrota();
                }

                jogador.getPerfil().setId(jogador.getID());
                perfilJogadorDAO.atualizar(jogador.getPerfil());


                jogadorDAO.atualizarSaldo(jogador);

                Perfilcassino registro = new Perfilcassino(
                        0,
                        resultado.equals("VITORIA") ? "Jogador" : "Cassino",
                        resultado.equals("VITORIA") ? jogador.getNome() : cassino.getNome(),
                        jogador.getPerfil().getVitorias(),
                        jogador.getPerfil().getDerrotas(),
                        jogador.getPerfil().getEmpates(),
                        (float) jogador.getPerfil().getApostaAtual(),
                        LocalDateTime.now()
                );


                perfilCassinoDAO.salvar(registro);
                cassinoDAO.atualizarSaldo(cassino);
                gamePanel.repaint();

                Cassino cassinoAtualizado = cassinoDAO.buscarPorNome(cassino.getNome());
                cassino.setSaldo(cassinoAtualizado.getSaldo());
                jogadorDAO.atualizarSaldo(jogador);
                }
        });

        reloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e){
                JOptionPane.showMessageDialog(
                        frame,
                        "Faça uma aposta ou volte para o menu"
                );
                    apostaButton.setEnabled(true);
                    MenuButton.setEnabled(true);
                    reloadButton.setEnabled(false);

                    gamePanel.repaint();

            }
        });
        MenuButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Jfreme(cassinos, jogadores);
                frame.setVisible(false);
                dealerSum = 0;
                dealerAceCount = 0;
                dealerHand.clear();
                playerSum = 0;
                playerAceCount = 0;
                playerHand.clear();
                buildDeck();
                drawdelerhend();
                drawplayerhend();

            }
        });
        gamePanel.repaint();
    }



    public void startGame() {
        //deck
        dealerSum = 0;
        dealerAceCount = 0;
        playerSum = 0;
        playerAceCount = 0;
        buildDeck();
        shuffleDeck();
        //dealer

        System.out.println("DEALER:");
        System.out.println(hiddenCard);
        System.out.println(dealerHand);
        System.out.println(dealerSum);
        System.out.println(dealerAceCount);


        //player

        System.out.println("PLAYER: ");
        System.out.println(playerHand);
        System.out.println(playerSum);
        System.out.println(playerAceCount);


    }
    public void drawdelerhend(){
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.remove(deck.size()-1); //remove card at last index
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.remove(deck.size()-1);
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);
    }

    public  void drawplayerhend(){
            playerHand = new ArrayList<Card>();
            Card card = deck.remove(deck.size()-1);

            playerSum = 0;
            playerAceCount = 0;
            for (int i = 0; i < 2; i++) {
                card = deck.remove(deck.size()-1);
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
        }
    }

    public void buildDeck() {
        deck = new ArrayList<Card>();
        String[] values = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        String[] types = {"C", "D", "H", "S"};

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < values.length; j++) {
                Card card = new Card(values[j], types[i]);
                deck.add(card);
            }
        }

        System.out.println("BUILD DECK:");
        System.out.println(deck);
    }

    public void shuffleDeck() {
        for (int i = 0; i < deck.size(); i++) {
            int j = random.nextInt(deck.size());
            Card currCard = deck.get(i);
            Card randomCard = deck.get(j);
            deck.set(i, randomCard);
            deck.set(j, currCard);
        }

        System.out.println("AFTER SHUFFLE");
        System.out.println(deck);
    }

    public int reducePlayerAce() {
        while (playerSum > 21 && playerAceCount > 0) {
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }

    public int reduceDealerAce() {
        while (dealerSum > 21 && dealerAceCount > 0) {
            dealerSum -= 10;
            dealerAceCount -= 1;
        } return dealerSum;
    }
}
