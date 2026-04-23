package Factory;

import java.util.ArrayList;
import java.util.List;

public class Fabrica {
    private static final int NUM_ESTACOES = 4;
    private final EstoqueFactory estoque;
    private final EsteiraDistribuicao esteiraDistribuicao;
    private final EsteiraCircular esteiraCircular;
    private final LoggerFactory logger;
    private final List<EstacaoProducao> estacoes;
    private final ServidorFabrica servidor;
    
    public Fabrica() {
        this.estoque = new EstoqueFactory();
        this.esteiraDistribuicao = new EsteiraDistribuicao(estoque);
        this.esteiraCircular = new EsteiraCircular();
        this.logger = new LoggerFactory();
        this.estacoes = new ArrayList<>();
        this.servidor = new ServidorFabrica(esteiraCircular, logger);
        
        inicializarEstacoes();
    }
    
    private void inicializarEstacoes() {
        for (int i = 1; i <= NUM_ESTACOES; i++) {
            EstacaoProducao estacao = new EstacaoProducao(
                i, 
                esteiraDistribuicao, 
                esteiraCircular, 
                logger
            );
            estacoes.add(estacao);
        }
        System.out.println("Fábrica inicializada com " + NUM_ESTACOES + " estações de produção");
    }
    
    public void iniciarProducao() {
        System.out.println("=== INICIANDO PRODUÇÃO DA FÁBRICA ===");
        System.out.println("Estoque inicial: " + estoque.getQuantidadePecas() + " peças");
        System.out.println("Capacidade da esteira circular: " + esteiraCircular.getCapacidade() + " veículos");
        
        // Iniciar servidor para atender lojas
        servidor.start();
        
        for (EstacaoProducao estacao : estacoes) {
            estacao.iniciarProducao();
        }
    }
    
    public void pararProducao() {
        System.out.println("=== PARANDO PRODUÇÃO DA FÁBRICA ===");
        
        // Parar servidor
        servidor.parar();
        
        for (EstacaoProducao estacao : estacoes) {
            estacao.pararProducao();
        }
        
        // Aguardar servidor terminar
        try {
            servidor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void statusFabrica() {
        System.out.println("\n=== STATUS DA FÁBRICA ===");
        System.out.println("Estoque de peças: " + estoque.getQuantidadePecas());
        System.out.println("Veículos na esteira: " + esteiraCircular.getCount() + "/" + 
                         esteiraCircular.getCapacidade());
        System.out.println("Estações ativas: " + estacoes.size());
    }
    
    public EsteiraCircular getEsteiraCircular() {
        return esteiraCircular;
    }
    
    public EstoqueFactory getEstoque() {
        return estoque;
    }
    
    public LoggerFactory getLogger() {
        return logger;
    }
    
    public List<EstacaoProducao> getEstacoes() {
        return estacoes;
    }
}
