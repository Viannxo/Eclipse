package br.com.petshop;

import br.com.petshop.dao.PetDAO;
import br.com.petshop.model.Pet;
import java.sql.Connection;
import java.util.List;

public class PetDAOTest {
    public static void main(String[] args) {
        try (Connection con = ConnectionFactory.getConnection()) {
            PetDAO dao = new PetDAO(con);

            // Crie um novo Pet para teste.
            // O ID do cliente (último parâmetro) deve ser um cliente que já existe no seu banco de dados.
            Pet pet = new Pet(-1, "Rex", "Cachorro", "Labrador", 3, 1);
            boolean inserido = dao.addPet(pet);
            System.out.println("Pet inserido? " + inserido);

            // Liste todos os pets para verificar
            List<Pet> pets = dao.getAllPets();
            System.out.println("\nPets cadastrados:");
            for (Pet p : pets) {
                System.out.println(p.getNome() + " - " + p.getEspecie() + " - ID Cliente: " + p.getIdCliente());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}