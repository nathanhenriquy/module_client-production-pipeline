package Factory;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE PRODUÇÃO VEICULAR ===\n");
        
        // Criar a fábrica
        Fabrica fabrica = new Fabrica();
        
        // Iniciar produção
        fabrica.iniciarProducao();
        
        // Thread para monitorar status da fábrica
        Thread monitorStatus = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(10000); // Status a cada 10 segundos
                    fabrica.statusFabrica();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitorStatus.start();
        
        // Aguardar por 300 segundos e então parar (5 minutos)
        try {
            Thread.sleep(300000); // Executar por 5 minutos
            
            System.out.println("\n=== FINALIZANDO SISTEMA ===");
            fabrica.pararProducao();
            monitorStatus.interrupt();
            
            // Status final
            fabrica.statusFabrica();
            System.out.println("Sistema finalizado!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sistema interrompido!");
        }
    }
}