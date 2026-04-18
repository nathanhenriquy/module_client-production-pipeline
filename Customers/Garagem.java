package Customers;
import java.util.concurrent.Semaphore;

public class Garagem {
    private String[] garagem;   // o array circular — cada posição guarda dados de um veículo
    private int tamanho;        // capacidade máxima da garagem
    private int entrada;        // índice onde o próximo veículo será inserido

    private Semaphore mutex;   
    private Semaphore vazio;    // conta quantas posições LIVRES existem (valor inicial = tamanho)
    private Semaphore cheio;    // conta quantos veículos EXISTEM na garagem (valor inicial = 0)

    public Garagem(int tamanho) {
        this.tamanho = tamanho;
        garagem = new String[tamanho];
        entrada = 0;        
        mutex = new Semaphore(1);        // exclusão mútua
        vazio = new Semaphore(tamanho);  // começa cheio de espaços livres
        cheio = new Semaphore(0);        // começa sem nenhum veículo
    }

    // Chamado pelo Cliente quando compra um veículo
    public void guardarVeiculo(String dadosVeiculo) throws InterruptedException {
        vazio.acquire();   // decrementa vagas livres
        mutex.acquire();   
        garagem[entrada] = dadosVeiculo;
        entrada = (entrada + 1) % tamanho;  // avança o ponteiro circularmente
        mutex.release();  
        cheio.release();   // incrementa veículos disponíveis
    }
}