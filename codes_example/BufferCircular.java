package BufferCircular;
import java.util.concurrent.Semaphore;

class BufferCircular {
    private int[] buffer;
    private int tamanho;
    private int entrada, saida;

    private Semaphore mutex;
    private Semaphore vazio;
    private Semaphore cheio;

    public BufferCircular(int tamanho) {
        this.tamanho = tamanho;
        buffer = new int[tamanho];
        entrada = 0;
        saida = 0;
        mutex = new Semaphore(1);
        vazio = new Semaphore(tamanho);
        cheio = new Semaphore(0);
    }

    public void produzir(int item) throws InterruptedException {
        vazio.acquire();
        mutex.acquire();
        buffer[entrada] = item;
        entrada = (entrada + 1) % tamanho;
        System.out.println("Produzido: " + item);
        mutex.release();
        cheio.release();
    }

    public int consumir() throws InterruptedException {
        cheio.acquire();
        mutex.acquire();
        int item = buffer[saida];
        saida = (saida + 1) % tamanho;
        System.out.println("Consumido: " + item);
        mutex.release();
        vazio.release();
        return item;
    }
}

class Produtor extends Thread {
    private BufferCircular buffer;

    public Produtor(BufferCircular buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.produzir(i);
                sleep(1000); // Apenas para fins de demonstração
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Consumidor extends Thread {
    private BufferCircular buffer;

    public Consumidor(BufferCircular buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) {
                buffer.consumir();
                sleep(1500); // Apenas para fins de demonstração
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

