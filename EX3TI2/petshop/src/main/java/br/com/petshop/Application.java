package br.com.petshop;

import br.com.petshop.service.ClienteService;
import br.com.petshop.service.PetService;

import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class Application {

    public static void main(String[] args) {
        port(4567); 
        staticFiles.location("\\src\\main\\resources\\templates");

        System.out.println("Servidor iniciado. Acesse o link: http://localhost:4567/cliente/list");
        System.out.println("Pressione Ctrl + C para encerrar o servidor.");

        ClienteService clienteService = new ClienteService();
        PetService petService = new PetService();

        // Página inicial redireciona para listagem de clientes
        get("/", (req, res) -> {
            res.redirect("/cliente/list");
            return null;
        });

        // Rotas Cliente
        get("/cliente/list", clienteService::getListarClientes, new FreeMarkerEngine());
        get("/cliente/add", (req, res) -> new ModelAndView(null, "addCliente.html"), new FreeMarkerEngine());
        post("/cliente/save", clienteService::inserirCliente);
        get("/cliente/edit/:id", clienteService::getEditarCliente, new FreeMarkerEngine());
        post("/cliente/update/:id", clienteService::atualizarCliente);
        get("/cliente/delete/:id", clienteService::deletarCliente);

        // Rotas Pet
        get("/pet/list/:clienteId", petService::getListarPetsDoCliente, new FreeMarkerEngine());
        get("/pet/add/:clienteId", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("clienteId", req.params(":clienteId"));
            return new ModelAndView(model, "addPet.html");
        }, new FreeMarkerEngine());
        post("/pet/save", petService::inserirPet);
        get("/pet/edit/:id", (req, res) -> {
            Map<String, Object> model = new HashMap<>();
            model.put("id", req.params(":id"));
            return new ModelAndView(model, "editPet.html");
        }, new FreeMarkerEngine());
        post("/pet/update/:id", petService::atualizarPet);
        get("/pet/delete/:id", petService::deletarPet);
    }
}
