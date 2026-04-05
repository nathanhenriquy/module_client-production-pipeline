import java.util.Random;
import java.util.concurrent.Semaphore;

class Buffer {
    private final int[] buffer;
    private int index = 0;
    
    private final Semaphore empty; // SemÃ¡foro para espaÃ§os vazios
    private final Semaphore full;  // SemÃ¡foro para itens disponÃ­veis
    private final Semaphore mutex; // SemÃ¡foro para exclusÃ£o mÃºtua

    public Buffer(int tamanho) {
        buffer = new int[tamanho];
        empty = new Semaphore(tamanho); // ComeÃ§a com todas as posiÃ§Ãµes vazias
        full = new Semaphore(0); // Nenhum item disponÃ­vel no inÃ­cio
        mutex = new Semaphore(1); // Apenas uma thread pode modificar o buffer por vez
    }

    public void produzir(int item) throws InterruptedException {
        empty.acquire(); // Aguarda espaÃ§o disponÃ­vel
        mutex.acquire(); // Entra na seÃ§Ã£o crÃ­tica
        
        buffer[index] = item;
        System.out.println("Produzido: " + item);
        index++;

        mutex.release(); // Libera a seÃ§Ã£o crÃ­tica
        full.release();  // Indica que hÃ¡ um item disponÃ­vel
    }

    public int consumir() throws InterruptedException {
        full.acquire(); // Aguarda um item disponÃ­vel
        mutex.acquire(); // Entra na seÃ§Ã£o crÃ­tica
        
        index--;
        int item = buffer[index];
        System.out.println("Consumido: " + item);
        
        mutex.release(); // Libera a seÃ§Ã£o crÃ­tica
        empty.release(); // Indica que hÃ¡ espaÃ§o disponÃ­vel

        return item;
    }
}

class Produtor implements Runnable {
    private final Buffer buffer;
    private final Random random = new Random();

    public Produtor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) { // Produz 10 itens
                int item = random.nextInt(100);
                buffer.produzir(item);
                Thread.sleep(500); // Simula tempo de produÃ§Ã£o
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

class Consumidor implements Runnable {
    private final Buffer buffer;

    public Consumidor(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 10; i++) { // Consome 10 itens
                buffer.consumir();
                Thread.sleep(1000); // Simula tempo de processamento
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



