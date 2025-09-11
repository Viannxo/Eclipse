package br.com.escola;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AlunoDAO {
    private final String url = "jdbc:postgresql://localhost:5432/escola";
    private final String user = "postgres";
    private final String password = "ti@cc";

    // Inserir aluno
    public void inserir(Aluno aluno) {
        String sql = "INSERT INTO aluno (nome, curso, idade) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getCurso());
            pstmt.setInt(3, aluno.getIdade());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Listar alunos
    public List<Aluno> listar() {
        List<Aluno> alunos = new ArrayList<>();
        String sql = "SELECT * FROM aluno";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Aluno aluno = new Aluno(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("curso"),
                    rs.getInt("idade")
                );
                alunos.add(aluno);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return alunos;
    }

    // Atualizar aluno
    public void atualizar(Aluno aluno) {
        String sql = "UPDATE aluno SET nome = ?, curso = ?, idade = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, aluno.getNome());
            pstmt.setString(2, aluno.getCurso());
            pstmt.setInt(3, aluno.getIdade());
            pstmt.setInt(4, aluno.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    // Deletar aluno
    public void deletar(int id) {
        String sql = "DELETE FROM aluno WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
