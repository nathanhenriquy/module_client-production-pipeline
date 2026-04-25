package Factory;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class VendedorFabrica extends Thread {
    private final EsteiraCircular esteiraCircular;
    private final LoggerFactory logger;
    private final String[] lojasPorts = {"5001", "5002", "5003"};
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
                venderVeiculo();
                Thread.sleep(3000 + random.nextInt(5000));
                
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void venderVeiculo() {
        try {
            Veiculo veiculo = esteiraCircular.removerVeiculo();
            
            String porta = lojasPorts[random.nextInt(lojasPorts.length)];
            int lojaId = Integer.parseInt(porta) - 5000;
            
            try (Socket socket = new Socket("10.130.43.2", Integer.parseInt(porta));
                 ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                 ObjectInputStream input = new ObjectInputStream(socket.getInputStream())) {
                
                VeiculoInfo veiculoInfo = new VeiculoInfo(
                    veiculo.getId(),
                    veiculo.getCor(),
                    veiculo.getTipo(),
                    veiculo.getEstacaoId(),
                    veiculo.getFuncionarioId(),
                    veiculo.getTimestampProducao()
                );
                
                output.writeObject("COMPRAR_VEICULO");
                output.writeObject(veiculoInfo);
                
                String resposta = (String) input.readObject();
                if ("SUCESSO".equals(resposta)) {
                    int posicaoLoja = input.readInt();
                    
                    logger.logVenda(veiculo, -1, lojaId, posicaoLoja);
                    System.out.println("Veículo vendido para loja " + lojaId + ": " + veiculo);
                } else {
                    System.err.println("Falha ao vender veículo para loja " + lojaId);
                }
                
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Erro ao conectar com loja na porta " + porta + ": " + e.getMessage());
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
