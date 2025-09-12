package br.com.loja;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.List;

import br.com.loja.dao.ProdutoDAO;
import br.com.loja.model.Produto;

public class Principal {
    public static void main(String[] args) {
       port(4568);

        ProdutoDAO dao = new ProdutoDAO();

        FreeMarkerEngine engine = new FreeMarkerEngine(); // Spark já configura o FreeMarker

        // Página inicial - formulário
        get("/", (req, res) -> new ModelAndView(new HashMap<>(), "addProduto.html"), engine);

        // Rota para inserir produto
        post("/produtos", (req, res) -> {
            String nome = req.queryParams("nome");
            int quantidade = Integer.parseInt(req.queryParams("quantidade"));
            double preco = Double.parseDouble(req.queryParams("preco"));

            dao.inserir(new Produto(nome, quantidade, preco));
            res.redirect("/produtos");
            return null;
        });

        // Rota para listar produtos
        get("/produtos", (req, res) -> {
            List<Produto> produtos = dao.listar();
            HashMap<String, Object> model = new HashMap<>();
            model.put("produtos", produtos);
            return new ModelAndView(model, "produtos.html");
        }, engine);
    }
}
