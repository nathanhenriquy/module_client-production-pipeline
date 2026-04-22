package Factory;

import java.io.Serializable;

public class VeiculoInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int id;
    private final String cor;
    private final String tipo;
    private final int estacaoId;
    private final int funcionarioId;
    private final long timestampProducao;
    
    public VeiculoInfo(int id, String cor, String tipo, int estacaoId, int funcionarioId, long timestampProducao) {
        this.id = id;
        this.cor = cor;
        this.tipo = tipo;
        this.estacaoId = estacaoId;
        this.funcionarioId = funcionarioId;
        this.timestampProducao = timestampProducao;
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
        return String.format("VeiculoInfo{id=%d, cor='%s', tipo='%s', estacao=%d, funcionario=%d}", 
                           id, cor, tipo, estacaoId, funcionarioId);
    }
}
