package Customers;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente extends Thread {

    private static final String[] ENDERECOS_LOJAS = {
        "ip das lojas", "ip das lojas", "ip das lojas"
    };
    private static final int[] PORTAS_LOJAS = { 5001, 5002, 5003 };

    private int idCliente;
    private Garagem garagem;
    private Random random;
   

    public Cliente(int idCliente) {
        this.idCliente = idCliente;
        this.garagem = new Garagem();
        this.random = new Random();      
    }

    @Override
    public void run() {
        try {
            while (true) {  
                int lojaEscolhida = random.nextInt(3);

                System.out.println("[CLIENTE " + idCliente + "] Tentando comprar na Loja " + (lojaEscolhida + 1));

                Veiculo v = comprarDaLoja(lojaEscolhida);

                if (v != null) {
                    garagem.guardarVeiculo(v);
                    System.out.println("[CLIENTE " + idCliente + "] Comprou: " + v
                        + " | Garagem: " + garagem.disponiveis());
                }

                sleep(2000);
            }
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