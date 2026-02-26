import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CassinoDAO {

    public void salvar(Cassino cassino) {
        String sql = "INSERT INTO cassino (nome, saldo, aposta_minima, aposta_maxima) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, cassino.getNome());
            ps.setDouble(2, cassino.getSaldo());
            ps.setDouble(3, cassino.getApostaMinima());
            ps.setDouble(4, cassino.getApostaMaxima());
            ps.executeUpdate();

            System.out.println("Cassino salvo!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void atualizarSaldo(Cassino cassino) {

        String sql = "UPDATE cassino SET saldo = ? WHERE nome = ?";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, cassino.getSaldo());
            ps.setString(2, cassino.getNome());

            ps.executeUpdate();

            System.out.println("Saldo do cassino atualizado!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Cassino buscarPorNome(String nome) {
        String sql = "SELECT * FROM cassino WHERE nome = ?";
        Cassino cassino = null;

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nome);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                cassino = new Cassino(
                        rs.getString("nome"),
                        rs.getDouble("saldo"),
                        rs.getDouble("aposta_minima"),
                        rs.getDouble("aposta_maxima")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cassino;
    }
    public List<Cassino> listar() {
        List<Cassino> cassinos = new ArrayList<>();
        String sql = "SELECT * FROM cassino";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cassino c = new Cassino(
                        rs.getString("nome"),
                        rs.getDouble("saldo"),
                        rs.getDouble("aposta_minima"),
                        rs.getDouble("aposta_maxima")
                );
                cassinos.add(c);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cassinos;
    }

}