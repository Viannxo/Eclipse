package br.com.loja;

import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import br.com.loja.model.Produto;
import br.com.loja.dao.ProdutoDAO;

import static org.junit.Assert.*;

public class ProdutoDAOTest {

    private ProdutoDAO dao;

    @Before
    public void setUp() {
        dao = new ProdutoDAO();
        limparTabela(); // Limpa os dados antes de cada teste
    }

    private void limparTabela() {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/loja", "postgres", "ti@cc");
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("TRUNCATE TABLE produtos RESTART IDENTITY");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInserirProduto() {
        Produto p = new Produto("Notebook", 5, 3500.00);
        dao.inserir(p);

        List<Produto> produtos = dao.listar();
        boolean encontrado = produtos.stream()
                .anyMatch(produto -> produto.getNome().equals("Notebook") 
                                 && produto.getQuantidade() == 5
                                 && produto.getPreco() == 3500.00);

        assertTrue("Produto inserido deve estar na lista", encontrado);
    }

    @Test
    public void testListarProdutos() {
        Produto p1 = new Produto("Mouse", 10, 50.0);
        Produto p2 = new Produto("Teclado", 7, 120.0);
        dao.inserir(p1);
        dao.inserir(p2);

        List<Produto> produtos = dao.listar();
        assertTrue(produtos.size() >= 2);

        boolean contemMouse = produtos.stream().anyMatch(prod -> prod.getNome().equals("Mouse"));
        boolean contemTeclado = produtos.stream().anyMatch(prod -> prod.getNome().equals("Teclado"));

        assertTrue("Lista deve conter Mouse", contemMouse);
        assertTrue("Lista deve conter Teclado", contemTeclado);
    }
}
