package BufferCircular;
public class Main {
    public static void main(String[] args) {
        BufferCircular buffer = new BufferCircular(5); // Tamanho do buffer é 5
        Produtor produtor = new Produtor(buffer);
        Consumidor consumidor = new Consumidor(buffer);

        produtor.start();
        consumidor.start();
    }
}
