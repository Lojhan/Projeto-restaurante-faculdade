package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Cardapio {
    public static String bebidaPath = "C:\\Users\\Vinic\\Desktop\\Nova pasta\\bebidas-tabuladas.txt";
    public static String vinhoPath = "C:\\Users\\Vinic\\Desktop\\Nova pasta\\vinhos-tabulados.txt";
    public static String pratoPath = "C:\\Users\\Vinic\\Desktop\\Nova pasta\\pratos.csv";
    private static String res;
    private static String escolha;

    public static String addToList(List<Item> cardapio, String patch) throws IOException {
        Scanner scanner = new Scanner(System.in);
        Item novo = new Item();

        System.out.println("\n Adição de Items (Digite \"cancelar\" para voltar)");
        if (scanner.nextLine().equals("cancelar")) return null; else System.out.println("Nome do Item: ");
        novo.setNome(scanner.nextLine());
        System.out.println("Valor do Item: "); novo.setPreco(scanner.nextDouble());
        res = "ok";
        cardapio.add(novo);
        Rewrite(cardapio, patch);
        return res;
    }
    public static String removeFromList(List<Item> cardapio, String patch) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Remoção de Items (Digite \"cancelar\" para voltar)");
        if (scanner.nextLine().equals("cancelar")) return null; else System.out.println("Nome do Item: ");
        String nome = scanner.nextLine();
        System.out.println(cardapio.removeIf(item -> item.getNome().contains(nome)) ? "Remoção realizada com sucesso!" : "Item não encontrado!");
        Rewrite(cardapio, patch);
        return nome;
    }
    public static String editItem(List<Item> cardapio, String patch) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Edição de Items (Digite \"cancelar\" para voltar)");
        if (scanner.nextLine().equals("cancelar")) return null; else System.out.println("Nome do Item: ");
        String nome = scanner.nextLine();

        for (Item item: cardapio) {
            if (item.getNome().contains(nome)){
                System.out.println("Escreva o novo nome"); item.setNome(scanner.nextLine());
                System.out.println("Escreva o novo valor"); item.setPreco(scanner.nextDouble());
            } else System.out.println("Item não encontrado!");
        }
        Rewrite(cardapio, patch);
        return res;
    }
    public static void Rewrite(List<Item> cardapio, String patch) throws IOException {
        File output = new File(patch);
        PrintWriter gravador = new PrintWriter(String.valueOf(output));
        if (patch.endsWith(("csv"))){
            gravador.println("PRATO;PREÇO");
            for (Item item: cardapio) gravador.println(item.getNome() + ";" + String.valueOf(item.getPreco()).replaceAll("\\.", ","));
        }else{
            gravador.println("PRECO\t" + (patch.endsWith("vinhos-tabulados") ? "VINHOS" : "BEBIDAS"));
            for (Item item: cardapio) gravador.println(String.valueOf(item.getPreco()).replaceAll("\\.", ",") + "\t" + item.getNome());
        }
        gravador.close();
    }
    public static void edit() throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Qual cardápio você deseja editar?\nOu digite \"voltar\" para voltar ao menu principal");
        escolha = scanner.nextLine();
        if (!escolha.equals("voltar")){
            res = switch (escolha) {
                case "Pratos" -> Cardapio.crud(pratoPath);
                case "Bebidas" -> Cardapio.crud(bebidaPath);
                case "Vinhos" -> Cardapio.crud(vinhoPath);
                default -> null;
            };
        }
    }
    public static String crud(String patch) throws IOException {
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("Escolha uma função entre Adicionar, Remover ou Editar\n\"Ou digite \\\"voltar\\\" para voltar ao menu principal\"");
            escolha = scanner.nextLine();
            if (!escolha.equals("voltar")){
                switch (escolha){
                    case "Adicionar" -> res = addToList(read(patch), patch);
                    case "Remover" -> res = removeFromList(read(patch), patch);
                    case "Editar" -> res = editItem(read(patch), patch);
                }
                System.out.println( res == null ? "Item não encontrado" :"Alteração Realizada com sucesso!");
            }
        } while (!escolha.equals("voltar"));
        return res;
    }
    public static List<Item> menu(String patch) throws IOException {
        Scanner scanner = new Scanner(System.in);
        List<Item> items = read(patch);
        List<Item> pedido = new ArrayList<>();

        while (true) {
            System.out.println("O que deseja da lista?");
            for (Item item : items) System.out.println((item.reqNum + 1) + " " + item.getNome() + " " + item.getPreco());
            pedido.add((items.get(scanner.nextInt() - 1)));
            scanner.nextLine();
            System.out.println("Deseja continuar escolhendo? (S/N)");
            if (scanner.nextLine().equals("n")) return pedido;
        }
    }
    public static List<Item> read(String path) throws IOException {
        List<Item> items = new ArrayList<>();
        File file = new File(path);
        Scanner leitor = new Scanner(file);
        leitor.nextLine();
        int i = 0;

        while (leitor.hasNext()) {
            String linha = leitor.nextLine();
            Item object = new Item();
            object.reqNum = i;
            if (path.contains("pratos")){
                String[] partes = linha.split(";");
                object.setPreco(Double.parseDouble(partes[1].replaceAll(",", ".")));
                object.setNome(partes[0]);
            }else{
                String[] partes = linha.split("\t");
                object.setPreco(Double.parseDouble(partes[0].replaceAll(",", ".")));
                object.setNome(partes[1]);
            }
            i++;
            items.add(object);
        }
        return items;
    }
}
