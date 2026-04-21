package Customers;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;

public class Garagem {
    private ArrayList<Veiculo> garagem;
    private Semaphore mutex;
    private Semaphore cheio;

    public Garagem() {
        garagem = new ArrayList<>();
        mutex = new Semaphore(1);
        cheio = new Semaphore(0);
    }

    public void guardarVeiculo(Veiculo v) throws InterruptedException {
        mutex.acquire();
        garagem.add(v);
        System.out.println("[GARAGEM] Guardado: " + v + " | Total: " + garagem.size());
        mutex.release();
        cheio.release();
    }

    public int disponiveis() {
        return cheio.availablePermits();
    }
}