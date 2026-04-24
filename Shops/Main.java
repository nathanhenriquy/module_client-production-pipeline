package Shops;

// Main.java
public class Main {
    private static final String FABRICA_HOST = "localhost";
    private static final int    FABRICA_PORTA = 6000;
    private static final String FABRICA_URL = FABRICA_HOST + ":" + FABRICA_PORTA;

    public static void main(String[] args) {

        Shop[] shops = {
            new Shop(1, 5001, FABRICA_HOST, FABRICA_PORTA),
            new Shop(2, 5002, FABRICA_HOST, FABRICA_PORTA),
            new Shop(3, 5003, FABRICA_HOST, FABRICA_PORTA),
        };

        for (Shop s : shops) {
            new Thread(s).start();
        }
    }
}