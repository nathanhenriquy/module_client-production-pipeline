public class ProdutorConsumidorSemaforos {
    public static void main(String[] args) {
        Buffer buffer = new Buffer(5); // Buffer com capacidade de 5 elementos

        Thread produtor = new Thread(new Produtor(buffer));
        Thread consumidor = new Thread(new Consumidor(buffer));

        produtor.start();
        consumidor.start();

        try {
            produtor.join();
            consumidor.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Processamento concluÃ­do.");
    }

}