package br.com.petshop.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import br.com.petshop.model.Pet;

public class PetDAO {
    private Connection conn;

    public PetDAO(Connection conn) {
        this.conn = conn;
    }

    // CREATE: adiciona um novo pet
    public boolean addPet(Pet pet) throws SQLException {
        String sql = "INSERT INTO pets (nome, especie, raca, idade, id_cliente) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setInt(4, pet.getIdade());
            stmt.setInt(5, pet.getIdCliente());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }

    // READ: retorna todos os pets
    public List<Pet> getAllPets() throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pet pet = new Pet();
                pet.setId(rs.getInt("id"));
                pet.setNome(rs.getString("nome"));
                pet.setEspecie(rs.getString("especie"));
                pet.setRaca(rs.getString("raca"));
                pet.setIdade(rs.getInt("idade"));
                pet.setIdCliente(rs.getInt("id_cliente"));
                pets.add(pet);
            }
        }
        return pets;
    }

    // READ: retorna um pet pelo ID
    public Pet getPetById(int id) throws SQLException {
        String sql = "SELECT * FROM pets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("id"));
                    pet.setNome(rs.getString("nome"));
                    pet.setEspecie(rs.getString("especie"));
                    pet.setRaca(rs.getString("raca"));
                    pet.setIdade(rs.getInt("idade"));
                    pet.setIdCliente(rs.getInt("id_cliente"));
                    return pet;
                }
            }
        }
        return null;
    }

    // UPDATE: atualiza os dados de um pet
    public boolean updatePet(Pet pet) throws SQLException {
        String sql = "UPDATE pets SET nome = ?, especie = ?, raca = ?, idade = ?, id_cliente = ? WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, pet.getNome());
            stmt.setString(2, pet.getEspecie());
            stmt.setString(3, pet.getRaca());
            stmt.setInt(4, pet.getIdade());
            stmt.setInt(5, pet.getIdCliente());
            stmt.setInt(6, pet.getId());
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        }
    }

    // DELETE: remove um pet pelo ID
    public boolean deletePet(int id) throws SQLException {
        String sql = "DELETE FROM pets WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        }
    }

    // READ: retorna pets de um cliente específico
    public List<Pet> getPetsByClienteId(int clienteId) throws SQLException {
        List<Pet> pets = new ArrayList<>();
        String sql = "SELECT * FROM pets WHERE id_cliente = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Pet pet = new Pet();
                    pet.setId(rs.getInt("id"));
                    pet.setNome(rs.getString("nome"));
                    pet.setEspecie(rs.getString("especie"));
                    pet.setRaca(rs.getString("raca"));
                    pet.setIdade(rs.getInt("idade"));
                    pet.setIdCliente(rs.getInt("id_cliente"));
                    pets.add(pet);
                }
            }
        }
        return pets;
    }
}