package Factory;

import java.util.concurrent.Semaphore;

public class EsteiraCircular {
    private static final int CAPACIDADE = 40;
    private final Veiculo[] esteira;
    private int entrada, saida;
    private int count;
    
    private final Semaphore mutex;
    private final Semaphore espacosVazios;
    private final Semaphore veiculosDisponiveis;
    
    public EsteiraCircular() {
        this.esteira = new Veiculo[CAPACIDADE];
        this.entrada = 0;
        this.saida = 0;
        this.count = 0;
        this.mutex = new Semaphore(1);
        this.espacosVazios = new Semaphore(CAPACIDADE);
        this.veiculosDisponiveis = new Semaphore(0);
    }
    
    public int inserirVeiculo(Veiculo veiculo) throws InterruptedException {
        espacosVazios.acquire();
        mutex.acquire();
        int posicao;
        try {
            esteira[entrada] = veiculo;
            posicao = entrada;
            entrada = (entrada + 1) % CAPACIDADE;
            count++;
            System.out.println("Veículo inserido na esteira na posição " + posicao + 
                             " - " + veiculo);
        } finally {
            mutex.release();
        }
        veiculosDisponiveis.release();
        return posicao;
    }
    
    public Veiculo removerVeiculo() throws InterruptedException {
        veiculosDisponiveis.acquire();
        mutex.acquire();
        Veiculo veiculo;
        try {
            veiculo = esteira[saida];
            esteira[saida] = null;
            saida = (saida + 1) % CAPACIDADE;
            count--;
            System.out.println("Veículo removido da esteira da posição " + saida + 
                             " - " + veiculo);
        } finally {
            mutex.release();
        }
        espacosVazios.release();
        return veiculo;
    }
    
    public int getCount() {
        return count;
    }
    
    public int getCapacidade() {
        return CAPACIDADE;
    }
}
