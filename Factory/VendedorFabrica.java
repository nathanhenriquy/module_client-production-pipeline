package Factory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class VendedorFabrica extends Thread {
    private final EsteiraCircular esteiraCircular;
    private final LoggerFactory logger;
    private final String[] lojasPorts = {"8081", "8082", "8083"};
    private final Random random = new Random();
    private volatile boolean ativo = true;
    
    public VendedorFabrica(EsteiraCircular esteiraCircular, LoggerFactory logger) {
        this.esteiraCircular = esteiraCircular;
        this.logger = logger;
        this.setName("VendedorFabrica");
    }
    
    @Override
    public void run() {
        while (ativo) {
            try {
                // Tentar vender um veículo
                venderVeiculo();
                Thread.sleep(3000 + random.nextInt(5000)); // Intervalo entre vendas
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void venderVeiculo() {
        try {
            // Remover veículo da esteira da fábrica
            Veiculo veiculo = esteiraCircular.removerVeiculo();
            
            // Escolher loja aleatória
            String porta = lojasPorts[random.nextInt(lojasPorts.length)];
            int lojaId = Integer.parseInt(porta) - 8080; // Converter porta para ID da loja
            
            // Conectar com a loja
            try (Socket socket = new Socket("localhost", Integer.parseInt(porta));
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                
                // Criar objeto de informação do veículo
                VeiculoInfo veiculoInfo = new VeiculoInfo(
                    veiculo.getId(),
                    veiculo.getCor(),
                    veiculo.getTipo(),
                    veiculo.getEstacaoId(),
                    veiculo.getFuncionarioId(),
                    veiculo.getTimestampProducao()
                );
                
                // Enviar comando e veículo
                output.writeObject("COMPRAR_VEICULO");
                output.writeObject(veiculoInfo);
                
                // Receber resposta
                String resposta = (String) input.readObject();
                if ("SUCESSO".equals(resposta)) {
                    int posicaoLoja = input.readInt();
                    
                    // Log de venda
                    logger.logVenda(veiculo, -1, lojaId, posicaoLoja);
                    System.out.println("Veículo vendido para loja " + lojaId + ": " + veiculo);
                } else {
                    System.err.println("Falha ao vender veículo para loja " + lojaId);
                    // Recolocar veículo na esteira (em uma implementação real)
                    // esteiraCircular.inserirVeiculo(veiculo);
                }
                
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao conectar com loja na porta " + porta + ": " + e.getMessage());
                // Recolocar veículo na esteira (em uma implementação real)
                // esteiraCircular.inserirVeiculo(veiculo);
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void parar() {
        ativo = false;
        interrupt();
    }
}
