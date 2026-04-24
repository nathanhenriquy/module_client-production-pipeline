package Factory;

import java.util.concurrent.Semaphore;

public class EstoqueFactory {
    private static final int CAPACIDADE_MAXIMA = 500;
    private int quantidadePecas;
    private final Semaphore mutex;
    private final Semaphore pecasDisponiveis;
    
    public EstoqueFactory() {
        this.quantidadePecas = CAPACIDADE_MAXIMA;
        this.mutex = new Semaphore(1);
        this.pecasDisponiveis = new Semaphore(CAPACIDADE_MAXIMA);
    }
    
    public void retirarPeca() throws InterruptedException {
        pecasDisponiveis.acquire();
        mutex.acquire();
        try {
            if (quantidadePecas > 0) {
                quantidadePecas--;
                System.out.println("Peça retirada do estoque. Restam: " + quantidadePecas);
            }
        } finally {
            mutex.release();
        }
    }
    
    public void reporPeca() throws InterruptedException {
        mutex.acquire();
        try {
            if (quantidadePecas < CAPACIDADE_MAXIMA) {
                quantidadePecas++;
                System.out.println("Peça reposta no estoque. Total: " + quantidadePecas);
                pecasDisponiveis.release();
            }
        } finally {
            mutex.release();
        }
    }
    
    public int getQuantidadePecas() {
        return quantidadePecas;
    }
}
