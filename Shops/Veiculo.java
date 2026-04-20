public class Veiculo {
    private final String id;
    private final int estacaoOrigem;

    public Veiculo(String id, int estacaoOrigem) {
        this.id = id;
        this.estacaoOrigem = estacaoOrigem;
    }

    // Serialização simples para envio via socket
    public String serializar() {
        return id + ":" + estacaoOrigem;
    }

    public static Veiculo desserializar(String dados) {
        String[] partes = dados.split(":");
        return new Veiculo(partes[0], Integer.parseInt(partes[1]));
    }

    public String getId() { return id; }
    public int getEstacaoOrigem() { return estacaoOrigem; }

    @Override
    public String toString() {
        return "Veiculo{id='" + id + "', estacao=" + estacaoOrigem + "}";
    }
}