package br.com.petshop.service;

import br.com.petshop.ConnectionFactory;
import br.com.petshop.dao.PetDAO;
import br.com.petshop.model.Pet;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import java.sql.Connection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PetService {

    public Object inserirPet(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            PetDAO dao = new PetDAO(con);
            String nome = request.queryParams("nome");
            String especie = request.queryParams("especie");
            String raca = request.queryParams("raca");
            int idade = Integer.parseInt(request.queryParams("idade"));
            int idCliente = Integer.parseInt(request.queryParams("idCliente"));

            Pet novoPet = new Pet(-1, nome, especie, raca, idade, idCliente);
            dao.addPet(novoPet);
            response.redirect("/cliente/list");
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao inserir pet: " + e.getMessage();
        }
    }

    public ModelAndView getListarPetsDoCliente(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            PetDAO dao = new PetDAO(con);
            int clienteId = Integer.parseInt(request.params(":clienteId"));
            List<Pet> pets = dao.getPetsByClienteId(clienteId);
            Map<String, Object> model = new HashMap<>();
            model.put("pets", pets);
            return new ModelAndView(model, "pet.html");
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return new ModelAndView(null, "error.html");
        }
    }

    // Métodos de update e delete do pet
    public Object atualizarPet(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            PetDAO dao = new PetDAO(con);
            int id = Integer.parseInt(request.params(":id"));
            Pet pet = dao.getPetById(id);
            if (pet != null) {
                pet.setNome(request.queryParams("nome"));
                pet.setEspecie(request.queryParams("especie"));
                pet.setRaca(request.queryParams("raca"));
                pet.setIdade(Integer.parseInt(request.queryParams("idade")));
                dao.updatePet(pet);
                response.redirect("/cliente/list");
                return "";
            } else {
                response.status(404);
                return "Pet não encontrado.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao atualizar pet: " + e.getMessage();
        }
    }

    public Object deletarPet(Request request, Response response) {
        try (Connection con = ConnectionFactory.getConnection()) {
            PetDAO dao = new PetDAO(con);
            int id = Integer.parseInt(request.params(":id"));
            if (dao.deletePet(id)) {
                response.redirect("/cliente/list");
                return "Pet removido com sucesso!";
            } else {
                response.status(404);
                return "Pet não encontrado.";
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.status(500);
            return "Erro ao deletar pet: " + e.getMessage();
        }
    }
}