package Customers;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();   

        int totalClientes = 20;
        int capacidadeGaragem = 10;             
        int comprasPorCliente = random.nextInt(10);

        for (int i = 1; i <= totalClientes; i++) {
            Cliente c = new Cliente(i, capacidadeGaragem, comprasPorCliente);
            c.start(); 
        }
    }
}