package Factory;

import java.util.concurrent.Semaphore;

public class EsteiraDistribuicao {
    private static final int CAPACIDADE_SOLICITACOES = 5;
    private final Semaphore capacidadeEsteira;
    private final EstoqueFactory estoque;
    
    public EsteiraDistribuicao(EstoqueFactory estoque) {
        this.capacidadeEsteira = new Semaphore(CAPACIDADE_SOLICITACOES);
        this.estoque = estoque;
    }
    
    public void solicitarPeca(int estacaoId) throws InterruptedException {
        capacidadeEsteira.acquire();
        try {
            System.out.println("Estação " + estacaoId + " solicitou peça via esteira");
            Thread.sleep(100);
            estoque.retirarPeca();
            System.out.println("Peça entregue à estação " + estacaoId);
        } finally {
            capacidadeEsteira.release();
        }
    }
}
