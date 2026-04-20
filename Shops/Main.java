package Shops;

public class Main {
    public static void main(string[] args) {
        private static final String FABRICA_HOST = "localhost";
        private static final int    FABRICA_PORTA = 6000;

        public static void main(String[] args) {
            int capacidadeEsteira = 40;

            Loja[] lojas = {
                new Loja(1, 5001, FABRICA_HOST, FABRICA_PORTA, capacidadeEsteira),
                new Loja(2, 5002, FABRICA_HOST, FABRICA_PORTA, capacidadeEsteira),
                new Loja(3, 5003, FABRICA_HOST, FABRICA_PORTA, capacidadeEsteira),
            };

            for (Loja l : lojas) {
                new Thread(l).start();
            }
        }
    }
}