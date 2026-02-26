import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PerfilcassinoDAO {

    public void salvar(Perfilcassino perfil) {

        String sql = "INSERT INTO perfil_cassino " +
                "(id, paraquemperdeu, dequemganhou, vitorias, derrotas, empates, quantoperdeuouganhou, data_hora) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = Conexao.conectar();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,perfil.getId());
            ps.setString(2, perfil.getParaquemperdeu());
            ps.setString(3, perfil.getDequemganhou());
            ps.setInt(4, perfil.getVitorias());
            ps.setInt(5, perfil.getDerrotas());
            ps.setInt(6, perfil.getEmpates());
            ps.setFloat(7, perfil.getQuantoperdeuouganhou());
            ps.setTimestamp(8, Timestamp.valueOf(perfil.getDataHora()));

            ps.executeUpdate();

            System.out.println("Partida salva no banco!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Perfilcassino> listar() {

        List<Perfilcassino> lista = new ArrayList<>();
        String sql = "SELECT * FROM perfil_cassino";

        try (Connection conn = Conexao.conectar();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {

                Perfilcassino p = new Perfilcassino(
                        rs.getInt("id"),
                        rs.getString("para_quem_perdeu"),
                        rs.getString("de_quem_ganhou"),
                        rs.getInt("vitorias"),
                        rs.getInt("derrotas"),
                        rs.getInt("empates"),
                        rs.getFloat("valor_movimentado"),
                        rs.getTimestamp("data_hora").toLocalDateTime()
                );

                lista.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}