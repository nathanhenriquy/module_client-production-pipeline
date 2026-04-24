package Shops;

import java.io.*;
import java.net.*;
import java.util.concurrent.Semaphore;


// Shop.java
public class Shop implements Runnable {
    private final int id;
    private final int porta;
    //private final String fabricaUrl;
    private final String fabricaHost;
    private final int fabricaPorta;
    private final EsteiraCircular esteira;

    private final Semaphore mutexFabrica = new Semaphore(1);

    public Shop(int id, int porta, String fabricaHost, int fabricaPorta) {
        this.id = id;
        this.porta = porta;
        this.fabricaHost = fabricaHost;
        this.fabricaPorta = fabricaPorta;
        this.esteira = new EsteiraCircular();
    }

    @Override
    public void run() {
        System.out.println("[Loja " + id + "] Iniciando na porta " + porta);

        Thread reabastecedor = new Thread(this::reabastecerEsteira);
        reabastecedor.setDaemon(true);
        reabastecedor.start();

        try (ServerSocket servidor = new ServerSocket(porta)) {
            while (true) {
                Socket clienteSocket = servidor.accept();

                Thread atendimento = new Thread(() -> atenderCliente(clienteSocket));
                atendimento.setDaemon(true);
                atendimento.start();
            }
        } catch (IOException e) {
            System.err.println("[Loja " + id + "] Erro no servidor: " + e.getMessage());
        }
    }

    private void reabastecerEsteira() {
        while (true) {
            try {
                Veiculo v = solicitarVeiculoFabrica();
                if (v != null) {
                    esteira.inserir(v);
                    System.out.println("[Loja " + id + "] Veículo recebido da fábrica: " + v);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private Veiculo solicitarVeiculoFabrica() {
        try {
            mutexFabrica.acquire();
            try (Socket socket = new Socket(fabricaHost, fabricaPorta);
                 PrintWriter out    = new PrintWriter(socket.getOutputStream(), true);
                 BufferedReader in  = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

                out.println("SOLICITAR:" + id);
                String resposta = in.readLine();

                if (resposta != null && resposta.startsWith("VEICULO:")) {
                    return Veiculo.desserializar(resposta.substring("VEICULO:".length()));
                }

                return null;

            } finally {
                mutexFabrica.release();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println("[Loja " + id + "] Erro ao contatar fábrica: " + e.getMessage());
            return null;
        }
    }


    private void atenderCliente(Socket socket) {
        try (PrintWriter out   = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            String mensagem = in.readLine();
            if ("COMPRAR".equals(mensagem)) {

                Veiculo v = esteira.retirar();
                out.println("VEICULO:" + v.serializar());
                System.out.println("[Loja " + id + "] Vendeu " + v + " ao cliente");
            }

        } catch (IOException | InterruptedException e) {
            System.err.println("[Loja " + id + "] Erro ao atender cliente: " + e.getMessage());
        }
    }
}