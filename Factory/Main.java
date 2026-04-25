package Factory;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE PRODUÇÃO VEICULAR ===\n");
        
        Fabrica fabrica = new Fabrica();
        
        fabrica.iniciarProducao();
        
        Thread monitorStatus = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    Thread.sleep(10000);
                    fabrica.statusFabrica();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        monitorStatus.start();
        
        try {
            Thread.sleep(300000);
            
            System.out.println("\n=== FINALIZANDO SISTEMA ===");
            fabrica.pararProducao();
            monitorStatus.interrupt();
            
            fabrica.statusFabrica();
            System.out.println("Sistema finalizado!");
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Sistema interrompido!");
        }
    }
}