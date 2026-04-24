package Customers;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Random random = new Random();
        int totalClientes = 20;

        for (int i = 1; i <= totalClientes; i++) {
            Cliente c = new Cliente(i);
            c.start();
        }
    }
}