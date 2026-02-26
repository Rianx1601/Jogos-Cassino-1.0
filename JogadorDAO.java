import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JogadorDAO {
    private PerfioJogadorDAO perfiljogadorDAO = new PerfioJogadorDAO();
    public void salvar(Jogador jogador) {
        String sql = "INSERT INTO jogador (nome, saldo) VALUES (?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, jogador.getNome());
            stmt.setDouble(2, jogador.getSaldo());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int idGerado = rs.getInt(1);
                jogador.setId(idGerado); // você precisa criar esse setter
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void atualizarSaldo(Jogador jogador) {
        String sql = "UPDATE jogador SET saldo = ? WHERE ID = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, jogador.getSaldo());
            ps.setInt(2, jogador.getID());
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Jogador buscarporID(Integer id) {
        String sql = "SELECT * FROM jogador WHERE ID = ?";
        Jogador jogador = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                jogador = new Jogador(
                        rs.getInt("ID"),
                        rs.getString("nome"),
                        rs.getDouble("saldo")
                );

                // 🔥 BUSCA O PERFIL NO BANCO
                Perfiljogador perfil = perfiljogadorDAO.buscarPorJogadorId(id);
                jogador.setPerfil(perfil);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jogador;
    }

    public List<Jogador> listar() {
        List<Jogador> jogadores = new ArrayList<>();
        String sql = "SELECT * FROM jogador";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Jogador j = new Jogador(
                        rs.getInt("ID"),
                        rs.getString("nome"),
                        rs.getDouble("saldo")
                );
                jogadores.add(j);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jogadores;
    }
}