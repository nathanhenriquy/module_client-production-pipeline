package Customers;
import java.io.*;
import java.net.*;
import java.util.Random;

public class Cliente extends Thread {

    private static final String[] ENDERECOS_LOJAS = {
        "ip do paulo", "ip do paulo", "ip do paulo"        
    };
    private static final int[] PORTAS_LOJAS = { 5001, 5002, 5003 };

    private int idCliente;
    private Garagem garagem;
    private Random random;
    private int totalCompras;

    public Cliente(int idCliente, int totalCompras) {
        this.idCliente = idCliente;
        this.garagem = new Garagem();
        this.random = new Random();
        this.totalCompras = totalCompras;
    }

   
}