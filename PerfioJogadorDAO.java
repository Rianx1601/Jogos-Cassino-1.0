import java.sql.*;

public class PerfioJogadorDAO {

    public void salvar(Perfiljogador perfil) {
        System.out.println("Jogador ID: " + perfil.getJogadorId());
        String sql = "INSERT INTO perfil_jogador " +
                "(jogador_id, aposta_atual, vitorias, derrotas, empates, rendimento, v_blackjack) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, perfil.getJogadorId()); // ✅ CORREÇÃO
            ps.setDouble(2, perfil.getApostaAtual());
            ps.setInt(3, perfil.getVitorias());
            ps.setInt(4, perfil.getDerrotas());
            ps.setInt(5, perfil.getEmpates());
            ps.setDouble(6, perfil.getRendimento());
            ps.setInt(7, perfil.getvBlackjack());

            ps.executeUpdate();

            System.out.println("Perfil do jogador salvo!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Perfiljogador perfil) {

        String sql = "UPDATE perfil_jogador SET " +
                "aposta_atual=?, vitorias=?, derrotas=?, empates=?, rendimento=?, v_blackjack=? " +
                "WHERE jogador_id=?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, perfil.getApostaAtual());
            ps.setInt(2, perfil.getVitorias());
            ps.setInt(3, perfil.getDerrotas());
            ps.setInt(4, perfil.getEmpates());
            ps.setDouble(5, perfil.getRendimento());
            ps.setInt(6, perfil.getvBlackjack());
            ps.setInt(7, perfil.getJogadorId());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Perfiljogador buscarPorJogadorId(int id) {
        String sql = "SELECT * FROM perfil_jogador WHERE jogador_id = ?";
        Perfiljogador perfil = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                perfil = new Perfiljogador();
                perfil.setId(rs.getInt("id"));
                perfil.setJogadorId(rs.getInt("jogador_id"));
                perfil.setApostaAtual(rs.getDouble("aposta_atual"));
                perfil.vitorias = rs.getInt("vitorias");
                perfil.derrotas = rs.getInt("derrotas");
                perfil.empates = rs.getInt("empates");
                perfil.setRendimento(rs.getDouble("rendimento"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return perfil;
    }
}