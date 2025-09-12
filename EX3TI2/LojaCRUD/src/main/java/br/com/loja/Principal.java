package br.com.loja;

import static spark.Spark.*;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import freemarker.template.Configuration;

import java.util.HashMap;
import java.util.List;

import br.com.loja.dao.ProdutoDAO;
import br.com.loja.model.Produto;

public class Principal {
    public static void main(String[] args) {
       port(4570); // qualquer porta livre


        // Cria o DAO
        ProdutoDAO dao = new ProdutoDAO();

        // Configura a pasta de arquivos estáticos (CSS, JS, imagens)
        staticFiles.location("/static"); // src/main/resources/static

        // Configuração do FreeMarker
        Configuration freeMarkerConfig = new Configuration(Configuration.VERSION_2_3_31);
        freeMarkerConfig.setClassForTemplateLoading(Principal.class, "/templates"); // procura templates dentro de resources/templates
        FreeMarkerEngine engine = new FreeMarkerEngine(freeMarkerConfig);

        // Tratamento global de exceção
        exception(Exception.class, (e, req, res) -> {
            e.printStackTrace();
            res.status(500);
            res.body("Erro interno do servidor: " + e.getMessage());
        });

        // Página inicial - redireciona para o formulário de adicionar produto
        get("/", (req, res) -> {
            res.redirect("/add");
            return null;
        });

        // Formulário de adicionar produto
        get("/add", (req, res) -> new ModelAndView(new HashMap<>(), "addProduto.ftl"), engine);

        // Inserir produto
        post("/produtos", (req, res) -> {
            try {
                String nome = req.queryParams("nome");
                int quantidade = Integer.parseInt(req.queryParams("quantidade"));
                double preco = Double.parseDouble(req.queryParams("preco"));

                dao.inserir(new Produto(nome, quantidade, preco));
                res.redirect("/produtos");
            } catch (NumberFormatException e) {
                res.status(400);
                return "Erro: quantidade ou preço inválido!";
            } catch (Exception e) {
                res.status(500);
                return "Erro interno do servidor: " + e.getMessage();
            }
            return null;
        });

        // Listar produtos
        get("/produtos", (req, res) -> {
            List<Produto> produtos = dao.listar();
            HashMap<String, Object> model = new HashMap<>();
            model.put("produtos", produtos);
            return new ModelAndView(model, "produtos.ftl");
        }, engine);

        System.out.println("Servidor rodando em http://localhost:4569/");
    }
}
