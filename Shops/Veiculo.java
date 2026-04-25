package Shops;

// Veiculo.java
public class Veiculo {
    private final int id;
    private final String cor;
    private final String tipo;
    private final int estacaoId;
    private final int funcionarioId;
    private final long timestampProducao;

    public Veiculo(int id, int estacaoId, int funcionarioId, String cor, String tipo, long timestampProducao) {
        this.id = id;
        this.cor = cor;
        this.tipo = tipo;
        this.estacaoId = estacaoId;
        this.funcionarioId = funcionarioId;
        this.timestampProducao = timestampProducao;
    }

    public String serializar() {
        return id + ":" + estacaoId + ":" + funcionarioId + ":" + cor + ":" + tipo + ":" + timestampProducao;
    }

    public static Veiculo desserializar(String dados) {
        String[] partes = dados.split(":");
        return new Veiculo(Integer.parseInt(partes[0]), 
            Integer.parseInt(partes[1]), 
            Integer.parseInt(partes[2]), 
            partes[3], partes[4], Long.parseLong(partes[5]));
    }

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