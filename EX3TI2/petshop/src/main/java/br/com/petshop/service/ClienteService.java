package br.com.petshop.service;

import br.com.petshop.ConnectionFactory;
import br.com.petshop.dao.ClienteDAO;
import br.com.petshop.model.Cliente;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ClienteService {

    public ModelAndView getListarClientes(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            ClienteDAO dao = new ClienteDAO(con);
            Map<String, Object> model = new HashMap<>();
            model.put("clientes", dao.listarTodos());
            return new ModelAndView(model, "cliente.html");
        } catch (Exception e) {
            e.printStackTrace();
            return new ModelAndView(null, "error.html");
        }
    }

    public Object inserirCliente(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            ClienteDAO dao = new ClienteDAO(con);
            String nome = request.queryParams("nome");
            String telefone = request.queryParams("telefone");
            String email = request.queryParams("email");
            Cliente novoCliente = new Cliente();
            novoCliente.setNome(nome);
            novoCliente.setTelefone(telefone);
            novoCliente.setEmail(email);
            dao.inserir(novoCliente);
            response.redirect("/cliente/list");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao inserir cliente: " + e.getMessage();
        }
    }

    public ModelAndView getEditarCliente(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            ClienteDAO dao = new ClienteDAO(con);
            int id = Integer.parseInt(request.params(":id"));
            Cliente cliente = dao.buscarPorId(id);
            if (cliente != null) {
                Map<String, Object> model = new HashMap<>();
                model.put("cliente", cliente);
                return new ModelAndView(model, "editCliente.html");
            } else {
                response.status(404);
                return new ModelAndView(null, "error.html");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return new ModelAndView(null, "error.html");
        }
    }
    
    public Object atualizarCliente(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            ClienteDAO dao = new ClienteDAO(con);
            int id = Integer.parseInt(request.params(":id"));
            Cliente cliente = dao.buscarPorId(id);
            if (cliente != null) {
                cliente.setNome(request.queryParams("nome"));
                cliente.setTelefone(request.queryParams("telefone"));
                cliente.setEmail(request.queryParams("email"));
                dao.atualizar(cliente);
                response.redirect("/cliente/list");
                return "";
            } else {
                response.status(404);
                return "Cliente não encontrado.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao atualizar cliente: " + e.getMessage();
        }
    }

    public Object deletarCliente(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            ClienteDAO dao = new ClienteDAO(con);
            int id = Integer.parseInt(request.params(":id"));
            dao.deletar(id);
            response.redirect("/cliente/list");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao deletar cliente: " + e.getMessage();
        }
    }
}