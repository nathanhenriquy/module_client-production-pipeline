package Shops;

import java.util.concurrent.Semaphore;
import java.util.LinkedList;
import java.util.Queue;

public class EsteiraCircular {
    private final Queue<Veiculo> esteira;
    
    private final Semaphore mutex  = new Semaphore(1);
    private final Semaphore cheio;

    public EsteiraCircular() {
        this.esteira = new LinkedList<>();
        this.cheio  = new Semaphore(0);
    }

    public void inserir(Veiculo v) throws InterruptedException {
        mutex.acquire();
        esteira.add(v);
        mutex.release();
        cheio.release();
    }

    public Veiculo retirar() throws InterruptedException {
        cheio.acquire();
        mutex.acquire();
        Veiculo v = esteira.poll();
        mutex.release();

        return v;
    }

    public int disponiveis() {
        return cheio.availablePermits();
    }
}