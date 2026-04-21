package Customers;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int totalClientes = 20;

        for (int i = 1; i <= totalClientes; i++) {
            int comprasPorCliente = random.nextInt(8) + 3; 
            Cliente c = new Cliente(i, comprasPorCliente);
            c.start();
        }
    }
}