package Factory;

import java.io.*;
import java.net.*;

public class ServidorFabrica extends Thread {
    private static final int PORTA_FABRICA = 6000;
    private final EsteiraCircular esteiraCircular;
    private final LoggerFactory logger;
    private volatile boolean ativo = true;
    private ServerSocket serverSocket;
    
    public ServidorFabrica(EsteiraCircular esteiraCircular, LoggerFactory logger) {
        this.esteiraCircular = esteiraCircular;
        this.logger = logger;
        this.setName("ServidorFabrica");
    }
    
    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(PORTA_FABRICA);
            System.out.println("[FABRICA] Servidor iniciado na porta " + PORTA_FABRICA);
            
            while (ativo) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    // Processar cada solicitação em thread separada
                    new Thread(() -> atenderLoja(clientSocket)).start();
                } catch (IOException e) {
                    if (ativo) {
                        System.err.println("[FABRICA] Erro ao aceitar conexão: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("[FABRICA] Erro ao iniciar servidor: " + e.getMessage());
        }
    }
    
    private void atenderLoja(Socket socket) {
        try (PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            
            String mensagem = in.readLine();
            System.out.println("[FABRICA] Recebida solicitação: " + mensagem);
            
            if (mensagem != null && mensagem.startsWith("SOLICITAR:")) {
                // Extrair ID da loja da mensagem
                String lojaIdStr = mensagem.substring("SOLICITAR:".length());
                int lojaId = Integer.parseInt(lojaIdStr);
                
                try {
                    // Pegar veículo da esteira
                    Veiculo veiculo = esteiraCircular.removerVeiculo();
                    
                    // Enviar veículo serializado
                    String veiculoSerializado = veiculo.serializar();
                    out.println("VEICULO:" + veiculoSerializado);
                    
                    // Log de venda
                    try {
                        logger.logVenda(veiculo, -1, lojaId, -1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    
                    System.out.println("[FABRICA] Enviado veículo " + veiculo + " para loja " + lojaId);
                    
                } catch (InterruptedException e) {
                    out.println("ERRO:Sem veículos disponíveis");
                    Thread.currentThread().interrupt();
                }
            } else {
                out.println("ERRO:Comando inválido");
            }
            
        } catch (IOException e) {
            System.err.println("[FABRICA] Erro ao atender loja: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("[FABRICA] Erro ao fechar socket: " + e.getMessage());
            }
        }
    }
    
    public void parar() {
        ativo = false;
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
        } catch (IOException e) {
            System.err.println("[FABRICA] Erro ao fechar servidor: " + e.getMessage());
        }
        interrupt();
    }
}
