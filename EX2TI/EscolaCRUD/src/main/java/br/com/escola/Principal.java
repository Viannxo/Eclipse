package br.com.escola;

import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {
        AlunoDAO dao = new AlunoDAO();
        Scanner sc = new Scanner(System.in);
        int opcao;

        do {
            System.out.println("\n===Menu===");
            System.out.println("1. Inserir aluno");
            System.out.println("2. Listar alunos");
            System.out.println("3. Atualizar aluno");
            System.out.println("4. Deletar aluno");
            System.out.println("0. Sair");
            System.out.print("Opção: ");
            opcao = sc.nextInt();
            sc.nextLine(); // Consumir a nova linha

            switch (opcao) {
                case 1:
                    System.out.print("Nome: ");
                    String nome = sc.nextLine();
                    System.out.print("Curso: ");
                    String curso = sc.nextLine();
                    System.out.print("Idade: ");
                    int idade = sc.nextInt();
                    Aluno novoAluno = new Aluno(nome, curso, idade);
                    dao.inserir(novoAluno);
                    System.out.println("Aluno inserido com sucesso!");
                    break;
                case 2:
                    System.out.println("Lista de alunos:");
                    for (Aluno aluno : dao.listar()) {
                        System.out.println(aluno);
                    }
                    break;
                case 3:
                    System.out.print("ID do aluno a ser atualizado: ");
                    int idAtualizar = sc.nextInt();
                    sc.nextLine(); // Consumir a nova linha
                    System.out.print("Novo nome: ");
                    String novoNome = sc.nextLine();
                    System.out.print("Novo curso: ");
                    String novoCurso = sc.nextLine();
                    System.out.print("Nova idade: ");
                    int novaIdade = sc.nextInt();
                    Aluno alunoAtualizado = new Aluno(idAtualizar, novoNome, novoCurso, novaIdade);
                    dao.atualizar(alunoAtualizado);
                    System.out.println("Aluno atualizado com sucesso!");
                    break;
                case 4:
                    System.out.print("ID do aluno a ser deletado: ");
                    int idDeletar = sc.nextInt();
                    dao.deletar(idDeletar);
                    System.out.println("Aluno deletado com sucesso!");
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
        sc.close();

    }
}
