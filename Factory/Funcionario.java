package Factory;

import java.util.concurrent.Semaphore;

public class Funcionario extends Thread {
    private final int id;
    private final int estacaoId;
    private final Semaphore ferramentaEsquerda;
    private final Semaphore ferramentaDireita;
    private final EsteiraDistribuicao esteiraDistribuicao;
    private final EsteiraCircular esteiraCircular;
    private final LoggerFactory logger;
    private volatile boolean ativo = true;
    
    public Funcionario(int id, int estacaoId, 
                      Semaphore ferramentaEsquerda, 
                      Semaphore ferramentaDireita,
                      EsteiraDistribuicao esteiraDistribuicao,
                      EsteiraCircular esteiraCircular,
                      LoggerFactory logger) {
        this.id = id;
        this.estacaoId = estacaoId;
        this.ferramentaEsquerda = ferramentaEsquerda;
        this.ferramentaDireita = ferramentaDireita;
        this.esteiraDistribuicao = esteiraDistribuicao;
        this.esteiraCircular = esteiraCircular;
        this.logger = logger;
        this.setName("Funcionario-" + estacaoId + "-" + id);
    }
    
    @Override
    public void run() {
        while (ativo) {
            try {
                produzirVeiculo();
                Thread.sleep(2000 + (int)(Math.random() * 3000)); // Intervalo entre produções
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
    
    private void produzirVeiculo() throws InterruptedException {
        // Solução para evitar deadlock: funcionário com id par pega primeiro ferramenta esquerda
        if (id % 2 == 0) {
            ferramentaEsquerda.acquire();
            ferramentaDireita.acquire();
        } else {
            ferramentaDireita.acquire();
            ferramentaEsquerda.acquire();
        }
        
        try {
            System.out.println("Funcionário " + id + " da estação " + estacaoId + 
                             " adquiriu ambas as ferramentas");
            
            // Solicitar peça via esteira
            esteiraDistribuicao.solicitarPeca(estacaoId);
            
            // Simular produção do veículo
            Thread.sleep(1000 + (int)(Math.random() * 2000));
            
            // Criar veículo
            Veiculo veiculo = new Veiculo(estacaoId, id);
            
            // Inserir na esteira circular
            int posicao = esteiraCircular.inserirVeiculo(veiculo);
            
            // Log de produção
            try {
                logger.logProducao(veiculo, posicao);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            System.out.println("Funcionário " + id + " da estação " + estacaoId + 
                             " produziu " + veiculo);
            
        } finally {
            ferramentaEsquerda.release();
            ferramentaDireita.release();
            System.out.println("Funcionário " + id + " da estação " + estacaoId + 
                             " liberou as ferramentas");
        }
    }
    
    public void parar() {
        ativo = false;
        interrupt();
    }
    
    public int getFuncionarioId() {
        return id;
    }
}
