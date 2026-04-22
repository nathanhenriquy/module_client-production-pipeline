package Factory;

public class Veiculo {
    private static int contadorId = 1;
    
    private final int id;
    private final String cor;
    private final String tipo;
    private final int estacaoId;
    private final int funcionarioId;
    private final long timestampProducao;
    
    public enum Cor {
        RED, GREEN, BLUE
    }
    
    public enum Tipo {
        SUV, SEDAN
    }
    
    public Veiculo(int estacaoId, int funcionarioId) {
        this.id = contadorId++;
        
        // Alternando cores RGB
        Cor[] cores = Cor.values();
        this.cor = cores[(id - 1) % cores.length].name();
        
        // Alternando tipos SUV e SEDAN
        Tipo[] tipos = Tipo.values();
        this.tipo = tipos[(id - 1) % tipos.length].name();
        
        this.estacaoId = estacaoId;
        this.funcionarioId = funcionarioId;
        this.timestampProducao = System.currentTimeMillis();
    }
    
    // Getters
    public int getId() { return id; }
    public String getCor() { return cor; }
    public String getTipo() { return tipo; }
    public int getEstacaoId() { return estacaoId; }
    public int getFuncionarioId() { return funcionarioId; }
    public long getTimestampProducao() { return timestampProducao; }
    
    @Override
    public String toString() {
        return String.format("Veiculo{id=%d, cor='%s', tipo='%s', estacao=%d, funcionario=%d}", 
                           id, cor, tipo, estacaoId, funcionarioId);
    }
}
