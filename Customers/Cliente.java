package Customers;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente extends Thread {

    private static final String[] ENDERECOS_LOJAS = {
        "ip do paulo", "ip do paulo", "ip do paulo"
    };
    private static final int[] PORTAS_LOJAS = { 5001, 5002, 5003 };

    private int idCliente;
    private Garagem garagem;
    private Random random;
    private int totalCompras;

    public Cliente(int idCliente, int totalCompras) {
        this.idCliente = idCliente;
        this.garagem = new Garagem();
        this.random = new Random();
        this.totalCompras = totalCompras;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < totalCompras; i++) {
                int lojaEscolhida = random.nextInt(3);

                System.out.println("[CLIENTE " + idCliente + "] Tentando comprar na Loja " + (lojaEscolhida + 1));

                Veiculo v = comprarDaLoja(lojaEscolhida);

                if (v != null) {
                    garagem.guardarVeiculo(v);
                    System.out.println("[CLIENTE " + idCliente + "] Comprou: " + v
                        + " | Garagem: " + garagem.disponiveis());
                }

                sleep(random.nextInt(2000) + 500);
            }

            System.out.println("[CLIENTE " + idCliente + "] Encerrou. Total na garagem: "
                + garagem.disponiveis());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Veiculo comprarDaLoja(int indLoja) {
        String host = ENDERECOS_LOJAS[indLoja];
        int porta = PORTAS_LOJAS[indLoja];

        try (
            Socket socket = new Socket(host, porta);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            out.println("COMPRAR");

            String resposta = in.readLine();

            if (resposta != null && resposta.startsWith("VEICULO:")) {
                return Veiculo.desserializar(resposta.substring("VEICULO:".length()));
            }

        } catch (IOException e) {
            System.out.println("[CLIENTE " + idCliente + "] Loja " + (indLoja + 1) + " indisponível.");
        }

        return null;
    }
}