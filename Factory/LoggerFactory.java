package Factory;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerFactory {
    private static final String LOG_PRODUCAO_FILE = "log_producao.txt";
    private static final String LOG_VENDA_FILE = "log_venda.txt";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public synchronized void logProducao(Veiculo veiculo, int posicaoEsteira) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_PRODUCAO_FILE, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String logEntry = String.format("[%s] PRODUÇÃO - ID: %d, Cor: %s, Tipo: %s, Estação: %d, Funcionário: %d, Posição Esteira: %d",
                    timestamp,
                    veiculo.getId(),
                    veiculo.getCor(),
                    veiculo.getTipo(),
                    veiculo.getEstacaoId(),
                    veiculo.getFuncionarioId(),
                    posicaoEsteira
            );
            writer.println(logEntry);
            System.out.println("LOG PRODUÇÃO: " + logEntry);
        } catch (IOException e) {
            System.err.println("Erro ao escrever log de produção: " + e.getMessage());
        }
    }
    
    public synchronized void logVenda(Veiculo veiculo, int posicaoEsteiraProducao, 
                                    int lojaId, int posicaoEsteiraLoja) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_VENDA_FILE, true))) {
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String logEntry = String.format("[%s] VENDA - ID: %d, Cor: %s, Tipo: %s, Estação: %d, Funcionário: %d, Posição Produção: %d, Loja: %d, Posição Loja: %d",
                    timestamp,
                    veiculo.getId(),
                    veiculo.getCor(),
                    veiculo.getTipo(),
                    veiculo.getEstacaoId(),
                    veiculo.getFuncionarioId(),
                    posicaoEsteiraProducao,
                    lojaId,
                    posicaoEsteiraLoja
            );
            writer.println(logEntry);
            System.out.println("LOG VENDA: " + logEntry);
        } catch (IOException e) {
            System.err.println("Erro ao escrever log de venda: " + e.getMessage());
        }
    }
}
