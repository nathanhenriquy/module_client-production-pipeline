import java.util.concurrent.Semaphore;

public class EsteiraCircular {
    private final Veiculo[] esteira;
    private final int tamanho;
    private int entrada = 0;
    private int saida = 0;

    private final Semaphore mutex  = new Semaphore(1);
    private final Semaphore vazio;
    private final Semaphore cheio;

    public EsteiraCircular(int tamanho) {
        this.tamanho = tamanho;
        this.esteira = new Veiculo[tamanho];
        this.vazio  = new Semaphore(tamanho);
        this.cheio  = new Semaphore(0);
    }

    public void inserir(Veiculo v) throws InterruptedException {
        vazio.acquire();
        mutex.acquire();
        esteira[entrada] = v;
        entrada = (entrada + 1) % tamanho;
        mutex.release();
        cheio.release();
    }

    public Veiculo retirar() throws InterruptedException {
        cheio.acquire();
        mutex.acquire();
        Veiculo v = esteira[saida];
        esteira[saida] = null;
        saida = (saida + 1) % tamanho;
        mutex.release();
        vazio.release();
        return v;
    }

    public int disponiveis() {
        return cheio.availablePermits();
    }
}