package com.company;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Cliente {
    private String nome;
    private List<Item> Pratos;
    private List<Item> Bebidas;
    private List<Item> Vinhos;
    private String complemento;
    public double total;

    public List<Item> getPratos() {
        return Pratos;
    }
    public void setPratos(List<Item> pratos) {
        Pratos = pratos;
    }

    public List<Item> getBebidas() {
        return Bebidas;
    }
    public void setBebidas(List<Item> bebidas) {
        Bebidas = bebidas;
    }

    public List<Item> getVinhos() {
        return Vinhos;
    }
    public void setVinhos(List<Item> vinhos) {
        Vinhos = vinhos;
    }

    public String getComplemento() {
        return complemento;
    }
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }

    public static double writeIn(PrintWriter gravador, List<Item> bruh, double total, String nome, String path) throws IOException {

        gravador.print(nome + ":");
            for (Item item : bruh) {
                total += Cardapio.read(path).stream().filter(e -> e.getNome().equals(item.getNome())).mapToDouble(Item::getPreco).sum();
                if (bruh.indexOf(item) == (bruh.size() -1)) gravador.print(item.getNome()); else gravador.print(item.getNome() + ";");
            }
        gravador.print("\n");
        return total;
    }
    public static void addPedido(Cliente cliente) throws IOException {
        File output = new File("C:\\Users\\Vinic\\Desktop\\Nova pasta\\pedidos\\" + cliente.getNome() + ".txt");
        PrintWriter gravador = new PrintWriter(String.valueOf(output));
        gravador.println("Cliente:" + cliente.getNome());
        double total = 0;

        if (cliente.getPratos() != null) total += writeIn(gravador, cliente.getPratos(), total, "Pratos", Cardapio.pratoPath);
        if (cliente.getBebidas() != null) total += writeIn(gravador, cliente.getBebidas(), total, "Bebidas", Cardapio.bebidaPath);
        if (cliente.getVinhos() != null) total += writeIn(gravador, cliente.getVinhos(), total, "Vinhos", Cardapio.vinhoPath);
        gravador.println(cliente.getComplemento() == null ? "" :"Complemento:" + cliente.getComplemento());
        gravador.println("Total:" + total);
        gravador.close();
    }
    public static String addComplemento(){
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        System.out.println("Digite o Complemento:");
        return scanner.nextLine();
    }
    public static void pedidos() {
        File directoryPath = new File("C:\\Users\\Vinic\\Desktop\\Nova pasta\\pedidos");
        File[] filesList = directoryPath.listFiles();
        for(File file : Objects.requireNonNull(filesList)) System.out.println(file.getName().split(".txt")[0]);
    }
    public static void lernovamente(Cliente pedido){
        if (!(pedido.getPratos() == null || pedido.getPratos().isEmpty())){System.out.println("Pratos:");for (Item element : pedido.getPratos()) System.out.println(element.getNome());}
        if(!(pedido.getBebidas() == null || pedido.getBebidas().isEmpty())){System.out.println("Bebidas:");for (Item element : pedido.getBebidas()) System.out.println(element.getNome());}
        if(!(pedido.getVinhos() == null || pedido.getVinhos().isEmpty())){System.out.println("Vinhos:");for (Item element : pedido.getVinhos()) System.out.println(element.getNome());}
        System.out.println("Complemento:");System.out.println(pedido.getComplemento());
        System.out.println("Total: R$" + pedido.total);
    }
    public static void verPedido() throws IOException {
        String escolhac;
        Scanner scanner = new Scanner(System.in);
        pedidos();
        do {
            System.out.println("Digite o nome do cliente que deseja ver o pedido\nOu digite \"voltar\" para voltar ao menu principal");
            escolhac = scanner.nextLine();
            if (!escolhac.equals("voltar")){
                Cliente pedido = readpedido(escolhac);
                assert pedido != null;
                addPedido(pedido);
                lernovamente(pedido);
                crud(pedido);
            }
        } while (!escolhac.equals("voltar"));

    }
    public static String addInPedido(Cliente cliente) throws IOException {
        System.out.println("O que deseja pedir? \n1. Pratos \n2. Bebidas \n3. Vinhos");
        Scanner scanner = new Scanner(System.in);
        int escolha = scanner.nextInt();

        switch (escolha) {
            case 1 -> cliente.setPratos(Stream.concat(cliente.getPratos().stream(), Cardapio.menu(Cardapio.pratoPath).stream())
                    .collect(Collectors.toList()));
            case 2 -> cliente.setBebidas(Stream.concat(cliente.getBebidas().stream(), Cardapio.menu(Cardapio.bebidaPath).stream())
                    .collect(Collectors.toList()));
            case 3 -> cliente.setVinhos(Stream.concat(cliente.getVinhos().stream(), Cardapio.menu(Cardapio.vinhoPath).stream())
                    .collect(Collectors.toList()));
            default -> System.out.println("Hmm... Nada aqui");
        }
        addPedido(cliente);
        return cliente.getNome();
    }
    private static String editComplemento(Cliente pedido) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Editar Complemento (Digite \"cancelar\" para voltar)");
        if (scanner.nextLine().equals("cancelar")) return null; else System.out.println("Nome do Item: ");
        pedido.setComplemento(scanner.nextLine());
        addPedido(pedido);
        return pedido.getComplemento();
    }
    public static String removefromPedido(Cliente cliente) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n Remoção de Items (Digite \"cancelar\" para voltar)");
        if (scanner.nextLine().equals("cancelar")) return null; else System.out.println("Nome do Item: ");
        String nome = scanner.nextLine();

        if(cliente.getPratos() != null) System.out.println(cliente.getPratos().removeIf(item -> item.getNome().contains(nome)) ? "Remoção realizada com sucesso!" : "");
        if(cliente.getBebidas() != null) System.out.println(cliente.getBebidas().removeIf(item -> item.getNome().contains(nome)) ? "Remoção realizada com sucesso!" : "");
        if(cliente.getVinhos() != null) System.out.println(cliente.getVinhos().removeIf(item -> item.getNome().contains(nome)) ? "Remoção realizada com sucesso!" : "");


        addPedido(cliente);
        return nome;
    }
    public static void crud(Cliente pedido) throws IOException {
        Scanner scanner = new Scanner(System.in);
        String escolha;
        String res = "";
        do {
            System.out.println("Escolha uma função entre: \n1 Adicionar\n2 Remover\n3 Editar Complemento\n4 Ler Pedido\n\"Ou digite \"voltar\" para voltar ao menu principal\"");
            escolha = scanner.nextLine();
            if (!escolha.equals("voltar")){
                switch (escolha){
                    case "1" -> res = addInPedido(pedido);
                    case "2" -> res = removefromPedido(pedido);
                    case "3" -> res = editComplemento(pedido);
                    case "4" -> lernovamente(pedido);
                    default -> res = null;
                }
                System.out.println( res == null ? "Essa função não existe" :"Alteração Realizada com sucesso!");
            }
        } while (!escolha.equals("voltar"));
    }
    public static List<Item> addToList(String[] items){
        List<Item> itemsInternos = new ArrayList<>();
        for (String s : items) {
            Item item = new Item();
            item.setNome(s);
            itemsInternos.add(item);
        }
        return itemsInternos;
    }
    public static Cliente readpedido(String pedido) {
        Cliente cliente = new Cliente();
        try {
            File file = new File("C:\\Users\\Vinic\\Desktop\\Nova pasta\\pedidos\\" + pedido + ".txt");
            Scanner leitor = new Scanner(file);

            leitor.nextLine();
            cliente.setNome(pedido);
            while (leitor.hasNext()){
                String linha = leitor.nextLine();
                String[] partes = linha.split(":");
                if (partes[0].equals("Total"))cliente.total = Double.parseDouble(partes[1]);
                if (partes[0].equals("Pratos"))cliente.setPratos(addToList(partes[1].split(";")));
                if (partes[0].equals("Bebidas"))cliente.setBebidas(addToList(partes[1].split(";")));
                if (partes[0].equals("Vinhos"))cliente.setVinhos(addToList(partes[1].split(";")));
                if (partes[0].equals("Complemento"))cliente.setComplemento(partes[1]);
            }
        } catch (Exception err){
            System.out.println("Cliente não encontrado");
            System.out.println(err.toString());
            return null;
        }
        return cliente;
    }
}
