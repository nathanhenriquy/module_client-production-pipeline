package Customers;

public class Veiculo {
    private final int id;
    private final int estacaoOrigem;
    private final int funcionarioId;
    private final String cor;
    private final String tipo;
    private final long timestampProducao;

    public Veiculo(int id, int estacaoOrigem, int funcionarioId, String cor, String tipo, long timestampProducao) {
        this.id = id;
        this.estacaoOrigem = estacaoOrigem;
        this.funcionarioId = funcionarioId;
        this.cor = cor;
        this.tipo = tipo;
        this.timestampProducao = timestampProducao;
    }

    public static Veiculo desserializar(String dados) {
        String[] partes = dados.split(":");

        return new Veiculo(
            Integer.parseInt(partes[0]),
            Integer.parseInt(partes[1]),
            Integer.parseInt(partes[2]),
            partes[3],
            partes[4],
            Long.parseLong(partes[5])
        );
    }

    public int getId() { return id; }
    public int getEstacaoOrigem() { return estacaoOrigem; }
    public int getFuncionarioId() { return funcionarioId; }
    public String getCor() { return cor; }
    public String getTipo() { return tipo; }
    public long getTimestampProducao() { return timestampProducao; }

    @Override
    public String toString() {
        return "Veiculo{" +
                "id=" + id +
                ", cor='" + cor + '\'' +
                ", tipo='" + tipo + '\'' +
                ", estacao=" + estacaoOrigem +
                ", funcionario=" + funcionarioId +
                ", timestamp=" + timestampProducao +
                '}';
    }
}