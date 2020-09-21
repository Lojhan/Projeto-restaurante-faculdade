package com.company;
import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[]args) throws IOException {
        Cliente cliente = new Cliente();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual seu nome? ");
        cliente.setNome(scanner.nextLine());
        int escolha = 0;

        do {
            if(escolha == 10){
                scanner.nextLine();
                System.out.println("Algum outro pedido? (S/N)");
                if (scanner.nextLine().equals("n")){
                    Cliente.addPedido(cliente);
                    return;
                }
            }
            System.out.println("O que deseja pedir? \n1. Pratos \n2. Bebidas \n3. Vinhos \n4. Adicionar comentário \n5. Ver pedidos \n6. Editar Cardápios");
            escolha = scanner.nextInt();

            switch (escolha) {
                case 1 -> cliente.setPratos(Cardapio.menu(Cardapio.pratoPath));
                case 2 -> cliente.setBebidas(Cardapio.menu(Cardapio.bebidaPath));
                case 3 -> cliente.setVinhos(Cardapio.menu(Cardapio.vinhoPath));
                case 4 -> cliente.setComplemento(Cliente.addComplemento());
                case 5 -> Cliente.verPedido();
                case 6 -> Cardapio.edit();
                default -> System.out.println("Hmm... Nada aqui");
            }
            escolha = 10;
        } while(true);
}}

