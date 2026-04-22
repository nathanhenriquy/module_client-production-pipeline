package Factory;

import java.util.concurrent.Semaphore;
import java.util.ArrayList;
import java.util.List;

public class EstacaoProducao {
    private static final int NUM_FUNCIONARIOS = 5;
    private final int id;
    private final List<Funcionario> funcionarios;
    private final List<Semaphore> ferramentas;
    private final EsteiraDistribuicao esteiraDistribuicao;
    private final EsteiraCircular esteiraCircular;
    private final LoggerFactory logger;
    
    public EstacaoProducao(int id, EsteiraDistribuicao esteiraDistribuicao, 
                          EsteiraCircular esteiraCircular, LoggerFactory logger) {
        this.id = id;
        this.funcionarios = new ArrayList<>();
        this.ferramentas = new ArrayList<>();
        this.esteiraDistribuicao = esteiraDistribuicao;
        this.esteiraCircular = esteiraCircular;
        this.logger = logger;
        
        inicializarFerramentas();
        inicializarFuncionarios();
    }
    
    private void inicializarFerramentas() {
        // Criar 5 ferramentas (uma entre cada par de funcionários adjacentes)
        for (int i = 0; i < NUM_FUNCIONARIOS; i++) {
            ferramentas.add(new Semaphore(1)); // Cada ferramenta pode ser usada por apenas 1 funcionário
        }
    }
    
    private void inicializarFuncionarios() {
        for (int i = 0; i < NUM_FUNCIONARIOS; i++) {
            // Ferramenta esquerda e direita para cada funcionário (estrutura circular)
            Semaphore ferramentaEsquerda = ferramentas.get(i);
            Semaphore ferramentaDireita = ferramentas.get((i + 1) % NUM_FUNCIONARIOS);
            
            Funcionario funcionario = new Funcionario(
                i + 1, // ID do funcionário (1-5)
                id,    // ID da estação
                ferramentaEsquerda,
                ferramentaDireita,
                esteiraDistribuicao,
                esteiraCircular,
                logger
            );
            
            funcionarios.add(funcionario);
        }
    }
    
    public void iniciarProducao() {
        System.out.println("Iniciando produção na estação " + id);
        for (Funcionario funcionario : funcionarios) {
            funcionario.start();
        }
    }
    
    public void pararProducao() {
        System.out.println("Parando produção na estação " + id);
        for (Funcionario funcionario : funcionarios) {
            funcionario.parar();
        }
        
        // Aguardar todos os funcionários terminarem
        for (Funcionario funcionario : funcionarios) {
            try {
                funcionario.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    public int getId() {
        return id;
    }
    
    public List<Funcionario> getFuncionarios() {
        return funcionarios;
    }
}
